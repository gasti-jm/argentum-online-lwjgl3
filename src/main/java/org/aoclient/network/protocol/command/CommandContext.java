package org.aoclient.network.protocol.command;

/**
 * Esta clase representa el contexto de un comando proporcionado como entrada en formato de texto. Su finalidad es analizar y
 * estructurar la entrada, permitiendo acceder al comando, argumentos asociados y otras propiedades relevantes.
 * <p>
 * Un comando es tipicamente identificado por un prefijo especifico, como "/". Este contexto permite reconocer tales prefijos, asi
 * como los argumentos que siguen al comando principal.
 */

public class CommandContext {

    private final String rawCommand;
    private final String command;
    private String[] arguments = new String[0];
    /** Cadena de argumentos de comando en crudo que preserva espacios y formateo. */
    private String argumentsRaw = "";

    /**
     * Constructor que inicializa un contexto de comando a partir de un comando en formato de texto.
     *
     * @param rawCommand Comando en formato de texto completo que incluye tanto el comando como sus argumentos. Por ejemplo, en la
     *                   entrada "/TELEP nickname map x y", el comando seria "/TELEP" y los argumentos serian "nickname map x y".
     */
    public CommandContext(String rawCommand) {
        this.rawCommand = rawCommand;

        /* El 2 es el limite que controla la cantidad de veces que se aplica el patron (la division del comando en crudo) y, por
         * lo tanto, afecta la longitud de la matriz resultante. Por lo que, para el comando principal, parts[0] contiene el
         * comando ("/TELEP", por ejemplo) y parts[1] los argumentos ("nickname map x y", por ejemplo). */
        String[] parts = rawCommand.split(" ", 2);
        command = parts[0];

        // Si hay argumentos
        if (parts.length > 1) {
            // Almacena los argumentos en crudo en argumentsRaw
            argumentsRaw = parts[1];
            /* Elimina los posibles espacios al principio y final de los argumentos, y divide los argumentos separados por un
             * espacio. Si parts[1].trim() esta vacio, split(" ") produce un array con un elemento vacio, no un array vacio. */
            arguments = parts[1].trim().split(" ");
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
