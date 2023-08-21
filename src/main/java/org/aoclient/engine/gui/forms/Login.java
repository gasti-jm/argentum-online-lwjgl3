package org.aoclient.engine.gui.forms;

import org.aoclient.connection.SocketConnection;
import org.aoclient.engine.gui.elements.Button;

import static org.aoclient.connection.Protocol.writeLoginExistingChar;
import static org.aoclient.engine.Engine.forms;

public class Login extends Form{
    private static Login instance;

    private Button btnConnect;

    private Login() {

    }

    public void init() {
        this.background.init("VentanaConectar.png");

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

        setButtonState(btnConnect);
    }


    @Override
    public void close() {
        super.close();
        btnConnect.clear();
    }

}
