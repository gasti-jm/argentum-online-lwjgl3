package org.aoclient.engine.renderer;

import org.aoclient.engine.utils.inits.GrhInfo;

import static org.aoclient.engine.Engine.renderer;
import static org.aoclient.engine.scenes.Camera.TILE_PIXEL_SIZE;
import static org.aoclient.engine.utils.GameData.grhData;
import static org.aoclient.engine.utils.Time.deltaTime;
import static org.lwjgl.opengl.GL11.*;

/**
 * <p>
 * Clase utilitaria central para el sistema de renderizado que proporciona metodos de dibujo para todos los elementos visuales.
 * <p>
 * Esta clase estatica contiene los metodos principales que permiten dibujar en la pantalla de renderizado, ofreciendo funciones
 * para renderizar texturas, figuras geometricas, lineas, rectangulos y otros elementos graficos con diferentes propiedades y
 * transformaciones.
 * <p>
 * Implementa la funcionalidad de bajo nivel para el renderizado mediante OpenGL, encapsulando las operaciones complejas en
 * metodos faciles de usar. Maneja aspectos como la aplicacion de efectos de mezcla (blending), transparencia, colores y
 * transformaciones de coordenadas.
 * <p>
 * Es utilizada por todos los subsistemas graficos del juego para dibujar desde elementos de la interfaz de usuario hasta
 * personajes, objetos del mapa y efectos visuales. La mayor parte del renderizado visible pasa por los metodos de esta clase.
 * <p>
 * El dibujado especifico de personajes se encuentra en la clase {@link Character}, el de la interfaz de usuario en
 * {@code ElementGUI} y los textos en la clase {@code FontText}.
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
     *  Se encargara de guardar la textura en la grafica y prepararla para su dibujado (en pocas palabras).
     */
    public static void geometryBoxRender(int grh_index, int x, int y, int src_width, int src_height, float sX, float sY, boolean blend, float alpha, RGBColor color) {
        final Texture texture = Surface.INSTANCE.getTexture(grhData[grh_index].getFileNum());
        renderer.draw(texture, x, y, sX, sY, src_width, src_height, blend, alpha, color);
    }

    /**
     * Lo mismo pero con una textura ya cargada.
     */
    public static void geometryBoxRender(Texture texture, int x, int y, int src_width, int src_height, float sX, float sY, boolean blend, float alpha, RGBColor color) {
        renderer.draw(texture, x, y, sX, sY, src_width, src_height, blend, alpha, color);
    }

    /**
     *  Dibuja una textura en la pantalla
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
     *  Dibujamos sin animacion
     */
    public static void drawGrhIndex(int grhIndex, int x, int y, RGBColor color) {
        if (color == null) color = new RGBColor(1.0f, 1.0f, 1.0f);
        geometryBoxRender(grhIndex, x, y,
                grhData[grhIndex].getPixelWidth(),
                grhData[grhIndex].getPixelHeight(),
                grhData[grhIndex].getsX(),
                grhData[grhIndex].getsY(), false, 1.0f, color);
    }





    /**
     * ===============================================================
     * Dibujado sin batch (lo hago para el frmCrearPersonaje).
     *
     * TODO: esto es una villeriada mas grande que una casa. lo voy a eliminar a la mierda.
     * ===============================================================
     */

    /**
     *  Dibuja una textura en la pantalla
     */
    public static void drawTextureNoBatch(GrhInfo grh, int x, int y, boolean center, boolean animate, boolean blend, float alpha, RGBColor color) {
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

        geometryBoxRenderNoBatch(currentGrhIndex, x, y,
                grhData[currentGrhIndex].getPixelWidth(),
                grhData[currentGrhIndex].getPixelHeight(),
                grhData[currentGrhIndex].getsX(),
                grhData[currentGrhIndex].getsY(), blend, alpha, color);
    }

    /**
     *  Dibujamos sin animacion
     */
    public static void drawGrhIndexNoBatch(int grhIndex, int x, int y, RGBColor color) {
        if (color == null) color = new RGBColor(1.0f, 1.0f, 1.0f);
        geometryBoxRenderNoBatch(grhIndex, x, y,
                grhData[grhIndex].getPixelWidth(),
                grhData[grhIndex].getPixelHeight(),
                grhData[grhIndex].getsX(),
                grhData[grhIndex].getsY(), false, 1.0f, color);
    }

    public static void geometryBoxRenderNoBatch(int grh_index, int x, int y, int src_width, int src_height, float sX, float sY, boolean blend, float alpha, RGBColor color) {
        if (blend) glBlendFunc(GL_SRC_ALPHA, GL_ONE);

        final Texture texture = Surface.INSTANCE.getTexture(grhData[grh_index].getFileNum());
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

    public static void geometryBoxRenderGUI(Texture texture, int x, int y, float alpha) {
        texture.bind();
        glBegin(GL_QUADS);

        {
            //  0----0
            //  |    |,
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

}
