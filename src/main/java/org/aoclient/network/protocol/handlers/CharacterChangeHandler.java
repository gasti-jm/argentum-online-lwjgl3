package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.Direction;
import org.aoclient.engine.renderer.TextureManager;
import org.aoclient.engine.utils.inits.BodyData;
import org.aoclient.engine.utils.inits.HeadData;
import org.aoclient.engine.utils.inits.ShieldData;
import org.aoclient.engine.utils.inits.WeaponData;
import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

import static org.aoclient.engine.game.models.Character.CASPER_HEAD;
import static org.aoclient.engine.game.models.Character.refreshAllChars;
import static org.aoclient.engine.utils.GameData.*;

public class CharacterChangeHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(18)) return;
        buffer.readByte();

        short charIndex = buffer.readInteger();
        short tempint = buffer.readInteger();

        // esta dentro del rango del array de bodydata?
        if (tempint < 1 || tempint > bodyData.length) {
            charList[charIndex].setBody(new BodyData(bodyData[0]));
            charList[charIndex].setiBody(0);
        } else {
            charList[charIndex].setBody(new BodyData(bodyData[tempint]));
            charList[charIndex].setiBody(tempint);
        }

        short headIndex = buffer.readInteger();

        if (headIndex < 1 || headIndex > headData.length) {
            charList[charIndex].setHead(headData[0]);
            charList[charIndex].setiHead(0);
        } else {
            charList[charIndex].setHead(headData[headIndex]);
            charList[charIndex].setiHead(headIndex);
        }

        charList[charIndex].setDead(headIndex == CASPER_HEAD);
        charList[charIndex].setHeading(Direction.values()[buffer.readByte() - 1]);

        tempint = buffer.readInteger();
        if (tempint != 0) charList[charIndex].setWeapon(new WeaponData(weaponData[tempint]));
        else charList[charIndex].setWeapon(new WeaponData());

        tempint = buffer.readInteger();
        if (tempint != 0) charList[charIndex].setShield(new ShieldData(shieldData[tempint]));
        else charList[charIndex].setShield(new ShieldData());

        tempint = buffer.readInteger();
        if (tempint != 0) charList[charIndex].setHelmet(new HeadData(helmetsData[tempint]));
        else charList[charIndex].setHelmet(new HeadData());

        User.INSTANCE.setCharacterFx(charIndex, buffer.readInteger(), buffer.readInteger());

        refreshAllChars();
        Logger.debug("handleCharacterChange Cargado! - FALTA TERMINAR!");
    }

}
