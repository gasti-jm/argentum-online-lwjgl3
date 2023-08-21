package org.aoclient.engine.gui.forms;

import org.aoclient.connection.SocketConnection;
import org.aoclient.engine.gui.elements.Button;
import org.aoclient.engine.listeners.MouseListener;

import static org.aoclient.connection.Protocol.writeLoginExistingChar;
import static org.aoclient.engine.Engine.forms;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class Login extends Form{
    private static Login instance;

    private Button btnConnect;

    private Login() {

    }

    public void init() {
        this.background.init("VentanaConectar.png");
        //this.loadParentAttributes();

        this.btnConnect = new Button(320, 264, 89, 25);
        this.btnConnect.loadTextures("BotonConectarse.jpg", "BotonConectarseRollover.jpg", "BotonConectarseClick.jpg");
        this.btnConnect.setAction( () -> {
            SocketConnection.getInstance().connect();
            writeLoginExistingChar();
        });
    }

    public static Login get(){
        if (instance == null){
            instance = new Login();
        }

        return instance;
    }

    @Override
    public void render() {
        this.background.render();
        this.btnConnect.render();
    }

    @Override
    public void checkButtons() {
        if(!forms.isEmpty()) return;

        if (MouseListener.mouseButtonClick(GLFW_MOUSE_BUTTON_LEFT)) {
            btnConnect.runAction();
        }
    }

    @Override
    public void close() {
        super.close();
        btnConnect.clear();
    }

}
