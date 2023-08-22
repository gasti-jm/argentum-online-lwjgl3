package org.aoclient.engine.gui.forms;

import org.aoclient.engine.Window;
import org.aoclient.engine.gui.elements.Button;
import org.aoclient.engine.gui.elements.ImageBox;
import org.aoclient.engine.listeners.MouseListener;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public abstract class Form {
    protected ImageBox background; // siempre, sino no seria un formulario para argentum, no podemos usar una interfaz de windows o un JFrame...
    protected int fPosX, fPosY;
    protected int fWidth, fHeight;
    protected boolean visible;

    public Form() {
        this.background = new ImageBox();
        this.visible = true;
    }

    protected void loadParentAttributes(){
        // posicionamos el formulario en el centro de la pantalla segun el background cargado!
        this.fPosX = (Window.get().getWidth() / 2) - (this.background.getWidth() / 2);
        this.fPosY = (Window.get().getHeight() / 2) - (this.background.getHeight() / 2);
        this.fHeight = this.background.getHeight();
        this.fWidth = this.background.getWidth();

        // posicionamos el fondo en la posicion de nuestro formulario
        this.background.setX(fPosX);
        this.background.setY(fPosY);
    }

    public abstract void render();
    public abstract void checkButtons();

    public void setButtonState(Button btn){
        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            btn.setPressed(true);
        } else {
            if(!MouseListener.isDragging()) {
                btn.runAction();
            }
        }
    }


    public void close() {
        this.visible = false;
        this.background.clear();
    }

    public boolean isVisible() {
        return visible;
    }



}
