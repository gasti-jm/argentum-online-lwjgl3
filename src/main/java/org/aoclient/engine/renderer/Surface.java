package org.aoclient.engine.renderer;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.glDeleteTextures;

/**
 * Clase responsable de la gestion y almacenamiento de texturas para el renderizado grafico.
 * <p>
 * Mantiene un mapa donde las texturas son almacenadas utilizando como clave el numero del grafico, permitiendo un acceso rapido y
 * eficiente.
 * <p>
 * Las texturas de interfaz de usuario y textos son manejadas de forma independiente en sus respectivas clases, pero utilizan los
 * metodos de carga proporcionados por {@code Surface}. Esta clase se encarga principalmente de las texturas de los elementos del
 * juego como personajes, objetos y escenarios.
 * <p>
 * Ofrece funcionalidad para crear, obtener y eliminar texturas, optimizando el uso de memoria al reutilizar texturas ya
 * cargadas.
 */

public class Surface {

    private static Surface instance;
    private Map<Integer, Texture> textures;

    private Surface() {

    }

    /**
     * @return Mismo objeto (Patron de diseño Singleton).
     */
    public static Surface get() {
        if (instance == null) instance = new Surface();
        return instance;
    }

    /**
     * Constructor que solo inicializa nuestro mapa.
     */
    public void initialize() {
        this.textures = new HashMap<>();
    }

    /**
     * @desc: Elimina todas las texturas del mapa, esto se utiliza al pasar de mapa en el juego, ya que en algun momento las
     * texturas que no se van a dibujar y deben eliminarse para ahorrar espacio.
     */
    public void deleteAllTextures() {
//        for (Map.Entry<Integer, Texture> entry : textures.entrySet()) {
//            glDeleteTextures(entry.getValue().getId());
//        }

        this.textures.clear();
    }

    /**
     * @param fileNum: Nombre del archivo del grafico (en este caso siempre es un numero)
     * @return Textura. Si existe: la devuelve segun la llave pasada por parametro. En caso contrario crea una nueva, la guarda en
     * el mapa y la retorna.
     */
    public Texture getTexture(int fileNum) {
        if (textures.containsKey(fileNum)) return textures.get(fileNum);
        return createTexture("graphics.ao", fileNum);
    }

    /**
     * @desc: Crea una textura y lo guarda en nuestro mapa de texturas con su id (en este caso el numero del archivo).
     */
    private Texture createTexture(String fileCompressed, int fileNum) {
        Texture texture = new Texture();
        texture.loadTexture(texture, fileCompressed, String.valueOf(fileNum), false);
        textures.put(fileNum, texture);
        return texture;
    }

    /**
     * @desc: Crea y retorna una textura
     */
    public Texture createTexture(String fileCompressed, String file, boolean isGUI) {
        if (file.isEmpty()) return null;
        Texture texture = new Texture();
        texture.loadTexture(texture, fileCompressed, file, isGUI);
        return texture;
    }

}
