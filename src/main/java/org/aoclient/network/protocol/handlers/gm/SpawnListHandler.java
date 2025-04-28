package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.handlers.PacketHandler;
import org.tinylog.Logger;

public class SpawnListHandler implements PacketHandler  {
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        buffer.readByte();

        String creatureList = buffer.readASCIIString();

        //Dim creatureList() As String
        //    Dim i As Long
        //
        //    creatureList = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //
        //    For i = 0 To UBound(creatureList())
        //        Call frmSpawnList.lstCriaturas.AddItem(creatureList(i))
        //    Next i
        //    frmSpawnList.Show , //FrmMain
        //

        data.copyBuffer(buffer);
        Logger.debug("handleSpawnList Cargado! - FALTA TERMINAR!");
    }
}
