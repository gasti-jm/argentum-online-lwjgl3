package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

public class MiniStatsHandler implements PacketHandler {
    
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(20)) return;

        // Remove packet ID
        data.readByte();

        data.readLong();
        data.readLong();
        data.readLong();
        data.readInteger();
        data.readByte();
        data.readLong();

        //With UserEstadisticas
        //        .CiudadanosMatados = data.ReadLong()
        //        .CriminalesMatados = data.ReadLong()
        //        .UsuariosMatados = data.ReadLong()
        //        .NpcsMatados = data.ReadInteger()
        //        .Clase = ListaClases(data.ReadByte())
        //        .PenaCarcel = data.ReadLong()
        //    End With

        Logger.debug("handleMiniStats Cargado! - FALTA TERMINAR!");
    }
    
}
