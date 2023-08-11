package org.aoclient.engine.scenes;

import org.aoclient.engine.Window;
import org.aoclient.engine.listeners.KeyListener;
import org.aoclient.engine.logic.User;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.engine.renderer.Renderer;
import org.aoclient.engine.utils.GameData;

import java.util.Map;

import static org.aoclient.engine.logic.E_Heading.*;
import static org.aoclient.engine.renderer.Drawn.*;
import static org.aoclient.engine.scenes.Camera.*;
import static org.aoclient.engine.utils.GameData.*;
import static org.aoclient.engine.utils.Time.FPS;
import static org.aoclient.engine.utils.Time.timerTicksPerFrame;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;

public class MainScene extends Scene {
    private User user;

    final int ScrollPixelsPerFrameX = 8;
    final int ScrollPixelsPerFrameY = 8;
    private float OffsetCounterX = 0;
    private float OffsetCounterY = 0;

    RGBColor ambientcolor;

    @Override
    public void init() {
        super.init();

        this.renderer = new Renderer(Window.getInstance().getWidth(), Window.getInstance().getHeight(), 0, 0);
        this.renderer.initialize();

        this.user = new User();
        //user.initialize();

        GameData.loadMap(1);
        mapData[50][50].setCharIndex( (short) user.makeChar(1, 127, SOUTH, 50, 50) );

        user.refreshAllChars();

        user.userPos.setX(50);
        user.userPos.setY(50);

        ambientcolor = new RGBColor(1.0f, 1.0f, 1.0f);

        this.camera.setHalfWindowTileWidth(((Window.getInstance().getWidth() / 32) / 2));
        this.camera.setHalfWindowTileHeight(((Window.getInstance().getHeight() / 32) / 2));
    }

    @Override
    public void keyEvents() {
        if (KeyListener.isKeyPressed(GLFW_KEY_F5)) {
            user.setCharacterFx(1, 2, -1);
        }

        if(!user.UserMoving) {
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
        minY = screenminY - TileBufferSize;
        maxY = screenmaxY + TileBufferSize;
        minX = screenminX - TileBufferSize;
        maxX = screenmaxX + TileBufferSize;

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
                            (ScreenY - 1) * TILE_PIXEL_SIZE + PixelOffsetY, true, true, false, ambientcolor);
                }

                if (mapData[x][y].getLayer(1).getGrhIndex() != 0) {
                    draw(mapData[x][y].getLayer(1),
                            (ScreenX - 1) * TILE_PIXEL_SIZE + PixelOffsetX,
                            (ScreenY - 1) * TILE_PIXEL_SIZE + PixelOffsetY, true, true, false, ambientcolor);
                }
                ScreenX++;
            }
            ScreenX = ScreenX - x + screenminX;
            ScreenY++;
        }

        ScreenY = minYOffset - TileBufferSize;
        for (int y = minY; y <= maxY; y++) {
            ScreenX = minXOffset - TileBufferSize;
            for(int x = minX; x <= maxX; x++) {
                if (mapData[x][y].getObjGrh().getGrhIndex() != 0) {
                    draw(mapData[x][y].getObjGrh(), ScreenX * 32 + PixelOffsetX, ScreenY * 32 + PixelOffsetY, true, true, false, ambientcolor);
                }

                if (mapData[x][y].getCharIndex() > 0) {
                    charRender(mapData[x][y].getCharIndex(), ScreenX * 32 + PixelOffsetX, ScreenY * 32 + PixelOffsetY);
                }

                if (mapData[x][y].getLayer(2).getGrhIndex() != 0) {
                    draw(mapData[x][y].getLayer(2), ScreenX * 32 + PixelOffsetX,
                            ScreenY * 32 + PixelOffsetY, true, true, false, ambientcolor);
                }
                ScreenX++;
            }
            ScreenY++;
        }

        ScreenY = minYOffset - TileBufferSize;
        for (int y = minY; y <= maxY; y++) {
            ScreenX = minXOffset - TileBufferSize;
            for(int x = minX; x <= maxX; x++) {
                if (mapData[x][y].getLayer(3).getGrhIndex() > 0) {
                    draw(mapData[x][y].getLayer(3), ScreenX * 32 + PixelOffsetX,
                            ScreenY * 32 + PixelOffsetY, true, true, false, ambientcolor);
                }
                ScreenX++;
            }
            ScreenY++;
        }

        final String txtFPS = FPS + " FPS";
        drawText(txtFPS, Window.getInstance().getWidth() - getSizeText(txtFPS) - 10, 8, ambientcolor, 0);
    }

    public void charRender(int charIndex, int PixelOffsetX, int PixelOffsetY) {
        boolean moved = false;
        RGBColor color = new RGBColor();

        if (charList.get(charIndex).getMoving() != 0) {
            if (charList.get(charIndex).getScrollDirectionX() != 0) {

                charList.get(charIndex).setMoveOffsetX(charList.get(charIndex).getMoveOffsetX() +
                        ScrollPixelsPerFrameX * sgn(charList.get(charIndex).getScrollDirectionX()) * timerTicksPerFrame);

                if (charList.get(charIndex).getBody().getWalk(charList.get(charIndex).getHeading().ordinal()).getSpeed() > 0.0f) {
                    charList.get(charIndex).getBody().getWalk(charList.get(charIndex).getHeading().ordinal()).setStarted(true);
                }

                charList.get(charIndex).getWeapon().getWeaponWalk(charList.get(charIndex).getHeading().ordinal()).setStarted(true);
                charList.get(charIndex).getShield().getShieldWalk(charList.get(charIndex).getHeading().ordinal()).setStarted(true);

                moved = true;

                if ((sgn(charList.get(charIndex).getScrollDirectionX()) == 1 && charList.get(charIndex).getMoveOffsetX() >= 0) ||
                        (sgn(charList.get(charIndex).getScrollDirectionX()) == -1 && charList.get(charIndex).getMoveOffsetX() <= 0)) {

                    charList.get(charIndex).setMoveOffsetX(0);
                    charList.get(charIndex).setScrollDirectionX(0);
                }
            }

            if (charList.get(charIndex).getScrollDirectionY() != 0) {
                charList.get(charIndex).setMoveOffsetY(charList.get(charIndex).getMoveOffsetY()
                        + ScrollPixelsPerFrameY * sgn(charList.get(charIndex).getScrollDirectionY()) * timerTicksPerFrame);


                if (charList.get(charIndex).getBody().getWalk(charList.get(charIndex).getHeading().ordinal()).getSpeed() > 0.0f) {
                    charList.get(charIndex).getBody().getWalk(charList.get(charIndex).getHeading().ordinal()).setStarted(true);
                }

                charList.get(charIndex).getWeapon().getWeaponWalk(charList.get(charIndex).getHeading().ordinal()).setStarted(true);
                charList.get(charIndex).getShield().getShieldWalk(charList.get(charIndex).getHeading().ordinal()).setStarted(true);

                moved = true;

                if ((sgn(charList.get(charIndex).getScrollDirectionY()) == 1 && charList.get(charIndex).getMoveOffsetY() >= 0)
                        || (sgn(charList.get(charIndex).getScrollDirectionY()) == -1 && charList.get(charIndex).getMoveOffsetY() <= 0)) {
                    charList.get(charIndex).setMoveOffsetY(0);
                    charList.get(charIndex).setScrollDirectionY(0);
                }
            }
        }

        if (!moved) {
            charList.get(charIndex).getBody().getWalk(charList.get(charIndex).getHeading().ordinal()).setStarted(false);
            charList.get(charIndex).getBody().getWalk(charList.get(charIndex).getHeading().ordinal()).setFrameCounter(1);

            charList.get(charIndex).getWeapon().getWeaponWalk(charList.get(charIndex).getHeading().ordinal()).setStarted(false);
            charList.get(charIndex).getWeapon().getWeaponWalk(charList.get(charIndex).getHeading().ordinal()).setFrameCounter(1);

            charList.get(charIndex).getShield().getShieldWalk(charList.get(charIndex).getHeading().ordinal()).setStarted(false);
            charList.get(charIndex).getShield().getShieldWalk(charList.get(charIndex).getHeading().ordinal()).setFrameCounter(1);

            charList.get(charIndex).setMoving(0);
        }

        PixelOffsetX += (int) charList.get(charIndex).getMoveOffsetX();
        PixelOffsetY += (int) charList.get(charIndex).getMoveOffsetY();

        if (charList.get(charIndex).getHead().getHead(charList.get(charIndex).getHeading().ordinal()).getGrhIndex() != 0) {
            if (!charList.get(charIndex).isInvisible()) {

                if (charList.get(charIndex).getBody().getWalk(charList.get(charIndex).getHeading().ordinal()).getGrhIndex() > 0) {
                    draw(charList.get(charIndex).getBody().getWalk(charList.get(charIndex).getHeading().ordinal()),
                            PixelOffsetX, PixelOffsetY, true, true, false, ambientcolor);
                }

                if (charList.get(charIndex).getHead().getHead(charList.get(charIndex).getHeading().ordinal()).getGrhIndex() != 0) {
                    draw(charList.get(charIndex).getHead().getHead(charList.get(charIndex).getHeading().ordinal()),
                            PixelOffsetX + charList.get(charIndex).getBody().getHeadOffset().getX(),
                            PixelOffsetY + charList.get(charIndex).getBody().getHeadOffset().getY(),
                            true, false, false, ambientcolor);

                    if (charList.get(charIndex).getHelmet().getHead(charList.get(charIndex).getHeading().ordinal()).getGrhIndex() != 0) {
                        draw(charList.get(charIndex).getHelmet().getHead(charList.get(charIndex).getHeading().ordinal()),
                                PixelOffsetX + charList.get(charIndex).getBody().getHeadOffset().getX(),
                                PixelOffsetY + charList.get(charIndex).getBody().getHeadOffset().getY() -34,
                                true, false, false, ambientcolor);
                    }

                    if (charList.get(charIndex).getWeapon().getWeaponWalk(charList.get(charIndex).getHeading().ordinal()).getGrhIndex() != 0) {
                        draw(charList.get(charIndex).getWeapon().getWeaponWalk(charList.get(charIndex).getHeading().ordinal()),
                                PixelOffsetX, PixelOffsetY, true, true, false, ambientcolor);
                    }

                    if (charList.get(charIndex).getShield().getShieldWalk(charList.get(charIndex).getHeading().ordinal()).getGrhIndex() != 0) {
                        draw(charList.get(charIndex).getShield().getShieldWalk(charList.get(charIndex).getHeading().ordinal()),
                                PixelOffsetX, PixelOffsetY, true, true, false, ambientcolor);
                    }

                    if (charList.get(charIndex).getName().length() > 0) {
                        if (charList.get(charIndex).getPriv() == 0) {
                            color.setRed(0.0f);
                            color.setGreen(0.5f);
                            color.setBlue(1.0f);
                        }

                        String line = charList.get(charIndex).getName();
                        drawText(line, PixelOffsetX - (getSizeText(line) / 2) + 15, PixelOffsetY + 30, color, 0);

                        line = charList.get(charIndex).getClanName();
                        drawText(line, PixelOffsetX - (getSizeText(line) / 2) + 15, PixelOffsetY + 43, color, 0);
                    }
                }
            }

            // Draw FX
            if (charList.get(charIndex).getFxIndex() != 0) {
                draw(charList.get(charIndex).getfX(),
                        PixelOffsetX + fxData[charList.get(charIndex).getFxIndex()].getOffsetX(),
                        PixelOffsetY + fxData[charList.get(charIndex).getFxIndex()].getOffsetY(),
                        true, true, true, ambientcolor);

                // Check if animation is over
                if (!charList.get(charIndex).getfX().isStarted())
                    charList.get(charIndex).setFxIndex(0);
            }

        } else {
            if (charList.get(charIndex).getBody().getWalk(charList.get(charIndex).getHeading().ordinal()).getGrhIndex() > 0) {
                draw(charList.get(charIndex).getBody().getWalk(charList.get(charIndex).getHeading().ordinal()),
                        PixelOffsetX, PixelOffsetY, true, true, false, ambientcolor);
            }

        }
    }

    private int sgn(short number) {
        if (number == 0) return 0;
        return (number / Math.abs(number));
    }

    @Override
    public void render() {
        if (user.UserMoving) {
            if (user.addToUserPos.getX() != 0) {
                OffsetCounterX -= ScrollPixelsPerFrameX * user.addToUserPos.getX() * timerTicksPerFrame;
                if (Math.abs(OffsetCounterX) >= Math.abs(TILE_PIXEL_SIZE * user.addToUserPos.getX())) {
                    OffsetCounterX = 0;
                    user.addToUserPos.setX(0);
                    user.UserMoving = false;
                }
            }

            if (user.addToUserPos.getY() != 0) {
                OffsetCounterY -= ScrollPixelsPerFrameY * user.addToUserPos.getY() * timerTicksPerFrame;
                if (Math.abs(OffsetCounterY) >= Math.abs(TILE_PIXEL_SIZE * user.addToUserPos.getY())) {
                    OffsetCounterY = 0;
                    user.addToUserPos.setY(0);
                    user.UserMoving = false;
                }
            }
        }

        renderScreen(user.userPos.getX() - user.addToUserPos.getX(),
                user.userPos.getY() - user.addToUserPos.getY(),
                (int)(OffsetCounterX), (int)(OffsetCounterY));
    }
}
