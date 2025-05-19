package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.Console;
import org.aoclient.engine.game.models.E_FontType;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class ConsoleMessageHandler implements PacketHandler {
    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(4)) return;

        PacketBuffer buffer = new PacketBuffer();
        buffer.copy(data);

        buffer.readByte();

        String chat = buffer.readCp1252String();
        E_FontType fontType = E_FontType.values()[buffer.readByte()];

        Console.INSTANCE.addMsgToConsole(chat, false, false, new RGBColor(fontType.r, fontType.g, fontType.b));


        //        With FontTypes(FontIndex)
        //            Call AddtoRichTextBox(//FrmMain.RecTxt, chat, .red, .green, .blue, .bold, .italic)
        //        End With
        //
        //        ' Para no perder el foco cuando chatea por party
        //        If FontIndex = FontTypeNames.FONTTYPE_PARTY Then
        //            If MirandoParty Then frmParty.SendTxt.SetFocus
        //        End If
        //    End If

        data.copy(buffer);
        Logger.debug("handleConsoleMessage CARGADO - FALTA TERMINAR!");
    }
}
