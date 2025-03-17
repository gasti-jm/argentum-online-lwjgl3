package org.aoclient.network;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Clase que gestiona los mensajes de texto utilizados en el juego.
 * <p>
 * {@code Messages} almacena como variables estaticas todos los mensajes predefinidos que el juego muestra durante su ejecucion.
 * Estos mensajes incluyen notificaciones de combate, mensajes del sistema, dialogos e instrucciones para el usuario.
 * <p>
 * Esta clase implementa funcionalidad de internacionalizacion, permitiendo cargar mensajes desde archivos de configuracion segun
 * la region o idioma seleccionado. Los mensajes son almacenados en archivos con formato {@code strings_[region].ini} donde
 * [region] representa el codigo de region (por ejemplo, "es" para español).
 * <p>
 * Los mensajes se cargan a traves del metodo {@code loadMessages()} y son accesibles directamente desde otras clases mediante las
 * variables estaticas publicas. Esta estructura centraliza todos los textos del juego, facilitando su mantenimiento y
 * traduccion.
 * <p>
 * El sistema esta diseñado para manejar diferentes tipos de mensajes como:
 * <ul>
 * <li>Mensajes de combate (golpes, daños, muertes)
 * <li>Notificaciones de estado (seguro, meditacion)
 * <li>Mensajes de sistema (conexion, desconexion)
 * <li>Instrucciones para diferentes acciones del juego
 * </ul>
 * <p>
 * TODO Mover a un archivo
 */

public class Messages {

    public static String MENSAJE_CRIATURA_FALLA_GOLPE;
    public static String MENSAJE_CRIATURA_MATADO;
    public static String MENSAJE_RECHAZO_ATAQUE_ESCUDO;
    public static String MENSAJE_USUARIO_RECHAZO_ATAQUE_ESCUDO;
    public static String MENSAJE_FALLADO_GOLPE;
    public static String MENSAJE_SEGURO_ACTIVADO;
    public static String MENSAJE_SEGURO_DESACTIVADO;
    public static String MENSAJE_PIERDE_NOBLEZA;
    public static String MENSAJE_USAR_MEDITANDO;

    public static String MENSAJE_SEGURO_RESU_ON;
    public static String MENSAJE_SEGURO_RESU_OFF;

    public static String MENSAJE_GOLPE_CABEZA;
    public static String MENSAJE_GOLPE_BRAZO_IZQ;
    public static String MENSAJE_GOLPE_BRAZO_DER;
    public static String MENSAJE_GOLPE_PIERNA_IZQ;
    public static String MENSAJE_GOLPE_PIERNA_DER;
    public static String MENSAJE_GOLPE_TORSO;

    // MENSAJE_[12]: Aparecen antes y despues del valor de los mensajes anteriores (MENSAJE_GOLPE_*)
    public static String MENSAJE_1;
    public static String MENSAJE_2;
    public static String MENSAJE_11;
    public static String MENSAJE_22;

    public static String MENSAJE_GOLPE_CRIATURA_1;

    public static String MENSAJE_ATAQUE_FALLO;

    public static String MENSAJE_RECIVE_IMPACTO_CABEZA;
    public static String MENSAJE_RECIVE_IMPACTO_BRAZO_IZQ;
    public static String MENSAJE_RECIVE_IMPACTO_BRAZO_DER;
    public static String MENSAJE_RECIVE_IMPACTO_PIERNA_IZQ;
    public static String MENSAJE_RECIVE_IMPACTO_PIERNA_DER;
    public static String MENSAJE_RECIVE_IMPACTO_TORSO;

    public static String MENSAJE_PRODUCE_IMPACTO_1;
    public static String MENSAJE_PRODUCE_IMPACTO_CABEZA;
    public static String MENSAJE_PRODUCE_IMPACTO_BRAZO_IZQ;
    public static String MENSAJE_PRODUCE_IMPACTO_BRAZO_DER;
    public static String MENSAJE_PRODUCE_IMPACTO_PIERNA_IZQ;
    public static String MENSAJE_PRODUCE_IMPACTO_PIERNA_DER;
    public static String MENSAJE_PRODUCE_IMPACTO_TORSO;

    public static String MENSAJE_TRABAJO_MAGIA;
    public static String MENSAJE_TRABAJO_PESCA;
    public static String MENSAJE_TRABAJO_ROBAR;
    public static String MENSAJE_TRABAJO_TALAR;
    public static String MENSAJE_TRABAJO_MINERIA;
    public static String MENSAJE_TRABAJO_FUNDIRMETAL;
    public static String MENSAJE_TRABAJO_PROYECTILES;

    public static String MENSAJE_ENTRAR_PARTY_1;
    public static String MENSAJE_ENTRAR_PARTY_2;

    public static String MENSAJE_NENE;

    public static String MENSAJE_FRAGSHOOTER_TE_HA_MATADO;
    public static String MENSAJE_FRAGSHOOTER_HAS_MATADO;
    public static String MENSAJE_FRAGSHOOTER_HAS_GANADO;
    public static String MENSAJE_FRAGSHOOTER_PUNTOS_DE_EXPERIENCIA;

    public static String MENSAJE_NO_VES_NADA_INTERESANTE;
    public static String MENSAJE_HAS_MATADO_A;
    public static String MENSAJE_HAS_GANADO_EXPE_1;
    public static String MENSAJE_HAS_GANADO_EXPE_2;
    public static String MENSAJE_TE_HA_MATADO;

    public static String MENSAJE_HOGAR;
    public static String MENSAJE_HOGAR_CANCEL;

    public static void loadMessages(String region) {
        try (BufferedReader reader = new BufferedReader(new FileReader("resources/strings_" + region + ".ini"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String option = parts[0].trim();
                    String value = parts[1].trim();
                    updateOption(option, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * HAY QUE METER ESTO EN UN ARRAY O UN MAPA POR FAVOR.
     *
     * @param option El nombre del mensaje.
     * @param value  El nuevo valor del mensaje.
     */
    private static void updateOption(String option, String value) {
        switch (option) {
            case "MENSAJE_CRIATURA_FALLA_GOLPE":
                MENSAJE_CRIATURA_FALLA_GOLPE = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_CRIATURA_MATADO":
                MENSAJE_CRIATURA_MATADO = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_RECHAZO_ATAQUE_ESCUDO":
                MENSAJE_RECHAZO_ATAQUE_ESCUDO = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_USUARIO_RECHAZO_ATAQUE_ESCUDO":
                MENSAJE_USUARIO_RECHAZO_ATAQUE_ESCUDO = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_FALLADO_GOLPE":
                MENSAJE_FALLADO_GOLPE = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_SEGURO_ACTIVADO":
                MENSAJE_SEGURO_ACTIVADO = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_SEGURO_DESACTIVADO":
                MENSAJE_SEGURO_DESACTIVADO = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_PIERDE_NOBLEZA":
                MENSAJE_PIERDE_NOBLEZA = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_USAR_MEDITANDO":
                MENSAJE_USAR_MEDITANDO = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_SEGURO_RESU_ON":
                MENSAJE_SEGURO_RESU_ON = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_SEGURO_RESU_OFF":
                MENSAJE_SEGURO_RESU_OFF = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_GOLPE_CABEZA":
                MENSAJE_GOLPE_CABEZA = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_GOLPE_BRAZO_IZQ":
                MENSAJE_GOLPE_BRAZO_IZQ = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_GOLPE_BRAZO_DER":
                MENSAJE_GOLPE_BRAZO_DER = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_GOLPE_PIERNA_IZQ":
                MENSAJE_GOLPE_PIERNA_IZQ = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_GOLPE_PIERNA_DER":
                MENSAJE_GOLPE_PIERNA_DER = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_GOLPE_TORSO":
                MENSAJE_GOLPE_TORSO = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_1":
                MENSAJE_1 = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_2":
                MENSAJE_2 = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_11":
                MENSAJE_11 = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_22":
                MENSAJE_22 = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_GOLPE_CRIATURA_1":
                MENSAJE_GOLPE_CRIATURA_1 = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_ATAQUE_FALLO":
                MENSAJE_ATAQUE_FALLO = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_RECIVE_IMPACTO_CABEZA":
                MENSAJE_RECIVE_IMPACTO_CABEZA = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_RECIVE_IMPACTO_BRAZO_IZQ":
                MENSAJE_RECIVE_IMPACTO_BRAZO_IZQ = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_RECIVE_IMPACTO_BRAZO_DER":
                MENSAJE_RECIVE_IMPACTO_BRAZO_DER = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_RECIVE_IMPACTO_PIERNA_IZQ":
                MENSAJE_RECIVE_IMPACTO_PIERNA_IZQ = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_RECIVE_IMPACTO_PIERNA_DER":
                MENSAJE_RECIVE_IMPACTO_PIERNA_DER = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_RECIVE_IMPACTO_TORSO":
                MENSAJE_RECIVE_IMPACTO_TORSO = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_PRODUCE_IMPACTO_1":
                MENSAJE_PRODUCE_IMPACTO_1 = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_PRODUCE_IMPACTO_CABEZA":
                MENSAJE_PRODUCE_IMPACTO_CABEZA = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_PRODUCE_IMPACTO_BRAZO_IZQ":
                MENSAJE_PRODUCE_IMPACTO_BRAZO_IZQ = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_PRODUCE_IMPACTO_BRAZO_DER":
                MENSAJE_PRODUCE_IMPACTO_BRAZO_DER = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_PRODUCE_IMPACTO_PIERNA_IZQ":
                MENSAJE_PRODUCE_IMPACTO_PIERNA_IZQ = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_PRODUCE_IMPACTO_PIERNA_DER":
                MENSAJE_PRODUCE_IMPACTO_PIERNA_DER = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_PRODUCE_IMPACTO_TORSO":
                MENSAJE_PRODUCE_IMPACTO_TORSO = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_TRABAJO_MAGIA":
                MENSAJE_TRABAJO_MAGIA = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_TRABAJO_PESCA":
                MENSAJE_TRABAJO_PESCA = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_TRABAJO_ROBAR":
                MENSAJE_TRABAJO_ROBAR = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_TRABAJO_TALAR":
                MENSAJE_TRABAJO_TALAR = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_TRABAJO_MINERIA":
                MENSAJE_TRABAJO_MINERIA = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_TRABAJO_FUNDIRMETAL":
                MENSAJE_TRABAJO_FUNDIRMETAL = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_TRABAJO_PROYECTILES":
                MENSAJE_TRABAJO_PROYECTILES = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_ENTRAR_PARTY_1":
                MENSAJE_ENTRAR_PARTY_1 = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_ENTRAR_PARTY_2":
                MENSAJE_ENTRAR_PARTY_2 = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_NENE":
                MENSAJE_NENE = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_FRAGSHOOTER_TE_HA_MATADO":
                MENSAJE_FRAGSHOOTER_TE_HA_MATADO = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_FRAGSHOOTER_HAS_MATADO":
                MENSAJE_FRAGSHOOTER_HAS_MATADO = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_FRAGSHOOTER_HAS_GANADO":
                MENSAJE_FRAGSHOOTER_HAS_GANADO = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_FRAGSHOOTER_PUNTOS_DE_EXPERIENCIA":
                MENSAJE_FRAGSHOOTER_PUNTOS_DE_EXPERIENCIA = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_NO_VES_NADA_INTERESANTE":
                MENSAJE_NO_VES_NADA_INTERESANTE = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_HAS_MATADO_A":
                MENSAJE_HAS_MATADO_A = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_HAS_GANADO_EXPE_1":
                MENSAJE_HAS_GANADO_EXPE_1 = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_HAS_GANADO_EXPE_2":
                MENSAJE_HAS_GANADO_EXPE_2 = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_TE_HA_MATADO":
                MENSAJE_TE_HA_MATADO = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_HOGAR":
                MENSAJE_HOGAR = new String(value.getBytes(), UTF_8);
                break;
            case "MENSAJE_HOGAR_CANCEL":
                MENSAJE_HOGAR_CANCEL = new String(value.getBytes(), UTF_8);
                break;
        }
    }

}
