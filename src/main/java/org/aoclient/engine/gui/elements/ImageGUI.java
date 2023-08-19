package org.aoclient.engine.gui.elements;

import org.aoclient.engine.gui.ElementGUI;

import java.util.ArrayList;

import static org.aoclient.engine.renderer.Drawn.geometryBoxRenderGUI;

public final class ImageGUI extends ElementGUI {

    public ImageGUI() {

    }

    public ImageGUI(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void render() {
        geometryBoxRenderGUI(this.texture.get(0), x, y,
                this.texture.get(0).getTex_width(), this.texture.get(0).getTex_height(), 0, 0, false, alphaTexture);
    }

}
