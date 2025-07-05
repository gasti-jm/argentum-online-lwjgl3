package org.aoclient.network.protocol.command.handlers.gm;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;
import org.aoclient.network.protocol.types.CharacterEditType;

import static org.aoclient.network.protocol.Protocol.writeEditChar;
import static org.aoclient.network.protocol.command.GameCommand.MOD_PLAYER;

/**
 * Comando para editar propiedades de personajes.
 * <p>
 * Uso: {@code /mod <nick> <property> <value> [extra_param]}
 * <p>
 * Propiedades disponibles:
 * <ul>
 *  <li>body: Cambiar cuerpo del personaje
 *  <li>head: Cambiar cabeza del personaje
 *  <li>oro: Establecer cantidad de oro
 *  <li>level: Cambiar nivel del personaje
 *  <li>skills: Modificar habilidades
 *  <li>skillslibres: Puntos de habilidad disponibles
 *  <li>clase: Cambiar clase del personaje
 *  <li>exp: Modificar experiencia
 *  <li>cri: Criminales asesinados
 *  <li>ciu: Ciudadanos asesinados
 *  <li>nob: Puntos de nobleza
 *  <li>ase: Puntos de asesino
 *  <li>sex: Cambiar sexo del personaje
 *  <li>raza: Cambiar raza del personaje
 *  <li>vagregar: Agregar oro (no establecer)
 * </ul>
 * Ejemplos:
 * <br>
 * {@code /mod juan_777 oro 5000}
 * <br>
 * {@code /mod admin level 50}
 * <br>
 * {@code /mod player1 agregar 1000}
 */

public class EditCharCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext commandContext) throws CommandException {
        requireArguments(commandContext, 3, MOD_PLAYER.getCommand() + " <nick> <property> <value> [extra_param]");
        requireString(commandContext, 0, "nick");
        requireString(commandContext, 1, "property");
        requireString(commandContext, 2, "value");

        String nick = commandContext.getArgument(0);
        String property = commandContext.getArgument(1);
        String value = commandContext.getArgument(2);
        String extraParam = commandContext.getArgumentCount() >= 4 ? commandContext.getArgument(3) : ""; // Evita un NPE

        CharacterEditType editType = getEditTypeFromProperty(property);
        if (editType == null) showError("Unknown property: " + property + ". See command documentation for valid properties.");

        writeEditChar(nick, editType.getValue(), value, extraParam);
    }

    /**
     * Convierte la propiedad string a CharacterEditType.
     *
     * @param property propiedad como string
     * @return CharacterEditType correspondiente o null si no existe
     */
    private CharacterEditType getEditTypeFromProperty(String property) {
        return switch (property.toLowerCase()) {
            case "body" -> CharacterEditType.BODY;
            case "head" -> CharacterEditType.HEAD;
            case "oro" -> CharacterEditType.GOLD;
            case "level" -> CharacterEditType.LEVEL;
            case "skills" -> CharacterEditType.SKILLS;
            case "skillslibres" -> CharacterEditType.SKILL_POINTS_LEFT;
            case "clase" -> CharacterEditType.CLASS;
            case "exp" -> CharacterEditType.EXPERIENCE;
            case "cri" -> CharacterEditType.CRIMINALS_KILLED;
            case "ciu" -> CharacterEditType.CITIZENS_KILLED;
            case "nob" -> CharacterEditType.NOBILITY;
            case "ase" -> CharacterEditType.ASSASSIN;
            case "sex" -> CharacterEditType.SEX;
            case "raza" -> CharacterEditType.RACE;
            case "agregar" -> CharacterEditType.ADD_GOLD;
            default -> null;
        };
    }

}

