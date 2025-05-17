package org.aoclient.engine.game.models;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.engine.utils.inits.*;

import static org.aoclient.engine.game.models.E_Heading.SOUTH;
import static org.aoclient.engine.renderer.Drawn.drawTexture;
import static org.aoclient.engine.renderer.FontRenderer.*;
import static org.aoclient.engine.utils.GameData.*;
import static org.aoclient.engine.utils.Time.timerTicksPerFrame;

/**
 * Clase que representa a un personaje dentro del mundo de Argentum Online.
 * <p>
 * Esta clase implementa toda la funcionalidad relacionada con los personajes, incluyendo jugadores controlados por usuarios, NPCs
 * y criaturas.
 * <p>
 * Gestiona los siguientes aspectos:
 * <ul>
 * <li>Atributos fisicos (cuerpo, cabeza, armas, escudos, cascos)
 * <li>Estados del personaje (vida, invisibilidad, paralisis, etc.)
 * <li>Posicionamiento y movimiento en el mundo
 * <li>Renderizado grafico y animaciones
 * <li>Efectos visuales asociados al personaje
 * <li>Dialogos y mensajes sobre la cabeza
 * <li>Faccion, clan y nombre del personaje
 * </ul>
 * <p>
 * La clase contiene numerosas constantes que definen los rangos de indices de cabezas y cuerpos para diferentes razas y generos,
 * asi como metodos estaticos para operaciones como crear, eliminar o redibujar personajes en el mapa.
 * <p>
 * Character es una pieza central del motor grafico, gestionando tanto la representacion visual como la logica de estado de todas
 * las entidades animadas que pueblan el mundo.
 */

public final class Character {

    public static final int CASPER_HEAD = 500;
    public static final int FRAGATA_FANTASMAL = 87;

    public static final int HUMANO_H_PRIMER_CABEZA = 1;
    public static final int HUMANO_H_ULTIMA_CABEZA = 40; //En verdad es hasta la 51, pero como son muchas estas las dejamos no seleccionables
    public static final int HUMANO_H_CUERPO_DESNUDO = 21;

    public static final int ELFO_H_PRIMER_CABEZA = 101;
    public static final int ELFO_H_ULTIMA_CABEZA = 122;
    public static final int ELFO_H_CUERPO_DESNUDO = 210;

    public static final int DROW_H_PRIMER_CABEZA = 201;
    public static final int DROW_H_ULTIMA_CABEZA = 221;
    public static final int DROW_H_CUERPO_DESNUDO = 32;

    public static final int ENANO_H_PRIMER_CABEZA = 301;
    public static final int ENANO_H_ULTIMA_CABEZA = 319;
    public static final int ENANO_H_CUERPO_DESNUDO = 53;

    public static final int GNOMO_H_PRIMER_CABEZA = 401;
    public static final int GNOMO_H_ULTIMA_CABEZA = 416;
    public static final int GNOMO_H_CUERPO_DESNUDO = 222;

    public static final int HUMANO_M_PRIMER_CABEZA = 70;
    public static final int HUMANO_M_ULTIMA_CABEZA = 89;
    public static final int HUMANO_M_CUERPO_DESNUDO = 39;

    public static final int ELFO_M_PRIMER_CABEZA = 170;
    public static final int ELFO_M_ULTIMA_CABEZA = 188;
    public static final int ELFO_M_CUERPO_DESNUDO = 259;

    public static final int DROW_M_PRIMER_CABEZA = 270;
    public static final int DROW_M_ULTIMA_CABEZA = 288;
    public static final int DROW_M_CUERPO_DESNUDO = 40;

    public static final int ENANO_M_PRIMER_CABEZA = 370;
    public static final int ENANO_M_ULTIMA_CABEZA = 384;
    public static final int ENANO_M_CUERPO_DESNUDO = 60;

    public static final int GNOMO_M_PRIMER_CABEZA = 470;
    public static final int GNOMO_M_ULTIMA_CABEZA = 484;
    public static final int GNOMO_M_CUERPO_DESNUDO = 260;

    // ultimo personaje del array
    public static short lastChar = 0;
    private boolean active;
    private E_Heading heading;
    private Position pos;
    private short iHead;
    private short iBody;
    private BodyData body;
    private HeadData head;
    private HeadData helmet;
    private WeaponData weapon;
    private ShieldData shield;
    private boolean usingArm;
    private int walkingSpeed;

    // FX
    private GrhInfo fX;
    private int fxIndex;

    private boolean criminal;
    private boolean attackable;

    // Nicknames
    private String name;
    private String clanName;

    private short scrollDirectionX;
    private short scrollDirectionY;

    private boolean moving;
    private float moveOffsetX;
    private float moveOffsetY;

    private boolean pie;
    private boolean dead;
    private boolean invisible;
    private boolean paralizado;
    private byte priv;

    // dialogs
    private String dialog;
    private RGBColor dialog_color;
    private int dialog_life;
    private int dialog_font_index;
    private float dialog_offset_counter_y;
    private boolean dialog_scroll;

    public Character() {
        body = new BodyData();
        head = new HeadData();
        helmet = new HeadData();
        weapon = new WeaponData();
        shield = new ShieldData();
        this.pos = new Position();
        this.fX = new GrhInfo();

        this.heading = SOUTH;
        this.active = false;
        this.criminal = false;
        this.attackable = false;
        this.fxIndex = 0;
        this.invisible = false;
        this.paralizado = false;
        this.moving = false;
        this.dead = false;
        this.name = "";
        this.pie = false;
        this.pos.setX(0);
        this.pos.setY(0);

        this.usingArm = false;
        this.clanName = "";
        this.walkingSpeed = 8;

        this.dialog = "";
        this.dialog_scroll = false;
        this.dialog_font_index = 0;
        this.dialog_color = new RGBColor(1f, 1f, 1f);
        this.dialog_offset_counter_y = 0;
    }

    /**
     * @desc Crea un nuevo personaje segun los parametros establecidos.
     */
    public static void makeChar(short charIndex, int body, int head, E_Heading heading, int x, int y, int weapon, int shield, int helmet) {
        // apuntamos al ultimo char
        if (charIndex > lastChar) lastChar = charIndex;

        if (weapon == 0) weapon = 2;
        if (shield == 0) shield = 2;
        if (helmet == 0) helmet = 2;

        char f = '<', u = '>';

        if (charList[charIndex].priv != 0) charList[charIndex].setClanName(f + "Game Master" + u);

        charList[charIndex].setDead(head == CASPER_HEAD);
        charList[charIndex].setiHead(head);
        charList[charIndex].setiBody(body);

        charList[charIndex].setHead(new HeadData(headData[head]));
        charList[charIndex].setBody(new BodyData(bodyData[body]));

        charList[charIndex].setWeapon(new WeaponData(weaponData[weapon]));

        charList[charIndex].setShield(new ShieldData(shieldData[shield]));
        charList[charIndex].setHelmet(new HeadData(helmetsData[helmet]));

        charList[charIndex].setHeading(heading);

        // reset moving stats
        charList[charIndex].setMoving(false);
        charList[charIndex].setMoveOffsetX(0);
        charList[charIndex].setMoveOffsetY(0);

        // update position
        charList[charIndex].getPos().setX(x);
        charList[charIndex].getPos().setY(y);

        // Make active
        charList[charIndex].setActive(true);

        // plot on map
        mapData[x][y].setCharIndex(charIndex);
    }

    /**
     * @param charIndex Numero de identificador de personaje
     * @desc Elimina un personaje del array de personajes.
     */
    public static void eraseChar(short charIndex) {
        charList[charIndex].setActive(false);

        if (charIndex == lastChar) {
            while (!charList[lastChar].isActive()) {
                lastChar--;
                if (lastChar == 0) break;
            }
        }

        mapData[charList[charIndex].getPos().getX()][charList[charIndex].getPos().getY()].setCharIndex(0);

        /*
            'Remove char's dialog
            Call Dialogos.RemoveDialog(CharIndex)
         */

        resetCharInfo(charIndex);
    }

    /**
     * @desc elimina todos los personajes de nuestro array charList.
     */
    public static void eraseAllChars() {
        for (short i = 1; i < charList.length; i++) {
            mapData[charList[i].getPos().getX()][charList[i].getPos().getY()].setCharIndex(0);
            resetCharInfo(i);
        }
        lastChar = 0;
    }

    /**
     * @param charIndex Numero de identificador del personaje
     * @desc Resetea los atributos del personaje.
     */
    private static void resetCharInfo(short charIndex) {
        charList[charIndex] = new Character(); // al crear un obj nuevo, el viejo sera eliminado por el recolector de basura de java.
    }

    /**
     * @desc Actualiza todos los personajes visibles.
     */
    public static void refreshAllChars() {
        for (int loopC = 1; loopC <= lastChar; loopC++)
            if (charList[loopC].isActive())
                mapData[charList[loopC].getPos().getX()][charList[loopC].getPos().getY()].setCharIndex(loopC);
    }

    /**
     * @desc Dibuja nuestro personaje!
     */
    public static void drawCharacter(int charIndex, int PixelOffsetX, int PixelOffsetY, RGBColor ambientcolor) {
        boolean moved = false;
        RGBColor color = new RGBColor();

        if (charList[charIndex].getMoving()) {
            if (charList[charIndex].getScrollDirectionX() != 0) {

                charList[charIndex].setMoveOffsetX(charList[charIndex].getMoveOffsetX() +
                        charList[charIndex].getWalkingSpeed() * sgn(charList[charIndex].getScrollDirectionX()) * timerTicksPerFrame);

                if (charList[charIndex].getBody().getWalk(charList[charIndex].getHeading().value).getSpeed() > 0.0f) {
                    charList[charIndex].getBody().getWalk(charList[charIndex].getHeading().value).setStarted(true);
                }

                charList[charIndex].getWeapon().getWeaponWalk(charList[charIndex].getHeading().value).setStarted(true);
                charList[charIndex].getShield().getShieldWalk(charList[charIndex].getHeading().value).setStarted(true);

                moved = true;

                if ((sgn(charList[charIndex].getScrollDirectionX()) == 1 && charList[charIndex].getMoveOffsetX() >= 0) ||
                        (sgn(charList[charIndex].getScrollDirectionX()) == -1 && charList[charIndex].getMoveOffsetX() <= 0)) {

                    charList[charIndex].setMoveOffsetX(0);
                    charList[charIndex].setScrollDirectionX(0);
                }
            }

            if (charList[charIndex].getScrollDirectionY() != 0) {
                charList[charIndex].setMoveOffsetY(charList[charIndex].getMoveOffsetY()
                        + charList[charIndex].getWalkingSpeed() * sgn(charList[charIndex].getScrollDirectionY()) * timerTicksPerFrame);


                if (charList[charIndex].getBody().getWalk(charList[charIndex].getHeading().value).getSpeed() > 0.0f) {
                    charList[charIndex].getBody().getWalk(charList[charIndex].getHeading().value).setStarted(true);
                }

                charList[charIndex].getWeapon().getWeaponWalk(charList[charIndex].getHeading().value).setStarted(true);
                charList[charIndex].getShield().getShieldWalk(charList[charIndex].getHeading().value).setStarted(true);

                moved = true;

                if ((sgn(charList[charIndex].getScrollDirectionY()) == 1 && charList[charIndex].getMoveOffsetY() >= 0)
                        || (sgn(charList[charIndex].getScrollDirectionY()) == -1 && charList[charIndex].getMoveOffsetY() <= 0)) {
                    charList[charIndex].setMoveOffsetY(0);
                    charList[charIndex].setScrollDirectionY(0);
                }
            }
        }

        if (!moved) {
            charList[charIndex].getBody().getWalk(charList[charIndex].getHeading().value).setStarted(false);
            charList[charIndex].getBody().getWalk(charList[charIndex].getHeading().value).setFrameCounter(1);

            charList[charIndex].getWeapon().getWeaponWalk(charList[charIndex].getHeading().value).setStarted(false);
            charList[charIndex].getWeapon().getWeaponWalk(charList[charIndex].getHeading().value).setFrameCounter(1);

            charList[charIndex].getShield().getShieldWalk(charList[charIndex].getHeading().value).setStarted(false);
            charList[charIndex].getShield().getShieldWalk(charList[charIndex].getHeading().value).setFrameCounter(1);

            charList[charIndex].setMoving(false);
        }

        PixelOffsetX += (int) charList[charIndex].getMoveOffsetX();
        PixelOffsetY += (int) charList[charIndex].getMoveOffsetY();

        if (charList[charIndex].getHead().getHead(charList[charIndex].getHeading().value).getGrhIndex() != 0) {
            if (!charList[charIndex].isInvisible()) {

                if (charList[charIndex].getBody().getWalk(charList[charIndex].getHeading().value).getGrhIndex() != 0) {
                    drawTexture(charList[charIndex].getBody().getWalk(charList[charIndex].getHeading().value),
                            PixelOffsetX, PixelOffsetY, true, true, false, 1.0f, ambientcolor);
                }

                if (charList[charIndex].getHead().getHead(charList[charIndex].getHeading().value).getGrhIndex() != 0) {
                    drawTexture(charList[charIndex].getHead().getHead(charList[charIndex].getHeading().value),
                            PixelOffsetX + charList[charIndex].getBody().getHeadOffset().getX(),
                            PixelOffsetY + charList[charIndex].getBody().getHeadOffset().getY(),
                            true, false, false, 1.0f, ambientcolor);


                    if (charList[charIndex].getHelmet().getHead(charList[charIndex].getHeading().value).getGrhIndex() != 0) {
                        drawTexture(charList[charIndex].getHelmet().getHead(charList[charIndex].getHeading().value),
                                PixelOffsetX + charList[charIndex].getBody().getHeadOffset().getX(),
                                PixelOffsetY + charList[charIndex].getBody().getHeadOffset().getY() - 34,
                                true, false, false, 1.0f, ambientcolor);
                    }

                    if (charList[charIndex].getWeapon().getWeaponWalk(charList[charIndex].getHeading().value).getGrhIndex() != 0) {
                        drawTexture(charList[charIndex].getWeapon().getWeaponWalk(charList[charIndex].getHeading().value),
                                PixelOffsetX, PixelOffsetY, true, true, false, 1.0f, ambientcolor);
                    }

                    if (charList[charIndex].getShield().getShieldWalk(charList[charIndex].getHeading().value).getGrhIndex() != 0) {
                        drawTexture(charList[charIndex].getShield().getShieldWalk(charList[charIndex].getHeading().value),
                                PixelOffsetX, PixelOffsetY, true, true, false, 1.0f, ambientcolor);
                    }

                    if (options.isShowName()) {
                        if (charList[charIndex].name.length() > 0) {

                            if (charList[charIndex].priv == 0) {
                                if (charList[charIndex].attackable) {
                                    color.setRed(0.54f);
                                    color.setGreen(0.0f);
                                    color.setBlue(1.0f);
                                } else {
                                    if (charList[charIndex].criminal) {
                                        color.setRed(1.0f);
                                        color.setGreen(0.0f);
                                        color.setBlue(0.0f);
                                    } else {
                                        color.setRed(0.0f);
                                        color.setGreen(0.5f);
                                        color.setBlue(1.0f);
                                    }
                                }
                            } else {
                                color.setRed(0.13f);
                                color.setGreen(0.7f);
                                color.setBlue(0.3f);
                            }

                            String line = charList[charIndex].name;
                            drawText(line,
                                    PixelOffsetX + 16 - getTextWidth(line, false) / 2,
                                    PixelOffsetY + 30, color, NORMAL_FONT, false);


                            line = charList[charIndex].clanName;
                            if (!line.isEmpty()) {
                                drawText(line,
                                        PixelOffsetX + 16 - getTextWidth(line, false) / 2,
                                        PixelOffsetY + 45, color, NORMAL_FONT, false);
                            }
                        }
                    }
                }
            }


        } else {
            if (charList[charIndex].getBody().getWalk(charList[charIndex].getHeading().value).getGrhIndex() > 0) {
                drawTexture(charList[charIndex].getBody().getWalk(charList[charIndex].getHeading().value),
                        PixelOffsetX, PixelOffsetY, true, true, false, 1.0f, ambientcolor);
            }
        }

        // Draw FX
        if (charList[charIndex].fxIndex != 0) {
            drawTexture(charList[charIndex].fX,
                    PixelOffsetX + fxData[charList[charIndex].fxIndex].getOffsetX(),
                    PixelOffsetY + fxData[charList[charIndex].fxIndex].getOffsetY(),
                    true, true, true, 1.0f, ambientcolor);

            // Check if animation is over
            if (!charList[charIndex].fX.isStarted())
                charList[charIndex].setFxIndex(0);
        }
    }

    public static int sgn(short number) {
        if (number == 0) return 0;
        return (number / Math.abs(number));
    }

    public int getWalkingSpeed() {
        return walkingSpeed;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public E_Heading getHeading() {
        return heading;
    }

    public void setHeading(E_Heading heading) {
        this.heading = heading;
    }

    public Position getPos() {
        return pos;
    }

    public short getiHead() {
        return iHead;
    }

    public void setiHead(int iHead) {
        this.iHead = (short) iHead;
    }

    public short getiBody() {
        return iBody;
    }

    public void setiBody(int iBody) {
        this.iBody = (short) iBody;
    }

    public BodyData getBody() {
        return body;
    }

    public void setBody(BodyData body) {
        this.body = body;
    }

    public HeadData getHead() {
        return head;
    }

    public void setHead(HeadData head) {
        this.head = head;
    }

    public HeadData getHelmet() {
        return helmet;
    }

    public void setHelmet(HeadData helmet) {
        this.helmet = helmet;
    }

    public WeaponData getWeapon() {
        return weapon;
    }

    public void setWeapon(WeaponData weapon) {
        this.weapon = weapon;
    }

    public ShieldData getShield() {
        return shield;
    }

    public void setShield(ShieldData shield) {
        this.shield = shield;
    }

    public boolean isUsingArm() {
        return usingArm;
    }

    public void setUsingArm(boolean usingArm) {
        this.usingArm = usingArm;
    }

    public GrhInfo getfX() {
        return fX;
    }

    public void setfX(GrhInfo fX) {
        this.fX = fX;
    }

    public int getFxIndex() {
        return fxIndex;
    }

    public void setFxIndex(int fxIndex) {
        this.fxIndex = fxIndex;
    }

    public boolean getCriminal() {
        return criminal;
    }

    public void setCriminal(boolean criminal) {
        this.criminal = criminal;
    }

    public boolean isAttackable() {
        return attackable;
    }

    public void setAttackable(boolean attackable) {
        this.attackable = attackable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClanName() {
        return clanName;
    }

    public void setClanName(String clanName) {
        this.clanName = clanName;
    }

    public short getScrollDirectionX() {
        return scrollDirectionX;
    }

    public void setScrollDirectionX(int scrollDirectionX) {
        this.scrollDirectionX = (short) scrollDirectionX;
    }

    public short getScrollDirectionY() {
        return scrollDirectionY;
    }

    public void setScrollDirectionY(int scrollDirectionY) {
        this.scrollDirectionY = (short) scrollDirectionY;
    }

    public boolean getMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public float getMoveOffsetX() {
        return moveOffsetX;
    }

    public void setMoveOffsetX(float moveOffsetX) {
        this.moveOffsetX = moveOffsetX;
    }

    public float getMoveOffsetY() {
        return moveOffsetY;
    }

    public void setMoveOffsetY(float moveOffsetY) {
        this.moveOffsetY = moveOffsetY;
    }

    public boolean isPie() {
        return pie;
    }

    public void setPie(boolean pie) {
        this.pie = pie;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public boolean isParalizado() {
        return paralizado;
    }

    public void setParalizado(boolean paralizado) {
        this.paralizado = paralizado;
    }

    public byte getPriv() {
        return priv;
    }

    public void setPriv(int priv) {
        this.priv = (byte) priv;
    }

    public String getDialog() {
        return dialog;
    }

    public void setDialog(String dialog) {
        this.dialog = dialog;
    }

    public RGBColor getDialog_color() {
        return dialog_color;
    }

    public void setDialog_color(RGBColor dialog_color) {
        this.dialog_color = dialog_color;
    }

    public int getDialog_life() {
        return dialog_life;
    }

    public void setDialog_life(int dialog_life) {
        this.dialog_life = dialog_life;
    }

    public int getDialog_font_index() {
        return dialog_font_index;
    }

    public void setDialog_font_index(int dialog_font_index) {
        this.dialog_font_index = dialog_font_index;
    }

    public float getDialog_offset_counter_y() {
        return dialog_offset_counter_y;
    }

    public void setDialog_offset_counter_y(float dialog_offset_counter_y) {
        this.dialog_offset_counter_y = dialog_offset_counter_y;
    }

    public boolean isDialog_scroll() {
        return dialog_scroll;
    }

    public void setDialog_scroll(boolean dialog_scroll) {
        this.dialog_scroll = dialog_scroll;
    }

}
