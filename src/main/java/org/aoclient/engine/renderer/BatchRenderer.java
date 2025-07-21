package org.aoclient.engine.renderer;

import org.aoclient.engine.renderer.Texture;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Clase Batch Renderer <br> <br>
 *
 * Que es mas rapido? Hacer dibujos 1x1 o tener toda la info necesaria para dibujar de una y una sola vez? <br> <br>
 *
 * Esto es lo que hace el batch rendering, antes de dibujar empieza a colocar cada textura en su posicion
 * con su recorte, blend, color, etc. Para luego al momento de dibujar solo tenga que hacerlo sabiendo donde se encuentra
 * cada textura y como tiene que estar.
 */
public class BatchRenderer {

    /**
     * Toda la informacion de una quad con una textura.
     */
    private static class Quad {
        float x, y, width, height;
        float srcX, srcY;
        float texWidth, texHeight;
        float r, g, b, a;
        boolean blend;
        Texture texture;
    }

    private List<Quad> quads = new ArrayList<>();

    /**
     * Vaciamos nuestro array de quads con texturas para que se prepare a dibujar una nueva imagen.
     */
    public void begin() {
        quads.clear();
    }

    /**
     * Preparamos las cosas para el dibujado, creando un nuevo quad con su informacion de textura, recorte, pos de recorte
     * color y demas...
     */
    public void draw(Texture texture, float x, float y, float srcX, float srcY, float width, float height, boolean blend, float alpha, RGBColor color) {
        Quad quad = new Quad();
        quad.x = x;
        quad.y = y;
        quad.width = width;
        quad.height = height;
        quad.srcX = srcX;
        quad.srcY = srcY;
        quad.texWidth = texture.getTex_width();
        quad.texHeight = texture.getTex_height();
        quad.r = color.getRed();
        quad.g = color.getGreen();
        quad.b = color.getBlue();
        quad.a = alpha;
        quad.texture = texture;
        quad.blend = blend;

        quads.add(quad);
    }

    /**
     * Recorre nuestro array de quads y dibuja todas las texturas una sola vez.
     */
    public void end() {
        Texture lastTexture = null;

        for (Quad quad : quads) {
            if (lastTexture != quad.texture) {
                if (lastTexture != null) {
                    glEnd();
                }

                quad.texture.bind();

                if (quad.blend) {
                    glBlendFunc(GL_SRC_ALPHA, GL_ONE);
                } else {
                    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                }

                glBegin(GL_QUADS);
                lastTexture = quad.texture;
            }

            float u0 = quad.srcX / quad.texWidth;
            float v0 = (quad.srcY + quad.height) / quad.texHeight;
            float u1 = (quad.srcX + quad.width) / quad.texWidth;
            float v1 = quad.srcY / quad.texHeight;

            glColor4f(quad.r, quad.g, quad.b, quad.a);

            glTexCoord2f(u0, v0); glVertex2f(quad.x, quad.y + quad.height);
            glTexCoord2f(u0, v1); glVertex2f(quad.x, quad.y);
            glTexCoord2f(u1, v1); glVertex2f(quad.x + quad.width, quad.y);
            glTexCoord2f(u1, v0); glVertex2f(quad.x + quad.width, quad.y + quad.height);
        }

        if (lastTexture != null) {
            glEnd();
        }
    }

}