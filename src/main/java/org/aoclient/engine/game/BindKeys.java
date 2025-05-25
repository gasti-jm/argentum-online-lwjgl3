package org.aoclient.engine.game;

import org.aoclient.engine.game.models.Key;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Gestiona la configuracion y asignacion de teclas para las acciones del juego. Este controlador permite personalizar
 * las teclas para cada accion, cargar configuraciones desde un archivo, restaurar valores predeterminados y verificar
 * si las teclas ya estan asignadas.
 * <p>
 * Las teclas se representan como objetos del enum Key, que define todas las posibles acciones del juego y permite
 * asignarles codigos de teclas asociados.
 * <p>
 * Funcionalidades principales:
 * <ul>
 * <li>Asignacion de teclas a acciones especificas.
 * <li>Cargar y guardar configuraciones de teclas en un archivo binario.
 * <li>Actualizar y verificar asignaciones de teclas.
 * <li>Restaurar a la configuracion predeterminada en caso de que no se pueda cargar un archivo valido.
 * </ul>
 * TODO No descartar la opcion de usar JSON para guardar las teclas en un futuro aunque es "menos eficiente" pero con la
 * ventaja de editar manualmente el archivo
 */

public enum BindKeys {

    INSTANCE;

    private static final String KEYS_CONFIG_FILE = "resources/keys.bin";
    /** Mapa de teclas en donde cada una esta asociada a un codigo. */
    private final Map<Integer, Key> keys;
    /** Set para detectar teclas duplicadas. */
    private final Set<Integer> assignedKey;

    BindKeys() {
        keys = new HashMap<>();
        assignedKey = new HashSet<>();
        initializeKeyBindings();
    }

    /**
     * Inicializa las asignaciones de teclas intentando cargar desde el archivo o usando valores predeterminados si no
     * es posible.
     */
    private void initializeKeyBindings() {
        boolean useDefaultKeys = false;

        // Intenta cargar desde el archivo primero
        try {
            loadBindKeys();
        } catch (IOException e) {
            System.err.println("Failed to load key configuration: " + e.getMessage());
            useDefaultKeys = true;
        }

        // Si no se pudo cargar el archivo, carga las teclas predeterminadas
        if (useDefaultKeys) {
            loadDefaultKeys();
            // Intenta guardar las teclas predeterminadas
            try {
                saveBindKeys();
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

    /**
     * Carga las teclas por defecto segun lo definido en el enum Key.
     */
    public void loadDefaultKeys() {
        for (Key key : Key.values())
            key.resetToDefault();
        updateKeyMaps();
    }

    /**
     * Carga las teclas guardadas desde un archivo.
     *
     * @throws IOException si hay un error al leer el archivo o si contiene menos datos de los esperados
     */
    public void loadBindKeys() throws IOException {
        try (RandomAccessFile f = new RandomAccessFile(KEYS_CONFIG_FILE, "rw")) {
            f.seek(0); // Establece el puntero del archivo en 0, es decir, el indice de la matriz de bytes del archivo
            for (Key key : Key.values())
                key.setKeyCode(f.readInt()); // Establece el codigo de tecla del archivo keys.bin a la tecla actual
        }
        updateKeyMaps();
    }

    /**
     * Guarda la configuracion actual de teclas en un archivo.
     */
    public void saveBindKeys() throws IOException {
        try (RandomAccessFile f = new RandomAccessFile(KEYS_CONFIG_FILE, "rw")) {
            f.seek(0);
            for (Key key : Key.values())
                f.writeInt(key.getKeyCode());
        }
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
     * Obtiene el codigo de la tecla asignada a una accion especifica.
     *
     * @param key accion para la que se quiere obtener la tecla asignada
     * @return codigo de la tecla asignada
     */
    public int getBindedKey(Key key) {
        return key.getKeyCode();
    }

    /**
     * Obtiene la tecla presionada.
     *
     * @param keyCode codigo de la tecla presionada
     * @return la tecla, o null si no hay ninguna tecla asociada al codigo
     */
    public Key getKeyPressed(int keyCode) {
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

}
