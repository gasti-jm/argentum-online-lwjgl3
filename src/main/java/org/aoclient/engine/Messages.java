package org.aoclient.engine;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Gestiona mensajes de texto en distintos idiomas segun la region.
 */

public class Messages {

    private static final String MESSAGE_PREFIX = "MENSAJE_";
    private static final Map<MessageKey, String> messageCache = new HashMap<>();

    private Messages() {
    }

    /**
     * Carga los mensajes del idioma actual desde el archivo de recursos.
     */
    public static void loadMessages(String region) {

        messageCache.clear();

        String filename = "strings_" + region + ".ini";

        try {
            Path resources = Paths.get("resources", filename);
            if (!Files.exists(resources)) {
                System.err.println("The " + filename + " file could not be found!");
                return;
            }

            // Leer el archivo con UTF-8
            try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(resources), StandardCharsets.UTF_8)) {
                Properties properties = new Properties();
                properties.load(reader);

                for (MessageKey key : MessageKey.values()) {
                    String value = properties.getProperty(MESSAGE_PREFIX + key.name());
                    if (value != null && !value.isEmpty()) {
                        messageCache.put(key, value);
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Error loading messages: " + e.getMessage());
        }
    }

    public static String get(MessageKey key) {
        return messageCache.get(key);
    }

    /**
     * Mensajes.
     */
    public enum MessageKey {
        CRIATURA_FALLA_GOLPE,
        CRIATURA_MATADO,
        RECHAZO_ATAQUE_ESCUDO,
        USUARIO_RECHAZO_ATAQUE_ESCUDO,
        FALLADO_GOLPE,
        SEGURO_ACTIVADO,
        SEGURO_DESACTIVADO,
        PIERDE_NOBLEZA,
        USAR_MEDITANDO,
        SEGURO_RESU_ON,
        SEGURO_RESU_OFF,
        GOLPE_CABEZA,
        GOLPE_BRAZO_IZQ,
        GOLPE_BRAZO_DER,
        GOLPE_PIERNA_IZQ,
        GOLPE_PIERNA_DER,
        GOLPE_TORSO,
        MENSAJE_1,
        MENSAJE_2,
        MENSAJE_11,
        MENSAJE_22,
        GOLPE_CRIATURA_1,
        ATAQUE_FALLO,
        RECIVE_IMPACTO_CABEZA,
        RECIVE_IMPACTO_BRAZO_IZQ,
        RECIVE_IMPACTO_BRAZO_DER,
        RECIVE_IMPACTO_PIERNA_IZQ,
        RECIVE_IMPACTO_PIERNA_DER,
        RECIVE_IMPACTO_TORSO,
        PRODUCE_IMPACTO_1,
        PRODUCE_IMPACTO_CABEZA,
        PRODUCE_IMPACTO_BRAZO_IZQ,
        PRODUCE_IMPACTO_BRAZO_DER,
        PRODUCE_IMPACTO_PIERNA_IZQ,
        PRODUCE_IMPACTO_PIERNA_DER,
        PRODUCE_IMPACTO_TORSO,
        TRABAJO_MAGIA,
        TRABAJO_PESCA,
        TRABAJO_ROBAR,
        TRABAJO_TALAR,
        TRABAJO_MINERIA,
        TRABAJO_FUNDIRMETAL,
        TRABAJO_PROYECTILES,
        ENTRAR_PARTY_1,
        ENTRAR_PARTY_2,
        NENE,
        FRAGSHOOTER_TE_HA_MATADO,
        FRAGSHOOTER_HAS_MATADO,
        FRAGSHOOTER_HAS_GANADO,
        FRAGSHOOTER_PUNTOS_DE_EXPERIENCIA,
        NO_VES_NADA_INTERESANTE,
        HAS_MATADO_A,
        HAS_GANADO_EXPE_1,
        HAS_GANADO_EXPE_2,
        TE_HA_MATADO,
        HOGAR,
        HOGAR_CANCEL,
        ESTAS_MUERTO
    }

}