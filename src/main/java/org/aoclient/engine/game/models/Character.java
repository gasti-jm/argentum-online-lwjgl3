package org.aoclient.engine.game.models;

import org.aoclient.engine.game.UserLogic;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.engine.utils.filedata.*;

import static org.aoclient.engine.game.models.E_Heading.SOUTH;
import static org.aoclient.engine.renderer.Drawn.drawTexture;
import static org.aoclient.engine.renderer.FontText.drawText;
import static org.aoclient.engine.renderer.FontText.getSizeText;
import static org.aoclient.engine.utils.GameData.*;
import static org.aoclient.engine.utils.Time.timerTicksPerFrame;

public final class Character {
    public static final int CASPER_HEAD = 500;
    public static final int FRAGATA_FANTASMAL = 87;

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

    private byte criminal;
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
    private byte priv;


    public Character() {
        body = new BodyData();
        head = new HeadData();
        helmet = new HeadData();
        weapon = new WeaponData();
        shield = new ShieldData();
        this.pos = new Position();
        this.heading = SOUTH;
        this.active = false;
        this.walkingSpeed = 8;
    }

    public static void makeChar(int charIndex, int body, int head,  E_Heading heading, int x, int y, int weapon, int shield, int helmet) {
        if (charIndex > UserLogic.get().getLastChar())
            UserLogic.get().setLastChar(charIndex);


        if (weapon <= 0) weapon = 2;
        if (shield <= 0) shield = 2;
        if (helmet <= 0) helmet = 2;

        char f = '<', u = '>';
        charList[charIndex].setClanName("");

        charList[charIndex].setiHead(head);
        charList[charIndex].setiBody(body);

        charList[charIndex].setHead(new HeadData(headData[head]));
        charList[charIndex].setBody(new BodyData(bodyData[body]));
        charList[charIndex].setWeapon(new WeaponData(weaponData[weapon]));
        charList[charIndex].setShield(new ShieldData(shieldData[shield]));
        charList[charIndex].setHelmet(new HeadData(helmetsData[helmet]));

        charList[charIndex].setHeading(heading);

        charList[charIndex].setMoving(false);
        charList[charIndex].setMoveOffsetX(0);
        charList[charIndex].setMoveOffsetY(0);

        charList[charIndex].getPos().setX(x);
        charList[charIndex].getPos().setY(y);

        charList[charIndex].setActive(true);

        mapData[x][y].setCharIndex(charIndex);
    }

    public static void eraseChar(int charIndex) {
        charList[charIndex].setActive(false);

        if (charIndex == UserLogic.get().getLastChar()) {
            while (!charList[UserLogic.get().getLastChar()].isActive()) {
                UserLogic.get().setLastChar(UserLogic.get().getLastChar() - 1);
                if (UserLogic.get().getLastChar() == 0) {
                    break;
                }
            }
        }

        mapData[charList[charIndex].getPos().getX()][charList[charIndex].getPos().getY()].setCharIndex(0);

        /*
            'Remove char's dialog
            Call Dialogos.RemoveDialog(CharIndex)
         */

        resetCharInfo(charIndex);
    }

    private static void resetCharInfo(int charIndex) {
        charList[charIndex] = new Character(); // al crear un obj nuevo, el viejo sera eliminado por el recolector de basura de java.
    }

    public static void refreshAllChars() {
        for (int LoopC = 1; LoopC <= UserLogic.get().getLastChar(); LoopC++) {
            if (charList[LoopC].isActive()) {
                mapData[charList[LoopC].getPos().getX()][charList[LoopC].getPos().getY()].setCharIndex(LoopC);
            }
        }
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

    public byte getCriminal() {
        return criminal;
    }

    public void setCriminal(byte criminal) {
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

    public void setScrollDirectionX(int  scrollDirectionX) {
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

    public byte getPriv() {
        return priv;
    }

    public void setPriv(byte priv) {
        this.priv = priv;
    }

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

                    if (charList[charIndex].getName().length() > 0) {
                        if (charList[charIndex].getPriv() == 0) {
                            color.setRed(0.0f);
                            color.setGreen(0.5f);
                            color.setBlue(1.0f);
                        }

                        String line = charList[charIndex].getName();
                        drawText(line, PixelOffsetX - (getSizeText(line) / 2) + 15, PixelOffsetY + 30, color, 0, true, false, false);

                        line = charList[charIndex].getClanName();
                        if (!line.isEmpty()) {
                            drawText(line, PixelOffsetX - (getSizeText(line) / 2) + 15, PixelOffsetY + 43, color, 0, true, false, false);
                        }

                    }

                }
            }

            // Draw FX
            if (charList[charIndex].getFxIndex() != 0) {
                drawTexture(charList[charIndex].getfX(),
                        PixelOffsetX + fxData[charList[charIndex].getFxIndex()].getOffsetX(),
                        PixelOffsetY + fxData[charList[charIndex].getFxIndex()].getOffsetY(),
                        true, true, true,1.0f, ambientcolor);

                // Check if animation is over
                if (!charList[charIndex].getfX().isStarted())
                    charList[charIndex].setFxIndex(0);
            }

        } else {
            if (charList[charIndex].getBody().getWalk(charList[charIndex].getHeading().value).getGrhIndex() > 0) {
                drawTexture(charList[charIndex].getBody().getWalk(charList[charIndex].getHeading().value),
                        PixelOffsetX, PixelOffsetY, true, true, false, 1.0f, ambientcolor);
            }

        }
    }

    public static int sgn(short number) {
        if (number == 0) return 0;
        return (number / Math.abs(number));
    }
}
