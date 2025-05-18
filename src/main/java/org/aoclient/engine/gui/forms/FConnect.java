package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import org.aoclient.engine.Engine;
import org.aoclient.engine.Window;
import org.aoclient.engine.game.User;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.widgets.ImageButton3State;
import org.aoclient.network.SocketConnection;

import java.io.IOException;

import static org.aoclient.engine.Sound.playMusic;
import static org.aoclient.engine.utils.GameData.options;
import static org.aoclient.network.protocol.Protocol.writeLoginExistingChar;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;

/**
 * Formulario de conexion al servidor.
 * <p>
 * La clase {@code FConnect} implementa la interfaz grafica que se muestra al usuario cuando inicia el cliente y necesita
 * conectarse al servidor. Este formulario es la puerta de entrada principal a la experiencia de juego, permitiendo a los usuarios
 * autenticarse con sus cuentas existentes.
 * <p>
 * El formulario proporciona las siguientes funcionalidades:
 * <ul>
 * <li>Campos para ingresar la direccion IP y puerto del servidor
 * <li>Campos para ingresar nombre de usuario y contraseña
 * <li>Boton para iniciar la conexion al servidor con las credenciales proporcionadas
 * <li>Boton para crear un nuevo personaje, dirigiendo al usuario a {@link FCreateCharacter}
 * <li>Botones adicionales para acceder a opciones como recuperacion de cuenta, manual del juego, reglas, codigo fuente y salida
 * del programa
 * </ul>
 * <p>
 * Esta clase maneja la validacion basica de campos y establece la comunicacion inicial con el servidor, enviando las credenciales
 * del usuario y procesando la respuesta para determinar si se permite el acceso. En caso de conexion exitosa, la aplicacion
 * avanzara a la pantalla principal.
 */

public final class FConnect extends Form {

    private final ImString ipStr = new ImString(options.getIpServer());
    private final ImString portStr = new ImString(options.getPortServer());
    private final ImString nickStr = new ImString(options.getNickName());
    private final ImString passStr = new ImString();

    // Botones gráficos de 3 estados
    private ImageButton3State btnConnect;
    private ImageButton3State btnCreateCharacter;
    private ImageButton3State btnRecovery;
    private ImageButton3State btnManual;
    private ImageButton3State btnRules;
    private ImageButton3State btnSourceCode;
    private ImageButton3State btnDeleteCharacter;
    private ImageButton3State btnExit;

    public FConnect() {
        try {
            this.backgroundImage = loadTexture("VentanaConectar");
            // Instanciación de botones con 3 estados (usa los tamaños y posiciones existentes)
            btnConnect = new ImageButton3State(
                loadTexture("BotonConectarse"),
                loadTexture("BotonConectarseRollover"),
                loadTexture("BotonConectarseClick"),
                    325, 264, 89, 25
            );
            btnCreateCharacter = new ImageButton3State(
                loadTexture("BotonCrearPersonajeConectar"),
                loadTexture("BotonCrearPersonajeRolloverConectar"),
                loadTexture("BotonCrearPersonajeClickConectar"),
                45, 561, 89, 25
            );
            btnRecovery = new ImageButton3State(
                loadTexture("BotonRecuperarPass"),
                loadTexture("BotonRecuperarPassRollover"),
                loadTexture("BotonRecuperarPassClick"),
                149, 561, 89, 25
            );
            btnManual = new ImageButton3State(
                loadTexture("BotonManual"),
                loadTexture("BotonManualRollover"),
                loadTexture("BotonManualClick"),
                253, 561, 89, 25
            );
            btnRules = new ImageButton3State(
                loadTexture("BotonReglamento"),
                loadTexture("BotonReglamentoRollover"),
                loadTexture("BotonReglamentoClick"),
                357, 561, 89, 25
            );
            btnSourceCode = new ImageButton3State(
                loadTexture("BotonCodigoFuente"),
                loadTexture("BotonCodigoFuenteRollover"),
                loadTexture("BotonCodigoFuenteClick"),
                461, 561, 89, 25
            );
            btnDeleteCharacter = new ImageButton3State(
                loadTexture("BotonBorrarPersonaje"),
                loadTexture("BotonBorrarPersonajeRollover"),
                loadTexture("BotonBorrarPersonajeClick"),
                565, 561, 89, 25
            );
            btnExit = new ImageButton3State(
                loadTexture("BotonSalirConnect"),
                loadTexture("BotonBotonSalirRolloverConnect"),
                loadTexture("BotonSalirClickConnect"),
                669, 561, 89, 25
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render() {
        ImGui.setNextWindowSize(Window.get().getWidth() + 10, Window.get().getHeight() + 5, ImGuiCond.Once);
        ImGui.setNextWindowPos(-5, -1, ImGuiCond.Once);

        // Start Custom window
        ImGui.begin(this.getClass().getSimpleName(), ImGuiWindowFlags.NoTitleBar |
                ImGuiWindowFlags.NoMove |
                ImGuiWindowFlags.NoFocusOnAppearing |
                ImGuiWindowFlags.NoDecoration |
                ImGuiWindowFlags.NoBackground |
                ImGuiWindowFlags.NoResize |
                ImGuiWindowFlags.NoSavedSettings |
                ImGuiWindowFlags.NoBringToFrontOnFocus);

        ImGui.getWindowDrawList().addImage(backgroundImage, 0, 0, Window.get().getWidth(), Window.get().getHeight());

        //txtPort
        ImGui.setCursorPos(329, 180);
        ImGui.pushItemWidth(58);
        ImGui.pushStyleColor(ImGuiCol.FrameBg, 0f, 0f, 0f, 1f);
        ImGui.pushStyleColor(ImGuiCol.Text, 0f, 1f, 0f, 1f);

        ImGui.pushID("Port");
        ImGui.inputText("", portStr, ImGuiInputTextFlags.CharsDecimal);
        ImGui.popID();

        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popItemWidth();

        //txtIP
        ImGui.setCursorPos(389, 180);
        ImGui.pushItemWidth(107);
        ImGui.pushStyleColor(ImGuiCol.FrameBg, 0f, 0f, 0f, 1f);
        ImGui.pushStyleColor(ImGuiCol.Text, 0f, 1f, 0f, 1f);

        ImGui.pushID("IP Server");
        ImGui.inputText("", ipStr, ImGuiInputTextFlags.CallbackResize);
        ImGui.popID();

        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popItemWidth();

        //txtNickname
        ImGui.setCursorPos(329, 214);
        ImGui.pushItemWidth(167);
        ImGui.pushStyleColor(ImGuiCol.FrameBg, 0, 0, 0, 1);

        ImGui.pushID("Nickname");
        ImGui.inputText("", nickStr, ImGuiInputTextFlags.CallbackResize);
        ImGui.popID();

        ImGui.popStyleColor();
        ImGui.popItemWidth();

        //txtPassword
        ImGui.setCursorPos(329, 247);
        ImGui.pushItemWidth(167);
        ImGui.pushStyleColor(ImGuiCol.FrameBg, 0, 0, 0, 1);

        ImGui.pushID("Password");
        ImGui.inputText("", passStr, ImGuiInputTextFlags.Password | ImGuiInputTextFlags.CallbackResize);
        ImGui.popID();

        ImGui.popStyleColor();
        ImGui.popItemWidth();

        // Botones gráficos de 3 estados
        if (btnConnect.render() || ImGui.isKeyPressed(GLFW_KEY_ENTER)) this.buttonConnect();
        if (btnCreateCharacter.render()) this.buttonCreateCharacter();
        if (btnRecovery.render()) {
            // Acción para recuperar contraseña (a implementar)
        }
        if (btnManual.render()) this.abrirURL("http://wiki.argentumonline.org/");
        if (btnRules.render()) this.abrirURL("http://wiki.argentumonline.org/reglamento.html");
        if (btnSourceCode.render()) this.abrirURL("https://github.com/gasti-jm/argentum-online-lwjgl3");
        if (btnDeleteCharacter.render()) {
            // Acción para borrar personaje (a implementar)
        }
        if (btnExit.render()) this.buttonExitGame();

        ImGui.end();
    }

    private void buttonConnect() {
        options.setIpServer(ipStr.get());
        options.setPortServer(portStr.get());
        if (!nickStr.get().isEmpty() && !passStr.get().isEmpty()) {
            new Thread(() -> {
                if (SocketConnection.getInstance().connect()) writeLoginExistingChar(nickStr.get(), passStr.get());
            }).start();
            options.setNickName(nickStr.get());
            User.get().setUserName(nickStr.get());
        } else ImGUISystem.get().show(new FMessage("Please enter a username and/or password."));
    }

    private void buttonCreateCharacter() {
        ImGUISystem.get().show(new FCreateCharacter());
        playMusic("7.ogg");

        btnConnect.delete();
        btnCreateCharacter.delete();
        btnExit.delete();
        btnManual.delete();
        btnRecovery.delete();
        btnDeleteCharacter.delete();
        btnRules.delete();
        btnSourceCode.delete();
        this.close();
    }

    private void buttonExitGame() {
        Engine.closeClient();
    }

}
