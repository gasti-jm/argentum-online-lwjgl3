package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.console.Console;
import org.aoclient.engine.game.console.FontStyle;
import org.aoclient.engine.game.models.FontType;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

public class ConsoleMessageHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(4)) return;
        PacketBuffer tempBuffer = new PacketBuffer();
        tempBuffer.copy(buffer);
        tempBuffer.readByte();

        String chat = tempBuffer.readCp1252String();
        FontType fontType = FontType.values()[tempBuffer.readByte()];

        Console.INSTANCE.addMsgToConsole(chat, FontStyle.REGULAR, new RGBColor(fontType.r, fontType.g, fontType.b));


        //
        //        ' Para no perder el foco cuando chatea por party
        //        If FontIndex = FontTypeNames.FONTTYPE_PARTY Then
        //            If MirandoParty Then frmParty.SendTxt.SetFocus
        //        End If
        //    End If

        buffer.copy(tempBuffer);
        Logger.debug("handleConsoleMessage CARGADO - FALTA TERMINAR: FOCO PARTY!");
    }

}
