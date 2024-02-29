package org.aoclient.engine.renderer;

/**
 * OpenGL guarda las texturas creadas con un id, ese id lo almacenamos para despues dibujar la textura.
 * El resto de atributos son para almacenar el tamaño de la textura.
 */
public class TextureOGL {
    int id;
    int tex_width;
    int tex_height;

    public TextureOGL() {

    }

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
