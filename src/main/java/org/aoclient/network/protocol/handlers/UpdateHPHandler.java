package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.player.Player;
import org.aoclient.engine.utils.Log;
import org.aoclient.network.PacketBuffer;

import static org.aoclient.engine.utils.GameData.charList;

public class UpdateHPHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(3)) return;
        buffer.readByte();

        Player.INSTANCE.setUserMinHP(buffer.readInteger());

        charList[Player.INSTANCE.getUserCharIndex()].setDead(Player.INSTANCE.getUserMinHP() <= 0);

        //
        //    'Is the user alive??
        //    If UserMinHP = 0 Then
        //        UserEstado = 1
        //        If //FrmMain.TrainingMacro Then Call //FrmMain.DesactivarMacroHechizos
        //        If //FrmMain.macrotrabajo Then Call //FrmMain.DesactivarMacroTrabajo
        //    Else
        //        UserEstado = 0
        //    End If

        Log.debug("handleUpdateHP CARGADO - FALTA DESACTIVAR MACROS DE TRABAJO!");
    }

}
