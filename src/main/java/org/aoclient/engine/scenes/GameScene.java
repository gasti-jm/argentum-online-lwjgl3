package org.aoclient.engine.scenes;

import org.aoclient.engine.Window;
import org.aoclient.engine.listeners.KeyListener;
import org.aoclient.engine.game.User;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.engine.utils.GameData;

import static org.aoclient.engine.game.models.Character.charRender;
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

    RGBColor ambientColor;

    @Override
    public void init() {
        super.init();

        canChangeTo = SceneNames.MAIN_SCENE;

        this.user = new User();

        GameData.loadMap(1);
        mapData[50][50].setCharIndex( (short) user.makeChar(1, 127, SOUTH, 50, 50) );

        user.refreshAllChars();

        user.getUserPos().setX(50);
        user.getUserPos().setY(50);

        ambientColor = new RGBColor(1.0f, 1.0f, 1.0f);

        camera.setHalfWindowTileWidth(((Window.getInstance().getWidth() / 32) / 2));
        camera.setHalfWindowTileHeight(((Window.getInstance().getHeight() / 32) / 2));
    }

    @Override
    public void keyEvents() {
        if (KeyListener.isKeyPressed(GLFW_KEY_F5)) {
            user.setCharacterFx(1, 2, -1);
        }

        if(!user.isUserMoving()) {
            if (KeyListener.isKeyPressed(GLFW_KEY_D)) {
                user.moveTo(EAST);
            } else if (KeyListener.isKeyPressed(GLFW_KEY_A)) {
                user.moveTo(WEST);
            } else if (KeyListener.isKeyPressed(GLFW_KEY_W)) {
                user.moveTo(NORTH);
            } else if (KeyListener.isKeyPressed(GLFW_KEY_S)) {
                user.moveTo(SOUTH);
            }
        }
    }

    private void renderScreen(int tileX, int tileY, int PixelOffsetX, int PixelOffsetY) {
        camera.update(tileX, tileY);

        for (int y = camera.getScreenminY(); y <= camera.getScreenmaxY(); y++) {
            int x;
            for(x = camera.getScreenminX(); x <= camera.getScreenmaxX(); x++) {

                if (mapData[x][y].getLayer(0).getGrhIndex() != 0) {
                    draw(mapData[x][y].getLayer(0),
                            (camera.getScreenX() - 1) * TILE_PIXEL_SIZE + PixelOffsetX,
                            (camera.getScreenY() - 1) * TILE_PIXEL_SIZE + PixelOffsetY, true, true, false,1.0f, ambientColor);
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
                            camera.getScreenX() * TILE_PIXEL_SIZE + PixelOffsetX,
                            camera.getScreenY() * TILE_PIXEL_SIZE + PixelOffsetY, true, true, false,1.0f, ambientColor);
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
                    draw(mapData[x][y].getObjGrh(),
                            camera.getScreenX() * TILE_PIXEL_SIZE + PixelOffsetX,
                            camera.getScreenY() * TILE_PIXEL_SIZE + PixelOffsetY,
                            true, true, false,1.0f, ambientColor);
                }

                if (mapData[x][y].getCharIndex() > 0) {
                    charRender(mapData[x][y].getCharIndex(),
                            camera.getScreenX() * TILE_PIXEL_SIZE + PixelOffsetX,
                            camera.getScreenY() * TILE_PIXEL_SIZE + PixelOffsetY, ambientColor);
                }

                if (mapData[x][y].getLayer(2).getGrhIndex() != 0) {
                    draw(mapData[x][y].getLayer(2),
                            camera.getScreenX() * TILE_PIXEL_SIZE + PixelOffsetX,
                            camera.getScreenY() * TILE_PIXEL_SIZE + PixelOffsetY,
                            true, true, false, 1.0f, ambientColor);
                }

                camera.incrementScreenX();
            }
            camera.incrementScreenY();
        }

        camera.setScreenY(camera.getMinYOffset() - TILE_BUFFER_SIZE);
        for (int y = camera.getMinY(); y <= camera.getMaxY(); y++) {
            camera.setScreenX(camera.getMinXOffset() - TILE_BUFFER_SIZE);
            for(int x = camera.getMinX(); x <= camera.getMaxX(); x++) {

                if (mapData[x][y].getLayer(3).getGrhIndex() > 0) {
                    draw(mapData[x][y].getLayer(3),
                            camera.getScreenX() * TILE_PIXEL_SIZE + PixelOffsetX,
                            camera.getScreenY() * TILE_PIXEL_SIZE + PixelOffsetY,
                            true, true, false,1.0f, ambientColor);
                }

                camera.incrementScreenX();
            }
            camera.incrementScreenY();
        }

        showFPS();
    }

    private void showFPS() {
        final String txtFPS = FPS + " FPS";
        drawText(txtFPS, Window.getInstance().getWidth() - getSizeText(txtFPS) - 10, 8, ambientColor, 0);
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
