package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.ImString;
import imgui.enums.ImGuiCond;
import imgui.enums.ImGuiInputTextFlags;
import imgui.enums.ImGuiWindowFlags;
import org.aoclient.connection.SocketConnection;
import org.aoclient.engine.game.User;
import org.aoclient.engine.gui.ImGUISystem;

import static org.aoclient.connection.Protocol.writeLoginExistingChar;
import static org.aoclient.engine.utils.GameData.options;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;

public final class FConnect extends Form {
    private final ImString portStr = new ImString(options.getPortServer());
    private final ImString ipStr = new ImString(options.getIpServer());
    private final ImString nickStr = new ImString(options.getNickName());
    private final ImString passStr = new ImString();

    public FConnect(){
        this.formName = "frmConnect";
    }

    @Override
    public void render() {
        ImGui.setNextWindowSize(250, 135, ImGuiCond.Once);

        ImGui.begin(formName, ImGuiWindowFlags.NoTitleBar
                | ImGuiWindowFlags.NoResize
                | ImGuiWindowFlags.NoBringToFrontOnFocus);

        ImGui.inputText("Port", portStr, ImGuiInputTextFlags.CallbackResize);
        ImGui.inputText("IP Server", ipStr, ImGuiInputTextFlags.CallbackResize);

        ImGui.separator();

        ImGui.inputText("Nickname", nickStr, ImGuiInputTextFlags.CallbackResize);
        ImGui.inputText("Password", passStr, ImGuiInputTextFlags.Password | ImGuiInputTextFlags.CallbackResize);

        ImGui.separator();

        if(ImGui.button("Create Character")) {
            close();
        }

        ImGui.sameLine();

        if(ImGui.button("Connect") || ImGui.isKeyPressed(GLFW_KEY_ENTER)) {
            options.setIpServer(ipStr.get());
            options.setPortServer(portStr.get());

            if (nickStr.get().isEmpty() || !passStr.get().isEmpty()) {
                SocketConnection.get().connect(options.getIpServer(), options.getPortServer());
                writeLoginExistingChar(nickStr.get(), passStr.get());

                options.setNickName(nickStr.get());
                User.get().setUserName(nickStr.get());
            } else {
                ImGUISystem.get().checkAddOrChange("frmMessage",
                        new FMessage("Por favor, ingrese un nombre de usuario y/o contraseña valida."));
            }
        }

        ImGui.end();
    }

}
