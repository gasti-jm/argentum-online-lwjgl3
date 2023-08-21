package org.aoclient.engine.scenes;

import org.aoclient.engine.game.BindKeys;
import org.aoclient.engine.game.models.E_KeyType;
import org.aoclient.engine.gui.forms.MainGame;
import org.aoclient.engine.listeners.KeyListener;
import org.aoclient.engine.game.UserLogic;
import org.aoclient.engine.listeners.MouseListener;
import org.aoclient.engine.renderer.RGBColor;

import static org.aoclient.connection.Protocol.*;
import static org.aoclient.engine.Engine.forms;
import static org.aoclient.engine.game.models.E_KeyType.*;
import static org.aoclient.engine.game.models.Character.*;
import static org.aoclient.engine.game.models.E_Heading.*;
import static org.aoclient.engine.renderer.Drawn.*;
import static org.aoclient.engine.renderer.FontText.drawText;
import static org.aoclient.engine.scenes.Camera.*;
import static org.aoclient.engine.utils.GameData.*;
import static org.aoclient.engine.utils.Time.*;
import static org.lwjgl.glfw.GLFW.*;

public final class GameScene extends Scene {
    private BindKeys bindKeys;
    private UserLogic user;

    private float offSetCounterX = 0;
    private float offSetCounterY = 0;
    private float alphaCeiling = 1.0f;

    RGBColor ambientColor;
    private boolean autoMove = false;
    private MainGame frm;

    @Override
    public void init() {
        super.init();
        canChangeTo = SceneType.MAIN_SCENE;

        bindKeys = BindKeys.get();
        user = UserLogic.getInstance();
        ambientColor = new RGBColor(1.0f, 1.0f, 1.0f);

        camera.setHalfWindowTileWidth   (( (SCREEN_SIZE_X / TILE_PIXEL_SIZE) / 2 ));
        camera.setHalfWindowTileHeight  (( (SCREEN_SIZE_Y / TILE_PIXEL_SIZE) / 2 ));

        frm = MainGame.get();
    }

    @Override
    public void render() {
        // si el usuario se desconecta debe regresar al menu principal.
        if(!user.isUserConected()) {
            this.close();
        }

        if(!visible) return;

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
                (int)(offSetCounterX), (int)(offSetCounterY));

    }

    @Override
    public void mouseEvents() {
        if (!forms.isEmpty()) return;

        if (MouseListener.mouseButtonClick(GLFW_MOUSE_BUTTON_LEFT)) {
            // intentamos ejecutar las acciones de los botones si es que estan dentro de nuestro
            frm.checkButtons();

            // si estamos haciendo click en el render. (osea fuera de la interface)
            if(inGameArea()) {
                writeLeftClick(getTileMouseX((int) MouseListener.getX() - POS_SCREEN_X), getTileMouseY((int) MouseListener.getY() - POS_SCREEN_Y));
            }

            // cheakeamos tambien del inventario
            user.getUserInventory().clickInventory();
        }

        if(MouseListener.mouseButtonDoubleClick(GLFW_MOUSE_BUTTON_LEFT)) {
            user.getUserInventory().dobleClickInventory();
        } else if(MouseListener.mouseButtonDoubleClick(GLFW_MOUSE_BUTTON_RIGHT)) {
            writeDoubleClick(getTileMouseX((int) MouseListener.getX() - POS_SCREEN_X), getTileMouseY((int) MouseListener.getY() - POS_SCREEN_Y));
        }
    }

    @Override
    public void keyEvents() {
        if (!forms.isEmpty()) return; // darle prioridad a los formularios
        checkBindedKeys();
    }

    @Override
    public void close() {
        this.visible = false;
        frm.close();
    }

    /**
     * Chequea y ejecuta la tecla que fue bindeada.
     */
    private void checkBindedKeys() {
        E_KeyType keyPressed = bindKeys.getKeyPressed(KeyListener.getLastKeyPressed());

        // Caminata!
        if(!user.isUserMoving()) {
            if(!autoMove){
                if (!KeyListener.lastKeysMovedPressed.isEmpty()) {
                    if (KeyListener.lastKeysMovedPressed.get(KeyListener.lastKeysMovedPressed.size() - 1) == bindKeys.getBindedKey(mKeyUp)) {
                        user.moveTo(NORTH);
                    } else if (KeyListener.lastKeysMovedPressed.get(KeyListener.lastKeysMovedPressed.size() - 1) == bindKeys.getBindedKey(mKeyDown)) {
                        user.moveTo(SOUTH);
                    } else if (KeyListener.lastKeysMovedPressed.get(KeyListener.lastKeysMovedPressed.size() - 1) == bindKeys.getBindedKey(mKeyLeft)) {
                        user.moveTo(WEST);
                    } else if (KeyListener.lastKeysMovedPressed.get(KeyListener.lastKeysMovedPressed.size() - 1) == bindKeys.getBindedKey(mKeyRight)) {
                        user.moveTo(EAST);
                    }
                }
            } else {
                autoWalk();
            }
        }

        if(keyPressed == null) return; // ni me gasto si la tecla presionada no existe en nuestro bind.
        if (KeyListener.isKeyReadyForAction(bindKeys.getBindedKey(keyPressed))) {
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

            }
        }

    }

    /**
     * Permite que si caminar automaticamente si el usuario activa la opcion de "autoMove"
     */
    private void autoWalk() {
        E_KeyType keyPressed = bindKeys.getKeyPressed(KeyListener.getLastKeyMovedPressed());
        if(keyPressed == null) return;

        switch(keyPressed) {
            case mKeyUp: user.moveTo(NORTH); break;
            case mKeyDown: user.moveTo(SOUTH); break;
            case mKeyLeft: user.moveTo(WEST); break;
            case mKeyRight: user.moveTo(EAST); break;
        }
    }

    /**
     * @desc: Dibuja cada capa y objeto del mapa, el personaje, la interfaz y demas.
     */
    private void renderScreen(int tileX, int tileY, int PixelOffsetX, int PixelOffsetY) {
        camera.update(tileX, tileY);

        // LAYER 1
        for (int y = camera.getScreenminY(); y <= camera.getScreenmaxY(); y++) {
            int x;
            for(x = camera.getScreenminX(); x <= camera.getScreenmaxX(); x++) {

                if (mapData[x][y].getLayer(1).getGrhIndex() != 0) {
                    drawTexture(mapData[x][y].getLayer(1),
                            POS_SCREEN_X + (camera.getScreenX() - 1) * TILE_PIXEL_SIZE + PixelOffsetX,
                            POS_SCREEN_Y + (camera.getScreenY() - 1) * TILE_PIXEL_SIZE + PixelOffsetY,
                            true, true, false,1.0f, ambientColor);
                }

                camera.incrementScreenX();
            }
            camera.setScreenX(camera.getScreenX() - x + camera.getScreenminX() );
            camera.incrementScreenY();
        }


        // LAYER 2 & OBJECTS 32x32
        camera.setScreenY(camera.getMinYOffset() - TILE_BUFFER_SIZE);
        for (int y = camera.getMinY(); y <= camera.getMaxY(); y++) {
            camera.setScreenX(camera.getMinXOffset() - TILE_BUFFER_SIZE);
            for(int x = camera.getMinX(); x <= camera.getMaxX(); x++) {

                if (mapData[x][y].getLayer(2).getGrhIndex() != 0) {
                    drawTexture(mapData[x][y].getLayer(2),
                            POS_SCREEN_X + camera.getScreenX() * TILE_PIXEL_SIZE + PixelOffsetX,
                            POS_SCREEN_Y + camera.getScreenY() * TILE_PIXEL_SIZE + PixelOffsetY,
                            true, true, false,1.0f, ambientColor);
                }

                if (mapData[x][y].getObjGrh().getGrhIndex() != 0) {
                    if(grhData[mapData[x][y].getObjGrh().getGrhIndex()].getPixelWidth() == TILE_PIXEL_SIZE &&
                        grhData[mapData[x][y].getObjGrh().getGrhIndex()].getPixelHeight() == TILE_PIXEL_SIZE) {

                        drawTexture(mapData[x][y].getObjGrh(),
                                POS_SCREEN_X + camera.getScreenX() * TILE_PIXEL_SIZE + PixelOffsetX,
                                POS_SCREEN_Y + camera.getScreenY() * TILE_PIXEL_SIZE + PixelOffsetY,
                                true, true, false,1.0f, ambientColor);
                    }
                }

                camera.incrementScreenX();
            }
            camera.incrementScreenY();
        }

        // LAYER 3, CHARACTERS & OBJECTS > 32x32
        camera.setScreenY(camera.getMinYOffset() - TILE_BUFFER_SIZE);
        for (int y = camera.getMinY(); y <= camera.getMaxY(); y++) {
            camera.setScreenX(camera.getMinXOffset() - TILE_BUFFER_SIZE);
            for(int x = camera.getMinX(); x <= camera.getMaxX(); x++) {

                if (mapData[x][y].getObjGrh().getGrhIndex() != 0) {
                    if(grhData[mapData[x][y].getObjGrh().getGrhIndex()].getPixelWidth() != TILE_PIXEL_SIZE &&
                            grhData[mapData[x][y].getObjGrh().getGrhIndex()].getPixelHeight() != TILE_PIXEL_SIZE) {

                        drawTexture(mapData[x][y].getObjGrh(),
                                POS_SCREEN_X + camera.getScreenX() * TILE_PIXEL_SIZE + PixelOffsetX,
                                POS_SCREEN_Y + camera.getScreenY() * TILE_PIXEL_SIZE + PixelOffsetY,
                                true, true, false,1.0f, ambientColor);
                    }
                }

                if (mapData[x][y].getCharIndex() != 0) {
                    drawCharacter(mapData[x][y].getCharIndex(),
                            POS_SCREEN_X + camera.getScreenX() * TILE_PIXEL_SIZE + PixelOffsetX,
                            POS_SCREEN_Y + camera.getScreenY() * TILE_PIXEL_SIZE + PixelOffsetY, ambientColor);
                }

                if (mapData[x][y].getLayer(3).getGrhIndex() != 0) {
                    drawTexture(mapData[x][y].getLayer(3),
                            POS_SCREEN_X + camera.getScreenX() * TILE_PIXEL_SIZE + PixelOffsetX,
                            POS_SCREEN_Y + camera.getScreenY() * TILE_PIXEL_SIZE + PixelOffsetY,
                            true, true, false, 1.0f, ambientColor);
                }

                camera.incrementScreenX();
            }
            camera.incrementScreenY();
        }

        // LAYER 4
        checkEffectCeiling();
        camera.setScreenY(camera.getMinYOffset() - TILE_BUFFER_SIZE);
        for (int y = camera.getMinY(); y <= camera.getMaxY(); y++) {
            camera.setScreenX(camera.getMinXOffset() - TILE_BUFFER_SIZE);
            for (int x = camera.getMinX(); x <= camera.getMaxX(); x++) {

                if (mapData[x][y].getLayer(4).getGrhIndex() > 0) {
                    drawTexture(mapData[x][y].getLayer(4),
                            POS_SCREEN_X + camera.getScreenX() * TILE_PIXEL_SIZE + PixelOffsetX,
                            POS_SCREEN_Y + camera.getScreenY() * TILE_PIXEL_SIZE + PixelOffsetY,
                            true, true, false, alphaCeiling, ambientColor);
                }

                camera.incrementScreenX();
            }
            camera.incrementScreenY();
        }

        frm.render();
        user.getUserInventory().drawInventory();
        showFPS();
    }

    /**
     * @desc: Detecta si el usuario esta debajo del techo, si es asi se desvanecera
     *        el techo, caso contrario re aparece.
     */
    private void checkEffectCeiling(){
        if(user.isUnderCeiling()) {
            if (alphaCeiling > 0.0f){
                alphaCeiling -= 0.5f * deltaTime;
            }
        } else {
            if (alphaCeiling < 1.0f){
                alphaCeiling += 0.5f * deltaTime;
            }
        }
    }

    /**
     * @desc: Mostramos y dibujamos en texto la cantidad de FPS que se van actualizando.
     */
    private void showFPS() {
        final String txtFPS = String.valueOf(FPS);
        drawText(txtFPS, (SCREEN_SIZE_X - getSizeText(txtFPS) / 2) - 90, 3, ambientColor, 0, true, false, false);
    }

    private boolean inGameArea() {
        if (MouseListener.getX() < POS_SCREEN_X || MouseListener.getX() > POS_SCREEN_X + SCREEN_SIZE_X)
            return false;

        if (MouseListener.getY() < POS_SCREEN_Y || MouseListener.getY() > POS_SCREEN_Y + SCREEN_SIZE_Y)
            return false;

        return true;
    }

    private byte getTileMouseX(int mouseX) {
        return (byte) (user.getUserPos().getX() + mouseX / TILE_PIXEL_SIZE - camera.getHalfWindowTileWidth());
    }

    private byte getTileMouseY(int mouseY) {
        return  (byte) (user.getUserPos().getY() +  mouseY / TILE_PIXEL_SIZE - camera.getHalfWindowTileHeight());
    }


}
