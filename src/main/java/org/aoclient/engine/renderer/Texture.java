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
        final ByteBuffer pixels;
        final BufferedImage image;

        try {
            // Generate texture on GPU
            this.id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);

            // CRÍTICO EN macOS
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

            // Lee los datos del recurso desde el archivo comprimido
            final byte[] resourceData = readResource("resources/" + compressedFile, file);
            final InputStream is = new ByteArrayInputStream(resourceData);

            image = ImageIO.read(is);

            refTexture.tex_width = image.getWidth();
            refTexture.tex_height = image.getHeight();

            // Leer pixeles ARGB
            int[] srcPixels = new int[refTexture.tex_width * refTexture.tex_height];
            image.getRGB(0, 0,
                    refTexture.tex_width,
                    refTexture.tex_height,
                    srcPixels,
                    0,
                    refTexture.tex_width);

            byte[] data = new byte[4 * refTexture.tex_width * refTexture.tex_height];

            // Flip vertical MANUAL (sin Graphics2D)
            for (int y = 0; y < refTexture.tex_height; y++) {
                for (int x = 0; x < refTexture.tex_width; x++) {

                    int srcIndex = y * refTexture.tex_width + x;
                    int dstIndex = (y * refTexture.tex_width + x) * 4;

                    int pixel = srcPixels[srcIndex];

                    byte a = (byte) ((pixel >> 24) & 0xFF);
                    byte r = (byte) ((pixel >> 16) & 0xFF);
                    byte g = (byte) ((pixel >> 8) & 0xFF);
                    byte b = (byte) (pixel & 0xFF);

                    if (!isGUI) {
                        if (r == 0 && g == 0 && b == 0) {
                            r = g = b = (byte) 255;
                            a = 0;
                        } else {
                            a = (byte) 255;
                        }
                    }

                    data[dstIndex]     = r;
                    data[dstIndex + 1] = g;
                    data[dstIndex + 2] = b;
                    data[dstIndex + 3] = a;
                }
            }

            pixels = BufferUtils.createByteBuffer(data.length);
            pixels.put(data);
            pixels.flip();

            glTexImage2D(
                    GL_TEXTURE_2D,
                    0,
                    GL_RGBA,
                    refTexture.tex_width,
                    refTexture.tex_height,
                    0,
                    GL_RGBA,
                    GL_UNSIGNED_BYTE,
                    pixels
            );

            // Texture parameters
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
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
