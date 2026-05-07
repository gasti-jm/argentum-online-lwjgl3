package org.aoclient.engine.renderer;

import org.aoclient.engine.utils.inits.GrhData;
import org.aoclient.engine.utils.inits.GrhInfo;

import static org.aoclient.engine.EngineGL.renderer;
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
    public static void geometryBoxRender(int grh_index, int x, int y,
                                         int src_width, int src_height,
                                         float sX, float sY,
                                         boolean blend, float alpha, RGBColor color) {

        final var tex = TextureManager.getTexture(grhData[grh_index].getFileNum());
        renderer.draw(tex, x, y, sX, sY, src_width, src_height, blend, alpha, color);
    }

    /**
     * Lo mismo pero con una textura ya cargada.
     */
    public static void geometryBoxRender(Texture texture, int x, int y,
                                         int src_width, int src_height,
                                         float sX, float sY,
                                         boolean blend, float alpha, RGBColor color) {

        renderer.draw(texture, x, y, sX, sY, src_width, src_height, blend, alpha, color);
    }

    /**
     *  Dibuja una textura en la pantalla
     */
    public static void drawTexture(GrhInfo grh, int x, int y,
                                   boolean center, boolean animate,
                                   boolean blend, float alpha, RGBColor color) {

        if (grh.getGrhIndex() == 0 || grhData[grh.getGrhIndex()].getNumFrames() == 0) return;

        if (animate)
            grh.updateAnim();

        // centra la textura en el medio del tile.
        if (center) {
            if (grhData[grh.getCurrentIndex()].getTileWidth() != 1)
                x = x - (int) (grhData[grh.getCurrentIndex()].getTileWidth() * TILE_PIXEL_SIZE / 2) + TILE_PIXEL_SIZE / 2;
            if (grhData[grh.getCurrentIndex()].getTileHeight() != 1)
                y = y - (int) (grhData[grh.getCurrentIndex()].getTileHeight() * TILE_PIXEL_SIZE) + TILE_PIXEL_SIZE;
        }

        final GrhData current = grhData[grh.getCurrentIndex()];
        geometryBoxRender(
                grh.getCurrentIndex(), x, y,
                current.getPixelWidth(),
                current.getPixelHeight(),
                current.getsX(),
                current.getsY(), blend, alpha, color
        );
    }

    /**
     *  Dibujamos sin animacion
     */
    public static void drawGrhIndex(int grhIndex, int x, int y, RGBColor color) {
        if (color == null) color = new RGBColor(1.0f, 1.0f, 1.0f);

        geometryBoxRender(
                grhIndex, x, y,
                grhData[grhIndex].getPixelWidth(),
                grhData[grhIndex].getPixelHeight(),
                grhData[grhIndex].getsX(),
                grhData[grhIndex].getsY(), false, 1.0f, color
        );
    }


}
