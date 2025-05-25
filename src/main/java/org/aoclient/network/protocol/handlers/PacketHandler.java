package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;

/**
 * Representa un contrato que define la accion de manejar los paquetes del servidor.
 */

public interface PacketHandler {

    void handle(PacketBuffer buffer);

}
