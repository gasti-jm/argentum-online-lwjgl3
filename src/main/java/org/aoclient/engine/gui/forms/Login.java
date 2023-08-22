package org.aoclient.engine.gui.forms;

import org.aoclient.connection.SocketConnection;
import org.aoclient.engine.Engine;
import org.aoclient.engine.gui.elements.Button;

import java.util.ArrayList;
import java.util.List;

import static org.aoclient.connection.Protocol.writeLoginExistingChar;
import static org.aoclient.engine.Engine.closeClient;
import static org.aoclient.engine.Engine.forms;

public class Login extends Form{
    private static Login instance;

    private static final int BTN_SIZE_WIDTH = 89;
    private static final int BTN_SIZE_HEIGHT = 25;

    private List<Button> buttonsLogin;



    private Login() {

    }

    public void init() {
        this.background.init("VentanaConectar.png");

        this.buttonsLogin = new ArrayList<>();

        // btnConectar
        buttonsLogin.add(addButton(320, 264, () -> {
            SocketConnection.getInstance().connect();
            writeLoginExistingChar();
        }, "BotonConectarse.jpg", "BotonConectarseRollover.jpg", "BotonConectarseClick.jpg"));

        // btnTeclas
        buttonsLogin.add(addButton(408, 264, () -> {
            // nothing to do yet...
        }, "BotonTeclas.jpg", "BotonTeclasRollover.jpg", "BotonTeclasClick.jpg"));

        // btnCrearPJ
        buttonsLogin.add(addButton(40, 560, () -> {
            // nothing to do yet...
        }, "BotonCrearPersonajeConectar.jpg",
                    "BotonCrearPersonajeRolloverConectar.jpg",
                    "BotonCrearPersonajeClickConectar.jpg"));

        // btnRecuperar
        buttonsLogin.add(addButton(144, 560, () -> {
                    // nothing to do yet...
                }, "BotonRecuperarPass.jpg",
                "BotonRecuperarPassRollover.jpg",
                "BotonRecuperarPassClick.jpg"));

        // btnManual
        buttonsLogin.add(addButton(248, 560, () -> {
                    // nothing to do yet...
                }, "BotonManual.jpg",
                "BotonManualRollover.jpg",
                "BotonManualClick.jpg"));

        // btnReglamento
        buttonsLogin.add(addButton(352, 560, () -> {
                    // nothing to do yet...
                }, "BotonReglamento.jpg",
                "BotonReglamentoRollover.jpg",
                "BotonReglamentoClick.jpg"));

        // btnCodigoFuente
        buttonsLogin.add(addButton(456, 560, () -> {
                    // nothing to do yet...
                }, "BotonCodigoFuente.jpg",
                "BotonCodigoFuenteRollover.jpg",
                "BotonCodigoFuenteClick.jpg"));

        // btnBorrarPJ
        buttonsLogin.add(addButton(560, 560, () -> {
                    // nothing to do yet...
                }, "BotonBorrarPersonaje.jpg",
                "BotonBorrarPersonajeRollover.jpg",
                "BotonBorrarPersonajeClick.jpg"));

        // btnBorrarPJ
        buttonsLogin.add(addButton(664, 560, Engine::closeClient,
                "BotonSalirConnect.jpg",
                "BotonBotonSalirRolloverConnect.jpg",
                "BotonSalirClickConnect.jpg"));

    }

    private Button addButton(int x, int y, Runnable action, String... textures) {
        Button btn = new Button(x, y, BTN_SIZE_WIDTH, BTN_SIZE_HEIGHT);
        btn.setAction(action);
        btn.loadTextures(textures);

        return btn;
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

        for (Button btn : buttonsLogin) {
            btn.render();
        }
    }

    @Override
    public void checkButtons() {
        if(!forms.isEmpty()) return;

        for(Button btn: buttonsLogin) {
            setButtonState(btn);
        }
    }


    @Override
    public void close() {
        super.close();
        buttonsLogin.clear();
    }

}
