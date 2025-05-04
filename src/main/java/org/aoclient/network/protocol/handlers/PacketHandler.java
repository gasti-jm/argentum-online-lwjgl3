package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;

/**
 * <p>
 * Representa un contrato que define la accion esencial de manejar paquetes de datos de tipo {@link ByteQueue}.
 * <p>
 * Su objetivo principal es proporcionar un punto de entrada para procesar paquetes especificos, permitiendo que cada clase que
 * implemente esta interfaz defina su propia logica de manejo para los datos recibidos.
 */

public interface PacketHandler {

    void handle(ByteQueue data);

}
