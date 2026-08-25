package org.aoclient.engine.game.player;

import org.aoclient.engine.game.Dialogs;
import org.aoclient.engine.game.Rain;
import org.aoclient.engine.game.inventory.InventorySpells;
import org.aoclient.engine.game.inventory.UserInventory;
import org.aoclient.engine.game.models.*;

import static org.aoclient.engine.audio.Sound.*;
import static org.aoclient.engine.game.models.Character.*;
import static org.aoclient.engine.game.models.Direction.*;
import static org.aoclient.engine.scenes.Camera.*;
import static org.aoclient.engine.utils.GameData.*;
import static org.aoclient.engine.utils.inits.GrhInfo.initGrh;
import static org.aoclient.network.protocol.Protocol.changeHeading;
import static org.aoclient.network.protocol.Protocol.walk;

/**
 * <p>
 * Representa el usuario controlado por el jugador. Centraliza toda la informacion, logica de movimiento y estados del personaje
 * principal.
 * <p>
 * Esta clase maneja aspectos fundamentales como la posicion del usuario en el mapa, direccion de movimiento, atributos y
 * estadisticas, inventario, hechizos y todas las interacciones con el entorno de juego. Actua como una entidad central que
 * coordina las acciones del jugador con el resto del mundo virtual.
 * <p>
 * Entre sus responsabilidades principales se encuentran:
 * <ul>
 * <li>Control del movimiento y posicionamiento del usuario</li>
 * <li>Gestion de estadisticas (vida, mana, stamina, experiencia, etc.)</li>
 * <li>Manejo del inventario y equipamiento</li>
 * <li>Administracion de hechizos disponibles</li>
 * <li>Control de estados especiales (navegando, bajo techo, hablando, etc.)</li>
 * <li>Interaccion con el entorno y otros personajes</li>
 * </ul>
 * <p>
 * La clase representa el nucleo de la experiencia de juego, siendo el punto de conexion entre las acciones del jugador y su
 * representacion en el mundo virtual.
 */

public enum Player {
    INSTANCE;

    private short userCharIndex;
    private int pingTime;
    private UserFlags flags;
    private UserInfo info;
    private UserData data;
    private SMSystem SMResu, SMSafe, SMSpells, SMWork;

    Player() {
        // como corresponde che.
        this.flags = new UserFlags();
        this.data = new UserData();
        this.info = new UserInfo();

        // SM system...
        SMResu    = new SMSystem(682, 564, SMType.sResucitation, false);
        SMSafe    = new SMSystem(707, 564, SMType.sSafemode, true);
        SMSpells  = new SMSystem(731, 564, SMType.mSpells, false);
        SMWork    = new SMSystem(755, 564, SMType.mWork, false);
    }


    public void resetGameState() {
        // mas facil crear otro obj que setear todos los datos.
        this.data = new UserData();
        this.flags = new UserFlags();
        this.info = new UserInfo();

        Rain.INSTANCE.setRainValue(false);
        Rain.INSTANCE.stopRainingSoundLoop();
    }

    /**
     * @param nDirection direccion pasada por parametro Mueve la camara hacia una direccion.
     */
    public void moveScreen(Direction nDirection) {
        int x = 0, y = 0;

        switch (nDirection) {
            case UP     -> y = -1;
            case RIGHT  -> x = 1;
            case DOWN   -> y = 1;
            case LEFT   -> x = -1;
        }

        final int tX = info.userPos.getX() + x;
        final int tY = info.userPos.getY() + y;

        if (!(tX < minXBorder || tX > maxXBorder || tY < minYBorder || tY > maxYBorder)) {
            info.addToUserPos.setX(x);
            info.userPos.setX(tX);
            info.addToUserPos.setY(y);
            info.userPos.setY(tY);
            flags.moving = true;
            flags.underCeiling = checkUnderCeiling();
        }

    }

    /**
     * Checkea si estamos bajo techo segun el trigger en donde esta parado el usuario.
     */
    public boolean checkUnderCeiling() {
        final int x = info.userPos.getX();
        final int y = info.userPos.getY();

        return  mapData[x][y].getTrigger() == 1 ||
                mapData[x][y].getTrigger() == 2 ||
                mapData[x][y].getTrigger() == 4;
    }

    /**
     * @param charIndex  Numero de identificador de personaje
     * @param nDirection Direccion del personaje Mueve el personaje segun la direccion establecida en "nHeading".
     */
    public void moveCharbyHead(short charIndex, Direction nDirection) {
        int addX = 0, addY = 0;
        switch (nDirection) {
            case UP ->     addY = -1;
            case RIGHT ->  addX =  1;
            case DOWN ->   addY =  1;
            case LEFT ->   addX = -1;
        }

        final int x  = charList[charIndex].getPos().getX();
        final int y  = charList[charIndex].getPos().getY();
        final int nX = x + addX;
        final int nY = y + addY;

        mapData[nX][nY].setCharIndex(charIndex);
        charList[charIndex].getPos().setX(nX);
        charList[charIndex].getPos().setY(nY);
        mapData[x][y].setCharIndex(0);

        charList[charIndex].setMoveOffsetX(-1 * (TILE_PIXEL_SIZE * addX));
        charList[charIndex].setMoveOffsetY(-1 * (TILE_PIXEL_SIZE * addY));

        charList[charIndex].setMoving(true);
        charList[charIndex].setHeading(nDirection);

        charList[charIndex].setScrollDirectionX(addX);
        charList[charIndex].setScrollDirectionY(addY);

        doPasosFx(charIndex);

        if ((nY < info.minLimiteY) || (nY > info.maxLimiteY) || (nX < info.minLimiteX) || (nX > info.maxLimiteX))
            if (charIndex != userCharIndex) eraseChar(charIndex);
    }

    /**
     * Actualiza las areas de vision de objetos y personajes.
     */
    public void areaChange(int x, int y) {
        info.minLimiteX = (x / 9 - 1) * 9;
        info.maxLimiteX = info.minLimiteX + 26;
        info.minLimiteY = (y / 9 - 1) * 9;
        info.maxLimiteY = info.minLimiteY + 26;

        for (int loopX = 1; loopX <= 100; loopX++) {
            for (int loopY = 1; loopY <= 100; loopY++) {
                if ((loopY < info.minLimiteY) || (loopY > info.maxLimiteY) || (loopX < info.minLimiteX) || (loopX > info.maxLimiteX)) {
                    // Erase NPCs
                    if (mapData[loopX][loopY].getCharIndex() > 0)
                        if (mapData[loopX][loopY].getCharIndex() != userCharIndex)
                            eraseChar(mapData[loopX][loopY].getCharIndex());
                    // Erase Objs
                    mapData[loopX][loopY].getObjGrh().setGrhIndex(0);
                }
            }
        }

        refreshAllChars();
    }

    /**
     * @param x Posicion X del usuario.
     * @param y Posicion Y del usuario.
     * @return True si se encuentra dentro del limite del mapa, false en caso contrario.
     */
    public boolean inMapBounds(int x, int y) {
        return x < TILE_BUFFER_SIZE || x > XMaxMapSize - TILE_BUFFER_SIZE || y < TILE_BUFFER_SIZE || y > YMaxMapSize - TILE_BUFFER_SIZE;
    }

    public boolean estaPCarea(int charIndex) {
        return charList[charIndex].getPos().getX() > info.userPos.getX() - minXBorder &&
                charList[charIndex].getPos().getX() < info.userPos.getX() + minXBorder &&
                charList[charIndex].getPos().getY() > info.userPos.getY() - minYBorder &&
                charList[charIndex].getPos().getY() < info.userPos.getY() + minYBorder;
    }

    public boolean hayAgua(int x, int y) {
        // TODO: esto esta hardcodeado, hay que modificarlo.
        return ((mapData[x][y].getLayer(1).getGrhIndex() >= 1505 && mapData[x][y].getLayer(1).getGrhIndex() <= 1520) ||
                (mapData[x][y].getLayer(1).getGrhIndex() >= 5665 && mapData[x][y].getLayer(1).getGrhIndex() <= 5680) ||
                (mapData[x][y].getLayer(1).getGrhIndex() >= 13547 && mapData[x][y].getLayer(1).getGrhIndex() <= 13562)) &&
                mapData[x][y].getLayer(2).getGrhIndex() == 0;
    }

    /**
     * @param charIndex Numero de identificador de personaje.
     * @param fx        Numero de efecto FX.
     * @param loops     Tiempo del efecto FX. Establece un efecto FX en un personaje.
     */
    public void setCharacterFx(int charIndex, int fx, int loops) {
        charList[charIndex].setFxIndex(fx);
        if (charList[charIndex].getFxIndex() > 0) {
            initGrh(charList[charIndex].getfX(), fxData[fx].getAnimacion(), true);
            charList[charIndex].getfX().setLoops(loops);
        }
    }

    /**
     * @param charIndex Numero de identificador de personaje
     * @param nX        Posicion X a actualizar
     * @param nY        Posicion Y a actualizar Mueve el personaje segun la direccion establecida en "nX" y "nY".
     */
    public void moveCharbyPos(short charIndex, int nX, int nY) {
        final int x = charList[charIndex].getPos().getX();
        final int y = charList[charIndex].getPos().getY();

        final int addX = nX - x;
        final int addY = nY - y;

        if (sgn((short) addX) == 1) charList[charIndex].setHeading(RIGHT);
        else if (sgn((short) addX) == -1) charList[charIndex].setHeading(LEFT);
        else if (sgn((short) addY) == -1) charList[charIndex].setHeading(UP);
        else if (sgn((short) addY) == 1) charList[charIndex].setHeading(DOWN);

        mapData[nX][nY].setCharIndex(charIndex);
        charList[charIndex].getPos().setX(nX);
        charList[charIndex].getPos().setY(nY);
        mapData[x][y].setCharIndex(0);

        charList[charIndex].setMoveOffsetX(-1 * (TILE_PIXEL_SIZE * addX));
        charList[charIndex].setMoveOffsetY(-1 * (TILE_PIXEL_SIZE * addY));

        charList[charIndex].setMoving(true);

        charList[charIndex].setScrollDirectionX(sgn((short) addX));
        charList[charIndex].setScrollDirectionY(sgn((short) addY));


        if (!estaPCarea(charIndex)) Dialogs.removeDialog(charIndex);

        if ((nY < info.minLimiteY) || (nY > info.maxLimiteY) || (nX < info.minLimiteX) || (nX > info.maxLimiteX))
            if (charIndex != userCharIndex) eraseChar(charIndex);

    }

    /**
     * @param direction Mueve nuestro personaje a una cierta direccion si es posible.
     */
    public void moveTo(Direction direction) {
        boolean legalOk = switch (direction) {
            case UP    -> moveToLegalPos(info.userPos.getX(), info.userPos.getY() - 1);
            case RIGHT -> moveToLegalPos(info.userPos.getX() + 1, info.userPos.getY());
            case DOWN  -> moveToLegalPos(info.userPos.getX(), info.userPos.getY() + 1);
            case LEFT  -> moveToLegalPos(info.userPos.getX() - 1, info.userPos.getY());
        };

        if (legalOk && !charList[userCharIndex].isParalizado()) {
            walk(direction);
            moveScreen(direction);
            moveCharbyHead(userCharIndex, direction);
        } else if (charList[userCharIndex].getHeading() != direction) changeHeading(direction);
    }

    /**
     * @param charIndex Numero de identificador de personaje Realiza sonidos de caminata segun el estado del personaje
     *                  <p>
     *                  EN PROGRESO....
     */
    public void doPasosFx(int charIndex) {
        if (!flags.browsing) {
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
        return flags.moving;
    }

    public void setUserMoving(boolean userMoving) {
        this.flags.moving = userMoving;
    }

    public Position getUserPos() {
        return info.userPos;
    }

    public Position getAddToUserPos() {
        return info.addToUserPos;
    }

    public boolean isUnderCeiling() {
        return flags.underCeiling;
    }

    public void setUnderCeiling(boolean underCeiling) {
        this.flags.underCeiling = underCeiling;
    }

    public boolean isUserConected() {
        return flags.connected;
    }

    public void setUserConected(boolean userConected) {
        this.flags.connected = userConected;
    }

    public boolean isGM() {
        return PlayerType.isGM(data.privilege);
    }

    public short getUserCharIndex() {
        return userCharIndex;
    }

    public void setUserCharIndex(short userCharIndex) {
        this.userCharIndex = userCharIndex;
    }

    public boolean isDead() {
        return charList[userCharIndex].isDead();
    }

    public UserInventory getUserInventory() {
        return info.userInventory;
    }

    public InventorySpells getInventorySpells() {
        return info.inventorySpells;
    }

    public String getUserName() {
        return info.name.toUpperCase();
    }

    public void setUserName(String userName) {
        this.info.name = userName;
    }

    public short getUserMaxHP() {
        return data.maxHP;
    }

    public void setUserMaxHP(short userMaxHP) {
        this.data.maxHP = userMaxHP;
    }

    public short getUserMinHP() {
        return data.minHP;
    }

    public void setUserMinHP(short userMinHP) {
        this.data.minHP = userMinHP;
    }

    public short getUserMaxMAN() {
        return data.maxMANA;
    }

    public void setUserMaxMAN(short userMaxMAN) {
        this.data.maxMANA = userMaxMAN;
    }

    public short getUserMinMAN() {
        return data.minMANA;
    }

    public void setUserMinMAN(short userMinMAN) {
        this.data.minMANA = userMinMAN;
    }

    public short getUserMaxSTA() {
        return data.maxSTA;
    }

    public void setUserMaxSTA(short userMaxSTA) {
        this.data.maxSTA = userMaxSTA;
    }

    public short getUserMinSTA() {
        return data.minSTA;
    }

    public void setUserMinSTA(short userMinSTA) {
        data.minSTA = userMinSTA;
    }

    public int getUserPasarNivel() {
        return data.nextLevel;
    }

    public void setUserPasarNivel(int userPasarNivel) {
        this.data.nextLevel = userPasarNivel;
    }

    public int getUserExp() {
        return data.exp;
    }

    public void setUserExp(int userExp) {
        this.data.exp = userExp;
    }

    public int getUserGLD() {
        return data.gold;
    }

    public void setUserGLD(int userGLD) {
        this.data.gold = userGLD;
    }

    public int getUserLvl() {
        return data.lvl;
    }

    public void setUserLvl(int userLvl) {
        this.data.lvl = userLvl;
    }

    public int getUserDext() {
        return data.dext;
    }

    public void setUserDext(int userDext) {
        this.data.dext = userDext;
    }

    public int getUserStrg() {
        return data.strg;
    }

    public void setUserStrg(int userStrg) {
        this.data.strg = userStrg;
    }

    public int getUserMaxAGU() {
        return data.maxAGU;
    }

    public void setUserMaxAGU(int userMaxAGU) {
        this.data.maxAGU = userMaxAGU;
    }

    public int getUserMinAGU() {
        return data.minAGU;
    }

    public void setUserMinAGU(int userMinAGU) {
        this.data.minAGU = userMinAGU;
    }

    public int getUserMaxHAM() {
        return data.maxHAM;
    }

    public void setUserMaxHAM(int userMaxHAM) {
        this.data.maxHAM = userMaxHAM;
    }

    public int getUserMinHAM() {
        return data.minHAM;
    }

    public void setUserMinHAM(int userMinHAM) {
        this.data.minHAM = userMinHAM;
    }

    public String getUserWeaponEqpHit() {
        return data.userWeaponEqpHit;
    }

    public void setUserWeaponEqpHit(String userWeaponEqpHit) {
        this.data.userWeaponEqpHit = userWeaponEqpHit;
    }

    public String getUserArmourEqpDef() {
        return data.userArmourEqpDef;
    }

    public void setUserArmourEqpDef(String userArmourEqpDef) {
        this.data.userArmourEqpDef = userArmourEqpDef;
    }

    public String getUserHelmEqpDef() {
        return data.userHelmEqpDef;
    }

    public void setUserHelmEqpDef(String userHelmEqpDef) {
        this.data.userHelmEqpDef = userHelmEqpDef;
    }

    public String getUserShieldEqpDef() {
        return data.userShieldEqpDef;
    }

    public void setUserShieldEqpDef(String userShieldEqpDef) {
        this.data.userShieldEqpDef = userShieldEqpDef;
    }

    public int getUserWeaponEqpSlot() {
        return data.userWeaponEqpSlot;
    }

    public void setUserWeaponEqpSlot(int userWeaponEqpSlot) {
        this.data.userWeaponEqpSlot = userWeaponEqpSlot;
    }

    public int getUserArmourEqpSlot() {
        return data.userArmourEqpSlot;
    }

    public void setUserArmourEqpSlot(int userArmourEqpSlot) {
        this.data.userArmourEqpSlot = userArmourEqpSlot;
    }

    public int getUserHelmEqpSlot() {
        return data.userHelmEqpSlot;
    }

    public void setUserHelmEqpSlot(int userHelmEqpSlot) {
        this.data.userHelmEqpSlot = userHelmEqpSlot;
    }

    public int getUserShieldEqpSlot() {
        return data.userShieldEqpSlot;
    }

    public void setUserShieldEqpSlot(int userShieldEqpSlot) {
        this.data.userShieldEqpSlot = userShieldEqpSlot;
    }

    public boolean isTalking() {
        return flags.talking;
    }

    public void setTalking(boolean talking) {
        this.flags.talking = talking;
    }

    public short getUserMap() {
        return info.map;
    }

    public void setUserMap(short userMap) {
        this.info.map = userMap;
    }

    public boolean isUserNavegando() {
        return flags.browsing;
    }

    public void setUserNavegando(boolean userNavegando) {
        this.flags.browsing = userNavegando;
    }

    public int getUsingSkill() {
        return flags.usingSkill;
    }

    public void setUsingSkill(int usingSkill) {
        this.flags.usingSkill = usingSkill;
    }

    public boolean isUserComerciando() {
        return flags.trading;
    }

    public void setUserComerciando(boolean userComerciando) {
        this.flags.trading = userComerciando;
    }

    public int getFreeSkillPoints() {
        return data.freeSkillPoints;
    }

    public void setFreeSkillPoints(int freeSkillPoints) {
        this.data.freeSkillPoints = freeSkillPoints;
    }

    public int getSkill(int skill) {
        return data.skills[skill - 1];
    }

    public void setSkill(int skill, int value) {
        data.skills[skill - 1] = value;
    }

    public int[] getSkills() {
        return data.skills;
    }

    public void setSkills(int[] skills) {
        this.data.skills = skills;
    }

    public int[] getAttributes() {
        return data.attributes;
    }

    public void setAttributes(int[] attributes) {
        this.data.attributes = attributes;
    }

    public int[] getReputations() {
        return data.reputations;
    }

    public void setReputations(int[] reputations) {
        this.data.reputations = reputations;
    }

    public boolean isCriminal() {
        return flags.criminal;
    }

    public void setCriminal(boolean criminal) {
        this.flags.criminal = criminal;
    }

    public int getKillCounter(int index) {
        return data.killCounters[index];
    }

    public void setKillCounter(int index, int value) {
        data.killCounters[index] = value;
    }

    public int getRole() {
        return data.role;
    }

    public void setRole(int role) {
        this.data.role = role;
    }

    public int getJailTime() {
        return data.jailTime;
    }

    public void setJailTime(int jailTime) {
        this.data.jailTime = jailTime;
    }

    public void setPrivilege(int privilege) {
        this.data.privilege = privilege;
    }

    /**
     * @param x Posicion X del usuario.
     * @param y Posicion Y del usuario.
     * @return True si el usuario puede caminar hacia cierta posicion, false caso contrario.
     */
    private boolean moveToLegalPos(int x, int y) {
        // Limite del mapa
        if (x < minXBorder || x > maxXBorder || y < minYBorder || y > maxYBorder) return false;

        // Tile Bloqueado?
        if (mapData[x][y].getBlocked()) return false;

        final int charIndex = mapData[x][y].getCharIndex();

        // ¿Hay un personaje?
        if (charIndex > 0) {
            if (mapData[info.userPos.getX()][info.userPos.getY()].getBlocked()) return false;
            if (charList[charIndex].getiHead() != CASPER_HEAD && charList[charIndex].getiBody() != FRAGATA_FANTASMAL) {
                return false;
            } else {
                // No puedo intercambiar con un casper que este en la orilla (Lado tierra)
                if (hayAgua(info.userPos.getX(), info.userPos.getY())) {
                    if (!hayAgua(x, y)) return false;
                } else {
                    // No puedo intercambiar con un casper que este en la orilla (Lado agua)
                    if (hayAgua(x, y)) return false;
                }
                // Los admins no pueden intercambiar pos con caspers cuando estan invisibles
                if (charList[userCharIndex].getPriv() > 0 && charList[userCharIndex].getPriv() < 6) {
                    if (charList[userCharIndex].isInvisible()) return false;
                }

            }

        }

        if (flags.browsing != hayAgua(x, y)) return false;

        return true;
    }


    public SMSystem getSMResu() {
        return SMResu;
    }

    public SMSystem getSMSafe() {
        return SMSafe;
    }

    public SMSystem getSMSpells() {
        return SMSpells;
    }

    public SMSystem getSMWork() {
        return SMWork;
    }

    public int getPingTime() {
        return pingTime;
    }

    public void setPingTime(int pingTime) {
        this.pingTime = pingTime;
    }
}
