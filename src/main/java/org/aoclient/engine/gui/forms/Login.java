package org.aoclient.engine.gui.forms;

import org.aoclient.connection.SocketConnection;
import org.aoclient.engine.Engine;
import org.aoclient.engine.gui.elements.Button;
import org.aoclient.engine.gui.elements.TextBox;
import org.aoclient.engine.listeners.KeyListener;
import org.aoclient.engine.renderer.RGBColor;

import java.util.ArrayList;
import java.util.List;

import static org.aoclient.connection.Protocol.writeLoginExistingChar;
import static org.aoclient.engine.Engine.forms;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;

public class Login extends Form{
    private static Login instance;

    private static final int BTN_SIZE_WIDTH = 89;
    private static final int BTN_SIZE_HEIGHT = 25;


    private List<Button> buttonsLogin;
    private List<TextBox> textBoxesLogin;


    private Login() {

    }

    public static Login get(){
        if (instance == null){
            instance = new Login();
        }

        return instance;
    }

    public void init() {
        this.background.init("VentanaConectar.png");

        this.loadTxtBoxes();

        this.buttonsLogin = new ArrayList<>();
        // btnConectar
        buttonsLogin.add(addButton(320, 264, () -> {
            SocketConnection.getInstance().connect();
            writeLoginExistingChar(textBoxesLogin.get(0).getText(), textBoxesLogin.get(1).getText());
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

    // OBLIGATORIO PARA LOS TEXTS BOX!
    private void loadTxtBoxes() {
        this.textBoxesLogin = new ArrayList<>();
        this.tabIndexSelected = 0;
        txtTabIndexsAdded = 0;

        // txtUserName
        textBoxesLogin.add(addTextBox(327, 214, 164, 15, true, false, false,
                new RGBColor(1.0f, 1.0f, 1.0f)));

        // txtPass
        textBoxesLogin.add(addTextBox(327, 248, 164, 15, true, false, true,
                new RGBColor(1.0f, 1.0f, 1.0f)));

        textBoxesLogin.get(tabIndexSelected).setSelected(true);
    }

    private Button addButton(int x, int y, Runnable action, String... textures) {
        Button btn = new Button(x, y, BTN_SIZE_WIDTH, BTN_SIZE_HEIGHT);
        btn.setAction(action);
        btn.loadTextures(textures);

        return btn;
    }

    private TextBox addTextBox(int x, int y, int w, int h, boolean b, boolean i, boolean hide, RGBColor color) {
        return new TextBox(txtTabIndexsAdded++, x, y, w, h, b, i, hide, color);
    }

    @Override
    public void render() {
        this.background.render();

        for (TextBox txt: textBoxesLogin) {
            txt.render();
        }

        for (Button btn : buttonsLogin) {
            btn.render();
        }
    }

    @Override
    public void checkButtons() {
        for(Button btn: buttonsLogin) {
            setButtonState(btn);
        }
    }

    // OBLIGATORIO ESTA LOGICA PARA LOS TEXTBOXES EN OTROS FORMULARIOS
    public void checkMouseTextBoxs() {
        for (TextBox txt: textBoxesLogin) {
            final int selected = txt.checkSelected();
            if (selected > -1) {
                textBoxesLogin.get(tabIndexSelected).setSelected(false);
                tabIndexSelected = selected;
                textBoxesLogin.get(selected).setSelected(true);
            }
        }
    }

    public void checkKeyTextBoxs() {
        if(KeyListener.isKeyReadyForAction(GLFW_KEY_TAB)) {
            if (tabIndexSelected < txtTabIndexsAdded - 1) {
                textBoxesLogin.get(tabIndexSelected).setSelected(false);
                tabIndexSelected++;
            } else {
                tabIndexSelected = 0;
            }

            textBoxesLogin.get(tabIndexSelected).setSelected(true);
        }

        for (TextBox txt: textBoxesLogin) {
            txt.keyEvents();
        }
    }

    @Override
    public void close() {
        super.close();

        textBoxesLogin.clear();
        buttonsLogin.clear();
    }

    public String getUsername() {
        return textBoxesLogin.get(0).getText();
    }

    public String getPassword() {
        return textBoxesLogin.get(1).getText();
    }
}
