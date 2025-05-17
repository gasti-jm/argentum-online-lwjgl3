package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.PacketBuffer;

import static org.aoclient.engine.utils.GameData.charList;

public class UpdateUserStatsHandler implements PacketHandler {
    
    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(26)) return;

        // Remove packet ID
        data.readByte();

        User.get().setUserMaxHP(data.readInteger());
        User.get().setUserMinHP(data.readInteger());
        User.get().setUserMaxMAN(data.readInteger());
        User.get().setUserMinMAN(data.readInteger());
        User.get().setUserMaxSTA(data.readInteger());
        User.get().setUserMinSTA(data.readInteger());
        User.get().setUserGLD(data.readLong());
        User.get().setUserLvl(data.readByte());
        User.get().setUserPasarNivel(data.readLong());
        User.get().setUserExp(data.readLong());


        charList[User.get().getUserCharIndex()].setDead(User.get().getUserMinHP() <= 0);

        //
        //    If UserMinHP = 0 Then
        //        UserEstado = 1
        //        If //FrmMain.TrainingMacro Then Call //FrmMain.DesactivarMacroHechizos
        //        If //FrmMain.macrotrabajo Then Call //FrmMain.DesactivarMacroTrabajo
        //    Else
        //        UserEstado = 0
        //    End If
        //
    }
    
}
