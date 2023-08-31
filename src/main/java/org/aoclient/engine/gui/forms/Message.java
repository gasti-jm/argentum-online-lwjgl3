package org.aoclient.engine.gui.forms;

import org.aoclient.engine.gui.elements.Button;
import org.aoclient.engine.gui.elements.Label;
import org.aoclient.engine.listeners.MouseListener;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

/**
 * Formulario frmMensaje.
 * Contiene constantes de posicion y tamaño para el formulario y algunos de sus elementos de GUI.
 *
 * SE RECOMIENDA CREAR ESTE OBJETO EN EL ARRAYLIST PUBLICO DE "forms" PARA QUE SE PUEDA DIBUJAR EN CUALQUIER ESCENA..
 */
public class Message extends Form {
    private static final int LBL_MSG_POS_X = 16;
    private static final int LBL_MSG_POS_Y = 32;
    private static final int LBL_SIZE_WIDTH = 233;
    private static final int LBL_SIZE_HEIGHT = 137;

    private static final int BTN_MSG_POS_X = 48;
    private static final int BTN_MSG_POS_Y = 179;
    private static final int BTN_SIZE_WIDTH = 177;
    private static final int BTN_SIZE_HEIGHT = 25;

    private final Label msg;
    private final Button accept;

    public Message(String msg) {
        this.background.init("VentanaMsj.jpg");
        this.loadPositionAttributes();
        this.visible = true;

        this.msg = new Label(msg, fPosX + LBL_MSG_POS_X, fPosY + LBL_MSG_POS_Y, LBL_SIZE_WIDTH, LBL_SIZE_HEIGHT, true, false);
        this.accept = new Button(fPosX + BTN_MSG_POS_X, fPosY + BTN_MSG_POS_Y, BTN_SIZE_WIDTH, BTN_SIZE_HEIGHT);
        this.accept.loadTextures("BotonCerrarMsj.jpg", "BotonCerrarRolloverMsj.jpg", "BotonCerrarClickMsj.jpg");
        this.accept.setAction(this::close);
    }

    /**
     * @desc Actualiza el estado del boton (segun lo que hagamos con el mouse) y en caso de hacerle click completamente
     *       realiza su accion.
     */
    @Override
    public void checkButtons() {
        setButtonState(accept);
    }

    /**
     * @desc Dibuja nuestros componentes del frm.
     */
    @Override
    public void render() {
        if (!visible) return; // Sino esta visible no dibujamos, ya que puede llamarse al momento de cerrar.

        this.background.render();
        this.msg.render();
        this.accept.render();
    }

    /**
     * @desc Cerramos el formulario.
     */
    @Override
    public void close() {
        super.close();
        background.clear();
        msg.clear();
        accept.clear();
    }

}
