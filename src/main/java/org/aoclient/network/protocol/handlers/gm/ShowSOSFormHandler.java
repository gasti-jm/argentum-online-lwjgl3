package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.network.PacketBuffer;
import org.aoclient.network.protocol.handlers.PacketHandler;
import org.tinylog.Logger;

public class ShowSOSFormHandler implements PacketHandler  {
    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(3)) return;

        PacketBuffer buffer = new PacketBuffer();
        buffer.copy(data);

        buffer.readByte();
        String sosList = buffer.readUTF8String();

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

        data.copy(buffer);
        Logger.debug("handleShowSOSForm Cargado! - FALTA TERMINAR!");
    }
}
