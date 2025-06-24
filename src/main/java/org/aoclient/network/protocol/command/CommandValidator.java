package org.aoclient.network.protocol.command;

/*
 * Enumeracion {@code CommandValidator} utilizada para realizar validaciones relacionadas con comandos y direcciones IP.
 * <p>
 * Contiene metodos utilitarios para validar direcciones IPv4 y manipular cadenas de texto asociadas a comandos, proporcionando
 * herramientas para manejar entradas de comandos en un sistema de red.
 */

public enum CommandValidator {

    INSTANCE;

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
