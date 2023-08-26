package org.aoclient.engine.scenes;

import org.aoclient.engine.renderer.RGBColor;

/**
 * Scene: Clase abstracta ya que no se sabe como se va a formar cada escena. En este caso hay varias:
 *        Intro (frmCargando - frmPres), Main (frmConectar - frmCrearPersonaje), Game (frmMain).
 *
 *        Cada uno de estos va a estar formado por varias cosas y cada uno tendra su propia funcion en la
 *        escucha del mouse y teclado, dibujado, etc.
 */
public abstract class Scene {
    protected RGBColor background;
    protected Camera camera;
    protected boolean visible = false;
    protected SceneType canChangeTo; // posible cambio de escena.

    /**
     * @desc: Inicializa la escena. No se crea un constructor ya que cada escena distinta puede estar compuesta por
     *        distintos atributos...
     */
    public void init() {
        this.visible = true;
        this.camera = new Camera();
        this.background = new RGBColor(0.0f, 0.0f, 0.0f);
    }

    /**
     * @desc: Escucha cada del mouse, segun lo definido en el MouseListener.
     */
    public abstract void mouseEvents();

    /**
     * @desc: Escucha cada evento del teclado, segun lo definido en el KeyListener.
     */
    public abstract void keyEvents();

    /**
     * @desc: Dibujado
     */
    public abstract void render();

    /**
     * Cierre de escena
     */
    public abstract void close();

    public RGBColor getBackground() {
        return background;
    }

    public boolean isVisible() {
        return visible;
    }

    public SceneType getChangeScene() {
        return canChangeTo;
    }
}
