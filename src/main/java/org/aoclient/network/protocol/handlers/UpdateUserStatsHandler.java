package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.PacketBuffer;

import static org.aoclient.engine.utils.GameData.charList;

public class UpdateUserStatsHandler implements PacketHandler {

    private final User user = User.INSTANCE;

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(30)) return;
        buffer.readByte();

        user.setUserMaxHP(buffer.readInteger());
        user.setUserMinHP(buffer.readInteger());
        user.setUserMaxMAN(buffer.readInteger());
        user.setUserMinMAN(buffer.readInteger());
        user.setUserMaxSTA(buffer.readInteger());
        user.setUserMinSTA(buffer.readInteger());
        user.setUserGLD(buffer.readLong());
        user.setUserLvl(buffer.readByte());
        user.setUserPasarNivel(buffer.readLong());
        user.setUserExp(buffer.readLong());
        int points = buffer.readLong();
        user.setFreeSkillPoints(points);

        charList[user.getUserCharIndex()].setDead(user.getUserMinHP() <= 0);
    }

}
