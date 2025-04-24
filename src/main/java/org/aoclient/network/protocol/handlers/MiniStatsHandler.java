package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.E_KillCounters;
import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;

public class MiniStatsHandler implements PacketHandler {
    
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(20)) return;

        // Remove packet ID
        data.readByte();

        int i = 1;
        for (E_KillCounters counter : E_KillCounters.values()) {
            if (i < E_KillCounters.values().length) {
                User.get().setKillCounter(counter.ordinal(), data.readLong());
            } else {
                User.get().setKillCounter(counter.ordinal(), data.readInteger());
            }
            i++;
        }
        User.get().setRole(data.readByte());
        User.get().setJailTime(data.readLong());

        //With UserEstadisticas
        //        .CiudadanosMatados = data.ReadLong()
        //        .CriminalesMatados = data.ReadLong()
        //        .UsuariosMatados = data.ReadLong()
        //        .NpcsMatados = data.ReadInteger()
        //        .Clase = ListaClases(data.ReadByte())
        //        .PenaCarcel = data.ReadLong()
        //    End With
    }
    
}
