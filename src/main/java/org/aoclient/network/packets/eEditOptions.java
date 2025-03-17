package org.aoclient.network.packets;

import org.aoclient.network.ProtocolCmdParse;

/**
 * <p>
 * Enumeracion que define las opciones de edicion disponibles para modificar atributos de personajes a traves de comandos
 * administrativos.
 * <p>
 * {@code eEditOptions} especifica los diferentes aspectos de un personaje que pueden ser modificados por administradores del
 * juego mediante el comando {@code /MOD}. Cada opcion tiene asignado un valor numerico que se utiliza en el protocolo de
 * comunicacion para identificar el tipo de modificacion a realizar.
 * <p>
 * Esta enumeracion es utilizada principalmente por {@link ProtocolCmdParse} al procesar comandos administrativos de edicion,
 * proporcionando una forma tipada y segura de identificar que atributo se desea modificar.
 * <p>
 * Las opciones de edicion incluyen:
 * <ul>
 * <li><b>eo_Gold</b>: Modifica la cantidad de oro del personaje</li>
 * <li><b>eo_Experience</b>: Modifica la experiencia del personaje</li>
 * <li><b>eo_Body</b>: Cambia el cuerpo/apariencia del personaje</li>
 * <li><b>eo_Head</b>: Cambia la cabeza del personaje</li>
 * <li><b>eo_CiticensKilled</b>: Ajusta el contador de ciudadanos asesinados</li>
 * <li><b>eo_CriminalsKilled</b>: Ajusta el contador de criminales asesinados</li>
 * <li><b>eo_Level</b>: Modifica el nivel del personaje</li>
 * <li><b>eo_Class</b>: Cambia la clase del personaje</li>
 * <li><b>eo_Skills</b>: Modifica las habilidades del personaje</li>
 * <li><b>eo_SkillPointsLeft</b>: Ajusta los puntos de habilidad disponibles</li>
 * <li><b>eo_Nobleza</b>: Modifica los puntos de nobleza</li>
 * <li><b>eo_Asesino</b>: Modifica los puntos de asesino</li>
 * <li><b>eo_Sex</b>: Cambia el genero del personaje</li>
 * <li><b>eo_Raza</b>: Cambia la raza del personaje</li>
 * <li><b>eo_addGold</b>: Agrega oro al personaje (a diferencia de establecer una cantidad)</li>
 * </ul>
 * <p>
 * Cada valor numerico corresponde al identificador utilizado en el protocolo para indicar al servidor que tipo de modificacion
 * debe realizar.
 */

public enum eEditOptions {

    eo_Gold(1),
    eo_Experience(2),
    eo_Body(3),
    eo_Head(4),
    eo_CiticensKilled(5),
    eo_CriminalsKilled(6),
    eo_Level(7),
    eo_Class(8),
    eo_Skills(9),
    eo_SkillPointsLeft(10),
    eo_Nobleza(11),
    eo_Asesino(12),
    eo_Sex(13),
    eo_Raza(14),
    eo_addGold(15);

    private final int value;

    eEditOptions(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
