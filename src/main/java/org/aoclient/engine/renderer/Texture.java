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
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;


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

        try {
            this.id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);

            // CLAVE para texturas chicas (fuentes)
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

            // Leer recurso
            final byte[] resourceData = readResource("resources/" + compressedFile, file);
            final InputStream is = new ByteArrayInputStream(resourceData);

            BufferedImage image = ImageIO.read(is);
            if (image == null) {
                throw new RuntimeException("No se pudo leer imagen: " + file);
            }

            refTexture.tex_width = image.getWidth();
            refTexture.tex_height = image.getHeight();

            int width = refTexture.tex_width;
            int height = refTexture.tex_height;

            // ¿Tiene alpha real?
            boolean hasAlpha = image.getColorModel().hasAlpha();

            int[] srcPixels = new int[width * height];
            image.getRGB(0, 0, width, height, srcPixels, 0, width);

            byte[] data = new byte[width * height * 4];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {

                    int srcIndex = y * width + x;
                    int dstIndex = srcIndex * 4;

                    int pixel = srcPixels[srcIndex];

                    int a = (pixel >> 24) & 0xFF;
                    int r = (pixel >> 16) & 0xFF;
                    int g = (pixel >> 8) & 0xFF;
                    int b = pixel & 0xFF;

                    // BMP legacy: negro = transparente
                    if (!hasAlpha) {
                        if (r == 0 && g == 0 && b == 0) {
                            a = 0;
                            r = 255;
                            g = 255;
                            b = 255;
                        } else {
                            a = 255;
                        }
                    }

                    data[dstIndex]     = (byte) r;
                    data[dstIndex + 1] = (byte) g;
                    data[dstIndex + 2] = (byte) b;
                    data[dstIndex + 3] = (byte) a;
                }
            }

            pixels = BufferUtils.createByteBuffer(data.length);
            pixels.put(data);
            pixels.flip();

            glTexImage2D(
                    GL_TEXTURE_2D,
                    0,
                    GL_RGBA,
                    width,
                    height,
                    0,
                    GL_RGBA,
                    GL_UNSIGNED_BYTE,
                    pixels
            );

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        } catch (Exception e) {
            e.printStackTrace();
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
