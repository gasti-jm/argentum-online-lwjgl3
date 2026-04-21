package org.aoclient;

import org.aoclient.engine.EngineGL;
import org.aoclient.engine.EngineVulkan;
import org.aoclient.engine.IEngine;
import org.aoclient.engine.utils.LaunchOptions;

import static org.aoclient.engine.utils.LaunchOptions.GRAPHIC_LIBRARY;


/**
 * Clase principal que actua como punto de entrada de la aplicacion.
 * <p>
 * Esta clase contiene el metodo main que sirve como punto de inicio para la ejecucion del cliente. Su unica
 * responsabilidad es instanciar el motor grafico (Engine) y ejecutar su metodo {@code start()}, iniciando asi toda la
 * secuencia de arranque del juego.
 */

public class Main {
    public static void main(String[] args) {
        LaunchOptions.init(args);
        IEngine engine;

        // TODO: if we want to implement another graphics library. The plan is to use Vulkan in the future.
        if(GRAPHIC_LIBRARY.equals("vulkan")) {
            engine = new EngineVulkan();
        } else {
            engine = new EngineGL();
        }

        engine.start();
    }

}