package org.aoclient.engine.game;

import org.aoclient.engine.game.inventory.UserInventory;
import org.aoclient.engine.game.models.E_Heading;
import org.aoclient.engine.game.models.Position;

import static org.aoclient.network.Protocol.writeChangeHeading;
import static org.aoclient.network.Protocol.writeWalk;
import static org.aoclient.engine.Sound.*;
import static org.aoclient.engine.game.models.Character.*;
import static org.aoclient.engine.game.models.E_Heading.*;
import static org.aoclient.engine.scenes.Camera.*;
import static org.aoclient.engine.utils.GameData.*;

public final class User {
    private static User instance;

    private final UserInventory userInventory;
    // private InventorySpells inventorySpells;

    private boolean underCeiling;
    private boolean userMoving;
    private boolean userNavegando;

    // mapa
    private short userMap;
    private final Position userPos;
    private final Position addToUserPos;
    private short userCharIndex;

    // conexion
    private boolean userConected;

    // areas
    private int minLimiteX, maxLimiteX;
    private int minLimiteY, maxLimiteY;

    // stats del usuario
    private String userName;
    private short userMaxHP;
    private short userMinHP;
    private short userMaxMAN;
    private short userMinMAN;
    private short userMaxSTA;
    private short userMinSTA;
    private int userPasarNivel;
    private int userExp;
    private int userGLD;
    private byte userLvl;
    private byte userDext;
    private byte userStrg;
    private short userMaxAGU;
    private short userMinAGU;
    private short userMaxHAM;
    private short userMinHAM;

    private String userWeaponEqpHit  = "0/0";
    private String userArmourEqpDef  = "0/0";
    private String userHelmEqpDef    = "0/0";
    private String userShieldEqpDef  = "0/0";

    private byte userWeaponEqpSlot;
    private byte userArmourEqpSlot;
    private byte userHelmEqpSlot;
    private byte userShieldEqpSlot;

    private boolean talking;

    /**
     * @desc: Constructor privado por singleton.
     */
    private User() {
        userPos = new Position();
        addToUserPos = new Position();
        userInventory = new UserInventory();
        this.talking = false;
        this.userNavegando = false;
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

            underCeiling = checkUnderCeiling();
        }
    }

    /**
     *
     * @desc: Checkea si estamos bajo techo segun el trigger en donde esta parado el usuario.
     */
    public boolean checkUnderCeiling() {
        return mapData[userPos.getX()][userPos.getY()].getTrigger() == 1 ||
                mapData[userPos.getX()][userPos.getY()].getTrigger() == 2 ||
                mapData[userPos.getX()][userPos.getY()].getTrigger() == 4;
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

    public boolean estaPCarea(int charIndex) {
        return charList[charIndex].getPos().getX() > userPos.getX() - minXBorder &&
                charList[charIndex].getPos().getX() < userPos.getX() + minXBorder &&
                charList[charIndex].getPos().getY() > userPos.getY() - minYBorder &&
                charList[charIndex].getPos().getY() < userPos.getY() + minYBorder;
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

            if(charList[charIndex].getiHead() != CASPER_HEAD &&
                    charList[charIndex].getiBody() != FRAGATA_FANTASMAL) {
                return false;
            } else {

                // No puedo intercambiar con un casper que este en la orilla (Lado tierra)
                if(hayAgua(userPos.getX(), userPos.getY())) {
                    if(!hayAgua(x, y)) return false;
                } else {
                    // No puedo intercambiar con un casper que este en la orilla (Lado agua)
                    if(hayAgua(x, y)) return false;
                }

                // Los admins no pueden intercambiar pos con caspers cuando estan invisibles
                if(charList[userCharIndex].getPriv() > 0 && charList[userCharIndex].getPriv() < 6) {
                    if(charList[userCharIndex].isInvisible()) return false;
                }

            }

        }

        if(User.get().isUserNavegando() != hayAgua(x, y))
            return false;


        return true;
    }

    public boolean hayAgua(int x, int y) {
        return ((mapData[x][y].getLayer(1).getGrhIndex() >= 1505 && mapData[x][y].getLayer(1).getGrhIndex() <= 1520) ||
                (mapData[x][y].getLayer(1).getGrhIndex() >= 5665 && mapData[x][y].getLayer(1).getGrhIndex() <= 5680) ||
                (mapData[x][y].getLayer(1).getGrhIndex() >= 13547 && mapData[x][y].getLayer(1).getGrhIndex() <= 13562)) &&
                mapData[x][y].getLayer(2).getGrhIndex() == 0;
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

        if(!estaPCarea(charIndex)) {
            Dialogs.removeDialog(charIndex);
        }

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

        if (legalOk && !charList[userCharIndex].isParalizado()){
            writeWalk(direction);
            moveScreen(direction);
            moveCharbyHead(userCharIndex, direction);

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
        if(!User.get().isUserNavegando()) {

            if (!charList[charIndex].isDead()
                    && estaPCarea(charIndex)
                    && (charList[charIndex].getPriv() == 0 || charList[charIndex].getPriv() > 5)) {

                if (charList[charIndex].isPie()) {
                    playSound(SND_PASOS1);
                    charList[charIndex].setPie(false);
                } else {
                    playSound(SND_PASOS2);
                    charList[charIndex].setPie(true);
                }
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

    public Position getAddToUserPos() {
        return addToUserPos;
    }

    public boolean isUnderCeiling() {
        return underCeiling;
    }

    public void setUnderCeiling(boolean underCeiling) {
        this.underCeiling = underCeiling;
    }

    public void setUserMap(short userMap) {
        this.userMap = userMap;
    }

    public boolean isUserConected() {
        return userConected;
    }

    public void setUserConected(boolean userConected) {
        this.userConected = userConected;
    }

    public short getUserCharIndex() {
        return userCharIndex;
    }

    public boolean isDead() {
        return charList[userCharIndex].isDead();
    }

    public void setUserCharIndex(short userCharIndex) {
        this.userCharIndex = userCharIndex;
    }

    public UserInventory getUserInventory() {
        return userInventory;
    }

    public String getUserName() {
        return userName.toUpperCase();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public short getUserMaxHP() {
        return userMaxHP;
    }

    public void setUserMaxHP(short userMaxHP) {
        this.userMaxHP = userMaxHP;
    }

    public short getUserMinHP() {
        return userMinHP;
    }

    public void setUserMinHP(short userMinHP) {
        this.userMinHP = userMinHP;
    }

    public short getUserMaxMAN() {
        return userMaxMAN;
    }

    public void setUserMaxMAN(short userMaxMAN) {
        this.userMaxMAN = userMaxMAN;
    }

    public short getUserMinMAN() {
        return userMinMAN;
    }

    public void setUserMinMAN(short userMinMAN) {
        this.userMinMAN = userMinMAN;
    }

    public short getUserMaxSTA() {
        return userMaxSTA;
    }

    public void setUserMaxSTA(short userMaxSTA) {
        this.userMaxSTA = userMaxSTA;
    }

    public short getUserMinSTA() {
        return userMinSTA;
    }

    public void setUserMinSTA(short userMinSTA) {
        this.userMinSTA = userMinSTA;
    }

    public int getUserPasarNivel() {
        return userPasarNivel;
    }

    public void setUserPasarNivel(int userPasarNivel) {
        this.userPasarNivel = userPasarNivel;
    }

    public int getUserExp() {
        return userExp;
    }

    public void setUserExp(int userExp) {
        this.userExp = userExp;
    }

    public int getUserGLD() {
        return userGLD;
    }

    public void setUserGLD(int userGLD) {
        this.userGLD = userGLD;
    }

    public byte getUserLvl() {
        return userLvl;
    }

    public void setUserLvl(byte userLvl) {
        this.userLvl = userLvl;
    }

    public byte getUserDext() {
        return userDext;
    }

    public void setUserDext(byte userDext) {
        this.userDext = userDext;
    }

    public byte getUserStrg() {
        return userStrg;
    }

    public void setUserStrg(byte userStrg) {
        this.userStrg = userStrg;
    }

    public short getUserMaxAGU() {
        return userMaxAGU;
    }

    public void setUserMaxAGU(short userMaxAGU) {
        this.userMaxAGU = userMaxAGU;
    }

    public short getUserMinAGU() {
        return userMinAGU;
    }

    public void setUserMinAGU(short userMinAGU) {
        this.userMinAGU = userMinAGU;
    }

    public short getUserMaxHAM() {
        return userMaxHAM;
    }

    public void setUserMaxHAM(short userMaxHAM) {
        this.userMaxHAM = userMaxHAM;
    }

    public short getUserMinHAM() {
        return userMinHAM;
    }

    public void setUserMinHAM(short userMinHAM) {
        this.userMinHAM = userMinHAM;
    }

    public String getUserWeaponEqpHit() {
        return userWeaponEqpHit;
    }

    public void setUserWeaponEqpHit(String userWeaponEqpHit) {
        this.userWeaponEqpHit = userWeaponEqpHit;
    }

    public String getUserArmourEqpDef() {
        return userArmourEqpDef;
    }

    public void setUserArmourEqpDef(String userArmourEqpDef) {
        this.userArmourEqpDef = userArmourEqpDef;
    }

    public String getUserHelmEqpDef() {
        return userHelmEqpDef;
    }

    public void setUserHelmEqpDef(String userHelmEqpDef) {
        this.userHelmEqpDef = userHelmEqpDef;
    }

    public String getUserShieldEqpDef() {
        return userShieldEqpDef;
    }

    public void setUserShieldEqpDef(String userShieldEqpDef) {
        this.userShieldEqpDef = userShieldEqpDef;
    }

    public byte getUserWeaponEqpSlot() {
        return userWeaponEqpSlot;
    }

    public void setUserWeaponEqpSlot(byte userWeaponEqpSlot) {
        this.userWeaponEqpSlot = userWeaponEqpSlot;
    }

    public byte getUserArmourEqpSlot() {
        return userArmourEqpSlot;
    }

    public void setUserArmourEqpSlot(byte userArmourEqpSlot) {
        this.userArmourEqpSlot = userArmourEqpSlot;
    }

    public byte getUserHelmEqpSlot() {
        return userHelmEqpSlot;
    }

    public void setUserHelmEqpSlot(byte userHelmEqpSlot) {
        this.userHelmEqpSlot = userHelmEqpSlot;
    }

    public byte getUserShieldEqpSlot() {
        return userShieldEqpSlot;
    }

    public void setUserShieldEqpSlot(byte userShieldEqpSlot) {
        this.userShieldEqpSlot = userShieldEqpSlot;
    }

    public boolean isTalking() {
        return talking;
    }

    public void setTalking(boolean talking) {
        this.talking = talking;
    }

    public short getUserMap() {
        return userMap;
    }

    public boolean isUserNavegando() {
        return userNavegando;
    }

    public void setUserNavegando(boolean userNavegando) {
        this.userNavegando = userNavegando;
    }
}
