package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;

public interface PacketHandler {

    void handle(ByteQueue data);

}
