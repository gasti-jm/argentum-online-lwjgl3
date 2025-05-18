package org.aoclient.engine.gui.forms;

import org.aoclient.engine.gui.ImGUISystem;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;

import static org.aoclient.scripts.Compressor.readResource;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

/**
 * <p>
 * Define la estructura comun y comportamiento basico que deben implementar todos los formularios. Proporciona un marco unificado
 * para la creacion de interfaces de usuario interactivas, obligando a todas las subclases a implementar su propia logica de
 * renderizado mediante el metodo abstracto {@code render()}.
 * <p>
 * Incluye funcionalidad para gestionar recursos graficos comunes como imagenes de fondo, y metodos utilitarios que pueden ser
 * utilizados por todos los formularios, como la carga de texturas desde archivos comprimidos y la apertura de URLs en el
 * navegador del sistema.
 * <p>
 * Establece el ciclo de vida basico de los formularios, incluyendo su inicializacion, renderizado y cierre, integrandose con el
 * sistema de gestion de interfaz {@code ImGUISystem} para garantizar un comportamiento coherente de todas las ventanas.
 * <p>
 * TODO Cambiar el concepto/palabra "Form" ya que no estamos trabajando mas con formularios, sino con ventanas
 */

public abstract class Form {

    protected int backgroundImage;

    public abstract void render();

    public void close() {
        glDeleteTextures(backgroundImage);
        ImGUISystem.get().deleteFrmArray(this);
    }

    protected int loadTexture(final String file) throws IOException {
        // Lee los datos del recurso desde el archivo comprimido
        byte[] resourceData = readResource("resources/gui.ao", file);
        /* if (resourceData == null) {
            System.err.println("No se pudieron cargar los datos de " + file);
            return -1; // TODO Deberia devolver -1 en este caso?
        } */

        InputStream is = new ByteArrayInputStream(resourceData);
        BufferedImage image = ImageIO.read(is);

        final int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        final ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); // 4 for RGBA, 3 for RGB
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                final int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        buffer.flip();

        final int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        return textureID;
    }

    protected void abrirURL(String url) {
        // Verifica si Desktop es compatible con la plataforma actual
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                // Crea una instancia de URI desde la URL
                URI uri = new URI(url);
                // Abrimos la URL en el navegador predeterminado
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else System.out.println("La apertura de URL no es compatible en esta plataforma.");
    }

}
