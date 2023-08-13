package org.aoclient.engine.game.models;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.engine.utils.filedata.*;

import static org.aoclient.engine.game.models.E_Heading.SOUTH;
import static org.aoclient.engine.renderer.Drawn.*;
import static org.aoclient.engine.renderer.Drawn.draw;
import static org.aoclient.engine.utils.GameData.charList;
import static org.aoclient.engine.utils.GameData.fxData;
import static org.aoclient.engine.utils.Time.timerTicksPerFrame;

public class Character {
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
        this.pos = new Position();
        this.heading = SOUTH;
        this.active = false;
        this.walkingSpeed = 8;
    }

    public int getWalkingSpeed() {
        return walkingSpeed;
    }

    public void setWalkingSpeed(int walkingSpeed) {
        this.walkingSpeed = walkingSpeed;
    }

    public boolean getActive() {
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

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public short getiHead() {
        return iHead;
    }

    public void setiHead(short iHead) {
        this.iHead = iHead;
    }

    public short getiBody() {
        return iBody;
    }

    public void setiBody(short iBody) {
        this.iBody = iBody;
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

    public static void charRender(int charIndex, int PixelOffsetX, int PixelOffsetY, RGBColor ambientcolor) {
        boolean moved = false;
        RGBColor color = new RGBColor();

        if (charList.get(charIndex).getMoving()) {
            if (charList.get(charIndex).getScrollDirectionX() != 0) {

                charList.get(charIndex).setMoveOffsetX(charList.get(charIndex).getMoveOffsetX() +
                        charList.get(charIndex).getWalkingSpeed() * sgn(charList.get(charIndex).getScrollDirectionX()) * timerTicksPerFrame);

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
                        + charList.get(charIndex).getWalkingSpeed() * sgn(charList.get(charIndex).getScrollDirectionY()) * timerTicksPerFrame);


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

            charList.get(charIndex).setMoving(false);
        }

        PixelOffsetX += (int) charList.get(charIndex).getMoveOffsetX();
        PixelOffsetY += (int) charList.get(charIndex).getMoveOffsetY();

        if (charList.get(charIndex).getHead().getHead(charList.get(charIndex).getHeading().ordinal()).getGrhIndex() != 0) {
            if (!charList.get(charIndex).isInvisible()) {

                if (charList.get(charIndex).getBody().getWalk(charList.get(charIndex).getHeading().ordinal()).getGrhIndex() > 0) {
                    draw(charList.get(charIndex).getBody().getWalk(charList.get(charIndex).getHeading().ordinal()),
                            PixelOffsetX, PixelOffsetY, true, true, false, 1.0f, ambientcolor);
                }

                if (charList.get(charIndex).getHead().getHead(charList.get(charIndex).getHeading().ordinal()).getGrhIndex() != 0) {
                    draw(charList.get(charIndex).getHead().getHead(charList.get(charIndex).getHeading().ordinal()),
                            PixelOffsetX + charList.get(charIndex).getBody().getHeadOffset().getX(),
                            PixelOffsetY + charList.get(charIndex).getBody().getHeadOffset().getY(),
                            true, false, false, 1.0f, ambientcolor);

                    if (charList.get(charIndex).getHelmet().getHead(charList.get(charIndex).getHeading().ordinal()).getGrhIndex() != 0) {
                        draw(charList.get(charIndex).getHelmet().getHead(charList.get(charIndex).getHeading().ordinal()),
                                PixelOffsetX + charList.get(charIndex).getBody().getHeadOffset().getX(),
                                PixelOffsetY + charList.get(charIndex).getBody().getHeadOffset().getY() -34,
                                true, false, false, 1.0f, ambientcolor);
                    }

                    if (charList.get(charIndex).getWeapon().getWeaponWalk(charList.get(charIndex).getHeading().ordinal()).getGrhIndex() != 0) {
                        draw(charList.get(charIndex).getWeapon().getWeaponWalk(charList.get(charIndex).getHeading().ordinal()),
                                PixelOffsetX, PixelOffsetY, true, true, false, 1.0f, ambientcolor);
                    }

                    if (charList.get(charIndex).getShield().getShieldWalk(charList.get(charIndex).getHeading().ordinal()).getGrhIndex() != 0) {
                        draw(charList.get(charIndex).getShield().getShieldWalk(charList.get(charIndex).getHeading().ordinal()),
                                PixelOffsetX, PixelOffsetY, true, true, false, 1.0f, ambientcolor);
                    }

                    if (charList.get(charIndex).getName().length() > 0) {
                        if (charList.get(charIndex).getPriv() == 0) {
                            color.setRed(0.0f);
                            color.setGreen(0.5f);
                            color.setBlue(1.0f);
                        }

                        String line = charList.get(charIndex).getName();
                        drawText(line, PixelOffsetX - (getSizeText(line) / 2) + 15, PixelOffsetY + 30, color, 0, true);

                        line = charList.get(charIndex).getClanName();
                        drawText(line, PixelOffsetX - (getSizeText(line) / 2) + 15, PixelOffsetY + 43, color, 0, true);
                    }

                }
            }

            // Draw FX
            if (charList.get(charIndex).getFxIndex() != 0) {
                draw(charList.get(charIndex).getfX(),
                        PixelOffsetX + fxData[charList.get(charIndex).getFxIndex()].getOffsetX(),
                        PixelOffsetY + fxData[charList.get(charIndex).getFxIndex()].getOffsetY(),
                        true, true, true,1.0f, ambientcolor);

                // Check if animation is over
                if (!charList.get(charIndex).getfX().isStarted())
                    charList.get(charIndex).setFxIndex(0);
            }

        } else {
            if (charList.get(charIndex).getBody().getWalk(charList.get(charIndex).getHeading().ordinal()).getGrhIndex() > 0) {
                draw(charList.get(charIndex).getBody().getWalk(charList.get(charIndex).getHeading().ordinal()),
                        PixelOffsetX, PixelOffsetY, true, true, false, 1.0f, ambientcolor);
            }

        }
    }

    public static int sgn(short number) {
        if (number == 0) return 0;
        return (number / Math.abs(number));
    }
}
