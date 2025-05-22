package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.Direction;
import org.aoclient.network.PacketBuffer;
import org.aoclient.network.protocol.types.NickColorType;
import org.aoclient.network.protocol.types.PlayerType;

import static org.aoclient.engine.game.models.Character.makeChar;
import static org.aoclient.engine.game.models.Character.refreshAllChars;
import static org.aoclient.engine.utils.GameData.charList;

public class CharacterCreateHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(24)) return;
        PacketBuffer tempBuffer = new PacketBuffer();
        tempBuffer.copy(buffer);
        tempBuffer.readByte();

        short charIndex = tempBuffer.readInteger();
        short body = tempBuffer.readInteger();
        short head = tempBuffer.readInteger();
        int numHeading = tempBuffer.readByte();
        Direction direction = Direction.values()[numHeading - 1];
        int x = tempBuffer.readByte();
        int y = tempBuffer.readByte();
        short weapon = tempBuffer.readInteger();
        short shield = tempBuffer.readInteger();
        short helmet = tempBuffer.readInteger();

        User.INSTANCE.setCharacterFx(charIndex, tempBuffer.readInteger(), tempBuffer.readInteger());

        charList[charIndex].setName(tempBuffer.readCp1252String());

        int nickColor = tempBuffer.readByte();
        int privs = tempBuffer.readByte();

        charList[charIndex].setCriminal((nickColor & NickColorType.CRIMINAL.getId()) != 0);

        charList[charIndex].setAttackable((nickColor & NickColorType.ATACABLE.getId()) != 0);

        if (privs != 0) {

            if ((privs & PlayerType.CHAOS_COUNCIL.getId()) != 0 && (privs & PlayerType.USER.getId()) == 0)
                privs = (short) (privs ^ PlayerType.CHAOS_COUNCIL.getId());
            if ((privs & PlayerType.ROYAL_COUNCIL.getId()) != 0 && (privs & PlayerType.USER.getId()) == 0)
                privs = (short) (privs ^ PlayerType.ROYAL_COUNCIL.getId());
            if ((privs & PlayerType.ROLE_MASTER.getId()) != 0) privs = PlayerType.ROLE_MASTER.getId();

            int logPrivs = (int) (Math.log(privs) / Math.log(2));
            charList[charIndex].setPriv(logPrivs);

        } else charList[charIndex].setPriv(0);

        makeChar(charIndex, body, head, direction, x, y, weapon, shield, helmet);
        refreshAllChars();

        buffer.copy(tempBuffer);
    }

}
