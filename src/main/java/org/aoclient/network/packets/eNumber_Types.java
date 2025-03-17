package org.aoclient.network.packets;

import org.aoclient.network.ProtocolCmdParse;

/**
 * Enumeracion que define los diferentes tipos de datos numericos utilizados en la validacion de parametros de comandos.
 * <p>
 * {@code eNumber_Types} especifica los tipos de valores numericos que pueden ser ingresados como parametros en los comandos del
 * juego, facilitando la validacion correcta de rangos y formatos antes de procesar los comandos o enviarlos al servidor.
 * <p>
 * Esta enumeracion es utilizada principalmente por la clase {@link ProtocolCmdParse} en su metodo
 * {@link ProtocolCmdParse#validNumber(String, eNumber_Types) validNumber(String, eNumber_Types)} para verificar que los valores
 * ingresados por el usuario cumplen con los rangos esperados para cada tipo de dato.
 * <p>
 * Los tipos definidos son:
 * <ul>
 * <li><b>ent_Byte</b>: Valores enteros en el rango de 0 a 255
 * <li><b>ent_Integer</b>: Valores enteros en el rango de -32768 a 32767
 * <li><b>ent_Long</b>: Valores enteros en el rango de -2147483648 a 2147483647
 * <li><b>ent_Trigger</b>: Valores enteros especificos para triggers del mapa, rango de 0 a 6
 * </ul>
 * <p>
 * Cada tipo establece limites que corresponden con los tipos de datos nativos utilizados en el protocolo de comunicacion del
 * juego.
 */

public enum eNumber_Types {
    ent_Byte,
    ent_Integer,
    ent_Long,
    ent_Trigger
}
