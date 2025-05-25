package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.network.PacketBuffer;
import org.aoclient.network.protocol.handlers.PacketHandler;
import org.tinylog.Logger;

public class ShowSOSFormHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(3)) return;
        PacketBuffer tempBuffer = new PacketBuffer();
        tempBuffer.copy(buffer);
        tempBuffer.readByte();
        String sosList = tempBuffer.readCp1252String();

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

        buffer.copy(tempBuffer);
        Logger.debug("handleShowSOSForm Cargado! - FALTA TERMINAR!");
    }

}
