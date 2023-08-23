package org.aoclient.engine.gui.forms;

import org.aoclient.connection.SocketConnection;
import org.aoclient.engine.Engine;
import org.aoclient.engine.gui.elements.Button;
import org.aoclient.engine.gui.elements.TextBox;
import org.aoclient.engine.renderer.RGBColor;

import java.util.ArrayList;

import static org.aoclient.connection.Protocol.writeLoginExistingChar;

public class Login extends Form{
    private static Login instance;

    private static final int TXT_USERNAME_INDEX = 0;
    private static final int TXT_PASSWORD_INDEX = 1;
    private static final int BTN_SIZE_WIDTH = 89;
    private static final int BTN_SIZE_HEIGHT = 25;

    private Login() {

    }

    public static Login get(){
        if (instance == null){
            instance = new Login();
        }

        return instance;
    }

    public void init() {
        this.visible = true;
        this.background.init("VentanaConectar.png");
        this.loadTxtBoxes();

        this.buttonList = new ArrayList<>();

        // btnConectar
        buttonList.add(addButton(320, 264, BTN_SIZE_WIDTH, BTN_SIZE_HEIGHT, () -> {
            SocketConnection.getInstance().connect();
            writeLoginExistingChar(txtList.get(TXT_USERNAME_INDEX).getText(), txtList.get(TXT_PASSWORD_INDEX).getText());
        }, "BotonConectarse.jpg", "BotonConectarseRollover.jpg", "BotonConectarseClick.jpg"));

        // btnTeclas
        buttonList.add(addButton(408, 264, BTN_SIZE_WIDTH, BTN_SIZE_HEIGHT, () -> {
            // nothing to do yet...
        }, "BotonTeclas.jpg", "BotonTeclasRollover.jpg", "BotonTeclasClick.jpg"));

        // btnCrearPJ
        buttonList.add(addButton(40, 560, BTN_SIZE_WIDTH, BTN_SIZE_HEIGHT, () -> {
            CreateCharacter.get().init();
            CreateCharacter.get().setVisible(true);
        }, "BotonCrearPersonajeConectar.jpg",
                    "BotonCrearPersonajeRolloverConectar.jpg",
                    "BotonCrearPersonajeClickConectar.jpg"));

        // btnRecuperar
        buttonList.add(addButton(144, 560, BTN_SIZE_WIDTH, BTN_SIZE_HEIGHT, () -> {
                    // nothing to do yet...
                }, "BotonRecuperarPass.jpg",
                "BotonRecuperarPassRollover.jpg",
                "BotonRecuperarPassClick.jpg"));

        // btnManual
        buttonList.add(addButton(248, 560, BTN_SIZE_WIDTH, BTN_SIZE_HEIGHT, () -> {
                    // nothing to do yet...
                }, "BotonManual.jpg",
                "BotonManualRollover.jpg",
                "BotonManualClick.jpg"));

        // btnReglamento
        buttonList.add(addButton(352, 560, BTN_SIZE_WIDTH, BTN_SIZE_HEIGHT, () -> {
                    // nothing to do yet...
                }, "BotonReglamento.jpg",
                "BotonReglamentoRollover.jpg",
                "BotonReglamentoClick.jpg"));

        // btnCodigoFuente
        buttonList.add(addButton(456, 560, BTN_SIZE_WIDTH, BTN_SIZE_HEIGHT, () -> {
                    // nothing to do yet...
                }, "BotonCodigoFuente.jpg",
                "BotonCodigoFuenteRollover.jpg",
                "BotonCodigoFuenteClick.jpg"));

        // btnBorrarPJ
        buttonList.add(addButton(560, 560, BTN_SIZE_WIDTH, BTN_SIZE_HEIGHT, () -> {
                    // nothing to do yet...
                }, "BotonBorrarPersonaje.jpg",
                "BotonBorrarPersonajeRollover.jpg",
                "BotonBorrarPersonajeClick.jpg"));

        // btnBorrarPJ
        buttonList.add(addButton(664, 560, BTN_SIZE_WIDTH, BTN_SIZE_HEIGHT, Engine::closeClient,
                "BotonSalirConnect.jpg",
                "BotonBotonSalirRolloverConnect.jpg",
                "BotonSalirClickConnect.jpg"));

    }

    // OBLIGATORIO PARA LOS TEXTS BOX!
    private void loadTxtBoxes() {
        this.txtList = new ArrayList<>();
        this.tabIndexSelected = 0;
        txtTabIndexsAdded = 0;

        // txtUserName
        txtList.add(addTextBox(327, 214, 164, 15, true, false, false,
                new RGBColor(1.0f, 1.0f, 1.0f)));

        // txtPass
        txtList.add(addTextBox(327, 248, 164, 15, true, false, true,
                new RGBColor(1.0f, 1.0f, 1.0f)));

        txtList.get(tabIndexSelected).setSelected(true);
    }

    @Override
    public void render() {
        if(!visible) return;

        this.background.render();
        this.renderTextBoxes();
        this.renderButtons();
    }

    @Override
    public void close() {
        super.close();
        txtList.clear();
        buttonList.clear();
    }

    public String getUsername() {
        return txtList.get(TXT_USERNAME_INDEX).getText();
    }

    public String getPassword() {
        return txtList.get(TXT_PASSWORD_INDEX).getText();
    }
}
