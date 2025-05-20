package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.E_KillCounters;
import org.aoclient.network.PacketBuffer;

public class MiniStatsHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(20)) return;
        buffer.readByte();

        int i = 1;
        for (E_KillCounters counter : E_KillCounters.values()) {
            if (i < E_KillCounters.values().length) User.INSTANCE.setKillCounter(counter.ordinal(), buffer.readLong());
            else User.INSTANCE.setKillCounter(counter.ordinal(), buffer.readInteger());
            i++;
        }
        User.INSTANCE.setRole(buffer.readByte());
        User.INSTANCE.setJailTime(buffer.readLong());

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
