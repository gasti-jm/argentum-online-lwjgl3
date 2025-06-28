package org.aoclient.network.protocol.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotacion que se utiliza para marcar una clase como un comando especifico en el sistema. Permite proporcionar un valor que
 * identifica el comando asociado.
 * <p>
 * La anotacion {@code Command} se define con un valor unico que sirve como identificador del comando. Este valor generalmente se
 * usa para registrar y procesar comandos dentro del sistema de manejo de comandos. Es especialmente util cuando se desea asociar
 * un comando con una accion o logica especifica.
 * <p>
 * Ejemplo de aplicacion:
 * <pre>{@code
 * @Command("/telep")
 * public class WarpCharCommand {
 *     // Implementacion del comando de teletransportacion
 * }
 * }</pre>
 * <p>
 * Esta anotacion es compatible con un sistema de registro de comandos donde los comandos anotados son descubiertos y gestionados
 * automaticamente basandose en el valor proporcionado.
 * <p>
 * Restricciones:
 * <ul>
 *   <li>Debe aplicarse unicamente a clases.
 *   <li>El valor proporcionado debe ser unico dentro del sistema para evitar colisiones.
 * </ul>
 * <p>
 * Requisitos de retencion y aplicacion:
 * <ul>
 *  <li>La anotacion esta destinada exclusivamente a clases (usando {@link ElementType#TYPE}).
 *  <li>Es retenida en tiempo de ejecucion (usando {@link RetentionPolicy#RUNTIME}),
 *      lo que permite su uso en mecanicas de reflexion y procesamiento dinamico.
 * </ul>
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String value();
}