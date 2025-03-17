package org.aoclient;

import org.aoclient.engine.Engine;

/**
 * Clase principal que actua como punto de entrada de la aplicacion.
 * <p>
 * Esta clase contiene el metodo main que sirve como punto de inicio para la ejecucion del cliente. Su unica responsabilidad es
 * instanciar el motor grafico (Engine) y ejecutar su metodo {@code start()}, iniciando asi toda la secuencia de arranque del
 * juego.
 */

public class Main {

    public static void main(String[] args) {
        new Engine().start();
    }

}