package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.network.PacketBuffer;
import org.aoclient.network.protocol.handlers.PacketHandler;
import org.tinylog.Logger;

public class SpawnListHandler implements PacketHandler  {
    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(3)) return;

        PacketBuffer buffer = new PacketBuffer();
        buffer.copy(data);

        buffer.readByte();

        String creatureList = buffer.readUTF8String();

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

        data.copy(buffer);
        Logger.debug("handleSpawnList Cargado! - FALTA TERMINAR!");
    }
}
