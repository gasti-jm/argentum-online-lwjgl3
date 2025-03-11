package org.aoclient.engine.renderer;

import org.aoclient.engine.utils.inits.GrhInfo;

import static org.aoclient.engine.scenes.Camera.TILE_PIXEL_SIZE;
import static org.aoclient.engine.utils.GameData.grhData;
import static org.aoclient.engine.utils.Time.deltaTime;
import static org.lwjgl.opengl.GL11.*;

/**
 * Aqui es donde se van a encontrar la mayoria de los metodos que nos permiten dibujar en nuestro renderizado.
 * <p>
 * El dibujado del personaje se encuentra en la clase Character, el de la Interfaz de usuario en ElementGUI y los textos en la
 * clase FontText
 */

public final class Drawn {

    /**
     * @param: grh_index = Numero de indice de grafico del GrhData
     * @param: x, y: posicion eje x e y de la pantalla.
     * @param: src_width, src_height: size de recorte
     * @param: sX, sY: posicion de recorte
     * @param: blend: efecto blend
     * @param: alpha: efecto de transparencia (0 a 1)
     * @param: color: objeto que contiene valores RGB como punto flotante (0 a 1).
     * @desc: Se encargara de guardar la textura en la grafica y prepararla para su dibujado (en pocas palabras).
     */
    public static void geometryBoxRender(int grh_index, int x, int y, int src_width, int src_height, float sX, float sY, boolean blend, float alpha, RGBColor color) {
        if (blend) glBlendFunc(GL_SRC_ALPHA, GL_ONE);

        final Texture texture = Surface.get().getTexture(grhData[grh_index].getFileNum());
        final float src_right = sX + src_width;
        final float src_bottom = sY + src_height;

        texture.bind();
        glBegin(GL_QUADS);

        {
            //  0----0
            //  |    |
            //  1----0
            glColor4f(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            glTexCoord2f(sX / texture.getTex_width(), (src_bottom) / texture.getTex_height());
            glVertex2d(x, y + src_height);

            //  1----0
            //  |    |
            //  0----0
            glColor4f(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            glTexCoord2f(sX / texture.getTex_width(), sY / texture.getTex_height());
            glVertex2d(x, y);

            //  0----1
            //  |    |
            //  0----0
            glColor4f(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            glTexCoord2f((src_right) / texture.getTex_width(), sY / texture.getTex_height());
            glVertex2d(x + src_width, y);

            //  0----0
            //  |    |
            //  0----1
            glColor4f(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            glTexCoord2f((src_right) / texture.getTex_width(), (src_bottom) / texture.getTex_height());
            glVertex2d(x + src_width, y + src_height);
        }

        texture.unbind();
        glEnd();

        if (blend) glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * @desc: Lo mismo pero para interfaces de usuario.
     */
    public static void geometryBoxRenderGUI(Texture texture, int x, int y, float alpha) {
        texture.bind();
        glBegin(GL_QUADS);

        {
            //  0----0
            //  |    |
            //  1----0
            glColor4f(1.0f, 1.0f, 1.0f, alpha);
            glTexCoord2f(0, 1);
            glVertex2d(x, y + texture.getTex_height());

            //  1----0
            //  |    |
            //  0----0
            glColor4f(1.0f, 1.0f, 1.0f, alpha);
            glTexCoord2f(0, 0);
            glVertex2d(x, y);

            //  0----1
            //  |    |
            //  0----0
            glColor4f(1.0f, 1.0f, 1.0f, alpha);
            glTexCoord2f(1, 0);
            glVertex2d(x + texture.getTex_width(), y);

            //  0----0
            //  |    |
            //  0----1
            glColor4f(1.0f, 1.0f, 1.0f, alpha);
            glTexCoord2f(1, 1);
            glVertex2d(x + texture.getTex_width(), y + texture.getTex_height());
        }

        texture.unbind();
        glEnd();
    }

    /**
     * Lo mismo pero con una textura ya cargada.
     */
    public static void geometryBoxRender(Texture texture, int x, int y, int src_width, int src_height, float sX, float sY, boolean blend, float alpha, RGBColor color) {
        if (blend) glBlendFunc(GL_SRC_ALPHA, GL_ONE);

        final float src_right = sX + src_width;
        final float src_bottom = sY + src_height;

        texture.bind();
        glBegin(GL_QUADS);

        {
            //  0----0
            //  |    |
            //  1----0
            glColor4f(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            glTexCoord2f(sX / texture.getTex_width(), (src_bottom) / texture.getTex_height());
            glVertex2d(x, y + src_height);

            //  1----0
            //  |    |
            //  0----0
            glColor4f(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            glTexCoord2f(sX / texture.getTex_width(), sY / texture.getTex_height());
            glVertex2d(x, y);

            //  0----1
            //  |    |
            //  0----0
            glColor4f(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            glTexCoord2f((src_right) / texture.getTex_width(), sY / texture.getTex_height());
            glVertex2d(x + src_width, y);

            //  0----0
            //  |    |
            //  0----1
            glColor4f(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            glTexCoord2f((src_right) / texture.getTex_width(), (src_bottom) / texture.getTex_height());
            glVertex2d(x + src_width, y + src_height);
        }

        texture.unbind();
        glEnd();

        if (blend)
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * @desc: Dibuja un cuadrado o rectangulo segun la dimencion que le asignemos.
     */
    public static void drawRectangle(int x, int y, int width, int height, RGBColor color) {
        if (color == null) color = new RGBColor(0.0f, 0.0f, 0.0f);

        glDisable(GL_BLEND);
        glBegin(GL_QUADS);

        {
            //  0----0
            //  |    |
            //  1----0

            glColor3f(color.getRed(), color.getGreen(), color.getBlue());
            glVertex2d(x, y + height);

            //  1----0
            //  |    |
            //  0----0
            glColor3f(color.getRed(), color.getGreen(), color.getBlue());
            glVertex2f(x, y);

            //  0----1
            //  |    |
            //  0----0
            glColor3f(color.getRed(), color.getGreen(), color.getBlue());
            glVertex2f(x + width, y);


            //  0----0
            //  |    |
            //  0----1
            glColor3f(color.getRed(), color.getGreen(), color.getBlue());
            glVertex2f(x + width, y + height);

        }
        glEnd();

        glEnable(GL_BLEND);
    }

    /**
     * @desc: Dibuja una linea seun las dimenciones que les demos.
     */
    public static void drawLine(int x, int y, int width, int height, RGBColor color) {
        if (color == null)
            color = new RGBColor(0.0f, 0.0f, 0.0f);

        glDisable(GL_BLEND);
        glBegin(GL_LINES);

        // Bottom-left to top-left
        glColor3f(color.getRed(), color.getGreen(), color.getBlue());
        glVertex2d(x, y);
        glVertex2d(x, y + height);

        // Top-left to top-right
        glColor3f(color.getRed(), color.getGreen(), color.getBlue());
        glVertex2d(x, y + height);
        glVertex2d(x + width, y + height);

        // Top-right to bottom-right
        glColor3f(color.getRed(), color.getGreen(), color.getBlue());
        glVertex2d(x + width, y + height);
        glVertex2d(x + width, y);

        // Bottom-right to bottom-left
        glColor3f(color.getRed(), color.getGreen(), color.getBlue());
        glVertex2d(x + width, y);
        glVertex2d(x, y);

        glEnd();
        glEnable(GL_BLEND);
    }

    /**
     * @desc: Dibuja una textura en la pantalla
     */
    public static void drawTexture(GrhInfo grh, int x, int y, boolean center, boolean animate, boolean blend, float alpha, RGBColor color) {
        if (grh.getGrhIndex() == 0 || grhData[grh.getGrhIndex()].getNumFrames() == 0) return;
        if (animate && grh.isStarted()) {
            grh.setFrameCounter(grh.getFrameCounter() + (deltaTime * grhData[grh.getGrhIndex()].getNumFrames() / grh.getSpeed()));
            if (grh.getFrameCounter() > grhData[grh.getGrhIndex()].getNumFrames()) {
                grh.setFrameCounter((grh.getFrameCounter() % grhData[grh.getGrhIndex()].getNumFrames()) + 1);
                if (grh.getLoops() != -1) {
                    if (grh.getLoops() > 0) grh.setLoops(grh.getLoops() - 1);
                    else grh.setStarted(false);
                }
            }
        }

        final int currentGrhIndex = grhData[grh.getGrhIndex()].getFrame((int) (grh.getFrameCounter()));

        if (center) {
            if (grhData[currentGrhIndex].getTileWidth() != 1)
                x = x - (int) (grhData[currentGrhIndex].getTileWidth() * TILE_PIXEL_SIZE / 2) + TILE_PIXEL_SIZE / 2;
            if (grhData[currentGrhIndex].getTileHeight() != 1)
                y = y - (int) (grhData[currentGrhIndex].getTileHeight() * TILE_PIXEL_SIZE) + TILE_PIXEL_SIZE;
        }

        if (currentGrhIndex == 0 || grhData[currentGrhIndex].getFileNum() == 0) return;

        geometryBoxRender(currentGrhIndex, x, y,
                grhData[currentGrhIndex].getPixelWidth(),
                grhData[currentGrhIndex].getPixelHeight(),
                grhData[currentGrhIndex].getsX(),
                grhData[currentGrhIndex].getsY(), blend, alpha, color);
    }

    /**
     * @desc: Dibujamos sin animacion
     */
    public static void drawGrhIndex(int grhIndex, int x, int y, RGBColor color) {
        if (color == null) color = new RGBColor(1.0f, 1.0f, 1.0f);
        geometryBoxRender(grhIndex, x, y,
                grhData[grhIndex].getPixelWidth(),
                grhData[grhIndex].getPixelHeight(),
                grhData[grhIndex].getsX(),
                grhData[grhIndex].getsY(), false, 1.0f, color);
    }

}
