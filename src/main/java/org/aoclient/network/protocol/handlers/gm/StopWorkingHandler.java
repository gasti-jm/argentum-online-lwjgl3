package org.aoclient.network.protocol.handlers.gm;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;

public class StopWorkingHandler implements PacketHandler {

    @Override
    public void handle(ByteQueue data) {
        data.readByte();

        //With FontTypes(FontTypeNames.FONTTYPE_INFO)
        //        Call ShowConsoleMsg("¡Has terminado de trabajar!", .red, .green, .blue, .bold, .italic)
        //    End With
        //
        //    If //FrmMain.macrotrabajo.Enabled Then Call //FrmMain.DesactivarMacroTrabajo
    }

}
