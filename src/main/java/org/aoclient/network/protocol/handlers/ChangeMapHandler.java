package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.Rain;
import org.aoclient.engine.game.User;
import org.aoclient.engine.utils.GameData;
import org.aoclient.network.ByteQueue;

import static org.aoclient.engine.utils.GameData.bLluvia;

public class ChangeMapHandler implements PacketHandler {
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(5)) return;

        data.readByte();

        short userMap = data.readInteger();
        User.get().setUserMap(userMap);

        // Once on-the-fly editor is implemented check for map version before loading....
        // For now we just drop it
        data.readInteger();

        GameData.loadMap(userMap);

        if (!bLluvia[userMap]) {
            if (Rain.get().isRaining()) Rain.get().stopRainingSoundLoop();
        }
    }

}
