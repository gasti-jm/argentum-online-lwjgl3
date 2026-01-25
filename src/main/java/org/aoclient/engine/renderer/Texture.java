package org.aoclient.engine.renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.aoclient.scripts.Compressor.readResourceAsBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.stb.STBImage.*;

public class Texture {

    private int id;
    private int tex_width;
    private int tex_height;

    public Texture() {

    }

    public void loadTexture(Texture refTexture, String compressedFile, String file, boolean isGUI) {
        TextureData data = loadToCPU(compressedFile, file);
        if (data != null) {
            uploadToGPU(data);
        }
    }

    public static TextureData loadToCPU(String compressedFile, String file) {
        try {
            // Leer recurso como buffer mapeado
            final ByteBuffer resourceBuffer = readResourceAsBuffer("assets/" + compressedFile, file);
            if (resourceBuffer == null) {
                // throw new RuntimeException("No se pudo leer recurso: " + file);
                return null;
            }

            IntBuffer w = BufferUtils.createIntBuffer(1);
            IntBuffer h = BufferUtils.createIntBuffer(1);
            IntBuffer comp = BufferUtils.createIntBuffer(1);

            // Cargamos con 4 canales (RGBA)
            ByteBuffer image = stbi_load_from_memory(resourceBuffer, w, h, comp, 4);
            if (image == null) {
                throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
            }

            int width = w.get(0);
            int height = h.get(0);

            // Argentum Online Legacy BMP: Negro (0,0,0) es transparente si no hay alpha real
            // STB nos dice cuantos canales tenia el archivo original en 'comp'
            boolean hasAlpha = comp.get(0) == 4;

            if (!hasAlpha) {
                for (int i = 0; i < width * height; i++) {
                    int r = image.get(i * 4) & 0xFF;
                    int g = image.get(i * 4 + 1) & 0xFF;
                    int b = image.get(i * 4 + 2) & 0xFF;

                    if (r == 0 && g == 0 && b == 0) {
                        image.put(i * 4, (byte) 255);
                        image.put(i * 4 + 1, (byte) 255);
                        image.put(i * 4 + 2, (byte) 255);
                        image.put(i * 4 + 3, (byte) 0);
                    } else {
                        image.put(i * 4 + 3, (byte) 255);
                    }
                }
            }

            return new TextureData(image, width, height);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void uploadToGPU(TextureData data) {
        if (data == null) return;

        this.tex_width = data.getWidth();
        this.tex_height = data.getHeight();

        this.id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGBA,
                tex_width,
                tex_height,
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                data.getBuffer()
        );

        stbi_image_free(data.getBuffer());

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
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