package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.tinylog.Logger;

public class TrainerCreatureListHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        buffer.readByte();

        String creatures = buffer.readASCIIString();

        //creatures = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //
        //    For i = 0 To UBound(creatures())
        //        Call frmEntrenador.lstCriaturas.AddItem(creatures(i))
        //    Next i
        //    frmEntrenador.Show , //FrmMain

        data.copyBuffer(buffer);
        Logger.debug("handleTrainerCreatureList Cargado! - FALTA TERMINAR!");
    }
}
