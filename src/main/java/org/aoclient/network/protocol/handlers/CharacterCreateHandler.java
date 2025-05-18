package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.E_Heading;
import org.aoclient.network.PacketBuffer;
import org.aoclient.network.protocol.types.NickColorType;
import org.aoclient.network.protocol.types.PlayerType;

import static org.aoclient.engine.game.models.Character.makeChar;
import static org.aoclient.engine.game.models.Character.refreshAllChars;
import static org.aoclient.engine.utils.GameData.charList;

public class CharacterCreateHandler implements PacketHandler {
    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(24)) return;

        PacketBuffer buffer = new PacketBuffer();
        buffer.copy(data);

        // Remove packet ID
        buffer.readByte();

        short charIndex = buffer.readInteger();
        short body = buffer.readInteger();
        short head = buffer.readInteger();
        int numHeading = buffer.readByte();
        E_Heading heading = E_Heading.values()[numHeading - 1];
        int x = buffer.readByte();
        int y = buffer.readByte();
        short weapon = buffer.readInteger();
        short shield = buffer.readInteger();
        short helmet = buffer.readInteger();

        User.get().setCharacterFx(charIndex, buffer.readInteger(), buffer.readInteger());

        charList[charIndex].setName(buffer.readCp1252String());

        int nickColor = buffer.readByte();
        int privs = buffer.readByte();

        if ((nickColor & NickColorType.CRIMINAL.getId()) != 0) {
            charList[charIndex].setCriminal(true);
        } else {
            charList[charIndex].setCriminal(false);
        }

        charList[charIndex].setAttackable((nickColor & NickColorType.ATACABLE.getId()) != 0);

        if (privs != 0) {
            if ((privs & PlayerType.CHAOS_COUNCIL.getId()) != 0 && (privs & PlayerType.USER.getId()) == 0) {
                privs = (short) (privs ^ PlayerType.CHAOS_COUNCIL.getId());
            }

            if ((privs & PlayerType.ROYAL_COUNCIL.getId()) != 0 && (privs & PlayerType.USER.getId()) == 0) {
                privs = (short) (privs ^ PlayerType.ROYAL_COUNCIL.getId());
            }

            if ((privs & PlayerType.ROLE_MASTER.getId()) != 0) {
                privs = PlayerType.ROLE_MASTER.getId();
            }

            final int logPrivs = (int) (Math.log(privs) / Math.log(2));
            charList[charIndex].setPriv(logPrivs);
        } else {
            charList[charIndex].setPriv(0);
        }

        makeChar(charIndex, body, head, heading, x, y, weapon, shield, helmet);
        refreshAllChars();

        data.copy(buffer);
    }
}
