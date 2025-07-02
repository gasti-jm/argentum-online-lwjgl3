package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.TextContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeNickToIP;

@Command("/nick2ip")
@SuppressWarnings("unused")
public class NickToIpCommand extends BaseCommandHandler {

    @Override
    public void handle(TextContext textContext) throws CommandException {
        requireArguments(textContext, 1, "/nick2ip <nick>");
        String nick = textContext.getArgument(0);
        writeNickToIP(nick);
    }

}
