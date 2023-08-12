package org.aoclient.engine.gui.elements;

import org.aoclient.engine.gui.ElementGUI;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.engine.renderer.Surface;
import org.aoclient.engine.renderer.TextureOGL;

import java.util.ArrayList;

import static org.aoclient.engine.renderer.Drawn.geometryBoxRenderGUI;

public final class ImageGUI extends ElementGUI {

    @Override
    public void init() {
        this.texture = new ArrayList<>();
    }

    @Override
    public void render() {
        geometryBoxRenderGUI(this.texture.get(0), x, y,
                this.texture.get(0).getTex_width(), this.texture.get(0).getTex_height(), 0, 0, false, 1.0f);
    }

}
