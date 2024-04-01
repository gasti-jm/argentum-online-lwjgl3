package org.aoclient.engine.renderer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Aca es donde se gestiona (en gran parte) la carga y el almacen de los graficos que se van renderizar en el juego.
 * El mapa "textures" definida en esta clase se va a utilizar para guardar y encontrar rapido todos los graficos
 * que se van a renderizar, donde la llave es el numero del grafico y el valor la textura en si.
 *
 * Los textos y la interfaz de usuario no van a trabajar con este mapa, cada uno de ellos lo almacenan en
 * su dicha clase, pero los metodos de carga tambien se trabajan desde aqui.
 */
public class Surface {
    private static Surface instance;
    private Map<Integer, Texture> textures;

    private Surface() {

    }

    /**
     *
     * @return Mismo objeto (Patron de diseño Singleton).
     */
    public static Surface get() {
        if (instance == null){
            instance = new Surface();
        }

        return instance;
    }

    /**
     * Constructor que solo inicializa nuestro mapa.
     */
    public void initialize() {
        this.textures = new HashMap<>();
    }

    /**
     * @desc: Elimina todas las texturas del mapa, esto se utiliza al pasar de mapa en el juego, ya que
     *        en algun momento las texturas que no se van a dibujar y deben eliminarse para ahorrar espacio.
     */
    public void deleteAllTextures() {
        this.textures.clear();
    }

    /**
     *
     * @param fileNum: Nombre del archivo del grafico (en este caso siempre es un numero)
     * @return Textura. Si existe: la devuelve segun la llave pasada por parametro. En caso contrario crea una nueva,
     *         la guarda en el mapa y la retorna.
     */
    public Texture getTexture(int fileNum) {
        if (textures.containsKey(fileNum)) {
            return textures.get(fileNum);
        }

        return createTexture(fileNum);
    }

    /**
     *
     * @desc: Crea una textura y lo guarda en nuestro mapa de texturas con su id (en este caso el numero del archivo).
     */
    private Texture createTexture(int fileNum) {
        Texture texture = new Texture();
        File file = new File("resources/graphics/" + fileNum + ".bmp");

        if (file.exists()) {
            texture.loadTexture(texture, "resources/graphics/" + fileNum + ".bmp", false);
        } else {
            texture.loadTexture(texture, "resources/graphics/" + fileNum + ".png", false);
        }

        textures.put(fileNum, texture);

        return texture;
    }

    /**
     *
     * @desc: Crea y retorna una textura
     */
    public Texture createTexture(String file, boolean isGUI) {
        if (file.isEmpty()) return null;

        Texture texture = new Texture();
        texture.loadTexture(texture, file, isGUI);
        return texture;
    }
}
