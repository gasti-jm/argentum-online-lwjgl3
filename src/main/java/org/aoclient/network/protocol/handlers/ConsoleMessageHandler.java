package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.Console;
import org.aoclient.engine.game.models.E_FontType;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.ByteQueue;
import org.tinylog.Logger;

public class ConsoleMessageHandler implements PacketHandler {
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(4)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        buffer.readByte();

        String chat = buffer.readASCIIString();
        E_FontType fontType = E_FontType.values()[buffer.readByte()];

        Console.get().addMsgToConsole(chat, false, false, new RGBColor(fontType.r, fontType.g, fontType.b));


        //        With FontTypes(FontIndex)
        //            Call AddtoRichTextBox(//FrmMain.RecTxt, chat, .red, .green, .blue, .bold, .italic)
        //        End With
        //
        //        ' Para no perder el foco cuando chatea por party
        //        If FontIndex = FontTypeNames.FONTTYPE_PARTY Then
        //            If MirandoParty Then frmParty.SendTxt.SetFocus
        //        End If
        //    End If

        data.copyBuffer(buffer);
        Logger.debug("handleConsoleMessage CARGADO - FALTA TERMINAR!");
    }
}
