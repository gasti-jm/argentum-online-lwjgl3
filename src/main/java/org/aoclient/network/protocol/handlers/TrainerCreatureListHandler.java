package org.aoclient.network.protocol.handlers;

import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class TrainerCreatureListHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(3)) return;

        PacketBuffer buffer = new PacketBuffer();
        buffer.copy(data);

        buffer.readByte();

        String creatures = buffer.readCp1252String();

        //creatures = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //
        //    For i = 0 To UBound(creatures())
        //        Call frmEntrenador.lstCriaturas.AddItem(creatures(i))
        //    Next i
        //    frmEntrenador.Show , //FrmMain

        data.copy(buffer);
        Logger.debug("handleTrainerCreatureList Cargado! - FALTA TERMINAR!");
    }
}
