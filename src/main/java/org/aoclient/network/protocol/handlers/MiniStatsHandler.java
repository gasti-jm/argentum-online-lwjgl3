package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.player.Player;
import org.aoclient.engine.game.models.KillCounter;
import org.aoclient.network.PacketBuffer;

public class MiniStatsHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(20)) return;
        buffer.readByte();

        int i = 1;
        for (KillCounter counter : KillCounter.values()) {
            if (i < KillCounter.values().length) Player.INSTANCE.setKillCounter(counter.ordinal(), buffer.readLong());
            else Player.INSTANCE.setKillCounter(counter.ordinal(), buffer.readInteger());
            i++;
        }

        Player.INSTANCE.setRole(buffer.readByte());
        Player.INSTANCE.setJailTime(buffer.readLong());

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
