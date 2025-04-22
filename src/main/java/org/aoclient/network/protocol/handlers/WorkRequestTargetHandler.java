package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.Window;
import org.aoclient.engine.game.Console;
import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.E_Skills;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

import static org.aoclient.engine.game.models.E_Skills.FundirMetal;
import static org.aoclient.network.Messages.*;

public class WorkRequestTargetHandler implements PacketHandler {

    private final Console console = Console.get();

    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(2)) return;

        data.readByte();

        final int usingSkill = data.readByte();
        User.get().setUsingSkill(usingSkill);

        Window.get().setCursorCrosshair(true);

        switch (E_Skills.values()[usingSkill - 1]) {
            case MAGIA:
                console.addMsgToConsole(MENSAJE_TRABAJO_MAGIA, false, false, new RGBColor());
                break;
            case PESCA:
                console.addMsgToConsole(MENSAJE_TRABAJO_PESCA, false, false, new RGBColor());
                break;
            case ROBAR:
                console.addMsgToConsole(MENSAJE_TRABAJO_ROBAR, false, false, new RGBColor());
                break;
            case TALAR:
                console.addMsgToConsole(MENSAJE_TRABAJO_TALAR, false, false, new RGBColor());
                break;
            case MINERIA:
                console.addMsgToConsole(MENSAJE_TRABAJO_MINERIA, false, false, new RGBColor());
                break;
            case PROYECTILES:
                console.addMsgToConsole(MENSAJE_TRABAJO_PROYECTILES, false, false, new RGBColor());
                break;
        }

        if (usingSkill == FundirMetal) console.addMsgToConsole(MENSAJE_TRABAJO_FUNDIRMETAL, false, false, new RGBColor());

        Logger.debug("handleWorkRequestTarget Cargado! - FALTA TESTIAR!");
    }
}
