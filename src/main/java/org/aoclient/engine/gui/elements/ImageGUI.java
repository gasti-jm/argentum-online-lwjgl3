package org.aoclient.engine.gui.elements;

import org.aoclient.engine.gui.ElementGUI;

import java.util.ArrayList;

import static org.aoclient.engine.renderer.Drawn.geometryBoxRenderGUI;

public final class ImageGUI extends ElementGUI {

    public ImageGUI() {

    }

    public ImageGUI(String... textures) {
        this.loadTextures(textures);
        this.width = this.texture.get(0).getTex_width();
        this.height = this.texture.get(0).getTex_height();
    }

    public void init(String... textures) {
        this.loadTextures(textures);
        this.width = this.texture.get(0).getTex_width();
        this.height = this.texture.get(0).getTex_height();
    }

    @Override
    public void render() {
        geometryBoxRenderGUI(this.texture.get(0), x, y, alphaTexture);
    }

}
