package org.aoclient.engine.gui.forms;

import org.aoclient.connection.SocketConnection;
import org.aoclient.engine.Engine;
import org.aoclient.engine.gui.elements.Button;
import org.aoclient.engine.gui.elements.TextBox;
import org.aoclient.engine.renderer.RGBColor;

import java.util.ArrayList;
import java.awt.Desktop;
import java.net.URI;

import static org.aoclient.connection.Protocol.writeLoginExistingChar;
import static org.aoclient.engine.Sound.playMusic;
import static org.aoclient.engine.utils.GameData.options;


/**
 * Este seria la clase que define nuestro formulario "frmConectar".
 * Contiene elementos de GUI.
 */
public class FrmLogin extends Form{
    private static FrmLogin instance;

    private static final int TXT_USERNAME_INDEX = 0;
    private static final int TXT_PASSWORD_INDEX = 1;
    private static final int BTN_SIZE_WIDTH = 89;
    private static final int BTN_SIZE_HEIGHT = 25;

    /**
     * @desc: Constructor privado por singleton.
     */
    private FrmLogin() {

    }

    /**
     *
     * @return Mismo objeto (Patron de diseño Singleton)
     */
    public static FrmLogin get(){

        if (instance == null){
            instance = new FrmLogin();
        }

        return instance;
    }

    /**
     * Como tenemos un singleton, necesitamos de una funcion init para incializar nuestros atributos de la clase.
     */
    public void init() {
        this.visible = true;
        this.background.init("VentanaConectar.png");
        this.loadTxtBoxes();

        this.buttonList = new ArrayList<>();

        // btnConectar
        buttonList.add(new Button(320, 264, BTN_SIZE_WIDTH, BTN_SIZE_HEIGHT, () -> {
            SocketConnection.getInstance().connect();
            writeLoginExistingChar(txtList.get(TXT_USERNAME_INDEX).getText(), txtList.get(TXT_PASSWORD_INDEX).getText());
            options.setName(txtList.get(TXT_USERNAME_INDEX).getText());
        }, "BotonConectarse.jpg", "BotonConectarseRollover.jpg", "BotonConectarseClick.jpg"));

        // btnTeclas
        buttonList.add(new Button(408, 264, BTN_SIZE_WIDTH, BTN_SIZE_HEIGHT, () -> {
            // nothing to do yet...
        }, "BotonTeclas.jpg", "BotonTeclasRollover.jpg", "BotonTeclasClick.jpg"));

        // btnCrearPJ
        buttonList.add(new Button(40, 560, BTN_SIZE_WIDTH, BTN_SIZE_HEIGHT, () -> {
            FrmCreateCharacter.get().init();
            FrmCreateCharacter.get().setVisible(true);
            playMusic("7.ogg");
        }, "BotonCrearPersonajeConectar.jpg",
                    "BotonCrearPersonajeRolloverConectar.jpg",
                    "BotonCrearPersonajeClickConectar.jpg"));

        // btnRecuperar
        buttonList.add(new Button(144, 560, BTN_SIZE_WIDTH, BTN_SIZE_HEIGHT, () -> {
                    // nothing to do yet...
                }, "BotonRecuperarPass.jpg",
                "BotonRecuperarPassRollover.jpg",
                "BotonRecuperarPassClick.jpg"));

        // btnManual
        buttonList.add(new Button(248, 560, BTN_SIZE_WIDTH, BTN_SIZE_HEIGHT, () -> {

                this.abrirURL(("http://wiki.argentumonline.org/"));

                }, "BotonManual.jpg",
                "BotonManualRollover.jpg",
                "BotonManualClick.jpg"));

        // btnReglamento
        buttonList.add(new Button(352, 560, BTN_SIZE_WIDTH, BTN_SIZE_HEIGHT, () -> {

                this.abrirURL(("http://wiki.argentumonline.org/reglamento.html"));

                }, "BotonReglamento.jpg",
                "BotonReglamentoRollover.jpg",
                "BotonReglamentoClick.jpg"));

        // btnCodigoFuente
        buttonList.add(new Button(456, 560, BTN_SIZE_WIDTH, BTN_SIZE_HEIGHT, () -> {

                this.abrirURL("https://github.com/gasti-jm/argentum-online-lwjgl3");

                }, "BotonCodigoFuente.jpg",
                "BotonCodigoFuenteRollover.jpg",
                "BotonCodigoFuenteClick.jpg"));

        // btnBorrarPJ
        buttonList.add(new Button(560, 560, BTN_SIZE_WIDTH, BTN_SIZE_HEIGHT, () -> {
                    // nothing to do yet...
                }, "BotonBorrarPersonaje.jpg",
                "BotonBorrarPersonajeRollover.jpg",
                "BotonBorrarPersonajeClick.jpg"));

        // btnBorrarPJ
        buttonList.add(new Button(664, 560, BTN_SIZE_WIDTH, BTN_SIZE_HEIGHT, Engine::closeClient,
                "BotonSalirConnect.jpg",
                "BotonBotonSalirRolloverConnect.jpg",
                "BotonSalirClickConnect.jpg"));

    }

    /**
     * @desc Inicializa el tabIndex del formulario y nuestra lista de TextBoxes, agregando tambien los definidos.
     *       No es necesario crear esta funcion.. Es mas que nada para dividir un poco la funcion "init", y podemos hacer
     *       una funcion similar si tenemos muchos botones o algun otro componente.
     */
    private void loadTxtBoxes() {
        this.txtList = new ArrayList<>();
        this.tabIndexSelected = 0;
        txtTabIndexsAdded = 0;

        // txtUserName
        txtList.add(new TextBox(txtTabIndexsAdded++, options.getName(),327, 214, 164, 15, true, false));

        // txtPass
        txtList.add(new TextBox(txtTabIndexsAdded++, 327, 248, 164, 15, true, false, true));

        // txtPort
        txtList.add(new TextBox(txtTabIndexsAdded++, "7666", 326, 184, 55, 13,
                true, false, true, new RGBColor(0.0f, 1.0f, 0.0f)));

        // txtIP
        txtList.add(new TextBox(txtTabIndexsAdded++, "127.0.0.1", 384, 184, 105, 13,
                true, false, true, new RGBColor(0.0f, 1.0f, 0.0f)));


        txtList.get(tabIndexSelected).setSelected(true);
    }

    private void abrirURL(String url) {
        // Verifica si Desktop es compatible con la plataforma actual
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                // Crea una instancia de URI desde la URL
                URI uri = new URI(url);
                // Abrimos la URL en el navegador predeterminado
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("La apertura de URL no es compatible en esta plataforma.");
        }
    }

    /**
     * @desc Renderiza el contenido de nuestro formulario.
     */
    @Override
    public void render() {
        if(!visible) return;

        this.background.render();
        this.renderTextBoxes();
        this.renderButtons();
    }

    /**
     * @desc Cierra y elimina nuestro formulario.
     */
    @Override
    public void close() {
        super.close();
        this.txtList.clear();
        this.buttonList.clear();
    }

    /**
     *
     * @return Texto que contiene nuestro "txtUsername"
     */
    public String getUsername() {
        return txtList.get(TXT_USERNAME_INDEX).getText();
    }

    /**
     *
     * @return Texto que contiene nuestro "txtPassword"
     */
    public String getPassword() {
        return txtList.get(TXT_PASSWORD_INDEX).getText();
    }

}
