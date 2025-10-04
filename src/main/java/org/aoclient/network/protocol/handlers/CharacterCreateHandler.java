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

        String name = tempBuffer.readUTF8String();
        charList[charIndex].setName(name);

        int nickColor = tempBuffer.readByte();
        /* Se nombra como plural ya que el servidor maneja varios privilegios (user, consejero, semidios, etc.), pero deberia
         * cambiar a solo dos privilegios: user o admin (gm). */
        int privileges = tempBuffer.readByte();

        charList[charIndex].setCriminal((nickColor & NickColorType.CRIMINAL.getId()) != 0);
        charList[charIndex].setAttackable((nickColor & NickColorType.ATACABLE.getId()) != 0);

        /* Si el nombre del usuario es igual al nombre del usuario leido desde el servidor, entonces establece el privilegio (que
         * se definio en el servidor dependiendo del nombre) al usuario. */
        if (User.INSTANCE.getUserName().equalsIgnoreCase(name)) User.INSTANCE.setPrivilege(privileges);

        if (privileges != 0) {

            if ((privileges & PlayerType.CHAOS_COUNCIL.getId()) != 0 && (privileges & PlayerType.USER.getId()) == 0)
                privileges = (short) (privileges ^ PlayerType.CHAOS_COUNCIL.getId());
            if ((privileges & PlayerType.ROYAL_COUNCIL.getId()) != 0 && (privileges & PlayerType.USER.getId()) == 0)
                privileges = (short) (privileges ^ PlayerType.ROYAL_COUNCIL.getId());
            if ((privileges & PlayerType.ROLE_MASTER.getId()) != 0) privileges = PlayerType.ROLE_MASTER.getId();

            int logPrivs = (int) (Math.log(privileges) / Math.log(2));
            charList[charIndex].setPriv(logPrivs);

        } else charList[charIndex].setPriv(0);

        makeChar(charIndex, body, head, direction, x, y, weapon, shield, helmet);
        refreshAllChars();

        buffer.copy(tempBuffer);
    }

}
