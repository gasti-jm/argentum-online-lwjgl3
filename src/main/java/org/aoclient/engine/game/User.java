package org.aoclient.engine.game;

import org.aoclient.engine.game.models.Character;
import org.aoclient.engine.game.models.E_Heading;
import org.aoclient.engine.scenes.Camera;
import org.aoclient.engine.game.models.Position;
import org.aoclient.engine.utils.filedata.*;

import static org.aoclient.engine.scenes.Camera.*;
import static org.aoclient.engine.utils.GameData.*;
import static org.aoclient.engine.utils.GameData.fxData;

public final class User {
    private boolean UserMoving;
    private Position userPos;
    private Position addToUserPos;
    public int lastChar = 0;

    public User () {
        this.UserMoving = false;
        userPos = new Position();
        addToUserPos = new Position();
    }

    public boolean isUserMoving() {
        return UserMoving;
    }

    public void setUserMoving(boolean userMoving) {
        UserMoving = userMoving;
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

    public int makeChar(int charIndex, int body, E_Heading heading, int x, int y) {
        if (charList.isEmpty() || !charList.containsKey(charIndex)) {
            charList.put(charIndex, new Character());
        }

        if (charIndex > lastChar)
            lastChar = charIndex;

        if (charList.get(charIndex).getActive())
            return 0;

        charList.get(charIndex).setName("Saurus");
        charList.get(charIndex).setClanName("<" + "Argentum Online Staff" + ">");
        charList.get(charIndex).setiBody((short) body);
        charList.get(charIndex).setiHead((short) 7);
        charList.get(charIndex).setBody(new BodyData(bodyData[body]));
        charList.get(charIndex).setHead(new HeadData(headData[7]));
        charList.get(charIndex).setWeapon(new WeaponData(weaponData[9]));
        charList.get(charIndex).setShield(new ShieldData(shieldData[1]));
        charList.get(charIndex).setHelmet(new HeadData(helmetsData[3]));
        charList.get(charIndex).setHeading(heading);
        charList.get(charIndex).setMoving(false);
        charList.get(charIndex).setMoveOffsetX(0);
        charList.get(charIndex).setMoveOffsetY(0);
        charList.get(charIndex).getPos().setX(x);
        charList.get(charIndex).getPos().setY(y);
        charList.get(charIndex).setActive(true);
        charList.get(charIndex).setfX(new GrhInfo());

        return charIndex;
    }

    public void refreshAllChars() {
        for (int LoopC = 1; LoopC <= lastChar; LoopC++) {
            if(charList.containsKey(LoopC)){
                if (charList.get(LoopC).getActive()) {
                    mapData[charList.get(LoopC).getPos().getX()][charList.get(LoopC).getPos().getY()].setCharIndex(LoopC);
                }
            }

        }
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

        if (inMapBounds(tX, tY)) {
            // logica que falta
            return;
        } else {
            addToUserPos.setX(x);
            userPos.setX(tX);

            addToUserPos.setY(y);
            userPos.setY(tY);

            UserMoving = true;
        }
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

        charList.get(charIndex).setScrollDirectionX((short) addX);
        charList.get(charIndex).setScrollDirectionY((short)addY);
        charList.get(1).setFxIndex(0);


        /*If UserEstado = 0 Then Call DoPasosFx(CharIndex)

        'areas viejos
        If (nY < MinLimiteY) Or (nY > MaxLimiteY) Or (nX < MinLimiteX) Or (nX > MaxLimiteX) Then
            If CharIndex <> UserCharIndex Then
                Call EraseChar(CharIndex)
            End If
        End If*/
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
}
