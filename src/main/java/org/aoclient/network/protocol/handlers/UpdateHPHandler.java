package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.ByteQueue;
import org.tinylog.Logger;

import static org.aoclient.engine.utils.GameData.charList;

public class UpdateHPHandler implements PacketHandler {
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(3)) return;

        data.readByte();

        User.get().setUserMinHP(data.readInteger());

        charList[User.get().getUserCharIndex()].setDead(User.get().getUserMinHP() <= 0);

        //
        //    'Is the user alive??
        //    If UserMinHP = 0 Then
        //        UserEstado = 1
        //        If //FrmMain.TrainingMacro Then Call //FrmMain.DesactivarMacroHechizos
        //        If //FrmMain.macrotrabajo Then Call //FrmMain.DesactivarMacroTrabajo
        //    Else
        //        UserEstado = 0
        //    End If

        Logger.debug("handleUpdateHP CARGADO - FALTA DESACTIVAR MACROS DE TRABAJO!");
    }
}
