package org.aoclient.engine.gui.forms;

import imgui.ImGui;

import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import org.aoclient.network.SocketConnection;
import org.aoclient.engine.Engine;
import org.aoclient.engine.Window;
import org.aoclient.engine.game.User;
import org.aoclient.engine.gui.ImGUISystem;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import static org.aoclient.engine.Sound.playMusic;
import static org.aoclient.engine.utils.GameData.options;
import static org.aoclient.network.Protocol.writeLoginExistingChar;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;

public final class FConnect extends Form {
    private final ImString portStr      = new ImString(options.getPortServer());
    private final ImString ipStr        = new ImString(options.getIpServer());
    private final ImString nickStr      = new ImString(options.getNickName());
    private final ImString passStr      = new ImString();

    public FConnect(){
        try {
            this.backgroundImage = loadTexture("VentanaConectar");
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
            ImGui.pushStyleColor(ImGuiCol.FrameBg, 0f, 0f,0f, 1f);
            ImGui.pushStyleColor(ImGuiCol.Text, 0f, 1f, 0f, 1f);

                ImGui.pushID("Port");
                    ImGui.inputText("", portStr,  ImGuiInputTextFlags.CharsDecimal);
                ImGui.popID();

            ImGui.popStyleColor();
            ImGui.popStyleColor();
        ImGui.popItemWidth();

        //txtIP
        ImGui.setCursorPos(389, 180);
        ImGui.pushItemWidth(107);
            ImGui.pushStyleColor(ImGuiCol.FrameBg, 0f, 0f,0f, 1f);
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
            ImGui.pushStyleColor(ImGuiCol.FrameBg, 0, 0,0, 1);

                ImGui.pushID("Nickname");
                    ImGui.inputText("", nickStr, ImGuiInputTextFlags.CallbackResize);
                ImGui.popID();

            ImGui.popStyleColor();
        ImGui.popItemWidth();

        //txtPassword
        ImGui.setCursorPos(329, 247);
        ImGui.pushItemWidth(167);
            ImGui.pushStyleColor(ImGuiCol.FrameBg, 0, 0,0, 1);

                ImGui.pushID("Password");
                    ImGui.inputText("", passStr, ImGuiInputTextFlags.Password | ImGuiInputTextFlags.CallbackResize);
                ImGui.popID();

            ImGui.popStyleColor();
        ImGui.popItemWidth();


        ImGui.setCursorPos(325, 266);
        if (ImGui.invisibleButton("Connect", 89, 25) || ImGui.isKeyPressed(GLFW_KEY_ENTER)) {
            this.buttonConnect();
        }

        ImGui.setCursorPos(45, 561);
        if (ImGui.invisibleButton("Create Character", 89, 25)) {
            this.buttonCreateCharacter();
        }

        ImGui.setCursorPos(149, 561);
        if (ImGui.invisibleButton("Recovery", 89, 25)) {
            // nothing to do yet..
        }

        ImGui.setCursorPos(253, 561);
        if (ImGui.invisibleButton("Manual", 89, 25)) {
            this.abrirURL("http://wiki.argentumonline.org/");
        }

        ImGui.setCursorPos(357, 561);
        if (ImGui.invisibleButton("Rules", 89, 25)) {
            this.abrirURL("http://wiki.argentumonline.org/reglamento.html");
        }

        ImGui.setCursorPos(461, 561);
        if (ImGui.invisibleButton("Source code", 89, 25)) {
            this.abrirURL("https://github.com/gasti-jm/argentum-online-lwjgl3");
        }

        ImGui.setCursorPos(565, 561);
        if (ImGui.invisibleButton("Delete Character", 89, 25)) {
            // nothing to do yet..
        }

        ImGui.setCursorPos(669, 561);
        if (ImGui.invisibleButton("Exit", 89, 25)) {
            this.buttonExitGame();
        }

        ImGui.end();
    }

    private void buttonConnect() {
        options.setIpServer(ipStr.get());
        options.setPortServer(portStr.get());

        if (nickStr.get().isEmpty() || !passStr.get().isEmpty()) {
            new Thread(() -> {
                if (SocketConnection.get().connect()) {
                    writeLoginExistingChar(nickStr.get(), passStr.get());
                }
            }).start();

            options.setNickName(nickStr.get());
            User.get().setUserName(nickStr.get());
        } else {
            ImGUISystem.get().show(new FMessage("Por favor, ingrese un nombre de usuario y/o contraseña valida."));
        }
    }

    private void buttonCreateCharacter() {
        ImGUISystem.get().show(new FCreateCharacter());
        playMusic("7.ogg");
        this.close();
    }

    private void buttonExitGame() {
        Engine.closeClient();
    }

}
