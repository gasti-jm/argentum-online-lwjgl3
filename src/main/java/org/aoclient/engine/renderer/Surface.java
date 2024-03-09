package org.aoclient.engine.renderer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;

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
    private ByteBuffer pixels;
    private Map<Integer, TextureOGL> textures;

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
    public TextureOGL getTexture(int fileNum) {
        if (textures.containsKey(fileNum)) {
            return textures.get(fileNum);
        }

        return createTexture(fileNum);
    }

    /**
     *
     * @desc: Crea una textura y lo guarda en nuestro mapa de texturas con su id (en este caso el numero del archivo).
     */
    private TextureOGL createTexture(int fileNum) {
        TextureOGL texture = new TextureOGL();
        File file = new File("resources/graphics/" + fileNum + ".BMP");

        if (file.exists()) {
            texture.id = loadTexture(texture, "resources/graphics/" + fileNum + ".BMP", false);
        } else {
            texture.id = loadTexture(texture, "resources/graphics/" + fileNum + ".png", false);
        }

        textures.put(fileNum, texture);

        return texture;
    }

    /**
     *
     * @desc: Crea y retorna una textura para nuestra fuente de letras (Va a ser almacenada en su clase).
     */
    public TextureOGL createFontTexture(String fileName) {
        TextureOGL texture = new TextureOGL();
        texture.id = loadTexture(texture, "resources/fonts/" + fileName + ".bmp", false);
        return texture;
    }

    /**
     *
     * @desc: Crea y retorna una textura
     */
    public TextureOGL createTexture(String file, boolean isGUI) {
        if (file.isEmpty()) return null;

        TextureOGL texture = new TextureOGL();
        texture.id = loadTexture(texture, file, isGUI);
        return texture;
    }

    /**
     * @desc: Carga una textura y lo guarda en la GPU, retorna su id.
     */
    public int loadTexture(TextureOGL refTexture, String file, boolean isGUI) {
        pixels = BufferUtils.createByteBuffer(1);
        BufferedImage bi;
        int id = 0;

        try {
            File fil = new File(file);
            BufferedImage image = ImageIO.read(fil);

            refTexture.tex_width = image.getWidth();
            refTexture.tex_height = image.getHeight();

            bi = new BufferedImage(refTexture.tex_width, refTexture.tex_height, BufferedImage.TYPE_4BYTE_ABGR);

            Graphics2D g = bi.createGraphics();
            g.scale(1, -1);
            g.drawImage(image, 0, 0, refTexture.tex_width, -refTexture.tex_height, null);

            byte[] data = new byte[4 * refTexture.tex_width * refTexture.tex_height];
            bi.getRaster().getDataElements(0, 0, refTexture.tex_width, refTexture.tex_height, data);

            if(!isGUI) {
                for (int j = 0; j < refTexture.tex_width * refTexture.tex_height; j++) {
                    if (data[j * 4] == 0 && data[j * 4 + 1] == 0 && data[j * 4 + 2] == 0) {
                        data[j * 4] = -1;
                        data[j * 4 + 1] = -1;
                        data[j * 4 + 2] = -1;
                        data[j * 4 + 3] = 0;
                    } else {
                        data[j * 4 + 3] = -1;
                    }
                }
            }

            IntBuffer i = BufferUtils.createIntBuffer(1);
            glGenTextures(i);

            id =  i.get(0);
            glBindTexture(GL_TEXTURE_2D, id);

            pixels = BufferUtils.createByteBuffer(data.length);
            pixels.put(data);
            pixels.rewind();

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA,
                    refTexture.tex_width, refTexture.tex_height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return id;
    }
}
