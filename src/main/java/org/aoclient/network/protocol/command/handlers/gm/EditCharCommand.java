package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.command.CommandHandler;
import org.aoclient.network.protocol.types.CharacterEditType;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.protocol.Protocol.writeEditChar;

/**
 * /MOD nickname oro x
 */

public class EditCharCommand implements CommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (context.hasArguments() && context.getArgumentCount() >= 3) {
            String arg = context.getArgument(1).toUpperCase();
            int tmpInt = switch (arg) {
                case "BODY" -> CharacterEditType.BODY.getValue();
                case "HEAD" -> CharacterEditType.HEAD.getValue();
                case "ORO" -> CharacterEditType.GOLD.getValue();
                case "LEVEL" -> CharacterEditType.LEVEL.getValue();
                case "SKILLS" -> CharacterEditType.SKILLS.getValue();
                case "SKILLSLIBRES" -> CharacterEditType.SKILL_POINTS_LEFT.getValue();
                case "CLASE" -> CharacterEditType.CLASS.getValue();
                case "EXP" -> CharacterEditType.EXPERIENCE.getValue();
                case "CRI" -> CharacterEditType.CRIMINALS_KILLED.getValue();
                case "CIU" -> CharacterEditType.CITIZENS_KILLED.getValue();
                case "NOB" -> CharacterEditType.NOBILITY.getValue();
                case "ASE" -> CharacterEditType.ASSASSIN.getValue();
                case "SEX" -> CharacterEditType.SEX.getValue();
                case "RAZA" -> CharacterEditType.RACE.getValue();
                case "AGREGAR" -> CharacterEditType.ADD_GOLD.getValue();
                default -> -1;
            };
            if (tmpInt > 0) {
                if (context.getArgumentCount() == 3) writeEditChar(context.getArgument(0), tmpInt, context.getArgument(2), "");
                else writeEditChar(context.getArgument(0), tmpInt, context.getArgument(2), context.getArgument(3));
            } else
                console.addMsgToConsole(new String("Incorrect command.".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
        } else
            console.addMsgToConsole(new String("Missing parameters.".getBytes(), StandardCharsets.UTF_8), false, true, new RGBColor());
    }

}
