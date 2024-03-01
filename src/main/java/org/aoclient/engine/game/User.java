package org.aoclient.engine.game;

import org.aoclient.engine.game.inventory.UserInventory;
import org.aoclient.engine.game.models.E_Heading;
import org.aoclient.engine.game.models.Position;

import static org.aoclient.connection.Protocol.writeChangeHeading;
import static org.aoclient.connection.Protocol.writeWalk;
import static org.aoclient.engine.Sound.*;
import static org.aoclient.engine.game.models.Character.*;
import static org.aoclient.engine.game.models.E_Heading.*;
import static org.aoclient.engine.scenes.Camera.*;
import static org.aoclient.engine.utils.GameData.*;

public final class User {
    private static User instance;

    private UserInventory userInventory;
    // private InventorySpells inventorySpells;

    private boolean underCeiling;
    private boolean userMoving;

    // mapa
    public short userMap;
    private Position userPos;
    private Position addToUserPos;
    private short userCharIndex;

    // conexion
    private boolean userConected;

    // areas
    private int minLimiteX, maxLimiteX;
    private int minLimiteY, maxLimiteY;

    // stats del usuario
    public short userMaxHP;
    public short userMinHP;
    public short userMaxMAN;
    public short userMinMAN;
    public short userMaxSTA;
    public short userMinSTA;
    public int userPasarNivel;
    public int userExp;

    public byte userWeaponEqpSlot;
    public byte userArmourEqpSlot;
    public byte userHelmEqpSlot;
    public byte userShieldEqpSlot;

    /**
     * @desc: Constructor privado por singleton.
     */
    private User() {
        userPos = new Position();
        addToUserPos = new Position();
        userInventory = new UserInventory();
    }

    /**
     *
     * @return Mismo objeto (Patron de diseño Singleton)
     */
    public static User get(){
        if(instance == null) {
            instance = new User();
        }

        return instance;
    }

    /**
     *
     * @param nHeading direccion pasada por parametro
     * @desc Mueve la camara hacia una direccion.
     */
    public void moveScreen(E_Heading nHeading) {
        int x = 0, y = 0;

        switch (nHeading) {
            case NORTH  : y = -1; break;
            case EAST   : x = 1;  break;
            case SOUTH  : y = 1;  break;
            case WEST   : x = -1; break;
        }

        final int tX = userPos.getX() + x;
        final int tY = userPos.getY() + y;

        if (!(tX < minXBorder || tX > maxXBorder || tY < minYBorder || tY > maxYBorder)) {
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

    /**
     *
     * @param charIndex Numero de identificador de personaje
     * @param nHeading Direccion del personaje
     *
     * @desc Mueve el personaje segun la direccion establecida en "nHeading".
     */
    public void moveCharbyHead(short charIndex, E_Heading nHeading) {
        int addX = 0, addY = 0;

        switch (nHeading) {
            case NORTH  : addY = -1;    break;
            case EAST   : addX = 1;     break;
            case SOUTH  : addY = 1;     break;
            case WEST   : addX = -1;    break;
        }

        final int x = charList[charIndex].getPos().getX();
        final int y = charList[charIndex].getPos().getY();
        final int nX = x + addX;
        final int nY = y + addY;

        mapData[nX][nY].setCharIndex(charIndex);
        charList[charIndex].getPos().setX(nX);
        charList[charIndex].getPos().setY(nY);
        mapData[x][y].setCharIndex(0);

        charList[charIndex].setMoveOffsetX(-1 * (TILE_PIXEL_SIZE * addX));
        charList[charIndex].setMoveOffsetY(-1 * (TILE_PIXEL_SIZE * addY));

        charList[charIndex].setMoving(true);
        charList[charIndex].setHeading(nHeading);

        charList[charIndex].setScrollDirectionX(addX);
        charList[charIndex].setScrollDirectionY(addY);
        charList[charIndex].setFxIndex(0);

        doPasosFx(charIndex);

        // areas viejos
        if ((nY < minLimiteY) || (nY > maxLimiteY) || (nX < minLimiteX) || (nX > maxLimiteX)) {
            if(charIndex != userCharIndex) {
                eraseChar(charIndex);
            }
        }
    }

    /**
     *
     * @desc: Actualiza las areas de vision de objetos y personajes.
     */
    public void areaChange(int x, int y) {
        minLimiteX = (x / 9 - 1) * 9;
        maxLimiteX = minLimiteX + 26;
        minLimiteY = (y / 9 - 1) * 9;
        maxLimiteY = minLimiteY + 26;

        for (int loopX = 1; loopX <= 100; loopX++) {
            for (int loopY = 1; loopY <= 100; loopY++) {

                if ((loopY < minLimiteY) || (loopY > maxLimiteY) || (loopX < minLimiteX) || (loopX > maxLimiteX)) {
                    // Erase NPCs
                    if(mapData[loopX][loopY].getCharIndex() > 0) {
                        if(mapData[loopX][loopY].getCharIndex() != userCharIndex) {
                            eraseChar(mapData[loopX][loopY].getCharIndex());
                        }
                    }

                    // Erase Objs
                    mapData[loopX][loopY].getObjGrh().setGrhIndex(0);
                }

            }
        }

        refreshAllChars();
    }

    /**
     *
     * @param x Posicion X del usuario.
     * @param y Posicion Y del usuario.
     * @return True si se encuentra dentro del limite del mapa, false en caso contrario.
     */
    public boolean inMapBounds(int x, int y) {
        return (x < TILE_BUFFER_SIZE || x > XMaxMapSize - TILE_BUFFER_SIZE ||
                y < TILE_BUFFER_SIZE || y > YMaxMapSize - TILE_BUFFER_SIZE);
    }

    /**
     *
     * @param x Posicion X del usuario.
     * @param y Posicion Y del usuario.
     * @return True si el usuario puede caminar hacia cierta posicion, false caso contrario.
     */
    private boolean moveToLegalPos(int x, int y) {
        // Limite del mapa
        if (x < minXBorder || x > maxXBorder || y < minYBorder || y > maxYBorder)
            return false;

        // Tile Bloqueado?
        if (mapData[x][y].getBlocked())
            return false;

        final int charIndex = mapData[x][y].getCharIndex();

        // ¿Hay un personaje?
        if (charIndex > 0) {
            if (mapData[userPos.getX()][userPos.getY()].getBlocked()) {
                return false;
            }

            return false;
            /*
            If .iHead <> CASPER_HEAD And .iBody <> FRAGATA_FANTASMAL Then
                Exit Function
            Else
                ' No puedo intercambiar con un casper que este en la orilla (Lado tierra)
                If HayAgua(UserPos.X, UserPos.Y) Then
                    If Not HayAgua(X, Y) Then Exit Function
                Else
                    ' No puedo intercambiar con un casper que este en la orilla (Lado agua)
                    If HayAgua(X, Y) Then Exit Function
                End If

                ' Los admins no pueden intercambiar pos con caspers cuando estan invisibles
                If charlist(UserCharIndex).priv > 0 And charlist(UserCharIndex).priv < 6 Then
                    If charlist(UserCharIndex).invisible = True Then Exit Function
                End If
            End If
             */

        }

        /*
            If UserNavegando <> HayAgua(X, Y) Then
                Exit Function
            End If
         */

        return true;
    }

    /**
     *
     * @param charIndex Numero de identificador de personaje.
     * @param fx Numero de efecto FX.
     * @param loops Tiempo del efecto FX.
     *
     * @desc Establece un efecto FX en un personaje.
     */
    public void setCharacterFx(int charIndex, int fx, int loops) {
        charList[charIndex].setFxIndex(fx);

        if (charList[charIndex].getFxIndex() > 0){
            initGrh(charList[charIndex].getfX(), fxData[fx].getAnimacion(), true);
            charList[charIndex].getfX().setLoops(loops);
        }
    }

    /**
     *
     * @param charIndex Numero de identificador de personaje
     * @param nX Posicion X a actualizar
     * @param nY Posicion Y a actualizar
     *
     *
     * @desc Mueve el personaje segun la direccion establecida en "nX" y "nY".
     */
    public void moveCharbyPos(short charIndex, int nX, int nY) {
        final int x = charList[charIndex].getPos().getX();
        final int y = charList[charIndex].getPos().getY();

        final int addX = nX - x;
        final int addY = nY - y;

        if(sgn( (short) addX) == 1) {
            charList[charIndex].setHeading(EAST);
        } else if(sgn( (short) addX) == -1) {
            charList[charIndex].setHeading(WEST);
        } else if (sgn( (short) addY) == -1) {
            charList[charIndex].setHeading(NORTH);
        } else if (sgn( (short) addY) == 1) {
            charList[charIndex].setHeading(SOUTH);
        }

        mapData[nX][nY].setCharIndex(charIndex);
        charList[charIndex].getPos().setX(nX);
        charList[charIndex].getPos().setY(nY);
        mapData[x][y].setCharIndex(0);

        charList[charIndex].setMoveOffsetX(-1 * (TILE_PIXEL_SIZE * addX));
        charList[charIndex].setMoveOffsetY(-1 * (TILE_PIXEL_SIZE * addY));

        charList[charIndex].setMoving(true);

        charList[charIndex].setScrollDirectionX( sgn( (short) addX));
        charList[charIndex].setScrollDirectionY( sgn( (short) addY));

        /*
            'parche para que no medite cuando camina
            If .FxIndex = FxMeditar.CHICO Or .FxIndex = FxMeditar.GRANDE Or .FxIndex = FxMeditar.MEDIANO Or .FxIndex = FxMeditar.XGRANDE Or .FxIndex = FxMeditar.XXGRANDE Then
                .FxIndex = 0
            End If
         */

        // If Not EstaPCarea(CharIndex) Then Call Dialogos.RemoveDialog(CharIndex)

        if ((nY < minLimiteY) || (nY > maxLimiteY) || (nX < minLimiteX) || (nX > maxLimiteX)) {
            if(charIndex != userCharIndex) {
                eraseChar(charIndex);
            }
        }
    }

    /**
     *
     * @param direction
     * @desc Mueve nuestro personaje a una cierta direccion si es posible.
     */
    public void moveTo(E_Heading direction) {
        boolean legalOk = false;

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

        if (legalOk){
            writeWalk(direction);
            moveScreen(direction);
            moveCharbyHead(userCharIndex, direction);

//            FrmMain.get().lblCoords.setText(userMap +
//                    " X: " + userPos.getX() + " Y: " + userPos.getY());

        } else {
            if(charList[userCharIndex].getHeading() != direction) {
                writeChangeHeading(direction);
            }
        }
    }

    /**
     *
     * @param charIndex Numero de identificador de personaje
     * @desc Realiza sonidos de caminata segun el estado del personaje
     *
     * EN PROGRESO....
     */
    public void doPasosFx(int charIndex) {
        if (!charList[charIndex].isDead()) {
            if(charList[charIndex].isPie()) {
                playSound(SND_PASOS1);
                charList[charIndex].setPie(false);
            } else {
                playSound(SND_PASOS2);
                charList[charIndex].setPie(true);
            }
        }
    }

    /**
     *
     * @return Getter del atributo userMoving
     */
    public boolean isUserMoving() {
        return userMoving;
    }

    /**
     *
     * @return Setter del atributo userMoving
     */
    public void setUserMoving(boolean userMoving) {
        this.userMoving = userMoving;
    }

    /**
     *
     * @return Getter del atributo userPos
     */
    public Position getUserPos() {
        return userPos;
    }

    /**
     *
     * @return Getter del atributo addToUserPos
     */
    public Position getAddToUserPos() {
        return addToUserPos;
    }

    /**
     *
     * @return Getter del atributo underCeiling
     */
    public boolean isUnderCeiling() {
        return underCeiling;
    }



    /**
     *
     * @return Setter del atributo userMap
     */
    public void setUserMap(short userMap) {
        this.userMap = userMap;
    }


    /**
     *
     * @return Getter del atributo userConected
     */
    public boolean isUserConected() {
        return userConected;
    }

    /**
     *
     * @return Setter del atributo userConected
     */
    public void setUserConected(boolean userConected) {
        this.userConected = userConected;
    }

    /**
     *
     * @return Getter del atributo userCharIndex
     */
    public short getUserCharIndex() {
        return userCharIndex;
    }

    /**
     *
     * @return Setter del atributo userCharIndex
     */
    public void setUserCharIndex(short userCharIndex) {
        this.userCharIndex = userCharIndex;
    }

    /**
     *
     * @return Getter del atributo userInventory
     */
    public UserInventory getUserInventory() {
        return userInventory;
    }
}
