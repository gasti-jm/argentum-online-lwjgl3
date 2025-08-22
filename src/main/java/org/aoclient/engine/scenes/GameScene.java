package org.aoclient.engine.scenes;

import org.aoclient.engine.Window;
import org.aoclient.engine.game.*;
import org.aoclient.engine.game.console.Console;
import org.aoclient.engine.game.console.FontStyle;
import org.aoclient.engine.game.models.Direction;
import org.aoclient.engine.game.models.Key;
import org.aoclient.engine.game.models.Skill;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FCantidad;
import org.aoclient.engine.gui.forms.FMain;
import org.aoclient.engine.listeners.KeyHandler;
import org.aoclient.engine.listeners.MouseListener;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.Protocol;
import org.aoclient.network.protocol.command.execution.CommandExecutor;

import static org.aoclient.engine.game.IntervalTimer.INT_SENTRPU;
import static org.aoclient.engine.game.models.Character.drawCharacter;
import static org.aoclient.engine.game.models.Key.TALK;
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

    private final IntervalTimer intervalToUpdatePos = new IntervalTimer(INT_SENTRPU);
    private final User user = User.INSTANCE;
    private Weather weather;// color de ambiente.
    private float offSetCounterX = 0;
    private float offSetCounterY = 0;
    private float alphaCeiling = 1.0f;
    private boolean autoMove = false;
    private FMain frmMain;

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
        this.checkBindedKeys();
    }

    /**
     * Cierre de la escena.
     */
    @Override
    public void close() {
        visible = false;
    }

    /**
     * Chequea y ejecuta la tecla que fue bindeada.
     */
    private void checkBindedKeys() {
        if (user.isUserComerciando() || !ImGUISystem.INSTANCE.isMainLast()) return;

        // Usando el metodo estatico de Key para obtener la tecla desde el codigo
        final Key key = Key.getKey(KeyHandler.getLastKeyPressed());

        checkWalkKeys();

        if (key == null) return; // ni me gasto si la tecla presionada no existe en nuestro bind.

        if (KeyHandler.isActionKeyJustPressed(key)) {

            // Para que al hablar no ejecute teclas bindeadas y solo permita cerrar nuevamente el sendText
            if (user.isTalking() && key != TALK) return;

            switch (key) {
                case USE_OBJECT:
                    user.getUserInventory().useItem();
                    break;
                case GET_OBJECT:
                    pickUp();
                    break;
                case ATTACK:
                    attack();
                    break;
                case EQUIP_OBJECT:
                    user.getUserInventory().equipItem();
                    break;
                case AUTO_MOVE:
                    autoMove = !autoMove;
                    break;
                case DROP_OBJECT:
                    ImGUISystem.INSTANCE.show(new FCantidad());
                    break;
                case TALK:
                    // TODO: Es para evitar bugs y no se te trabe todo el juego cuando tenes algun frm abierto
                    //  y sin querer pulsar para hablar.
                    if (ImGUISystem.INSTANCE.isMainLast()) {
                        if (!frmMain.getSendText().isBlank() && user.isTalking()) {
                            if (!frmMain.getSendText().startsWith("/")) {
                                Console.INSTANCE.addMsgToConsole("[" + User.INSTANCE.getUserName().toLowerCase() + "] " + frmMain.getSendText(), FontStyle.REGULAR, new RGBColor(1f, 1f, 1f));
                                Protocol.talk(frmMain.getSendText());
                            } else CommandExecutor.INSTANCE.execute(frmMain.getSendText());
                        }
                        user.setTalking(!user.isTalking());
                        frmMain.clearSendTxt();
                    }
                    break;
                case HIDE:
                    work(Skill.CONCEALMENT.getId());
                    break;
                case STEAL:
                    work(Skill.THEFT.getId());
                    break;
                case REQUEST_REFRESH:
                    if (intervalToUpdatePos.check()) requestPositionUpdate();
                    break;
                case EXIT_GAME:
                    quit();
                    break;
            }
        }

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

        renderFirstLayer(pixelOffsetX, pixelOffsetY);
        renderSecondLayer(pixelOffsetX, pixelOffsetY);
        renderThirdLayer(pixelOffsetX, pixelOffsetY);

        // Dialogs
        camera.setScreenY(camera.getMinYOffset() - TILE_BUFFER_SIZE);
        for (int y = camera.getMinY(); y <= camera.getMaxY(); y++) {
            camera.setScreenX(camera.getMinXOffset() - TILE_BUFFER_SIZE);
            for (int x = camera.getMinX(); x <= camera.getMaxX(); x++) {
                Dialogs.renderDialogs(camera, x, y, pixelOffsetX, pixelOffsetY);
                camera.incrementScreenX();
            }
            camera.incrementScreenY();
        }

        renderFourthLayer(pixelOffsetX, pixelOffsetY);

        Dialogs.updateDialogs();
        Rain.INSTANCE.render(weather.getWeatherColor());
    }


    private void renderFirstLayer(final int pixelOffsetX, final int pixelOffsetY) {
        for (int y = camera.getScreenminY(); y <= camera.getScreenmaxY(); y++) {
            int x;
            for (x = camera.getScreenminX(); x <= camera.getScreenmaxX(); x++) {
                if (mapData[x][y].getLayer(1).getGrhIndex() != 0) {
                    drawTexture(mapData[x][y].getLayer(1),
                            POS_SCREEN_X + (camera.getScreenX() - 1) * TILE_PIXEL_SIZE + pixelOffsetX,
                            POS_SCREEN_Y + (camera.getScreenY() - 1) * TILE_PIXEL_SIZE + pixelOffsetY,
                            true, true, false, 1.0f, weather.getWeatherColor());
                }


                camera.incrementScreenX();
            }
            camera.setScreenX(camera.getScreenX() - x + camera.getScreenminX());
            camera.incrementScreenY();
        }
    }

    private void renderSecondLayer(final int pixelOffsetX, final int pixelOffsetY) {
        camera.setScreenY(camera.getMinYOffset() - TILE_BUFFER_SIZE);
        for (int y = camera.getMinY(); y <= camera.getMaxY(); y++) {
            camera.setScreenX(camera.getMinXOffset() - TILE_BUFFER_SIZE);
            for (int x = camera.getMinX(); x <= camera.getMaxX(); x++) {
                if (mapData[x][y].getLayer(2).getGrhIndex() != 0) {
                    drawTexture(mapData[x][y].getLayer(2),
                            POS_SCREEN_X + camera.getScreenX() * TILE_PIXEL_SIZE + pixelOffsetX,
                            POS_SCREEN_Y + camera.getScreenY() * TILE_PIXEL_SIZE + pixelOffsetY,
                            true, true, false, 1.0f, weather.getWeatherColor());
                }
                if (mapData[x][y].getObjGrh().getGrhIndex() != 0) {
                    if (grhData[mapData[x][y].getObjGrh().getGrhIndex()].getPixelWidth() == TILE_PIXEL_SIZE &&
                            grhData[mapData[x][y].getObjGrh().getGrhIndex()].getPixelHeight() == TILE_PIXEL_SIZE) {
                        drawTexture(mapData[x][y].getObjGrh(),
                                POS_SCREEN_X + camera.getScreenX() * TILE_PIXEL_SIZE + pixelOffsetX,
                                POS_SCREEN_Y + camera.getScreenY() * TILE_PIXEL_SIZE + pixelOffsetY,
                                true, true, false, 1.0f, weather.getWeatherColor());
                    }
                }
                camera.incrementScreenX();
            }
            camera.incrementScreenY();
        }
    }

    private void renderThirdLayer(final int pixelOffsetX, final int pixelOffsetY) {
        // LAYER 3, CHARACTERS & OBJECTS > 32x32
        camera.setScreenY(camera.getMinYOffset() - TILE_BUFFER_SIZE);
        for (int y = camera.getMinY(); y <= camera.getMaxY(); y++) {
            camera.setScreenX(camera.getMinXOffset() - TILE_BUFFER_SIZE);
            for (int x = camera.getMinX(); x <= camera.getMaxX(); x++) {

                if (mapData[x][y].getObjGrh().getGrhIndex() != 0) {
                    if (grhData[mapData[x][y].getObjGrh().getGrhIndex()].getPixelWidth() != TILE_PIXEL_SIZE &&
                            grhData[mapData[x][y].getObjGrh().getGrhIndex()].getPixelHeight() != TILE_PIXEL_SIZE) {

                        drawTexture(mapData[x][y].getObjGrh(),
                                POS_SCREEN_X + camera.getScreenX() * TILE_PIXEL_SIZE + pixelOffsetX,
                                POS_SCREEN_Y + camera.getScreenY() * TILE_PIXEL_SIZE + pixelOffsetY,
                                true, true, false, 1.0f, weather.getWeatherColor());
                    }
                }

                if (mapData[x][y].getCharIndex() != 0) {
                    drawCharacter(mapData[x][y].getCharIndex(),
                            POS_SCREEN_X + camera.getScreenX() * TILE_PIXEL_SIZE + pixelOffsetX,
                            POS_SCREEN_Y + camera.getScreenY() * TILE_PIXEL_SIZE + pixelOffsetY, weather.getWeatherColor());
                }


                if (mapData[x][y].getLayer(3).getGrhIndex() != 0) {
                    drawTexture(mapData[x][y].getLayer(3),
                            POS_SCREEN_X + camera.getScreenX() * TILE_PIXEL_SIZE + pixelOffsetX,
                            POS_SCREEN_Y + camera.getScreenY() * TILE_PIXEL_SIZE + pixelOffsetY,
                            true, true, false, 1.0f, weather.getWeatherColor());
                }

                camera.incrementScreenX();
            }
            camera.incrementScreenY();
        }
    }

    private void renderFourthLayer(final int pixelOffsetX, final int pixelOffsetY) {
        this.checkEffectCeiling();
        if (alphaCeiling > 0.0f) {
            camera.setScreenY(camera.getMinYOffset() - TILE_BUFFER_SIZE);
            for (int y = camera.getMinY(); y <= camera.getMaxY(); y++) {
                camera.setScreenX(camera.getMinXOffset() - TILE_BUFFER_SIZE);
                for (int x = camera.getMinX(); x <= camera.getMaxX(); x++) {

                    if (mapData[x][y].getLayer(4).getGrhIndex() > 0) {
                        drawTexture(mapData[x][y].getLayer(4),
                                POS_SCREEN_X + camera.getScreenX() * TILE_PIXEL_SIZE + pixelOffsetX,
                                POS_SCREEN_Y + camera.getScreenY() * TILE_PIXEL_SIZE + pixelOffsetY,
                                true, true, false, alphaCeiling, weather.getWeatherColor());
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
    private void checkEffectCeiling() {
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
