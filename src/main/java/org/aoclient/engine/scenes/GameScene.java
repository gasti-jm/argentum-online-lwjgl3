package org.aoclient.engine.scenes;

import org.aoclient.engine.Window;
import org.aoclient.engine.game.*;
import org.aoclient.engine.game.models.E_KeyType;
import org.aoclient.engine.game.models.E_Skills;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FCantidad;
import org.aoclient.engine.gui.forms.FMain;
import org.aoclient.engine.listeners.KeyListener;
import org.aoclient.engine.listeners.MouseListener;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.ProtocolCmdParse;

import static org.aoclient.engine.game.IntervalTimer.INT_SENTRPU;
import static org.aoclient.engine.game.models.Character.drawCharacter;
import static org.aoclient.engine.game.models.E_Heading.*;
import static org.aoclient.engine.game.models.E_KeyType.*;
import static org.aoclient.engine.renderer.Drawn.drawTexture;
import static org.aoclient.engine.scenes.Camera.*;
import static org.aoclient.engine.utils.GameData.*;
import static org.aoclient.engine.utils.Time.deltaTime;
import static org.aoclient.engine.utils.Time.timerTicksPerFrame;
import static org.aoclient.network.protocol.Protocol.*;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

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
    RGBColor ambientColor; // color de ambiente.
    private BindKeys bindKeys;
    private User user;
    private Rain rain;
    private float offSetCounterX = 0;
    private float offSetCounterY = 0;
    private float alphaCeiling = 1.0f;
    private boolean autoMove = false;
    private FMain frmMain;
    private ProtocolCmdParse protocolCmdParse;

    @Override
    public void init() {
        super.init();

        protocolCmdParse = ProtocolCmdParse.getInstance();

        canChangeTo = SceneType.MAIN_SCENE;
        bindKeys = BindKeys.get();
        user = User.get();
        rain = Rain.get();
        ambientColor = new RGBColor(1.0f, 1.0f, 1.0f);
        frmMain = new FMain();

        ImGUISystem.get().addFrm(frmMain);
    }

    @Override
    public void render() {
        // si el usuario se desconecta debe regresar al menu principal.
        if (!user.isUserConected()) {
            frmMain.close();
            this.close();
        }

        if (!visible) return;

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
     * @desc: Escucha los eventos del mouse.
     */
    @Override
    public void mouseEvents() {
        if(user.isUserComerciando()) return;

        // Estamos haciendo click en el render?
        if (inGameArea()) {
            if (MouseListener.mouseButtonClick(GLFW_MOUSE_BUTTON_LEFT)) {
                if (user.getUsingSkill() == 0) {
                    writeLeftClick(getTileMouseX((int) MouseListener.getX() - POS_SCREEN_X), getTileMouseY((int) MouseListener.getY() - POS_SCREEN_Y));
                } else {
                    writeWorkLeftClick(getTileMouseX((int) MouseListener.getX() - POS_SCREEN_X),
                            getTileMouseY((int) MouseListener.getY() - POS_SCREEN_Y),
                            user.getUsingSkill());

                    user.setUsingSkill(0);
                    Window.get().setCursorCrosshair(false);
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
                writeDoubleClick(getTileMouseX((int) MouseListener.getX() - POS_SCREEN_X), getTileMouseY((int) MouseListener.getY() - POS_SCREEN_Y));
            }


    }

    /**
     * @desc: Escucha los eventos del teclado.
     */
    @Override
    public void keyEvents() {
        if (KeyListener.isKeyPressed(bindKeys.getBindedKey(E_KeyType.mKeyExitGame))) writeQuit();
        this.checkBindedKeys();
    }

    /**
     * Cierre de la escena.
     */
    @Override
    public void close() {
        this.visible = false;
    }

    /**
     * Chequea y ejecuta la tecla que fue bindeada.
     */
    private void checkBindedKeys() {
        if(user.isUserComerciando()) return;

        final E_KeyType keyPressed = bindKeys.getKeyPressed(KeyListener.getLastKeyPressed());

        this.checkWalkKeys();

        if (keyPressed == null) return; // ni me gasto si la tecla presionada no existe en nuestro bind.

        if (KeyListener.isKeyReadyForAction(bindKeys.getBindedKey(keyPressed))) {

            // Para que al hablar no ejecute teclas bindeadas y solo permita cerrar nuevamente el sendText
            if (User.get().isTalking() && keyPressed != mKeyTalk) return;

            switch (keyPressed) {
                case mKeyUseObject:
                    user.getUserInventory().useItem();
                    break;
                case mKeyGetObject:
                    writePickUp();
                    break;
                case mKeyAttack:
                    writeAttack();
                    break;
                case mKeyEquipObject:
                    user.getUserInventory().equipItem();
                    break;
                case mKeyAutoMove:
                    KeyListener.setLastKeyMovedPressed(0);
                    autoMove = !autoMove;
                    break;
                case mKeyDropObject:
                    ImGUISystem.get().show(new FCantidad());
                    break;
                case mKeyTalk:
                    if (!ImGUISystem.get().isFormVisible(FCantidad.class.getSimpleName())) {
                        if (User.get().isTalking()) {
                            // send msg
                            // No vamos a mandar paquetes con datos vacios.
                            if (!frmMain.getSendText().isBlank()) protocolCmdParse.parseUserCommand(frmMain.getSendText());
                            User.get().setTalking(false);
                        } else User.get().setTalking(true);
                        frmMain.clearSendTxt();
                    }
                    break;
                case mKeyHide:
                    writeWork(E_Skills.OCULTARSE.getValue());
                    break;
                case mKeySteal:
                    writeWork(E_Skills.ROBAR.getValue());
                    break;
                case mKeyRequestRefresh:
                    if (intervalToUpdatePos.check()) writeRequestPositionUpdate();
                    break;
            }
        }

    }

    private void checkWalkKeys() {
        // Caminata!
        if (!user.isUserMoving()) {
            if (!autoMove) {
                if (!KeyListener.lastKeysMovedPressed.isEmpty()) {
                    if (KeyListener.lastKeysMovedPressed.get(KeyListener.lastKeysMovedPressed.size() - 1) == bindKeys.getBindedKey(mKeyUp))
                        user.moveTo(NORTH);
                    else if (KeyListener.lastKeysMovedPressed.get(KeyListener.lastKeysMovedPressed.size() - 1) == bindKeys.getBindedKey(mKeyDown))
                        user.moveTo(SOUTH);
                    else if (KeyListener.lastKeysMovedPressed.get(KeyListener.lastKeysMovedPressed.size() - 1) == bindKeys.getBindedKey(mKeyLeft))
                        user.moveTo(WEST);
                    else if (KeyListener.lastKeysMovedPressed.get(KeyListener.lastKeysMovedPressed.size() - 1) == bindKeys.getBindedKey(mKeyRight))
                        user.moveTo(EAST);
                }
            } else autoWalk();
        }
    }

    /**
     * Permite que si caminar automaticamente si el usuario activa la opcion de "autoMove"
     */
    private void autoWalk() {
        E_KeyType keyPressed = bindKeys.getKeyPressed(KeyListener.getLastKeyMovedPressed());
        if (keyPressed == null) return;
        switch (keyPressed) {
            case mKeyUp:
                user.moveTo(NORTH);
                break;
            case mKeyDown:
                user.moveTo(SOUTH);
                break;
            case mKeyLeft:
                user.moveTo(WEST);
                break;
            case mKeyRight:
                user.moveTo(EAST);
                break;
        }
    }

    /**
     * @desc: Dibuja cada capa y objeto del mapa, el personaje, la interfaz y demas.
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
        rain.render(ambientColor);
    }


    private void renderFirstLayer(final int pixelOffsetX, final int pixelOffsetY) {
        for (int y = camera.getScreenminY(); y <= camera.getScreenmaxY(); y++) {
            int x;
            for (x = camera.getScreenminX(); x <= camera.getScreenmaxX(); x++) {
                if (mapData[x][y].getLayer(1).getGrhIndex() != 0) {
                    drawTexture(mapData[x][y].getLayer(1),
                            POS_SCREEN_X + (camera.getScreenX() - 1) * TILE_PIXEL_SIZE + pixelOffsetX,
                            POS_SCREEN_Y + (camera.getScreenY() - 1) * TILE_PIXEL_SIZE + pixelOffsetY,
                            true, true, false, 1.0f, ambientColor);
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
                            true, true, false, 1.0f, ambientColor);
                }
                if (mapData[x][y].getObjGrh().getGrhIndex() != 0) {
                    if (grhData[mapData[x][y].getObjGrh().getGrhIndex()].getPixelWidth() == TILE_PIXEL_SIZE &&
                            grhData[mapData[x][y].getObjGrh().getGrhIndex()].getPixelHeight() == TILE_PIXEL_SIZE) {
                        drawTexture(mapData[x][y].getObjGrh(),
                                POS_SCREEN_X + camera.getScreenX() * TILE_PIXEL_SIZE + pixelOffsetX,
                                POS_SCREEN_Y + camera.getScreenY() * TILE_PIXEL_SIZE + pixelOffsetY,
                                true, true, false, 1.0f, ambientColor);
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
                                true, true, false, 1.0f, ambientColor);
                    }
                }

                if (mapData[x][y].getCharIndex() != 0) {
                    drawCharacter(mapData[x][y].getCharIndex(),
                            POS_SCREEN_X + camera.getScreenX() * TILE_PIXEL_SIZE + pixelOffsetX,
                            POS_SCREEN_Y + camera.getScreenY() * TILE_PIXEL_SIZE + pixelOffsetY, ambientColor);
                }


                if (mapData[x][y].getLayer(3).getGrhIndex() != 0) {
                    drawTexture(mapData[x][y].getLayer(3),
                            POS_SCREEN_X + camera.getScreenX() * TILE_PIXEL_SIZE + pixelOffsetX,
                            POS_SCREEN_Y + camera.getScreenY() * TILE_PIXEL_SIZE + pixelOffsetY,
                            true, true, false, 1.0f, ambientColor);
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
                                true, true, false, alphaCeiling, ambientColor);
                    }

                    camera.incrementScreenX();
                }
                camera.incrementScreenY();
            }
        }
    }


    /**
     * @desc: Detecta si el usuario esta debajo del techo. Si es asi, se desvanecera y en caso contrario re aparece.
     */
    private void checkEffectCeiling() {
        if (user.isUnderCeiling()) {
            if (alphaCeiling > 0.0f) alphaCeiling -= 0.5f * deltaTime;
        } else {
            if (alphaCeiling < 1.0f) alphaCeiling += 0.5f * deltaTime;
        }
    }

    /**
     * @desc: Detecta si tenemos el mouse adentro del "render MainViewPic".
     */
    private boolean inGameArea() {
        if (MouseListener.getX() < POS_SCREEN_X || MouseListener.getX() > POS_SCREEN_X + SCREEN_SIZE_X) return false;
        if (MouseListener.getY() < POS_SCREEN_Y || MouseListener.getY() > POS_SCREEN_Y + SCREEN_SIZE_Y) return false;
        return true;
    }

    /**
     * @param mouseX: Posicion X del mouse en la pantalla
     * @return: Devuelve la posicion en tile del eje X del mouse.
     * @desc: Se utiliza al hacer click izquierdo por el mapa, para interactuar con NPCs, etc.
     */
    private byte getTileMouseX(int mouseX) {
        return (byte) (user.getUserPos().getX() + mouseX / TILE_PIXEL_SIZE - HALF_WINDOW_TILE_WIDTH);
    }

    /**
     * @param mouseY: Posicion X del mouse en la pantalla
     * @return: Devuelve la posicion en tile del eje Y del mouse.
     * @desc: Se utiliza al hacer click izquierdo por el mapa, para interactuar con NPCs, etc.
     */
    private byte getTileMouseY(int mouseY) {
        return (byte) (user.getUserPos().getY() + mouseY / TILE_PIXEL_SIZE - HALF_WINDOW_TILE_HEIGHT);
    }

}
