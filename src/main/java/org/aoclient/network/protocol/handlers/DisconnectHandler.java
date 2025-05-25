package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.Sound;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.network.PacketBuffer;
import org.aoclient.network.SocketConnection;
import org.tinylog.Logger;

import static org.aoclient.engine.Sound.playMusic;
import static org.aoclient.engine.game.models.Character.eraseAllChars;

public class DisconnectHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        buffer.readByte();

        SocketConnection.INSTANCE.disconnect();
        eraseAllChars();

        ImGUISystem.INSTANCE.closeAllFrms();

        Sound.clearSounds();

        /*
        'Hide main form
    //FrmMain.Visible = False

    'Stop audio
    Call Audio.StopWave
    //FrmMain.IsPlaying = PlayLoop.plNone

    'Show connection form
    frmConnect.Visible = True

    'Reset global vars
    UserDescansar = False
    UserParalizado = False
    pausa = False
    UserCiego = False
    UserMeditar = False
    UserNavegando = False
    bRain = False
    bFogata = False
    SkillPoints = 0
    Comerciando = False
    'new
    Traveling = False
    'Delete all kind of dialogs
    Call CleanDialogs

    'Reset some char variables...
    For i = 1 To LastChar
        charlist(i).invisible = False
    Next i


    For i = 1 To MAX_INVENTORY_SLOTS
        Call Inventario.SetItem(i, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "")
    Next i

    Call Audio.PlayMIDI("2.mid")
         */

        playMusic("2.ogg");
        Logger.debug("handleDisconnect CARGADO - FALTA TERMINAR!");
    }

}
