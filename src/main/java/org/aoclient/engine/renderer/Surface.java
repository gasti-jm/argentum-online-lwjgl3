package org.aoclient.engine.renderer;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.glDeleteTextures;

/**
 * Gestiona un conjunto de texturas (instancias de la clase {@code Texture}) mediante un mapa de identificadores numericos.
 * Proporciona metodos para inicializar, obtener, crear y eliminar texturas.
 */

public enum Surface {

    INSTANCE;

    /** Mapa que asocia identificadores numericos con sus correspondientes texturas en memoria. */
    private Map<Integer, Texture> textures;

    /**
     * Inicializa el contenedor de texturas.
     * <p>
     * Este metodo establece un nuevo mapa de texturas vacio, preparando la instancia para gestionar texturas asociadas en el
     * ciclo de vida del objeto.
     */
    public void init() {
        textures = new HashMap<>();
    }

    /**
     * Elimina todas las texturas gestionadas en el mapa de texturas.
     * <p>
     * Este metodo limpia completamente el contenedor que almacena las texturas, liberando todas las referencias a dichas
     * instancias. Es util para liberar recursos o reinicializar el estado del sistema de gestion de texturas.
     */
    public void deleteAllTextures() {
        textures.clear();
    }

    /**
     * Obtiene una textura asociada al identificador numerico especificado.
     * <p>
     * Si la textura no existe en el mapa, se crea y se agrega al mismo.
     *
     * @param fileNum identificador numerico asociado al archivo de textura
     * @return la textura obtenida o creada asociada al identificador proporcionado
     */
    public Texture getTexture(int fileNum) {
        if (textures.containsKey(fileNum)) return textures.get(fileNum);
        return createTexture(fileNum);
    }

    /**
     * Crea una nueva textura asociada a un identificador numerico y la agrega al mapa de texturas.
     * <p>
     * La textura es cargada desde un archivo comprimido especificado y configurada adecuadamente.
     *
     * @param fileNum identificador numerico unico asociado a la textura que se desea crear
     * @return la nueva textura creada asociada al identificador proporcionado
     */
    private Texture createTexture(int fileNum) {
        Texture texture = new Texture();
        texture.loadTexture(texture, "graphics.ao", String.valueOf(fileNum), false);
        textures.put(fileNum, texture);
        return texture;
    }

    /**
     * Crea una nueva textura a partir de un archivo especificado.
     * <p>
     * La textura es inicializada con la informacion proporcionada, incluyendo el archivo comprimido, el archivo de textura y si
     * esta destinada a interfaces graficas de usuario (GUI).
     *
     * @param fileCompressed nombre del archivo comprimido que contiene la textura
     * @param file           nombre del archivo dentro del archivo comprimido que contiene los datos de la textura
     * @param isGUI          indica si la textura esta destinada a ser utilizada en interfaces graficas de usuario (GUI)
     * @return la textura creada, o {@code null} si el nombre del archivo especificado esta vacio
     */
    public Texture createTexture(String fileCompressed, String file, boolean isGUI) {
        if (file.isEmpty()) return null;
        Texture texture = new Texture();
        texture.loadTexture(texture, fileCompressed, file, isGUI);
        return texture;
    }

}
