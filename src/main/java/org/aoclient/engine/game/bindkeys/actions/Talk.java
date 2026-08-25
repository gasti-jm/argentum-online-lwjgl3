package org.aoclient.engine.game.bindkeys.actions;

import org.aoclient.engine.game.player.Player;
import org.aoclient.engine.game.bindkeys.KeyAction;
import org.aoclient.engine.game.console.Console;
import org.aoclient.engine.game.console.FontStyle;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.Protocol;
import org.aoclient.network.protocol.command.execution.CommandExecutor;

import static org.aoclient.engine.scenes.GameScene.frmMain;

public class Talk implements KeyAction {
    @Override
    public void action() {
        // TODO: Es para evitar bugs y no se te trabe todo el juego cuando tenes algun frm abierto
        //  y sin querer pulsar para hablar.
        if (ImGUISystem.INSTANCE.isMainLast()) {
            if (!frmMain.getSendText().isBlank() && Player.INSTANCE.isTalking()) {
                if (!frmMain.getSendText().startsWith("/")) {
                    Console.INSTANCE.addMsgToConsole("[" + Player.INSTANCE.getUserName().toLowerCase() + "] " + frmMain.getSendText(), FontStyle.REGULAR, new RGBColor(1f, 1f, 1f));
                    Protocol.talk(frmMain.getSendText());
                } else CommandExecutor.INSTANCE.execute(frmMain.getSendText());
            }
            Player.INSTANCE.setTalking(!Player.INSTANCE.isTalking());
            frmMain.clearSendTxt();
        }
    }
}
