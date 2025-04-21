package org.aoclient.network.protocol;

import org.aoclient.network.ByteQueue;

public interface PacketHandler {

    void handle(ByteQueue data);

}
