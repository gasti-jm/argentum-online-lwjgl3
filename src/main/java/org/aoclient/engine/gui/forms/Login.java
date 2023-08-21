package org.aoclient.engine.gui.forms;

public class Login extends Form{
    private static Login instance;

    private Login() {
        super("VentanaConectar.png");
    }

    public static Login get(){
        if (instance == null){
            instance = new Login();
        }

        return instance;
    }

    @Override
    public void render() {

    }

    @Override
    public void checkButtons() {

    }

    @Override
    public void close() {
        super.close();
    }

}
