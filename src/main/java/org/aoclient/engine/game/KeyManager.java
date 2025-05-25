package org.aoclient.engine.game;

import org.aoclient.engine.game.models.Key;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Administrador de teclas utilizado para mapear comandos a acciones especificas dentro del sistema.
 * <p>
 * Proporciona mecanismos para cargar y guardar configuraciones de teclas desde un archivo, redefinir mapeos, y consultar teclas
 * actualmente asignadas. Este gestor asegura que cada tecla pueda ser configurada de acuerdo con las preferencias del usuario,
 * permitiendo cambiar o restablecer configuraciones por defecto si es necesario.
 * <p>
 * Los datos de configuracion de las teclas se almacenan en el archivo binario {@code keys.bin}.
 * <p>
 * TODO No descartar la opcion de usar JSON para guardar las teclas en un futuro aunque es "menos eficiente" pero con la ventaja
 * de editar manualmente el archivo
 */

public enum KeyManager {

    INSTANCE;

    /** Nombre del archivo que almacena la configuracion de las teclas en formato binario. */
    private static final String KEYS_CONFIG_FILE = "resources/keys.bin";
    /** Mapa de teclas en donde cada una esta asociada a un codigo. */
    private final Map<Integer, Key> keys;
    /** Set para detectar teclas duplicadas. */
    private final Set<Integer> assignedKey;

    KeyManager() {
        keys = new HashMap<>();
        assignedKey = new HashSet<>();
        initKeys();
    }

    /**
     * Cambia la tecla asignada a una accion especifica.
     *
     * @param key        accion a la que se asignara una nueva tecla
     * @param newKeyCode codigo de la nueva tecla
     * @return true si la asignacion fue exitosa, false si la tecla ya esta asignada a otra accion
     */
    public boolean changeBindedKey(Key key, int newKeyCode) {
        // Verifica si la tecla ya esta asignada a otra accion
        Key existingKey = keys.get(newKeyCode);
        if (existingKey != null && existingKey != key) return false; // La tecla ya esta asignada a otra accion
        // Actualizar la tecla
        key.setKeyCode(newKeyCode);
        updateKeyMaps();
        return true;
    }

    /**
     * Obtiene el codigo de la tecla.
     *
     * @param key tecla
     * @return codigo de la tecla
     */
    public int getKeyCode(Key key) {
        return key.getKeyCode();
    }

    /**
     * Obtiene la tecla.
     *
     * @param keyCode codigo de la tecla
     * @return la tecla, o null si no hay ninguna tecla asociada al codigo
     */
    public Key getKey(int keyCode) {
        return keys.get(keyCode);
    }

    /**
     * Verifica si la tecla ya esta asignada a alguna accion.
     *
     * @param keyCode codigo de la tecla a verificar
     * @return true si la tecla ya esta asignada, false en caso contrario
     */
    public boolean isKeyAssigned(int keyCode) {
        return assignedKey.contains(keyCode);
    }

    /**
     * Carga las teclas por defecto segun lo definido en el enum Key.
     */
    private void loadDefaultKeys() {
        for (Key key : Key.values())
            key.resetToDefault();
        updateKeyMaps();
    }

    /**
     * Carga las teclas guardadas desde un archivo.
     *
     * @throws IOException si hay un error al leer el archivo o si contiene menos datos de los esperados
     */
    private void loadKeys() throws IOException {
        try (RandomAccessFile f = new RandomAccessFile(KEYS_CONFIG_FILE, "rw")) {
            f.seek(0); // Establece el puntero del archivo en 0, es decir, el indice de la matriz de bytes del archivo
            for (Key key : Key.values()) {
                int keyCode = f.readInt();
                // System.out.println("Key code [" + keyCode + "] was loaded on the " + key.name() + " key");
                // Verifica que el codigo de la tecla leido desde el archivo este en un rango razonable para teclas GLFW
                if (keyCode < 0 || keyCode > 1000)
                    key.resetToDefault(); // Usa el valor predeterminado si esta fuera del rango
                else key.setKeyCode(keyCode); // Establece el codigo de tecla del archivo keys.bin a la tecla actual
            }
        }
        updateKeyMaps();
    }

    /**
     * Guarda la configuracion actual de teclas en un archivo.
     */
    private void saveKeys() throws IOException {
        try (RandomAccessFile f = new RandomAccessFile(KEYS_CONFIG_FILE, "rw")) {
            f.seek(0);
            for (Key key : Key.values())
                f.writeInt(key.getKeyCode()); // Escribe el codigo de las teclas del enum Key en el archivo keys.bin ordenadas tal cual estan en el enum
        }
    }

    /**
     * Inicializa las teclas intentando cargarlas desde el archivo {@code keys.bin} o usando valores predeterminados si no es
     * posible.
     */
    private void initKeys() {
        boolean useDefaultKeys = false;

        // Primero intenta cargar las teclas desde el archivo
        try {
            loadKeys();
        } catch (IOException e) {
            System.err.println("Failed to load key configuration: " + e.getMessage());
            useDefaultKeys = true;
        }

        // Si no se pudo cargar el archivo, carga las teclas predeterminadas
        if (useDefaultKeys) {
            loadDefaultKeys();
            // Intenta guardar las teclas predeterminadas
            try {
                saveKeys();
            } catch (IOException e) {
                System.err.println("Could not save default key configuration: " + e.getMessage());
                // No es crítico, podemos continuar con las teclas predeterminadas en memoria
            }
        }
    }

    /**
     * Actualiza las colecciones para reflejar el estado actual de las teclas asignadas.
     */
    private void updateKeyMaps() {
        keys.clear();
        assignedKey.clear();
        for (Key key : Key.values()) {
            keys.put(key.getKeyCode(), key);
            assignedKey.add(key.getKeyCode());
        }
    }

}
