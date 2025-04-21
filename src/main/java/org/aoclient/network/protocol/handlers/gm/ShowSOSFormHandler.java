package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

public class ShowSOSFormHandler implements PacketHandler  {
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        buffer.readByte();
        String sosList = buffer.readASCIIString();

        //Dim sosList() As String
        //    Dim i As Long
        //
        //    sosList = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //
        //    For i = 0 To UBound(sosList())
        //        Call frmMSG.List1.AddItem(sosList(i))
        //    Next i
        //
        //    frmMSG.Show , //FrmMain

        data.copyBuffer(buffer);
        Logger.debug("handleShowSOSForm Cargado! - FALTA TERMINAR!");
    }
}
