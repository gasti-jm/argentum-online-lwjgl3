package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.network.PacketBuffer;
import org.aoclient.network.protocol.handlers.PacketHandler;
import org.tinylog.Logger;

public class UserNameListHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(3)) return;
        PacketBuffer tempBuffer = new PacketBuffer();
        tempBuffer.copy(buffer);
        tempBuffer.readByte();

        String userList = tempBuffer.readCp1252String();

        //Dim userList() As String
        //    Dim i As Long
        //
        //    userList = Split(Buffer.ReadASCIIString(), SEPARATOR)
        //
        //    If frmPanelGm.Visible Then
        //        frmPanelGm.cboListaUsus.Clear
        //        For i = 0 To UBound(userList())
        //            Call frmPanelGm.cboListaUsus.AddItem(userList(i))
        //        Next i
        //        If frmPanelGm.cboListaUsus.ListCount > 0 Then frmPanelGm.cboListaUsus.ListIndex = 0
        //    End If


        buffer.copy(tempBuffer);
        Logger.debug("handleUserNameList Cargado! - FALTA TERMINAR!");
    }

}
