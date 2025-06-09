package org.aoclient.network.protocol.command;

import org.aoclient.engine.game.User;
import org.aoclient.network.protocol.types.NumericType;

public class CommandValidator {

    public void requireAlive() throws CommandException {
        if (User.INSTANCE.isDead())
            throw new CommandException("¡Estás muerto!");
    }

    public void requireArguments(CommandContext context, int count) throws CommandException {
        if (context.getArgumentCount() < count)
            throw new CommandException("Faltan parámetros para el comando " + context.getCommand());
    }

    public void requireNumeric(String value, String paramName) throws CommandException {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new CommandException(paramName + " debe ser un número válido");
        }
    }

    public boolean isValidNumber(String numero, NumericType tipo) {
        if (!numero.matches("-?\\d+(\\.\\d+)?")) return false;
        try {
            long valor = Long.parseLong(numero);
            return switch (tipo) {
                case BYTE -> valor >= 0 && valor <= 255;
                case INTEGER -> valor >= -32768 && valor <= 32767;
                case LONG -> valor >= -2147483648L && valor <= 2147483647L;
                case TRIGGER -> valor >= 0 && valor <= 6;
            };
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // FIXME Esta cortando mal la cadena ejemplo: "jua.chr" falta la n
    public String[] AEMAILSplit(String text) {
        String[] tmpArr = new String[2];
        byte Pos;
        Pos = (byte) text.indexOf("-");
        if (Pos != 0) {
            tmpArr[0] = text.substring(0, Pos - 1);
            tmpArr[1] = text.substring(Pos + 1);
        } else tmpArr[0] = "";
        return tmpArr;
    }

    public boolean isValidIPv4(String ip) {
        String[] parts = ip.split("\\.");
        if (parts.length != 4) return false;
        for (String part : parts)
            if (!isValidNumber(part, NumericType.BYTE)) return false;
        return true;
    }

    // TODO Se podria cambiar de nombre
    public int[] str2ipv4l(String ip) {
        String[] parts = ip.split("\\.");
        if (parts.length != 4) return null;
        int[] ipArray = new int[4];
        try {
            for (int i = 0; i < 4; i++) {
                ipArray[i] = Integer.parseInt(parts[i]);
                // Valida que este en el rango de un byte (0-255)
                if (ipArray[i] < 0 || ipArray[i] > 255) return null;
            }
            return ipArray;
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
