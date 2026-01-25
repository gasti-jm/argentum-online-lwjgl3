package org.aoclient.engine.game;

import org.aoclient.engine.game.inventory.InventorySpells;
import org.aoclient.engine.game.inventory.UserInventory;
import org.aoclient.engine.game.models.*;
import org.aoclient.engine.renderer.TextureManager;

import static org.aoclient.engine.audio.Sound.*;
import static org.aoclient.engine.game.models.Character.*;
import static org.aoclient.engine.game.models.Direction.*;
import static org.aoclient.engine.scenes.Camera.*;
import static org.aoclient.engine.utils.GameData.*;
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

public enum User {

    INSTANCE;

    private final UserInventory userInventory;
    private final InventorySpells inventorySpells;
    private final Position userPos;
    private final Position addToUserPos;
    private boolean underCeiling;
    private boolean userMoving;
    private boolean userNavegando;
    private boolean userComerciando;
    private boolean meditating;
    // mapa
    private short userMap;
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
    private int userLvl;
    private int userDext;
    private int userStrg;
    private int userMaxAGU;
    private int userMinAGU;
    private int userMaxHAM;
    private int userMinHAM;
    private int freeSkillPoints;
    private int[] skills = new int[Skill.values().length];
    private int[] attributes = new int[Attribute.values().length];
    private int[] reputations = new int[Reputation.values().length];
    private int[] killCounters = new int[KillCounter.values().length];

    private String userWeaponEqpHit = "0/0";
    private String userArmourEqpDef = "0/0";
    private String userHelmEqpDef = "0/0";
    private String userShieldEqpDef = "0/0";

    private int userWeaponEqpSlot;
    private int userArmourEqpSlot;
    private int userHelmEqpSlot;
    private int userShieldEqpSlot;

    private boolean talking;
    private boolean criminal;

    private int usingSkill;

    private int role;
    private int jailTime;

    private int privilege;

    User() {
        this.userPos = new Position();
        this.addToUserPos = new Position();
        this.userInventory = new UserInventory();
        this.inventorySpells = new InventorySpells();
        this.talking = false;
        this.userNavegando = false;
        this.userComerciando = false;
    }

    public void resetGameState() {
        resetState();
        Rain.INSTANCE.setRainValue(false);
        Rain.INSTANCE.stopRainingSoundLoop();
    }

    /**
     * @param nDirection direccion pasada por parametro Mueve la camara hacia una direccion.
     */
    public void moveScreen(Direction nDirection) {
        int x = 0, y = 0;
        switch (nDirection) {
            case UP:
                y = -1;
                break;
            case RIGHT:
                x = 1;
                break;
            case DOWN:
                y = 1;
                break;
            case LEFT:
                x = -1;
                break;
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
     * Checkea si estamos bajo techo segun el trigger en donde esta parado el usuario.
     */
    public boolean checkUnderCeiling() {
        return mapData[userPos.getX()][userPos.getY()].getTrigger() == 1 ||
                mapData[userPos.getX()][userPos.getY()].getTrigger() == 2 ||
                mapData[userPos.getX()][userPos.getY()].getTrigger() == 4;
    }

    /**
     * @param charIndex  Numero de identificador de personaje
     * @param nDirection Direccion del personaje Mueve el personaje segun la direccion establecida en "nHeading".
     */
    public void moveCharbyHead(short charIndex, Direction nDirection) {
        int addX = 0, addY = 0;
        switch (nDirection) {
            case UP:
                addY = -1;
                break;
            case RIGHT:
                addX = 1;
                break;
            case DOWN:
                addY = 1;
                break;
            case LEFT:
                addX = -1;
                break;
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
        charList[charIndex].setHeading(nDirection);

        charList[charIndex].setScrollDirectionX(addX);
        charList[charIndex].setScrollDirectionY(addY);

        doPasosFx(charIndex);

        // areas viejos
        if ((nY < minLimiteY) || (nY > maxLimiteY) || (nX < minLimiteX) || (nX > maxLimiteX))
            if (charIndex != userCharIndex) eraseChar(charIndex);

    }

    /**
     * Actualiza las areas de vision de objetos y personajes.
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
        return charList[charIndex].getPos().getX() > userPos.getX() - minXBorder &&
                charList[charIndex].getPos().getX() < userPos.getX() + minXBorder &&
                charList[charIndex].getPos().getY() > userPos.getY() - minYBorder &&
                charList[charIndex].getPos().getY() < userPos.getY() + minYBorder;
    }

    public boolean hayAgua(int x, int y) {
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
            TextureManager.requestTexture(charList[charIndex].getfX().getGrhIndex());
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

        /*
            'parche para que no medite cuando camina
            If .FxIndex = FxMeditar.CHICO Or .FxIndex = FxMeditar.GRANDE Or .FxIndex = FxMeditar.MEDIANO Or .FxIndex = FxMeditar.XGRANDE Or .FxIndex = FxMeditar.XXGRANDE Then
                .FxIndex = 0
            End If
         */

        if (!estaPCarea(charIndex)) Dialogs.removeDialog(charIndex);

        // If Not EstaPCarea(CharIndex) Then Call Dialogos.RemoveDialog(CharIndex)

        if ((nY < minLimiteY) || (nY > maxLimiteY) || (nX < minLimiteX) || (nX > maxLimiteX))
            if (charIndex != userCharIndex) eraseChar(charIndex);

    }

    /**
     * @param direction Mueve nuestro personaje a una cierta direccion si es posible.
     */
    public void moveTo(Direction direction) {
        boolean legalOk = switch (direction) {
            case UP -> moveToLegalPos(userPos.getX(), userPos.getY() - 1);
            case RIGHT -> moveToLegalPos(userPos.getX() + 1, userPos.getY());
            case DOWN -> moveToLegalPos(userPos.getX(), userPos.getY() + 1);
            case LEFT -> moveToLegalPos(userPos.getX() - 1, userPos.getY());
        };
        if (legalOk && !charList[userCharIndex].isParalizado()) {
            if (meditating) meditating = false;
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
        if (!userNavegando) {
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

    public boolean isUserConected() {
        return userConected;
    }

    public void setUserConected(boolean userConected) {
        this.userConected = userConected;
    }

    public boolean isGM() {
        return PlayerType.isGM(privilege);
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
        return userInventory;
    }

    public InventorySpells getInventorySpells() {
        return inventorySpells;
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

    public int getUserLvl() {
        return userLvl;
    }

    public void setUserLvl(int userLvl) {
        this.userLvl = userLvl;
    }

    public int getUserDext() {
        return userDext;
    }

    public void setUserDext(int userDext) {
        this.userDext = userDext;
    }

    public int getUserStrg() {
        return userStrg;
    }

    public void setUserStrg(int userStrg) {
        this.userStrg = userStrg;
    }

    public int getUserMaxAGU() {
        return userMaxAGU;
    }

    public void setUserMaxAGU(int userMaxAGU) {
        this.userMaxAGU = userMaxAGU;
    }

    public int getUserMinAGU() {
        return userMinAGU;
    }

    public void setUserMinAGU(int userMinAGU) {
        this.userMinAGU = userMinAGU;
    }

    public int getUserMaxHAM() {
        return userMaxHAM;
    }

    public void setUserMaxHAM(int userMaxHAM) {
        this.userMaxHAM = userMaxHAM;
    }

    public int getUserMinHAM() {
        return userMinHAM;
    }

    public void setUserMinHAM(int userMinHAM) {
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

    public int getUserWeaponEqpSlot() {
        return userWeaponEqpSlot;
    }

    public void setUserWeaponEqpSlot(int userWeaponEqpSlot) {
        this.userWeaponEqpSlot = userWeaponEqpSlot;
    }

    public int getUserArmourEqpSlot() {
        return userArmourEqpSlot;
    }

    public void setUserArmourEqpSlot(int userArmourEqpSlot) {
        this.userArmourEqpSlot = userArmourEqpSlot;
    }

    public int getUserHelmEqpSlot() {
        return userHelmEqpSlot;
    }

    public void setUserHelmEqpSlot(int userHelmEqpSlot) {
        this.userHelmEqpSlot = userHelmEqpSlot;
    }

    public int getUserShieldEqpSlot() {
        return userShieldEqpSlot;
    }

    public void setUserShieldEqpSlot(int userShieldEqpSlot) {
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

    public void setUserMap(short userMap) {
        this.userMap = userMap;
    }

    public boolean isUserNavegando() {
        return userNavegando;
    }

    public void setUserNavegando(boolean userNavegando) {
        this.userNavegando = userNavegando;
    }

    public int getUsingSkill() {
        return usingSkill;
    }

    public void setUsingSkill(int usingSkill) {
        this.usingSkill = usingSkill;
    }

    public boolean isUserComerciando() {
        return userComerciando;
    }

    public void setUserComerciando(boolean userComerciando) {
        this.userComerciando = userComerciando;
    }

    public boolean isMeditating() {
        return meditating;
    }

    public void setMeditating(boolean meditating) {
        this.meditating = meditating;
    }

    public int getFreeSkillPoints() {
        return freeSkillPoints;
    }

    public void setFreeSkillPoints(int freeSkillPoints) {
        this.freeSkillPoints = freeSkillPoints;
    }

    public int getSkill(int skill) {
        return skills[skill - 1];
    }

    public void setSkill(int skill, int value) {
        skills[skill - 1] = value;
    }

    public int[] getSkills() {
        return skills;
    }

    public void setSkills(int[] skills) {
        this.skills = skills;
    }

    public int[] getAttributes() {
        return attributes;
    }

    public void setAttributes(int[] attributes) {
        this.attributes = attributes;
    }

    public int[] getReputations() {
        return reputations;
    }

    public void setReputations(int[] reputations) {
        this.reputations = reputations;
    }

    public boolean isCriminal() {
        return criminal;
    }

    public void setCriminal(boolean criminal) {
        this.criminal = criminal;
    }

    public int getKillCounter(int index) {
        return killCounters[index];
    }

    public void setKillCounter(int index, int value) {
        killCounters[index] = value;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getJailTime() {
        return jailTime;
    }

    public void setJailTime(int jailTime) {
        this.jailTime = jailTime;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }

    private void resetState() {
        this.setUserConected(false);
        this.setUserNavegando(false);
        this.setUserComerciando(false);
        this.setMeditating(false);
        this.setFreeSkillPoints(0);
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
            if (mapData[userPos.getX()][userPos.getY()].getBlocked()) return false;
            if (charList[charIndex].getiHead() != CASPER_HEAD && charList[charIndex].getiBody() != FRAGATA_FANTASMAL) {
                return false;
            } else {
                // No puedo intercambiar con un casper que este en la orilla (Lado tierra)
                if (hayAgua(userPos.getX(), userPos.getY())) {
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

        if (userNavegando != hayAgua(x, y)) return false;

        return true;
    }
}
