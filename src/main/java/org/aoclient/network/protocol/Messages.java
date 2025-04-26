package org.aoclient.network.protocol;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

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
 * TODO Tendria que ser finals?
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
    public static String MENSAJE_ESTAS_MUERTO;

    public static void loadMessages(String region) {
        try (BufferedReader reader = new BufferedReader(new FileReader("resources/strings_" + region + ".ini"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String id = parts[0].trim();
                    String value = parts[1].trim();
                    populateMessages(id, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param id El nombre del mensaje.
     * @param value  El nuevo valor del mensaje.
     */
    private static void populateMessages(String id, String value) {
        try {
            Field field = Messages.class.getDeclaredField(id);
            if (Modifier.isStatic(field.getModifiers()) && field.getType() == String.class) {
                field.set(null, new String(value.getBytes(), UTF_8));
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Campo inválido en Messages: " + id);
        }
    }

}
