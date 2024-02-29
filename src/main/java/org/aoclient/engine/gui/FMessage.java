package org.aoclient.engine.gui;

import imgui.ImGui;
import imgui.enums.ImGuiCond;
import imgui.enums.ImGuiWindowFlags;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;

public final class FMessage extends FormGUI {
    private final String txtMessage;

    public FMessage() {
        this.formName = "frmMessage";
        this.txtMessage = "";
    }

    public FMessage(String text) {
        this.formName = "frmMessage";
        this.txtMessage = text;
    }

    @Override
    public void render() {
        ImGui.setNextWindowSize(250, 230, ImGuiCond.Always);

        // Start Custom window
        ImGui.begin(formName, ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.AlwaysAutoResize);

        ImGui.textWrapped(this.txtMessage);

        ImGui.setCursorPos(8, ImGui.getWindowHeight() - 40);
        ImGui.separator();

        ImGui.setCursorPos(8, ImGui.getWindowHeight() - 30);
        if(ImGui.button("Aceptar", ImGui.getWindowWidth() - 16, 20) || ImGui.isKeyPressed(GLFW_KEY_ENTER)) {
            close();
        }

        ImGui.end();
    }
}
