package org.aoclient.engine.utils;

/**
 * En esta clase nos encaramos de los parametros de lanzamientos que establecemos en nuestra app de java.
 * Se utilizará para hacer pruebas en nuestro codigo.
 *
 * <p>
 * Ejemplo:
 * <pre>{@code
 * java org.aoclient.Main -vulkan -debug
 *
 * -vulkan: ejecuta el cliente con la libreria grafia VULKAN.
 * -debug: MODO DEBUG para la visualizacion de logs y datos.
 *
 * }</pre>
 * <p>
 */
public class LaunchOptions {
    // parametros de lanzamiento
    public static Boolean CLIENT_DEBUG      = false;
    public static String  GRAPHIC_LIBRARY   = "opengl";

    /**
     * Obtenemos "args" de main y establecemos los parametros según lo asignado.
     */
    public static void init(String[] args) {
        for (String arg : args) {
            if (arg.equals("-vulkan")) {
                GRAPHIC_LIBRARY = "vulkan"; break;
            }

            if(arg.equals("-debug")) {
                CLIENT_DEBUG = true; // cliente en modo DEBUG para ver más información del código.
            }
        }
    }

}
