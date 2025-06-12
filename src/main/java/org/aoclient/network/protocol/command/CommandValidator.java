package org.aoclient.network.protocol.command;

import org.aoclient.engine.game.User;
import org.aoclient.network.protocol.types.NumericType;

/**
 * Esta clase proporciona metodos para validar comandos, argumentos y diversos tipos de datos. Incluye validaciones relacionadas
 * con estados del usuario, formatos numericos y direcciones IP. Los metodos de esta clase son utilizados para garantizar que las
 * entradas cumplan con los criterios especificados antes de ejecutar las operaciones asociadas.
 */

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

    /**
     * Valida si la cadena representativa de un numero pertenece al rango definido por el tipo numerico especificado.
     *
     * @param number      cadena que representa un numero a validar
     * @param numericType tipo numerico (por ejemplo, BYTE, INTEGER, LONG) que define el rango permitido
     * @return {@code true} si el numero esta dentro del rango especificado por el tipo numerico; {@code false} en caso contrario,
     * o si la cadena no es un numero valido
     */
    public boolean isValidNumber(String number, NumericType numericType) {
        try {
            /* Se usa long como un "tipo intermedio" que puede contener cualquier valor de los tipos mas pequeños, permitiendo una
             * validacion uniforme antes de convertir al tipo especifico requerido. */
            return numericType.isInRange(Long.parseLong(number));
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

    /**
     * Verifica si una direccion IP proporcionada es una direccion IPv4 valida.
     * <p>
     * Una direccion IPv4 valida debe cumplir con los siguientes criterios:
     * <ul>
     *  <li>Estar compuesta de exactamente cuatro segmentos separados por puntos (".").</li>
     *  <li>Cada segmento debe ser un numero entero entre 0 y 255.</li>
     *  <li>No debe contener ceros a la izquierda en ninguno de los segmentos, excepto cuando el segmento sea "0".</li>
     * </ul>
     *
     * @param ip direccion IP que se desea validar como cadena de texto
     * @return {@code true} si la direccion IP es una IPv4 valida; {@code false} en caso contrario, o si la cadena es {@code null}
     */
    public boolean isValidIPv4(String ip) {
        if (ip == null) return false;
        String[] parts = ip.split("\\.", -1);
        if (parts.length != 4) return false;
        for (String part : parts) {
            try {
                int num = Integer.parseInt(part);
                if (num < 0 || num > 255) return false;
                // Evita leading zeros
                if (!part.equals(String.valueOf(num))) return false;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * Convierte una direccion IPv4 en formato de texto a un arreglo de enteros.
     * <p>
     * La direccion IP debe estar compuesta por cuatro segmentos separados por puntos ("."), donde cada segmento debe ser un
     * numero entero entre 0 y 255. Si alguno de los criterios no se cumple o si el formato no es valido, el metodo devolvera
     * {@code null}.
     *
     * @param ip direccion IPv4 en formato de cadena que se desea convertir
     * @return un arreglo de cuatro enteros que representan cada segmento de la direccion IP, o {@code null} si la direccion no es
     * valida
     */
    public int[] parseIPv4ToArray(String ip) {
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
