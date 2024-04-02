package org.aoclient.network;

import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Aqui solo hay constantes de mensajes, estaria bueno guardarlas en un archivo.
 */
public class Messages {
    public static final String MENSAJE_CRIATURA_FALLA_GOLPE = new String("¡¡¡La criatura falló el golpe!!!".getBytes(), UTF_8);
    public static final String MENSAJE_CRIATURA_MATADO = new String ("¡¡¡La criatura te ha matado!!!".getBytes(), UTF_8);
    public static final String MENSAJE_RECHAZO_ATAQUE_ESCUDO = new String ("¡¡¡Has rechazado el ataque con el escudo!!!".getBytes(), UTF_8);
    public static final String MENSAJE_USUARIO_RECHAZO_ATAQUE_ESCUDO = new String ("¡¡¡El usuario rechazó el ataque con su escudo!!!".getBytes(), UTF_8);
    public static final String MENSAJE_FALLADO_GOLPE = new String("¡¡¡Has fallado el golpe!!!".getBytes(), UTF_8);
    public static final String MENSAJE_SEGURO_ACTIVADO = new String(">>SEGURO ACTIVADO<<".getBytes(), UTF_8);
    public static final String MENSAJE_SEGURO_DESACTIVADO = new String(">>SEGURO DESACTIVADO<<".getBytes(), UTF_8);
    public static final String MENSAJE_PIERDE_NOBLEZA = new String("¡¡Has perdido puntaje de nobleza y ganado puntaje de criminalidd!! Si sigues ayudando a criminales te convertirás en uno de ellos y serás perseguido por las tropas de las ciudades.".getBytes(), UTF_8);
    public static final String MENSAJE_USAR_MEDITANDO = new String("¡Estás meditando! Debes dejar de meditar para usar objetos.".getBytes(), UTF_8);

    public static final String MENSAJE_SEGURO_RESU_ON = "SEGURO DE RESURRECCION ACTIVADO";
    public static final String MENSAJE_SEGURO_RESU_OFF = "SEGURO DE RESURRECCION DESACTIVADO";

    public static final String MENSAJE_GOLPE_CABEZA = new String("¡¡La criatura te ha pegado en la cabeza por ".getBytes(), UTF_8);
    public static final String MENSAJE_GOLPE_BRAZO_IZQ = new String("¡¡La criatura te ha pegado el brazo izquierdo por ".getBytes(), UTF_8);
    public static final String MENSAJE_GOLPE_BRAZO_DER = new String("¡¡La criatura te ha pegado el brazo derecho por ".getBytes(), UTF_8);
    public static final String MENSAJE_GOLPE_PIERNA_IZQ = new String("¡¡La criatura te ha pegado la pierna izquierda por ".getBytes(), UTF_8);
    public static final String MENSAJE_GOLPE_PIERNA_DER = new String("¡¡La criatura te ha pegado la pierna derecha por ".getBytes(), UTF_8);
    public static final String MENSAJE_GOLPE_TORSO  = new String("¡¡La criatura te ha pegado en el torso por ".getBytes(), UTF_8);

    // MENSAJE_[12]: Aparecen antes y despues del valor de los mensajes anteriores (MENSAJE_GOLPE_*)
    public static final String MENSAJE_1 = new String("¡¡".getBytes(), UTF_8);
    public static final String MENSAJE_2 = new String("!!".getBytes(), UTF_8);
    public static final String MENSAJE_11 = new String("¡".getBytes(), UTF_8);
    public static final String MENSAJE_22 = new String("!".getBytes(), UTF_8);

    public static final String MENSAJE_GOLPE_CRIATURA_1 = new String("¡¡Le has pegado a la criatura por ".getBytes(), UTF_8);

    public static final String MENSAJE_ATAQUE_FALLO = new String(" te atacó y falló!!".getBytes(), UTF_8);

    public static final String MENSAJE_RECIVE_IMPACTO_CABEZA = " te ha pegado en la cabeza por ";
    public static final String MENSAJE_RECIVE_IMPACTO_BRAZO_IZQ = " te ha pegado el brazo izquierdo por ";
    public static final String MENSAJE_RECIVE_IMPACTO_BRAZO_DER = " te ha pegado el brazo derecho por ";
    public static final String MENSAJE_RECIVE_IMPACTO_PIERNA_IZQ = " te ha pegado la pierna izquierda por ";
    public static final String MENSAJE_RECIVE_IMPACTO_PIERNA_DER = " te ha pegado la pierna derecha por ";
    public static final String MENSAJE_RECIVE_IMPACTO_TORSO = " te ha pegado en el torso por ";

    public static final String MENSAJE_PRODUCE_IMPACTO_1 = new String("¡¡Le has pegado a ".getBytes(), UTF_8);
    public static final String MENSAJE_PRODUCE_IMPACTO_CABEZA = " en la cabeza por ";
    public static final String MENSAJE_PRODUCE_IMPACTO_BRAZO_IZQ = " en el brazo izquierdo por ";
    public static final String MENSAJE_PRODUCE_IMPACTO_BRAZO_DER = " en el brazo derecho por ";
    public static final String MENSAJE_PRODUCE_IMPACTO_PIERNA_IZQ = " en la pierna izquierda por ";
    public static final String MENSAJE_PRODUCE_IMPACTO_PIERNA_DER = " en la pierna derecha por ";
    public static final String MENSAJE_PRODUCE_IMPACTO_TORSO = " en el torso por ";

    public static final String MENSAJE_TRABAJO_MAGIA = "Haz click sobre el objetivo...";
    public static final String MENSAJE_TRABAJO_PESCA = "Haz click sobre el sitio donde quieres pescar...";
    public static final String MENSAJE_TRABAJO_ROBAR = new String("Haz click sobre la víctima...".getBytes(), UTF_8);
    public static final String MENSAJE_TRABAJO_TALAR = new String("Haz click sobre el árbol...".getBytes(), UTF_8);
    public static final String MENSAJE_TRABAJO_MINERIA = "Haz click sobre el yacimiento...";
    public static final String MENSAJE_TRABAJO_FUNDIRMETAL = "Haz click sobre la fragua...";
    public static final String MENSAJE_TRABAJO_PROYECTILES = new String("Haz click sobre la víctima...".getBytes(), UTF_8);

    public static final String MENSAJE_ENTRAR_PARTY_1 = "Si deseas entrar en una party con ";
    public static final String MENSAJE_ENTRAR_PARTY_2 = ", escribe /entrarparty";

    public static final String MENSAJE_NENE = "Cantidad de NPCs: ";

    public static final String MENSAJE_FRAGSHOOTER_TE_HA_MATADO = new String("te ha matado!".getBytes(), UTF_8);
    public static final String MENSAJE_FRAGSHOOTER_HAS_MATADO = "Has matado a";
    public static final String MENSAJE_FRAGSHOOTER_HAS_GANADO = "Has ganado ";
    public static final String MENSAJE_FRAGSHOOTER_PUNTOS_DE_EXPERIENCIA = "puntos de experiencia.";

    public static final String MENSAJE_NO_VES_NADA_INTERESANTE = "No ves nada interesante.";
    public static final String MENSAJE_HAS_MATADO_A = "Has matado a ";
    public static final String MENSAJE_HAS_GANADO_EXPE_1 = "Has ganado ";
    public static final String MENSAJE_HAS_GANADO_EXPE_2 = " puntos de experiencia.";
    public static final String MENSAJE_TE_HA_MATADO = new String(" te ha matado!".getBytes(), UTF_8);

    public static final String MENSAJE_HOGAR = "Has llegado a tu hogar. El viaje ha finalizado.";
    public static final String MENSAJE_HOGAR_CANCEL = "Tu viaje ha sido cancelado.";

}
