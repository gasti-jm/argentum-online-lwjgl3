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
}
