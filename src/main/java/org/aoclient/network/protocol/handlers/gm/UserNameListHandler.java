package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.handlers.PacketHandler;
import org.tinylog.Logger;

public class UserNameListHandler implements PacketHandler {
    
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(3)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        buffer.readByte();

        String userList = buffer.readASCIIString();

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


        data.copyBuffer(buffer);
        Logger.debug("handleUserNameList Cargado! - FALTA TERMINAR!");
    }
    
}
