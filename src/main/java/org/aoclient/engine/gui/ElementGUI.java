package org.aoclient.engine.gui;


import org.aoclient.engine.listeners.MouseListener;
import org.aoclient.engine.renderer.Surface;
import org.aoclient.engine.renderer.TextureOGL;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase abstracta donde implementa ciertos metodos y atributos importantes para un elemento GUI (Interfaz de usuario).
 *
 * Tenemos la posicion X e Y de la pantalla, tamaño, visbilidad, y una lista de texturas.
 */
public abstract class ElementGUI {
    protected float alphaTexture = 1.0f;

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean visible = true;
    protected List<TextureOGL> texture;

    /**
     * @desc Esta sobrecarga de constructores permite que las clases hijas puedan tener uno o varios
     *       constructores personalizables.
     */
    public ElementGUI() {
        this.texture = new ArrayList<>();
    }

    public ElementGUI(int x, int y, int width, int height) {
        this.texture = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * @desc Cada componente de GUI se renderiza de distintas formas.
     *       Ejemplo: Un bonton renderiza 3 texturas segun su estado (Normal, Click y Rollover)
     */
    public abstract void render();

    /**
     * @desc Vacia y destruye la lista de texturas.
     */
    public void clear() {
        this.texture.clear();
    }

    /**
     *
     * @param files Nombre del grafico, en este caso se puede agregar la cantidad que quieras porque es un varargs.
     * @desc Crea una textura y la guarda en el listado de texturas de nuestro componente.
     */
    public void loadTextures(String... files) {
        for (int i = 0; i < files.length; i++) {
            texture.add(Surface.get().createTexture(files[i]));
        }
    }

    /**
     *
     * @return True si la posicion del mouse se encuentra dentro del componenete.
     */
    public boolean isInside(){
        return MouseListener.getX() >= this.x && MouseListener.getX() <= this.x + this.width &&
                MouseListener.getY() >= this.y && MouseListener.getY() <= this.y + this.height;
    }

    /**
     *
     * @return Getter del atributo x.
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @desc Setter del atributo x.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     *
     * @return Getter del atributo y.
     */
    public int getY() {
        return y;
    }

    /**
     *
     * @desc Setter del atributo y.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     *
     * @return Getter del atributo width.
     */
    public int getWidth() {
        return width;
    }

    /**
     *
     * @desc Setter del atributo width.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     *
     * @return Getter del atributo height.
     */
    public int getHeight() {
        return height;
    }


    /**
     *
     * @desc Setter del atributo height.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     *
     * @return Getter del atributo alphaTexture.
     */
    public float getAlphaTexture() {
        return alphaTexture;
    }

    /**
     *
     * @desc Setter del atributo alphaTexture.
     */
    public void setAlphaTexture(float alphaTexture) {
        this.alphaTexture = alphaTexture;
    }

    /**
     *
     * @return Getter del atributo visible.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     *
     * @desc Setter del atributo visible.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
