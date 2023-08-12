package org.aoclient.engine.renderer;

public class TextureOGL {
    int id;
    int tex_width;
    int tex_height;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTex_width() {
        return tex_width;
    }

    public void setTex_width(int tex_width) {
        this.tex_width = tex_width;
    }

    public int getTex_height() {
        return tex_height;
    }

    public void setTex_height(int tex_height) {
        this.tex_height = tex_height;
    }
}
