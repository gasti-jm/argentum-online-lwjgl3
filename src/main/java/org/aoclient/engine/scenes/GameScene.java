package org.aoclient.engine.scenes;

import org.aoclient.engine.gui.ElementGUI;
import org.aoclient.engine.gui.elements.ImageGUI;
import org.aoclient.engine.listeners.KeyListener;
import org.aoclient.engine.game.User;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.engine.utils.GameData;

import static org.aoclient.engine.game.models.Character.*;
import static org.aoclient.engine.game.models.E_Heading.*;
import static org.aoclient.engine.renderer.Drawn.*;
import static org.aoclient.engine.scenes.Camera.*;
import static org.aoclient.engine.utils.GameData.*;
import static org.aoclient.engine.utils.Time.*;
import static org.lwjgl.glfw.GLFW.*;

public final class GameScene extends Scene {
    private User user;

    private float offSetCounterX = 0;
    private float offSetCounterY = 0;
    private float alphaCeiling = 1.0f;

    RGBColor ambientColor;
    private boolean autoMove = false;

    private ElementGUI main;


    @Override
    public void init() {
        super.init();
        canChangeTo = SceneType.MAIN_SCENE;

        user = User.getInstance();

        GameData.loadMap(1);
        mapData[50][50].setCharIndex(makeChar(1, 127, SOUTH, 50, 50));

        refreshAllChars();

        user.getUserPos().setX(50);
        user.getUserPos().setY(50);

        ambientColor = new RGBColor(1.0f, 1.0f, 1.0f);

        camera.setHalfWindowTileWidth   (( (SCREEN_SIZE_X / TILE_PIXEL_SIZE) / 2 ));
        camera.setHalfWindowTileHeight  (( (SCREEN_SIZE_Y / TILE_PIXEL_SIZE) / 2 ));


        // Interface
        main = new ImageGUI();
        main.init();
        main.loadTextures("VentanaPrincipal.png");
    }

    @Override
    public void keyEvents() {
        if (KeyListener.isKeyReadyForAction(GLFW_KEY_F5)) {
            user.setCharacterFx(1, 7, -1);
        }

        if (KeyListener.isKeyReadyForAction(GLFW_KEY_F)) {
            GameData.loadMap(34);
        }

        if (KeyListener.isKeyReadyForAction(GLFW_KEY_TAB)) {
            KeyListener.setLastKeyPressed(0);
            autoMove = !autoMove;
        }

        if(!user.isUserMoving()) {
            if(!autoMove){
                if (!KeyListener.lastKeysPressed.isEmpty()) {
                    switch (KeyListener.lastKeysPressed.get(KeyListener.lastKeysPressed.size() - 1)) {
                        case GLFW_KEY_W:
                            if (KeyListener.isKeyPressed(GLFW_KEY_W)) user.moveTo(NORTH);
                            break;

                        case GLFW_KEY_S:
                            if (KeyListener.isKeyPressed(GLFW_KEY_S)) user.moveTo(SOUTH);
                            break;

                        case GLFW_KEY_A:
                            if (KeyListener.isKeyPressed(GLFW_KEY_A)) user.moveTo(WEST);
                            break;

                        case GLFW_KEY_D:
                            if (KeyListener.isKeyPressed(GLFW_KEY_D)) user.moveTo(EAST);
                            break;
                    }
                }

            } else {
                autoWalk();
            }

        }
    }

    /**
     * Permite que si caminar automaticamente si el usuario activa la opcion de "autoMove"
     */
    private void autoWalk() {
        switch(KeyListener.getLastKeyPressed()) {
            case GLFW_KEY_W:
                user.moveTo(NORTH);
                break;

            case GLFW_KEY_S:
                user.moveTo(SOUTH);
                break;

            case GLFW_KEY_A:
                user.moveTo(WEST);
                break;

            case GLFW_KEY_D:
                user.moveTo(EAST);
                break;
        }
    }

    /**
     * @desc: Dibuja cada capa y objeto del mapa, el personaje, la interfaz y demas.
     */
    private void renderScreen(int tileX, int tileY, int PixelOffsetX, int PixelOffsetY) {
        camera.update(tileX, tileY);

        for (int y = camera.getScreenminY(); y <= camera.getScreenmaxY(); y++) {
            int x;
            for(x = camera.getScreenminX(); x <= camera.getScreenmaxX(); x++) {

                if (mapData[x][y].getLayer(0).getGrhIndex() != 0) {
                    draw(mapData[x][y].getLayer(0),
                            POS_SCREEN_X + (camera.getScreenX() - 1) * TILE_PIXEL_SIZE + PixelOffsetX,
                            POS_SCREEN_Y + (camera.getScreenY() - 1) * TILE_PIXEL_SIZE + PixelOffsetY,
                            true, true, false,1.0f, ambientColor);
                }

                camera.incrementScreenX();
            }
            camera.setScreenX(camera.getScreenX() - x + camera.getScreenminX() );
            camera.incrementScreenY();
        }

        camera.setScreenY(camera.getMinYOffset() - TILE_BUFFER_SIZE);
        for (int y = camera.getMinY(); y <= camera.getMaxY(); y++) {
            camera.setScreenX(camera.getMinXOffset() - TILE_BUFFER_SIZE);
            for(int x = camera.getMinX(); x <= camera.getMaxX(); x++) {

                if (mapData[x][y].getLayer(1).getGrhIndex() != 0) {
                    draw(mapData[x][y].getLayer(1),
                            POS_SCREEN_X + camera.getScreenX() * TILE_PIXEL_SIZE + PixelOffsetX,
                            POS_SCREEN_Y + camera.getScreenY() * TILE_PIXEL_SIZE + PixelOffsetY,
                            true, true, false,1.0f, ambientColor);
                }

                if (mapData[x][y].getObjGrh().getGrhIndex() != 0) {
                    if(grhData[mapData[x][y].getObjGrh().getGrhIndex()].getPixelWidth() == TILE_PIXEL_SIZE &&
                        grhData[mapData[x][y].getObjGrh().getGrhIndex()].getPixelHeight() == TILE_PIXEL_SIZE) {

                        draw(mapData[x][y].getObjGrh(),
                                POS_SCREEN_X + camera.getScreenX() * TILE_PIXEL_SIZE + PixelOffsetX,
                                POS_SCREEN_Y + camera.getScreenY() * TILE_PIXEL_SIZE + PixelOffsetY,
                                true, true, false,1.0f, ambientColor);
                    }
                }

                camera.incrementScreenX();
            }
            camera.incrementScreenY();
        }

        camera.setScreenY(camera.getMinYOffset() - TILE_BUFFER_SIZE);
        for (int y = camera.getMinY(); y <= camera.getMaxY(); y++) {
            camera.setScreenX(camera.getMinXOffset() - TILE_BUFFER_SIZE);
            for(int x = camera.getMinX(); x <= camera.getMaxX(); x++) {

                if (mapData[x][y].getObjGrh().getGrhIndex() != 0) {
                    if(grhData[mapData[x][y].getObjGrh().getGrhIndex()].getPixelWidth() != TILE_PIXEL_SIZE &&
                            grhData[mapData[x][y].getObjGrh().getGrhIndex()].getPixelHeight() != TILE_PIXEL_SIZE) {

                        draw(mapData[x][y].getObjGrh(),
                                POS_SCREEN_X + camera.getScreenX() * TILE_PIXEL_SIZE + PixelOffsetX,
                                POS_SCREEN_Y + camera.getScreenY() * TILE_PIXEL_SIZE + PixelOffsetY,
                                true, true, false,1.0f, ambientColor);
                    }
                }

                if (mapData[x][y].getCharIndex() > 0) {
                    charRender(mapData[x][y].getCharIndex(),
                            POS_SCREEN_X + camera.getScreenX() * TILE_PIXEL_SIZE + PixelOffsetX,
                            POS_SCREEN_Y + camera.getScreenY() * TILE_PIXEL_SIZE + PixelOffsetY, ambientColor);
                }

                if (mapData[x][y].getLayer(2).getGrhIndex() != 0) {
                    draw(mapData[x][y].getLayer(2),
                            POS_SCREEN_X + camera.getScreenX() * TILE_PIXEL_SIZE + PixelOffsetX,
                            POS_SCREEN_Y + camera.getScreenY() * TILE_PIXEL_SIZE + PixelOffsetY,
                            true, true, false, 1.0f, ambientColor);
                }

                camera.incrementScreenX();
            }
            camera.incrementScreenY();
        }

        checkEffectCeiling();
        camera.setScreenY(camera.getMinYOffset() - TILE_BUFFER_SIZE);
        for (int y = camera.getMinY(); y <= camera.getMaxY(); y++) {
            camera.setScreenX(camera.getMinXOffset() - TILE_BUFFER_SIZE);
            for (int x = camera.getMinX(); x <= camera.getMaxX(); x++) {

                if (mapData[x][y].getLayer(3).getGrhIndex() > 0) {
                    draw(mapData[x][y].getLayer(3),
                            POS_SCREEN_X + camera.getScreenX() * TILE_PIXEL_SIZE + PixelOffsetX,
                            POS_SCREEN_Y + camera.getScreenY() * TILE_PIXEL_SIZE + PixelOffsetY,
                            true, true, false, alphaCeiling, ambientColor);
                }

                camera.incrementScreenX();
            }
            camera.incrementScreenY();
        }

        main.render();
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
        drawText(txtFPS, (SCREEN_SIZE_X - getSizeText(txtFPS) / 2) - 90, 3, ambientColor, 0, false);
    }

    @Override
    public void render() {
        if(!visible) return;

        if (user.isUserMoving()) {
            if (user.getAddToUserPos().getX() != 0) {
                offSetCounterX -= charList.get(1).getWalkingSpeed() * user.getAddToUserPos().getX() * timerTicksPerFrame;
                if (Math.abs(offSetCounterX) >= Math.abs(TILE_PIXEL_SIZE * user.getAddToUserPos().getX())) {
                    offSetCounterX = 0;
                    user.getAddToUserPos().setX(0);
                    user.setUserMoving(false);
                }
            }

            if (user.getAddToUserPos().getY() != 0) {
                offSetCounterY -= charList.get(1).getWalkingSpeed() * user.getAddToUserPos().getY() * timerTicksPerFrame;
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
}
