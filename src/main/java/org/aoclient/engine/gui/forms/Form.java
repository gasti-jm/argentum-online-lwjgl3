package org.aoclient.engine.gui.forms;

import org.aoclient.engine.Window;
import org.aoclient.engine.gui.elements.Button;
import org.aoclient.engine.gui.elements.ImageBox;
import org.aoclient.engine.gui.elements.TextBox;
import org.aoclient.engine.listeners.KeyListener;
import org.aoclient.engine.listeners.MouseListener;
import org.aoclient.engine.renderer.RGBColor;

import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public abstract class Form {
    protected ImageBox background; // siempre, sino no seria un formulario para argentum, no podemos usar una interfaz de windows o un JFrame...
    protected int fPosX, fPosY;
    protected int fWidth, fHeight;
    protected boolean visible;

    // posibles objetos
    protected List<Button> buttonList;
    protected List<TextBox> txtList;

    // para la gestion del formulario con los TextBoxes
    protected static int txtTabIndexsAdded;
    protected int tabIndexSelected;

    public Form() {
        this.background = new ImageBox();
        this.visible = false;
    }

    /**
     * @desc: Sirve para centrar nuestro formulario segun el tama;o de la imagen de fondo.
     *        Por ejemplo: Se usa unicamente en el frmMensaje.
     */
    protected void loadPositionAttributes(){
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

    public void checkButtons() {
        for(Button btn: buttonList) {
            setButtonState(btn);
        }
    }

    public void setButtonState(Button btn){
        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            btn.setPressed(true);
        } else {
            if(!MouseListener.isDragging()) {
                btn.runAction();
            }
        }
    }

    public void checkKeyTextBoxes() {
        if(KeyListener.isKeyReadyForAction(GLFW_KEY_TAB)) {
            if (tabIndexSelected < txtTabIndexsAdded - 1) {
                txtList.get(tabIndexSelected).setSelected(false);
                tabIndexSelected++;
            } else {
                tabIndexSelected = 0;
            }

            txtList.get(tabIndexSelected).setSelected(true);
        }

        for (TextBox txt: txtList) {
            txt.keyEvents();
        }
    }

    public void checkMouseTextBoxes() {
        boolean insideTxt = false;

        for (TextBox txt: txtList) {
            if (txt.isInside()) {
                insideTxt = true;
                glfwSetCursor(Window.get().getWindow(), glfwCreateStandardCursor(GLFW_IBEAM_CURSOR));
            }

            final int selected = txt.checkSelected();
            if (selected > -1) {
                txtList.get(tabIndexSelected).setSelected(false);
                tabIndexSelected = selected;
                txtList.get(selected).setSelected(true);
            }
        }

        if (!insideTxt) {
            glfwSetCursor(Window.get().getWindow(), glfwCreateStandardCursor(GLFW_ARROW_CURSOR));
        }
    }

    protected void renderTextBoxes(){
        for (TextBox txt: txtList) {
            txt.render();
        }
    }

    protected void renderButtons(){
        for (Button btn : buttonList) {
            btn.render();
        }
    }

    public void close() {
        this.visible = false;
        this.background.clear();
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }



}
