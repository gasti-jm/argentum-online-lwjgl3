package org.aoclient.engine.gui.elements;

import org.aoclient.engine.gui.ElementGUI;

import static org.aoclient.engine.renderer.Drawn.geometryBoxRenderGUI;

/**
 * Clase ImageBox heredada de la clase abstracta ElementGUI
 *
 * Esto se utilza como pictureBox en pocas palabras, lo utilizamos tambien para cargar una
 * interface en nuestros formularios.
 */
public final class ImageBox extends ElementGUI {

    public ImageBox() {

    }

    public ImageBox(String... textures) {
        this.loadTextures(textures);
        this.width = this.texture.get(0).getTex_width();
        this.height = this.texture.get(0).getTex_height();
    }

    /**
     *
     * @param textures textura a cargar
     * @desc Carga la textura para el ImageBox y le asigna un width y height segun el que tenga la textura cargada.
     *       Esta funcion se utiliza para los formularios en su  fucnion init (al momento de crearse).
     */
    public void init(String... textures) {
        this.loadTextures(textures);
        this.width = this.texture.get(0).getTex_width();
        this.height = this.texture.get(0).getTex_height();
    }

    /**
     * @desc Dibuja nuestra textura.
     */
    @Override
    public void render() {
        geometryBoxRenderGUI(this.texture.get(0), x, y, alphaTexture);
    }

}
