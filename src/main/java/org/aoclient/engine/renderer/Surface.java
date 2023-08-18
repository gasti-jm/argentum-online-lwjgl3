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

public class Surface {
    private static Surface instance;
    private ByteBuffer pixels;
    private Map<Integer, TextureOGL> textures;

    private Surface() {

    }

    public static Surface get() {
        if (instance == null){
            instance = new Surface();
        }

        return instance;
    }

    public void initialize() {
        this.textures = new HashMap<>();
    }

    public void deleteAllTextures() {
        this.textures.clear();
    }

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
        File file = new File("resources/graphics/" + fileNum + ".bmp");

        if (file.exists()) {
            texture.id = loadTexture(texture, "resources/graphics/" + fileNum + ".bmp", false);
        } else {
            texture.id = loadTexture(texture, "resources/graphics/" + fileNum + ".png", false);
        }

        textures.put(fileNum, texture);

        return texture;
    }

    /**
     *
     * @desc: Crea y retorna una textura (en este caso una interfaz de usuario).
     */
    public TextureOGL createTexture(String file) {
        TextureOGL texture = new TextureOGL();
        texture.id = loadTexture(texture,"resources/gui/" + file, true);
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
