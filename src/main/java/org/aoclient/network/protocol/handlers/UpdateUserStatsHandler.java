package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.network.PacketBuffer;

import static org.aoclient.engine.utils.GameData.charList;

public class UpdateUserStatsHandler implements PacketHandler {
    
    private final User user = User.INSTANCE;
    
    @Override
    public void handle(PacketBuffer data) {
        if (data.checkBytes(26)) return;

        // Remove packet ID
        data.readByte();

        user.setUserMaxHP(data.readInteger());
        user.setUserMinHP(data.readInteger());
        user.setUserMaxMAN(data.readInteger());
        user.setUserMinMAN(data.readInteger());
        user.setUserMaxSTA(data.readInteger());
        user.setUserMinSTA(data.readInteger());
        user.setUserGLD(data.readLong());
        user.setUserLvl(data.readByte());
        user.setUserPasarNivel(data.readLong());
        user.setUserExp(data.readLong());


        charList[user.getUserCharIndex()].setDead(user.getUserMinHP() <= 0);

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
