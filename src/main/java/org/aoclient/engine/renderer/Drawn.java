package org.aoclient.engine.renderer;

import org.aoclient.engine.utils.inits.GrhInfo;

import static org.aoclient.engine.Engine.renderer;
import static org.aoclient.engine.scenes.Camera.TILE_PIXEL_SIZE;
import static org.aoclient.engine.utils.GameData.grhData;
import static org.aoclient.engine.utils.Time.deltaTime;



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
        //TextureManager.requestTexture(grhData[grh_index].getFileNum());
        Texture tex = TextureManager.getTexture(grhData[grh_index].getFileNum());
        if (tex == null) return; // todavía no está lista

        renderer.draw(tex, x, y, sX, sY, src_width, src_height, blend, alpha, color);
    }

    /**
     * Lo mismo pero con una textura ya cargada.
     */
    public static void geometryBoxRender(Texture texture, int x, int y, int src_width, int src_height, float sX, float sY, boolean blend, float alpha, RGBColor color) {
        if (texture == null) return;
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


}
