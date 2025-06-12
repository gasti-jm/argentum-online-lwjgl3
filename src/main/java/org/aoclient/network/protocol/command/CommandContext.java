package org.aoclient.network.protocol.command;

/**
 * Representa el contexto de un comando proporcionado, incluyendo su forma en crudo, la parte principal del comando, los
 * argumentos asociados y su forma en texto completo.
 * <p>
 * Esta clase ofrece funcionalidades para analizar comandos en formato texto y extraer su información principal, como el nombre
 * del comando, los argumentos, entre otros datos. Adicionalmente, incluye verificaciones que permiten identificar si el texto
 * representa un comando o un mensaje especial.
 */

public class CommandContext {

    private final String rawCommand;
    private final String command;
    private final String[] arguments;
    private final String argumentsRaw;

    /**
     * Analiza el comando en crudo (rawCommand) en formato de texto separandolo en su parte principal (command) y sus argumentos
     * (arguments).
     */
    public CommandContext(String rawCommand) {
        this.rawCommand = rawCommand;

        /* El 2 es el limite que controla la cantidad de veces que se aplica el patron (la division del comando en crudo) y, por
         * lo tanto, afecta la longitud de la matriz resultante. Por lo que, para el comando principal, parts[0] contiene el
         * comando ("/TELEP", por ejemplo) y parts[1] los argumentos ("nickname map x y", por ejemplo). */
        String[] parts = rawCommand.split(" ", 2);
        command = parts[0];

        if (parts.length > 1) {
            argumentsRaw = parts[1];
            arguments = parts[1].split(" ");
        } else {
            argumentsRaw = "";
            arguments = new String[0];
        }
    }

    public boolean isCommand() {
        return command.startsWith("/");
    }

    public boolean isYell() {
        return command.startsWith("-");
    }

    public String getCommand() {
        return command;
    }

    public String[] getArguments() {
        return arguments;
    }

    public String getArgumentsRaw() {
        return argumentsRaw;
    }

    public String getMessage() {
        return isYell() ? rawCommand.substring(1) : rawCommand;
    }

    public int getArgumentCount() {
        return arguments.length;
    }

    /**
     * Verifica si hay argumentos presentes en el comando.
     *
     * @return {@code true} si el comando tiene al menos un argumento y la cadena de texto que los representa no esta vacia o solo
     * contiene espacios en blanco, de lo contrario, {@code false}.
     */
    public boolean hasArguments() {
        return arguments.length > 0 && !argumentsRaw.trim().isEmpty();
    }

    public String getArgument(int index) {
        return index < arguments.length ? arguments[index] : "";
    }

    public void setArgument(int index, String str) {
        arguments[index] = str;
    }

}
