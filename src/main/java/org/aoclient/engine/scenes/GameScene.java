package org.aoclient.engine.scenes;

import org.aoclient.engine.Window;
import org.aoclient.engine.listeners.KeyListener;
import org.aoclient.engine.logic.User;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.engine.utils.GameData;

import static org.aoclient.engine.logic.models.Character.charRender;
import static org.aoclient.engine.logic.models.E_Heading.*;
import static org.aoclient.engine.renderer.Drawn.*;
import static org.aoclient.engine.scenes.Camera.*;
import static org.aoclient.engine.utils.GameData.*;
import static org.aoclient.engine.utils.Time.*;
import static org.lwjgl.glfw.GLFW.*;

public final class GameScene extends Scene {
    private User user;

    private float offSetCounterX = 0;
    private float offSetCounterY = 0;

    RGBColor ambientcolor;

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

        ambientcolor = new RGBColor(1.0f, 1.0f, 1.0f);

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

    private void renderScreen(int tilex, int tiley, int PixelOffsetX, int PixelOffsetY) {
        // esto hay que agregarlo a la clase Camera.
        int screenminY, screenmaxY;
        int screenminX, screenmaxX;
        int minY, maxY;
        int minX, maxX;
        int ScreenX = 0, ScreenY = 0;
        int minXOffset = 0, minYOffset = 0;

        // esto es un rango de vision segun la pantalla y donde este parado el user
        screenminY = tiley - camera.getHalfWindowTileHeight();
        screenmaxY = tiley + camera.getHalfWindowTileHeight();
        screenminX = tilex - camera.getHalfWindowTileWidth();
        screenmaxX = tilex + camera.getHalfWindowTileWidth();

        // este es el rango minimo que se va a recorrer
        minY = screenminY - TILE_BUFFER_SIZE;
        maxY = screenmaxY + TILE_BUFFER_SIZE;
        minX = screenminX - TILE_BUFFER_SIZE;
        maxX = screenmaxX + TILE_BUFFER_SIZE;

        if (minY < XMinMapSize) {
            minYOffset = YMinMapSize - minY;
            minY = YMinMapSize;
        }

        if (maxY > YMaxMapSize) maxY = YMaxMapSize;

        if (minX < XMinMapSize) {
            minXOffset = XMinMapSize - minX;
            minX = XMinMapSize;
        }

        if (maxX > XMaxMapSize) maxX = XMaxMapSize;

        if (screenminY > YMinMapSize) {
            screenminY--;
        } else {
            screenminY = 1;
            ScreenY = 1;
        }

        if (screenmaxY < YMaxMapSize) screenmaxY++;

        if (screenminX > XMinMapSize) {
            screenminX--;
        } else {
            screenminX = 1;
            ScreenX = 1;
        }

        if (screenmaxX < XMaxMapSize) screenmaxX++;

        for (int y = screenminY; y <= screenmaxY; y++) {
            int x;
            for(x = screenminX; x <= screenmaxX; x++) {
                if (mapData[x][y].getLayer(0).getGrhIndex() != 0) {
                    draw(mapData[x][y].getLayer(0),
                            (ScreenX - 1) * TILE_PIXEL_SIZE + PixelOffsetX,
                            (ScreenY - 1) * TILE_PIXEL_SIZE + PixelOffsetY, true, true, false,1.0f, ambientcolor);
                }

                ScreenX++;
            }
            ScreenX = ScreenX - x + screenminX;
            ScreenY++;
        }

        ScreenY = minYOffset - TILE_BUFFER_SIZE;
        for (int y = minY; y <= maxY; y++) {
            ScreenX = minXOffset - TILE_BUFFER_SIZE;
            for(int x = minX; x <= maxX; x++) {

                if (mapData[x][y].getLayer(1).getGrhIndex() != 0) {
                    draw(mapData[x][y].getLayer(1),
                            ScreenX * TILE_PIXEL_SIZE + PixelOffsetX,
                            ScreenY * TILE_PIXEL_SIZE + PixelOffsetY, true, true, false,1.0f, ambientcolor);
                }

                ScreenX++;
            }
            ScreenY++;
        }


        ScreenY = minYOffset - TILE_BUFFER_SIZE;
        for (int y = minY; y <= maxY; y++) {
            ScreenX = minXOffset - TILE_BUFFER_SIZE;
            for(int x = minX; x <= maxX; x++) {

                if (mapData[x][y].getObjGrh().getGrhIndex() != 0) {
                    draw(mapData[x][y].getObjGrh(), ScreenX * 32 + PixelOffsetX, ScreenY * 32 + PixelOffsetY, true, true, false,1.0f, ambientcolor);
                }

                if (mapData[x][y].getCharIndex() > 0) {
                    charRender(mapData[x][y].getCharIndex(), ScreenX * 32 + PixelOffsetX, ScreenY * 32 + PixelOffsetY, ambientcolor);
                }

                if (mapData[x][y].getLayer(2).getGrhIndex() != 0) {
                    draw(mapData[x][y].getLayer(2), ScreenX * 32 + PixelOffsetX,
                            ScreenY * 32 + PixelOffsetY, true, true, false, 1.0f, ambientcolor);
                }

                ScreenX++;
            }
            ScreenY++;
        }

        ScreenY = minYOffset - TILE_BUFFER_SIZE;
        for (int y = minY; y <= maxY; y++) {
            ScreenX = minXOffset - TILE_BUFFER_SIZE;
            for(int x = minX; x <= maxX; x++) {
                if (mapData[x][y].getLayer(3).getGrhIndex() > 0) {
                    draw(mapData[x][y].getLayer(3), ScreenX * 32 + PixelOffsetX,
                            ScreenY * 32 + PixelOffsetY, true, true, false,1.0f, ambientcolor);
                }
                ScreenX++;
            }
            ScreenY++;
        }

        showFPS();
    }

    private void showFPS() {
        final String txtFPS = FPS + " FPS";
        drawText(txtFPS, Window.getInstance().getWidth() - getSizeText(txtFPS) - 10, 8, ambientcolor, 0);
    }

    @Override
    public void render() {

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
