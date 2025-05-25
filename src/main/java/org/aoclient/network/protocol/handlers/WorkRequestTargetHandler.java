package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.Messages;
import org.aoclient.engine.Window;
import org.aoclient.engine.game.Console;
import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.Skill;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.PacketBuffer;
import org.tinylog.Logger;

import static org.aoclient.engine.Messages.MessageKey;
import static org.aoclient.engine.game.models.Skill.FundirMetal;

public class WorkRequestTargetHandler implements PacketHandler {

    private final Console console = Console.INSTANCE;

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(2)) return;
        buffer.readByte();

        final int usingSkill = buffer.readByte();
        User.INSTANCE.setUsingSkill(usingSkill);

        Window.INSTANCE.setCursorCrosshair(true);

        switch (Skill.values()[usingSkill - 1]) {
            case MAGIC:
                console.addMsgToConsole(Messages.get(MessageKey.TRABAJO_MAGIA), false, false, new RGBColor());
                break;
            case FISHING:
                console.addMsgToConsole(Messages.get(MessageKey.TRABAJO_PESCA), false, false, new RGBColor());
                break;
            case THEFT:
                console.addMsgToConsole(Messages.get(MessageKey.TRABAJO_ROBAR), false, false, new RGBColor());
                break;
            case WOODCUTTING:
                console.addMsgToConsole(Messages.get(MessageKey.TRABAJO_TALAR), false, false, new RGBColor());
                break;
            case MINING:
                console.addMsgToConsole(Messages.get(MessageKey.TRABAJO_MINERIA), false, false, new RGBColor());
                break;
            case ARCHERY:
                console.addMsgToConsole(Messages.get(MessageKey.TRABAJO_PROYECTILES), false, false, new RGBColor());
                break;
        }

        if (usingSkill == FundirMetal)
            console.addMsgToConsole(Messages.get(MessageKey.TRABAJO_FUNDIRMETAL), false, false, new RGBColor());

        Logger.debug("handleWorkRequestTarget Cargado! - FALTA TESTIAR!");
    }

}
