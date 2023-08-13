package org.aoclient.engine.renderer;

import org.aoclient.engine.scenes.Camera;
import org.aoclient.engine.utils.filedata.GrhInfo;

import static org.aoclient.engine.utils.GameData.fontTypes;
import static org.aoclient.engine.utils.GameData.grhData;
import static org.aoclient.engine.utils.Time.deltaTime;
import static org.lwjgl.opengl.GL11.*;

public final class Drawn {
    /**
     *
     * @desc: Se encargara de guardar la textura en la grafica y prepararla para su dibujado (en pocas palabras).
     */
    public static void geometryBoxRender(int grh_index, int x, int y, int src_width, int src_height, int sX, int sY, boolean blend, float alpha, RGBColor color) {
        if (blend)
            glBlendFunc(GL_SRC_ALPHA, GL_ONE);

        TextureOGL texture = Surface.getInstance().getTexture(grhData[grh_index].getFileNum());

        final float src_left = sX;
        final float src_top = sY;
        final float src_right = src_left + src_width;
        final float src_bottom = src_top + (src_height);

        final float dest_left = x;
        final float dest_top = y;
        final float dest_right = x + (src_right - src_left);
        final float dest_bottom = y + (src_bottom - src_top);

        float x_cor, y_cor;
        glBindTexture(GL_TEXTURE_2D, texture.id);
        glBegin(GL_QUADS);

        {
            //  0----0
            //  |    |
            //  1----0
            x_cor = dest_left;
            y_cor = dest_bottom;

            glColor4f(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            glTexCoord2f (src_left / texture.tex_width, (src_bottom) / texture.tex_height);
            glVertex2d(x_cor, y_cor);

            //  1----0
            //  |    |
            //  0----0
            x_cor = dest_left;
            y_cor = dest_top;

            glColor4f(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            glTexCoord2f(src_left / texture.tex_width, src_top / texture.tex_height);
            glVertex2d(x_cor, y_cor);

            //  0----1
            //  |    |
            //  0----0
            x_cor = dest_right;
            y_cor = dest_top;

            glColor4f(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            glTexCoord2f((src_right) / texture.tex_width, src_top / texture.tex_height);
            glVertex2d(x_cor, y_cor);

            //  0----0
            //  |    |
            //  0----1
            x_cor = dest_right;
            y_cor = dest_bottom;

            glColor4f(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            glTexCoord2f((src_right) / texture.tex_width, (src_bottom) / texture.tex_height);
            glVertex2d(x_cor, y_cor);
        }

        glEnd();

        if (blend)
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     *
     * @desc: Lo mismo pero para interfaces de usuario.
     */
    public static void geometryBoxRenderGUI(TextureOGL texture, int x, int y, int src_width, int src_height, int sX, int sY, boolean blend, float alpha) {
        if (blend)
            glBlendFunc(GL_SRC_ALPHA, GL_ONE);


        final float src_left = sX;
        final float src_top = sY;
        final float src_right = src_left + src_width;
        final float src_bottom = src_top + (src_height);

        final float dest_left = x;
        final float dest_top = y;
        final float dest_right = x + (src_right - src_left);
        final float dest_bottom = y + (src_bottom - src_top);

        float x_cor, y_cor;
        glBindTexture(GL_TEXTURE_2D, texture.id);
        glBegin(GL_QUADS);

        {
            //  0----0
            //  |    |
            //  1----0
            x_cor = dest_left;
            y_cor = dest_bottom;

            glColor4f(1.0f, 1.0f, 1.0f, alpha);
            glTexCoord2f (src_left / texture.tex_width, (src_bottom) / texture.tex_height);
            glVertex2d(x_cor, y_cor);

            //  1----0
            //  |    |
            //  0----0
            x_cor = dest_left;
            y_cor = dest_top;

            glColor4f(1.0f, 1.0f, 1.0f, alpha);
            glTexCoord2f(src_left / texture.tex_width, src_top / texture.tex_height);
            glVertex2d(x_cor, y_cor);

            //  0----1
            //  |    |
            //  0----0
            x_cor = dest_right;
            y_cor = dest_top;

            glColor4f(1.0f, 1.0f, 1.0f, alpha);
            glTexCoord2f((src_right) / texture.tex_width, src_top / texture.tex_height);
            glVertex2d(x_cor, y_cor);

            //  0----0
            //  |    |
            //  0----1
            x_cor = dest_right;
            y_cor = dest_bottom;

            glColor4f(1.0f, 1.0f, 1.0f, alpha);
            glTexCoord2f((src_right) / texture.tex_width, (src_bottom) / texture.tex_height);
            glVertex2d(x_cor, y_cor);
        }

        glEnd();

        if (blend)
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     *
     * @desc: Dibuja un grafico en la pantalla
     */
    public static void draw(GrhInfo grh, int x, int y, boolean center, boolean animate, boolean blend, float alpha, RGBColor color) {
        if (grh.getGrhIndex() == 0 || grhData[grh.getGrhIndex()].getNumFrames() == 0)
            return;

        if (animate && grh.isStarted()) {
            //grh.setFrameCounter(grh.getFrameCounter() + (deltaTime * grhData[grh.getGrhIndex()].getNumFrames() / (grh.getSpeed() * 0.002f)));
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
                x = x - (int)(grhData[currentGrhIndex].getTileWidth() * Camera.TILE_PIXEL_SIZE / 2) + Camera.TILE_PIXEL_SIZE / 2;
            }

            if (grhData[currentGrhIndex].getTileHeight() != 1) {
                y = y - (int)(grhData[currentGrhIndex].getTileHeight() * Camera.TILE_PIXEL_SIZE) + Camera.TILE_PIXEL_SIZE;
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
        geometryBoxRender(grhIndex, x, y,
                grhData[grhIndex].getPixelWidth(),
                grhData[grhIndex].getPixelHeight(),
                grhData[grhIndex].getsX(),
                grhData[grhIndex].getsY(), false, 1.0f, color);
    }

    /**
     *
     * @desc: Dibuja texto en la pantlla
     */
    public static void drawText(String text, int x, int y, RGBColor color, int font_index, boolean shadow) {
        if (text.length() == 0) return;

        int d = 0;
        for (int a = 0; a < text.length(); a++) {
            int b = text.charAt(a);
            if (b > 255) b = 0;

            if (b != 32) {
                if (fontTypes[font_index].getAscii_code(b) != 0) {

                    // sombra
                    if (shadow) {
                        drawGrhIndex(fontTypes[font_index].getAscii_code(b) + 100, (x + d) + 1, y + 1, color);
                    }

                    drawGrhIndex(fontTypes[font_index].getAscii_code(b), (x + d) + 1, y, color);
                    d = d + grhData[grhData[fontTypes[font_index].getAscii_code(b)].getFrame(1)].getPixelWidth();
                }
            } else {
                d = d + 4;
            }
        }
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
