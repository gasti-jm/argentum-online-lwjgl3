package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;
import org.aoclient.network.protocol.types.CharacterEditType;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeEditChar;

/**
 * por ejemplo: /mod <nick> <oro> <amount>
 */

public class EditCharCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments() && context.getArgumentCount() >= 3) {
            String arg = context.getArgument(1);
            int tmpInt = switch (arg) {
                case "body" -> CharacterEditType.BODY.getValue();
                case "head" -> CharacterEditType.HEAD.getValue();
                case "oro" -> CharacterEditType.GOLD.getValue();
                case "level" -> CharacterEditType.LEVEL.getValue();
                case "skills" -> CharacterEditType.SKILLS.getValue();
                case "skillslibres" -> CharacterEditType.SKILL_POINTS_LEFT.getValue();
                case "clase" -> CharacterEditType.CLASS.getValue();
                case "exp" -> CharacterEditType.EXPERIENCE.getValue();
                case "cri" -> CharacterEditType.CRIMINALS_KILLED.getValue();
                case "ciu" -> CharacterEditType.CITIZENS_KILLED.getValue();
                case "nob" -> CharacterEditType.NOBILITY.getValue();
                case "ase" -> CharacterEditType.ASSASSIN.getValue();
                case "sex" -> CharacterEditType.SEX.getValue();
                case "raza" -> CharacterEditType.RACE.getValue();
                case "agregar" -> CharacterEditType.ADD_GOLD.getValue();
                default -> -1;
            };
            if (tmpInt > 0) {
                if (context.getArgumentCount() == 3) writeEditChar(context.getArgument(0), tmpInt, context.getArgument(2), "");
                else writeEditChar(context.getArgument(0), tmpInt, context.getArgument(2), context.getArgument(3));
            } else
                console.addMsgToConsole(new String("Incorrect command.".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
        } else
            console.addMsgToConsole(new String("Missing arguments.".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
