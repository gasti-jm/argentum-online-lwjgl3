package org.aoclient.engine.gui.forms;

import org.aoclient.engine.Window;
import org.aoclient.engine.gui.elements.Button;
import org.aoclient.engine.gui.elements.ImageBox;
import org.aoclient.engine.gui.elements.TextBox;
import org.aoclient.engine.listeners.KeyListener;
import org.aoclient.engine.listeners.MouseListener;

import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Clase abstracta que contiene ciertos atributos y metodos para el desarrollo de ciertos formularios.
 *
 * 1 - Contiene su fondo (background) que es una textura.
 * 2 - Posicionamiento en X e Y en pantalla.
 * 3 - Tamaño del formulario: Esto se debe asignar en los formularios que van a estar en el centro de la pantalla.
 *     Como hacemos con el frmMensaje, llamamos al metodo "loadPositionAttributes" en la funcion init.
 * 4 - Visiblidad
 * 5 - Lista de posibles elementos a agregar. (Por el momento tenemos Listas de Botones y Cajas de texto).
 * 6 - Gestion de tabIndex para las cajas de texto.
 */
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
     * @desc: Sirve para centrar nuestro formulario segun el tamaño de la imagen de fondo.
     *        Por ejemplo: Se usa en el frmMensaje.
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

    /**
     * @desc Renderiza el contenido del formulario definido.
     */
    public abstract void render();

    /**
     * Recorre todos los botones que agregamos al formulario y actualiza su estado (segun lo que hagamos con el mouse)
     * y en caso de hacerle click completamente realiza su accion.
     */
    public void checkButtons() {
        if(!visible) return;

        for(Button btn: buttonList) {
            setButtonState(btn);
        }
    }

    /**
     *
     * @param btn button al que se le quiera realizar cierta accion con el mouse
     * @desc Permite actualizar el estado de "apretado/pressed" al mantener apretado el boton, si ya no estamos
     *       manteniendo y soltamos (o hacemos click rapido) realiza la accion del boton.
     */
    public void setButtonState(Button btn){
        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            btn.setPressed(true);
        } else {
            if(!MouseListener.isDragging()) {
                btn.runAction();
            }
        }
    }

    /**
     * @desc detecta los eventos de teclado para las cajas de texto. Si apretamos TAB selecciona otra posible caja
     *       de texto (por defecto el tabIndex del formulario es 0, en el cual seria el primer TextBox creado).
     *       Por ultimo, escucha los eventos de teclado para la caja de texto seleccionada, es decir, cuando vamos a
     *       escribir algo en la caja de texto seleccionada.
     */
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

    /**
     * @desc Recorre todos los TextBoxes, checkea si hicimos click en algun TextBox y cambia al tabIndex selecionado.
     *       Si tenemos el mouse por encima de algun TextBox cambia el cursor.
     *       En caso de hacer click en uno, activa el estado de "selected" del objeto TextBox (desactivando tambien
     *       el anterior) y cambia el tabIndex seleccionado en el formulario.
     *
     */
    public void checkMouseTextBoxes() {
        boolean insideTxt = false; // bandera para checkear si el mouse se encuentra dentro de algun TextBox

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

    /**
     * @desc Recorre toda nuestra lista de TextBoxes y las renderiza.
     *       Se utiliza para la funcion render definida en la subclase.
     */
    protected void renderTextBoxes(){
        for (TextBox txt: txtList) {
            txt.render();
        }
    }

    /**
     * @desc Recorre toda nuestra lista de Botones y las renderiza.
     *       Se utiliza para la funcion render definida en la subclase.
     */
    protected void renderButtons(){
        for (Button btn : buttonList) {
            btn.render();
        }
    }

    /**
     * @desc Cierra nuestro formulario, sacando su visiblidad y elimina la textura de fondo.
     */
    public void close() {
        this.visible = false;
        this.background.clear();
    }

    /**
     * @return Getter del atributo visible.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @desc Setter del atributo visible.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}
