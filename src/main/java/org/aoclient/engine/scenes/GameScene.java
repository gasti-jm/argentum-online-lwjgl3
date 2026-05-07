package org.aoclient.engine.scenes;

import org.aoclient.engine.window.Window;
import org.aoclient.engine.game.*;
import org.aoclient.engine.game.models.Direction;
import org.aoclient.engine.game.bindkeys.Key;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FMain;
import org.aoclient.engine.listeners.KeyHandler;
import org.aoclient.engine.listeners.MouseListener;
import org.aoclient.engine.utils.inits.GrhInfo;
import org.aoclient.engine.utils.inits.MapData;

import static org.aoclient.engine.game.IntervalTimer.INT_SENTRPU;
import static org.aoclient.engine.game.models.Character.drawCharacter;
import static org.aoclient.engine.game.bindkeys.Key.TALK;
import static org.aoclient.engine.renderer.Drawn.drawTexture;
import static org.aoclient.engine.scenes.Camera.*;
import static org.aoclient.engine.utils.GameData.*;
import static org.aoclient.engine.utils.Time.deltaTime;
import static org.aoclient.engine.utils.Time.timerTicksPerFrame;
import static org.aoclient.network.protocol.Protocol.*;
import static org.lwjgl.glfw.GLFW.*;

/**
 * <p>
 * {@code GameScene} es la escena mas compleja, responsable de manejar toda la logica y renderizado del mundo de Argentum Online
 * cuando el jugador esta activamente conectado y controlando su personaje. Esta escena se activa una vez que el usuario ha
 * iniciado sesion satisfactoriamente desde {@code MainScene}.
 * <p>
 * Funcionalidades principales:
 * <ul>
 * <li>Renderizado del mapa con sus multiples capas
 * <li>Control del personaje del usuario mediante entradas de teclado y raton
 * <li>Visualizacion de otros personajes y NPCs en el mundo
 * <li>Manejo de efectos como la lluvia y efectos visuales
 * <li>Mostrar dialogos sobre los personajes
 * <li>Control de la camara centrada en el personaje
 * <li>Renderizado de la interfaz de usuario superpuesta (inventario, chat, estadisticas)
 * </ul>
 * <p>
 * Esta escena monitorea constantemente el estado de conexion del usuario. Si se detecta una desconexion, la escena se cierra
 * automaticamente y regresa a {@code MainScene} para permitir una nueva conexion.
 * <p>
 * El metodo {@link GameScene#render()} es particularmente complejo en esta escena, ya que maneja el renderizado de multiples
 * capas en orden especifico para lograr el efecto visual correcto del mundo.
 *
 * @see Scene
 * @see MainScene
 * @see User
 * @see Camera
 * @see Rain
 */

public final class GameScene extends Scene {

    public static final IntervalTimer intervalToUpdatePos = new IntervalTimer(INT_SENTRPU);
    private final User user = User.INSTANCE;
    private Weather weather;// color de ambiente.
    private float offSetCounterX = 0;
    private float offSetCounterY = 0;
    private float alphaCeiling = 1.0f;
    public static boolean autoMove = false;
    public static FMain frmMain;

    @Override
    public void init() {
        super.init();

        canChangeTo = SceneType.MAIN_SCENE;
        weather = Weather.INSTANCE;
        frmMain = new FMain();

        ImGUISystem.INSTANCE.addFrm(frmMain);
    }

    @Override
    public void render() {
        // si el usuario se desconecta debe regresar al menu principal.
        if (!user.isUserConected()) {
            frmMain.close();
            this.close();
        }

        if (!visible) return;

        weather.update();
        intervalToUpdatePos.update();

        if (user.isUserMoving()) {
            if (user.getAddToUserPos().getX() != 0) {
                offSetCounterX -= charList[user.getUserCharIndex()].getWalkingSpeed() * user.getAddToUserPos().getX() * timerTicksPerFrame;
                if (Math.abs(offSetCounterX) >= Math.abs(TILE_PIXEL_SIZE * user.getAddToUserPos().getX())) {
                    offSetCounterX = 0;
                    user.getAddToUserPos().setX(0);
                    user.setUserMoving(false);
                }
            }

            if (user.getAddToUserPos().getY() != 0) {
                offSetCounterY -= charList[user.getUserCharIndex()].getWalkingSpeed() * user.getAddToUserPos().getY() * timerTicksPerFrame;
                if (Math.abs(offSetCounterY) >= Math.abs(TILE_PIXEL_SIZE * user.getAddToUserPos().getY())) {
                    offSetCounterY = 0;
                    user.getAddToUserPos().setY(0);
                    user.setUserMoving(false);
                }
            }
        }

        renderScreen(user.getUserPos().getX() - user.getAddToUserPos().getX(),
                user.getUserPos().getY() - user.getAddToUserPos().getY(),
                (int) (offSetCounterX), (int) (offSetCounterY));
    }

    /**
     * Escucha los eventos del mouse.
     */
    @Override
    public void mouseEvents() {
        if (user.isUserComerciando() || !ImGUISystem.INSTANCE.isMainLast()) return;

        // Estamos haciendo click en el render?
        if (inGameArea()) {
            if (MouseListener.mouseButtonClick(GLFW_MOUSE_BUTTON_LEFT)) {
                if (user.getUsingSkill() == 0) {

                    // Estamos manteniendo Shift derecho?
                    if ((KeyHandler.isKeyPressed(GLFW_KEY_RIGHT_SHIFT) || KeyHandler.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) &&
                            charList[user.getUserCharIndex()].getPriv() != 0) {

                        teleport("YO",
                                user.getUserMap(),
                                getTileMouseX((int) MouseListener.getX() - POS_SCREEN_X),
                                getTileMouseY((int) MouseListener.getY() - POS_SCREEN_Y));

                    } else {

                        leftClick(
                                getTileMouseX((int) MouseListener.getX() - POS_SCREEN_X),
                                getTileMouseY((int) MouseListener.getY() - POS_SCREEN_Y));

                    }

                }
            }

            if (MouseListener.mouseButtonReleased(GLFW_MOUSE_BUTTON_LEFT)) {
                if (user.getUsingSkill() != 0) {
                    workLeftClick(getTileMouseX((int) MouseListener.getX() - POS_SCREEN_X),
                            getTileMouseY((int) MouseListener.getY() - POS_SCREEN_Y),
                            user.getUsingSkill());

                    user.setUsingSkill(0);
                    Window.INSTANCE.setCursorCrosshair(false);
                }
            }
        }

        // si no agrego esto, pierde el foco al npc selecionado.
        // por ende al comerciar no va a permitir comprar o vender.


        // estamos haciendo click en el inventario?
        if (user.getUserInventory().inInventoryArea()) {
            if (MouseListener.mouseButtonClick(GLFW_MOUSE_BUTTON_LEFT)) {
                // cheakeamos tambien del inventario
                user.getUserInventory().clickInventory();
            }
        }

        // estamos haciendo doble click?
        if (MouseListener.mouseButtonDoubleClick(GLFW_MOUSE_BUTTON_LEFT)) {
            user.getUserInventory().dobleClickInventory();
        } else if (MouseListener.mouseButtonDoubleClick(GLFW_MOUSE_BUTTON_RIGHT)) {
            doubleClick(getTileMouseX((int) MouseListener.getX() - POS_SCREEN_X), getTileMouseY((int) MouseListener.getY() - POS_SCREEN_Y));
        }


    }

    /**
     * Escucha los eventos del teclado.
     */
    @Override
    public void keyEvents() {
        if (user.isUserComerciando() || !ImGUISystem.INSTANCE.isMainLast()) return;

        // Usando el metodo estatico de Key para obtener la tecla desde el codigo
        final Key key = Key.getKey(KeyHandler.getLastKeyPressed());

        checkWalkKeys();

        if (key == null) return; // ni me gasto si la tecla presionada no existe en nuestro bind.

        if (KeyHandler.isActionKeyJustPressed(key)) {
            // Para que al hablar no ejecute teclas bindeadas y solo permita cerrar nuevamente el sendText
            if (user.isTalking() && key != TALK) return;

            key.playAction();

            switch (key) {

                case REQUEST_REFRESH:
                    if (intervalToUpdatePos.check())
                        requestPositionUpdate();
                    break;

            }
        }
    }

    /**
     * Cierre de la escena.
     */
    @Override
    public void close() {
        visible = false;
    }

    private void checkWalkKeys() {
        if (!user.isUserMoving()) {
            if (!autoMove) {
                if (KeyHandler.getEffectiveMovementKey() != -1) {
                    int keyCode = KeyHandler.getEffectiveMovementKey();
                    if (keyCode == Key.UP.getKeyCode()) user.moveTo(Direction.UP);
                    else if (keyCode == Key.DOWN.getKeyCode()) user.moveTo(Direction.DOWN);
                    else if (keyCode == Key.LEFT.getKeyCode()) user.moveTo(Direction.LEFT);
                    else if (keyCode == Key.RIGHT.getKeyCode()) user.moveTo(Direction.RIGHT);
                }
            } else autoWalk();
        }
    }

    /**
     * Gestiona el movimiento automatico del usuario en una direccion dependiendo de la ultima tecla de direccion presionada.
     */
    private void autoWalk() {
        int keyCode = KeyHandler.getLastMovementKeyPressed();
        if (keyCode == Key.UP.getKeyCode()) user.moveTo(Direction.UP);
        else if (keyCode == Key.DOWN.getKeyCode()) user.moveTo(Direction.DOWN);
        else if (keyCode == Key.LEFT.getKeyCode()) user.moveTo(Direction.LEFT);
        else if (keyCode == Key.RIGHT.getKeyCode()) user.moveTo(Direction.RIGHT);
    }

    /**
     * Dibuja cada capa y objeto del mapa, el personaje, la interfaz y demas.
     */
    private void renderScreen(int tileX, int tileY, int pixelOffsetX, int pixelOffsetY) {
        camera.update(tileX, tileY);

        renderLayer(1, pixelOffsetX, pixelOffsetY);
        renderLayer(2, pixelOffsetX, pixelOffsetY);
        renderLayer(3, pixelOffsetX, pixelOffsetY);

        // Dialogs
        Dialogs.renderDialogs(camera, pixelOffsetX, pixelOffsetY);

        this.updateEffectCeiling();

        if (alphaCeiling > 0.0f) {
            renderLayer(4, pixelOffsetX, pixelOffsetY);
        }

        Dialogs.updateDialogs();
        Rain.INSTANCE.render(weather.getWeatherColor());
    }

    /**
     * Dibujamos la capa que se desea
     *
     * Orden:
     * Layer 1 - Piso (Se dibuja de una manera más óptima que las demás capas)
     * Layer 2 - graficos por encima del suelo (Costas, objetos de 32x32px, etc.)
     * Layer 3 - objetos con más tamaño, personaje y graficos por encima del personaje.
     * Dialogos - dialogos de los personajes.
     * Layer 4 - Techos
     */
    private void renderLayer(final int layer, int pixelOffsetX, int pixelOffsetY) {
        if (layer == 1) { //  floor.
            for (int y = camera.getScreenminY(); y <= camera.getScreenmaxY(); y++) {
                int x;
                for (x = camera.getScreenminX(); x <= camera.getScreenmaxX(); x++) {
                    final GrhInfo grh = mapData[x][y].getLayer(layer);

                    final int drawX = POS_SCREEN_X + (camera.getScreenX() - 1) * TILE_PIXEL_SIZE + pixelOffsetX;
                    final int drawY = POS_SCREEN_Y + (camera.getScreenY() - 1) * TILE_PIXEL_SIZE + pixelOffsetY;

                    if (grh.getGrhIndex() > 1) { // no dibujar texturas con grafico negro xd (MAS FPS).
                        drawTexture(grh, drawX, drawY, true, true, false, 1.0f, weather.getWeatherColor());
                    }

                    camera.incrementScreenX();
                }
                camera.setScreenX(camera.getScreenX() - x + camera.getScreenminX());
                camera.incrementScreenY();
            }

        } else { // another layer

            camera.setScreenY(camera.getMinYOffset() - TILE_BUFFER_SIZE);
            for (int y = camera.getMinY(); y <= camera.getMaxY(); y++) {
                camera.setScreenX(camera.getMinXOffset() - TILE_BUFFER_SIZE);
                for (int x = camera.getMinX(); x <= camera.getMaxX(); x++) {

                    final MapData tile = mapData[x][y];
                    GrhInfo grh = tile.getLayer(layer);

                    final int drawX = POS_SCREEN_X + camera.getScreenX() * TILE_PIXEL_SIZE + pixelOffsetX;
                    final int drawY = POS_SCREEN_Y + camera.getScreenY() * TILE_PIXEL_SIZE + pixelOffsetY;

                    switch (layer) {
                        case 2:
                            if (grh.getGrhIndex() != 0) {
                                drawTexture(grh, drawX, drawY, true, true, false, 1.0f, weather.getWeatherColor());
                            }

                            grh = tile.getObjGrh();
                            if (grh.getGrhIndex() != 0) {
                                if (grhData[grh.getGrhIndex()].getPixelWidth() == TILE_PIXEL_SIZE && grhData[grh.getGrhIndex()].getPixelHeight() == TILE_PIXEL_SIZE) {
                                    drawTexture(grh, drawX, drawY, true, true, false, 1.0f, weather.getWeatherColor());
                                }
                            }

                            break;

                        case 3: // LAYER 3, CHARACTERS & OBJECTS > 32x32
                            grh = tile.getObjGrh();
                            if (grh.getGrhIndex() != 0) {
                                if (grhData[grh.getGrhIndex()].getPixelWidth() != TILE_PIXEL_SIZE && grhData[grh.getGrhIndex()].getPixelHeight() != TILE_PIXEL_SIZE) {
                                    drawTexture(grh, drawX, drawY, true, true, false, 1.0f, weather.getWeatherColor());
                                }
                            }

                            if (tile.getCharIndex() != 0) {
                                drawCharacter(tile.getCharIndex(), drawX, drawY, weather.getWeatherColor());
                            }

                            grh = tile.getLayer(3);
                            if (grh.getGrhIndex() != 0) {
                                drawTexture(grh, drawX, drawY, true, true, false, 1.0f, weather.getWeatherColor());
                            }

                            break;

                        case 4:
                            if (grh.getGrhIndex() != 0) {
                                drawTexture(grh, drawX, drawY, true, true, false, alphaCeiling, weather.getWeatherColor());
                            }

                            break;
                    }

                    camera.incrementScreenX();
                }
                camera.incrementScreenY();
            }

        }
    }


    /**
     * Detecta si el usuario esta debajo del techo. Si es asi, se desvanecera y en caso contrario re aparece.
     */
    private void updateEffectCeiling() {
        if (user.isUnderCeiling()) {
            if (alphaCeiling > 0.0f) alphaCeiling -= 0.5f * deltaTime;
        } else {
            if (alphaCeiling < 1.0f) alphaCeiling += 0.5f * deltaTime;
        }
    }

    /**
     * Detecta si tenemos el mouse adentro del "render MainViewPic".
     */
    private boolean inGameArea() {
        if (MouseListener.getX() < POS_SCREEN_X || MouseListener.getX() > POS_SCREEN_X + SCREEN_SIZE_X) return false;
        if (MouseListener.getY() < POS_SCREEN_Y || MouseListener.getY() > POS_SCREEN_Y + SCREEN_SIZE_Y) return false;
        return true;
    }

    /**
     * @param mouseX: Posicion X del mouse en la pantalla
     * @return: Devuelve la posicion en tile del eje X del mouse. Se utiliza al hacer click izquierdo por el mapa, para
     * interactuar con NPCs, etc.
     */
    private byte getTileMouseX(int mouseX) {
        return (byte) (user.getUserPos().getX() + mouseX / TILE_PIXEL_SIZE - HALF_WINDOW_TILE_WIDTH);
    }

    /**
     * @param mouseY: Posicion X del mouse en la pantalla
     * @return: Devuelve la posicion en tile del eje Y del mouse. Se utiliza al hacer click izquierdo por el mapa, para
     * interactuar con NPCs, etc.
     */
    private byte getTileMouseY(int mouseY) {
        return (byte) (user.getUserPos().getY() + mouseY / TILE_PIXEL_SIZE - HALF_WINDOW_TILE_HEIGHT);
    }

}
