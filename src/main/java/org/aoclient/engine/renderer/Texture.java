package org.aoclient.engine.renderer;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.aoclient.scripts.Compressor.readResource;
import static org.lwjgl.opengl.GL11.*;

/**
 * La clase {@code Texture} representa una textura en OpenGL para el renderizado grafico.
 * <p>
 * OpenGL guarda las texturas creadas con un identificador numerico (ID), que esta clase almacena junto con informacion sobre el
 * tamaño de la textura. Proporciona funcionalidad para cargar texturas desde archivos comprimidos, y metodos para vincular y
 * desvincular texturas durante el proceso de renderizado.
 * <p>
 * Esta clase es fundamental en el sistema de renderizado, ya que permite que las texturas sean cargadas en memoria grafica y se
 * puedan dibujar en pantalla de manera eficiente a traves de OpenGL.
 */

public class Texture {

    private int id;
    private int tex_width;
    private int tex_height;

    public Texture() {

    }

    public void loadTexture(Texture refTexture, String compressedFile, String file, boolean isGUI) {
        ByteBuffer pixels;
        BufferedImage bi;

        try {
            // Generate texture on GPU
            this.id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);

            // Lee los datos del recurso desde el archivo comprimido
            byte[] resourceData = readResource("resources/" + compressedFile, file);
            /* if (resourceData == null) {
            System.err.println("No se pudieron cargar los datos de " + file);
            return -1;
            } */

            InputStream is = new ByteArrayInputStream(resourceData);

            //File fil = new File(file);
            BufferedImage image = ImageIO.read(is);

            refTexture.tex_width = image.getWidth();
            refTexture.tex_height = image.getHeight();

            bi = new BufferedImage(refTexture.tex_width, refTexture.tex_height, BufferedImage.TYPE_4BYTE_ABGR);

            Graphics2D g = bi.createGraphics();
            g.scale(1, -1);
            g.drawImage(image, 0, 0, refTexture.tex_width, -refTexture.tex_height, null);

            byte[] data = new byte[4 * refTexture.tex_width * refTexture.tex_height];
            bi.getRaster().getDataElements(0, 0, refTexture.tex_width, refTexture.tex_height, data);

            if (!isGUI) {
                for (int j = 0; j < refTexture.tex_width * refTexture.tex_height; j++) {
                    if (data[j * 4] == 0 && data[j * 4 + 1] == 0 && data[j * 4 + 2] == 0) {
                        data[j * 4] = -1;
                        data[j * 4 + 1] = -1;
                        data[j * 4 + 2] = -1;
                        data[j * 4 + 3] = 0;
                    } else data[j * 4 + 3] = -1;
                }
            }

            pixels = BufferUtils.createByteBuffer(data.length);
            pixels.put(data);
            pixels.rewind();

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA,
                    refTexture.tex_width, refTexture.tex_height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

            //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            // Set texture parameters
            // Repeat image in both directions
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            // When stretching the image, pixelate
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            // When shrinking an image, pixelate
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getId() {
        return this.id;
    }

    public int getTex_width() {
        return tex_width;
    }

    public int getTex_height() {
        return tex_height;
    }

}
