package org.aoclient.engine.gui.forms;

import org.aoclient.engine.Window;
import org.aoclient.engine.gui.elements.Button;
import org.aoclient.engine.gui.elements.ImageGUI;
import org.aoclient.engine.gui.elements.Label;
import org.aoclient.engine.listeners.MouseListener;

public class Message extends Form {
    private static final int LBL_MSG_POS_X = 16;
    private static final int LBL_MSG_POS_Y = 32;
    private static final int LBL_SIZE_WIDTH = 233;
    private static final int LBL_SIZE_HEIGHT = 137;

    private static final int BTN_MSG_POS_X = 48;
    private static final int BTN_MSG_POS_Y = 179;
    private static final int BTN_SIZE_WIDTH = 177;
    private static final int BTN_SIZE_HEIGHT = 25;

    private final ImageGUI background;
    private final Label msg;
    private final Button accept;

    public Message(String msg) {
        this.background = new ImageGUI("VentanaMsj.jpg");

        // despues de cargar una imagen de fondo establecemos un size al frm
        this.frmPosX -= this.background.getWidth() / 2;
        this.frmPosY -= this.background.getHeight() / 2;
        this.frmHeight = this.background.getHeight();
        this.frmWidth = this.background.getWidth();


        this.background.setX(frmPosX);
        this.background.setY(frmPosY);

        this.msg = new Label(msg, frmPosX + LBL_MSG_POS_X, frmPosY + LBL_MSG_POS_Y, LBL_SIZE_WIDTH, LBL_SIZE_HEIGHT);
        this.accept = new Button(frmPosX + BTN_MSG_POS_X, frmPosY + BTN_MSG_POS_Y, BTN_SIZE_WIDTH, BTN_SIZE_HEIGHT);
        this.accept.loadTextures("BotonCerrarMsj.jpg", "BotonCerrarRolloverMsj.jpg", "BotonCerrarClickMsj.jpg");
        this.accept.setAction(this::close);
    }

    public void checkButtons() {
        accept.runAction();
    }

    @Override
    public void render() {
        this.background.render();
        this.msg.render();
        this.accept.render();
    }

    @Override
    public void close() {
        super.close();
        background.clear();
    }
}
