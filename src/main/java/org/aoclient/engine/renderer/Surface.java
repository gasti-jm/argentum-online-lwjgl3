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
    private int textureWidth, textureHeight;
    private Map<Integer, TextureOGL> textures;

    private Surface() {

    }

    public static Surface getInstance() {
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

        texture.id = loadTexture("resources/graphics/" + fileNum + ".png", false);
        texture.tex_width = textureWidth;
        texture.tex_height = textureHeight;

        textures.put(fileNum, texture);

        return texture;
    }

    /**
     *
     * @desc: Crea y retorna una textura (en este caso una interfaz de usuario).
     */
    public TextureOGL createTexture(String file) {
        TextureOGL texture = new TextureOGL();

        texture.id = loadTexture("resources/gui/" + file, true);
        texture.tex_width = textureWidth;
        texture.tex_height = textureHeight;

        return texture;
    }

    public int loadTexture(String file, boolean isGUI) {
        pixels = BufferUtils.createByteBuffer(1);
        BufferedImage bi;
        int id = 0;

        try {
            File fil = new File(file);
            BufferedImage image = ImageIO.read(fil);

            textureWidth = image.getWidth();
            textureHeight = image.getHeight();

            bi = new BufferedImage(textureWidth, textureHeight, BufferedImage.TYPE_4BYTE_ABGR);

            Graphics2D g = bi.createGraphics();
            g.scale(1, -1);
            g.drawImage(image, 0, 0, textureWidth, -textureHeight, null);

            byte[] data = new byte[4 * textureWidth * textureHeight];
            bi.getRaster().getDataElements(0, 0, textureWidth, textureHeight, data);

            if(!isGUI) {
                for (int j = 0; j < textureWidth * textureHeight; j++) {
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
                    textureWidth, textureHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return id;
    }
}
