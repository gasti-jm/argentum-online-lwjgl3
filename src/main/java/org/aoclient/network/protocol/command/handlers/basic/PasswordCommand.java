package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FNewPassword;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;

public class PasswordCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        ImGUISystem.INSTANCE.show(new FNewPassword());
    }

}
