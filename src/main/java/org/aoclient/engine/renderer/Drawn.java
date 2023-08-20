package org.aoclient.engine.renderer;

import org.aoclient.engine.utils.filedata.GrhInfo;

import static org.aoclient.engine.scenes.Camera.TILE_PIXEL_SIZE;
import static org.aoclient.engine.utils.GameData.*;
import static org.aoclient.engine.utils.Time.deltaTime;
import static org.lwjgl.opengl.GL11.*;

public final class Drawn {
    /**
     *
     * @param: grh_index = Numero de indice de grafico del GrhData
     * @param: x, y: posicion eje x e y de la pantalla.
     * @param: src_width, src_height: size de recorte
     * @param: sX, sY: posicion de recorte
     * @param: blend: efecto blend
     * @param: alpha: efecto de transparencia (0 a 1)
     * @param: color: objeto que contiene valores RGB como punto flotante (0 a 1).
     *
     * @desc: Se encargara de guardar la textura en la grafica y prepararla para su dibujado (en pocas palabras).
     */
    public static void geometryBoxRender(int grh_index, int x, int y, int src_width, int src_height, float sX, float sY, boolean blend, float alpha, RGBColor color) {
        if (blend)
            glBlendFunc(GL_SRC_ALPHA, GL_ONE);

        TextureOGL texture = Surface.get().getTexture(grhData[grh_index].getFileNum());

        final float src_right = sX + src_width;
        final float src_bottom = sY + src_height;

        glBindTexture(GL_TEXTURE_2D, texture.id);
        glBegin(GL_QUADS);

        {
            //  0----0
            //  |    |
            //  1----0
            glColor4f(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            glTexCoord2f (sX / texture.tex_width, (src_bottom) / texture.tex_height);
            glVertex2d(x, y + src_height);

            //  1----0
            //  |    |
            //  0----0
            glColor4f(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            glTexCoord2f(sX / texture.tex_width, sY / texture.tex_height);
            glVertex2d(x, y);

            //  0----1
            //  |    |
            //  0----0
            glColor4f(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            glTexCoord2f((src_right) / texture.tex_width, sY / texture.tex_height);
            glVertex2d(x + src_width, y);

            //  0----0
            //  |    |
            //  0----1
            glColor4f(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            glTexCoord2f((src_right) / texture.tex_width, (src_bottom) / texture.tex_height);
            glVertex2d(x + src_width, y + src_height);
        }

        glEnd();

        if (blend)
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     *
     * @desc: Lo mismo pero para interfaces de usuario.
     */
    public static void geometryBoxRenderGUI(TextureOGL texture, int x, int y, float alpha) {
        glBindTexture(GL_TEXTURE_2D, texture.id);
        glBegin(GL_QUADS);

        {
            //  0----0
            //  |    |
            //  1----0
            glColor4f(1.0f, 1.0f, 1.0f, alpha);
            glTexCoord2f (0, 1);
            glVertex2d(x, y + texture.tex_height);

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
            glVertex2d(x + texture.tex_width, y);

            //  0----0
            //  |    |
            //  0----1
            glColor4f(1.0f, 1.0f, 1.0f, alpha);
            glTexCoord2f(1, 1);
            glVertex2d(x + texture.tex_width, y + texture.tex_height);
        }

        glEnd();
    }

    /**
     * @desc: Dibuja un cuadrado o rectangulo segun la dimencion que le asignemos.
     */
    public static void drawRectangle(int x, int y, int width, int height, RGBColor color) {
        if(color == null)
            color = new RGBColor(0.0f, 0.0f, 0.0f);

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
        if(color == null)
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
     *
     * @desc: Dibuja una textura en la pantalla
     */
    public static void drawTexture(GrhInfo grh, int x, int y, boolean center, boolean animate, boolean blend, float alpha, RGBColor color) {
        if (grh.getGrhIndex() == 0 || grhData[grh.getGrhIndex()].getNumFrames() == 0)
            return;

        if (animate && grh.isStarted()) {
            grh.setFrameCounter(grh.getFrameCounter() + (deltaTime * grhData[grh.getGrhIndex()].getNumFrames() / grh.getSpeed()));

            if (grh.getFrameCounter() > grhData[grh.getGrhIndex()].getNumFrames()) {
                grh.setFrameCounter((grh.getFrameCounter() % grhData[grh.getGrhIndex()].getNumFrames()) + 1);

                if (grh.getLoops() != -1) {
                    if (grh.getLoops() > 0) {
                        grh.setLoops(grh.getLoops() - 1);
                    } else {
                        grh.setStarted(false);
                    }
                }

            }

        }

        final int currentGrhIndex = grhData[grh.getGrhIndex()].getFrame((int)(grh.getFrameCounter()));

        if (center) {
            if (grhData[currentGrhIndex].getTileWidth() != 1) {
                x = x - (int)(grhData[currentGrhIndex].getTileWidth() * TILE_PIXEL_SIZE / 2) + TILE_PIXEL_SIZE / 2;
            }

            if (grhData[currentGrhIndex].getTileHeight() != 1) {
                y = y - (int)(grhData[currentGrhIndex].getTileHeight() * TILE_PIXEL_SIZE) + TILE_PIXEL_SIZE;
            }
        }

        if (currentGrhIndex == 0 || grhData[currentGrhIndex].getFileNum() == 0)
            return;

        geometryBoxRender(currentGrhIndex, x, y,
                grhData[currentGrhIndex].getPixelWidth(),
                grhData[currentGrhIndex].getPixelHeight(),
                grhData[currentGrhIndex].getsX(),
                grhData[currentGrhIndex].getsY(), blend, alpha, color);
    }

    /**
     *
     * @desc: Dibujamos sin animacion
     */
    public static void drawGrhIndex(int grhIndex, int x, int y, RGBColor color) {
        if (color == null){
            color = new RGBColor(1.0f, 1.0f, 1.0f);
        }

        geometryBoxRender(grhIndex, x, y,
                grhData[grhIndex].getPixelWidth(),
                grhData[grhIndex].getPixelHeight(),
                grhData[grhIndex].getsX(),
                grhData[grhIndex].getsY(), false, 1.0f, color);
    }

    /**
     *
     * @desc: Esta funcion sirve para pasarle una cadena (ej: nickname) y sacar el size total
     *          que hay por cada grafico en caracter (ya que los textos son atravez de graficos).
     */
    public static int getSizeText(String text) {
        if (text.length() == 0) return 0;
        int totalSize = 0;

        // recorremos los caracteres
        for (int a = 0; a < text.length(); a++) {
            int b = text.charAt(a); // obtenemos el ASCII
            if (b > 255) break;

            if (b != 32) {
                totalSize += grhData[grhData[fontTypes[0].getAscii_code(b)].getFrame(1)].getPixelWidth();
            } else {
                totalSize += 4;
            }
        }

        return totalSize;
    }

}
