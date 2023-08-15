package org.aoclient.engine.game;

import org.aoclient.engine.game.models.Character;
import org.aoclient.engine.game.models.E_Heading;
import org.aoclient.engine.scenes.Camera;
import org.aoclient.engine.game.models.Position;
import org.aoclient.engine.utils.filedata.*;

import static org.aoclient.engine.Sound.*;
import static org.aoclient.engine.game.models.Character.eraseChar;
import static org.aoclient.engine.game.models.Character.refreshAllChars;
import static org.aoclient.engine.scenes.Camera.*;
import static org.aoclient.engine.utils.GameData.*;
import static org.aoclient.engine.utils.GameData.fxData;

public final class User {
    private static User instance;

    private boolean underCeiling;
    private boolean userMoving;

    private Position userPos;
    private Position addToUserPos;
    private int lastChar = 0;

    // areas
    private int minLimiteX, maxLimiteX;
    private int minLimiteY, maxLimiteY;


    private User () {
        userPos = new Position();
        addToUserPos = new Position();
    }

    public static User getInstance(){
        if(instance == null) {
            instance = new User();
        }

        return instance;
    }

    private void moveScreen(E_Heading nHeading) {
        int x = 0, y = 0;

        switch (nHeading) {
            case NORTH  : y = -1; break;
            case EAST   : x = 1;  break;
            case SOUTH  : y = 1;  break;
            case WEST   : x = -1; break;
        }

        final int tX = userPos.getX() + x;
        final int tY = userPos.getY() + y;

        if (!inMapBounds(tX, tY)) {
            addToUserPos.setX(x);
            userPos.setX(tX);

            addToUserPos.setY(y);
            userPos.setY(tY);

            userMoving = true;

            underCeiling = checkUnderCeiling(mapData[userPos.getX()][userPos.getY()].getTrigger());
        }
    }

    /**
     *
     * @desc: Checkea si estamos bajo techo segun el trigger en donde esta parado el usuario.
     */
    private boolean checkUnderCeiling(short trigger) {
        return trigger == 1 || trigger == 2 || trigger == 4;
    }

    public void moveCharbyHead(int charIndex, E_Heading nHeading) {
        int addX = 0, addY = 0;

        final int x = charList.get(charIndex).getPos().getX();
        final int y = charList.get(charIndex).getPos().getY();

        switch (nHeading) {
            case NORTH  : addY = -1;    break;
            case EAST   : addX = 1;     break;
            case SOUTH  : addY = 1;     break;
            case WEST   : addX = -1;    break;
        }

        final int nX = x + addX;
        final int nY = y + addY;

        mapData[x][y].setCharIndex(0);
        mapData[nX][nY].setCharIndex( charIndex);
        charList.get(charIndex).getPos().setX(nX);
        charList.get(charIndex).getPos().setY(nY);

        charList.get(charIndex).setMoveOffsetX(-1 * (Camera.TILE_PIXEL_SIZE * addX));
        charList.get(charIndex).setMoveOffsetY(-1 * (Camera.TILE_PIXEL_SIZE * addY));

        charList.get(charIndex).setMoving(true);

        charList.get(charIndex).setScrollDirectionX(addX);
        charList.get(charIndex).setScrollDirectionY(addY);
        charList.get(charIndex).setFxIndex(0);

        doPasosFx(charIndex);

        // areas viejos
        if ((nY < minLimiteY) || (nY > maxLimiteY) || (nX < minLimiteX) || (nX > maxLimiteX)) {
//            if(charIndex != userCharIndex) {
//                eraseChar(charIndex);
//            }
        }

    }

    /**
     *
     * @desc: Cambio de area... (INCOMPLETO)
     */
    public void areaChange(int x, int y) {
        minLimiteX = (x / TILE_BUFFER_SIZE - 1) * TILE_BUFFER_SIZE;
        maxLimiteX = minLimiteX + 26;
        minLimiteY = (y / TILE_BUFFER_SIZE - 1) * TILE_BUFFER_SIZE;
        maxLimiteY = minLimiteY + 26;

        for (int loopX = 0; loopX < 100; loopX++) {
            for (int loopY = 0; loopY < 100; loopY++) {
                if ((loopY < minLimiteY) || (loopY > maxLimiteY) || (loopX < minLimiteX) || (loopX > maxLimiteX)) {

                    // Erase NPCs
                    if(mapData[loopX][loopY].getCharIndex() > 0) {
//                        if(mapData[loopX][loopY].getCharIndex() != userCharIndex) {
//                            eraseChar(mapData[loopX][loopY].getCharIndex());
//                        }

                        // Erase Objs
                        mapData[loopX][loopY].getObjGrh().setGrhIndex(0);

                    }

                }
            }
        }

        refreshAllChars();
    }

    private boolean inMapBounds(int x, int y) {
        return (x < TILE_BUFFER_SIZE || x > XMaxMapSize - TILE_BUFFER_SIZE ||
                y < TILE_BUFFER_SIZE || y > YMaxMapSize - TILE_BUFFER_SIZE);
    }

    private boolean moveToLegalPos(int x, int y) {
        final int charIndex = mapData[x][y].getCharIndex();

        // Limite del mapa
        if (inMapBounds(x, y))
            return false;

        // Tile Bloqueado?
        if (mapData[x][y].getBlocked()) {
            return false;
        }

        // ¿Hay un personaje?
        if (charIndex > 0) {
            if (mapData[userPos.getX()][userPos.getY()].getBlocked()) {
                return false;
            }
        }

        return true;
    }

    public void setCharacterFx(int charIndex, int fx, int loops) {
        charList.get(charIndex).setFxIndex(fx);

        if (charList.get(charIndex).getFxIndex() > 0){
            initGrh(charList.get(charIndex).getfX(), fxData[fx].getAnimacion(), true);
            charList.get(charIndex).getfX().setLoops(loops);
        }
    }

    public void moveTo(E_Heading direction) {
        boolean legalOk = true;

        switch (direction){
            case NORTH:
                legalOk = moveToLegalPos(userPos.getX(), userPos.getY() - 1);
                break;

            case EAST:
                legalOk = moveToLegalPos(userPos.getX() + 1, userPos.getY());
                break;

            case SOUTH:
                legalOk = moveToLegalPos(userPos.getX(), userPos.getY() + 1);
                break;

            case WEST:
                legalOk = moveToLegalPos(userPos.getX() - 1, userPos.getY());
                break;
        }

        charList.get(lastChar).setHeading(direction);

        if (legalOk){
            moveScreen(direction);
            moveCharbyHead(lastChar, direction);
        }
    }

    private void doPasosFx(int charIndex) {
        if (!charList.get(charIndex).isDead()) {
            if(charList.get(charIndex).isPie()) {
                playSound(SND_PASOS1);
                charList.get(charIndex).setPie(false);
            } else {
                playSound(SND_PASOS2);
                charList.get(charIndex).setPie(true);
            }
        }
    }

    public boolean isUserMoving() {
        return userMoving;
    }

    public void setUserMoving(boolean userMoving) {
        this.userMoving = userMoving;
    }

    public Position getUserPos() {
        return userPos;
    }

    public void setUserPos(Position userPos) {
        this.userPos = userPos;
    }

    public Position getAddToUserPos() {
        return addToUserPos;
    }

    public void setAddToUserPos(Position addToUserPos) {
        this.addToUserPos = addToUserPos;
    }

    public boolean isUnderCeiling() {
        return underCeiling;
    }

    public void setUnderCeiling(boolean underCeiling) {
        this.underCeiling = underCeiling;
    }

    public int getLastChar() {
        return lastChar;
    }

    public void setLastChar(int lastChar) {
        this.lastChar = lastChar;
    }

    public void setConnected() {

    }
}
