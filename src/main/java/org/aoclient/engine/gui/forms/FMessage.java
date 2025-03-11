package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;


import java.nio.charset.StandardCharsets;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;

public final class FMessage extends Form {
    private final String txtMessage;

    public FMessage() {
        this.txtMessage = "";
    }

    public FMessage(String text) {
        this.txtMessage = new String(text.getBytes(), StandardCharsets.UTF_8);
    }

    @Override
    public void render() {

        ImGui.setNextWindowSize(250, 230, ImGuiCond.Always);

        // Start Custom window
        ImGui.begin(this.getClass().getSimpleName(), ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.AlwaysAutoResize);

        ImGui.textWrapped(this.txtMessage);

        ImGui.setCursorPos(8, ImGui.getWindowHeight() - 40);
        ImGui.separator();

        ImGui.setCursorPos(8, ImGui.getWindowHeight() - 30);
        if(ImGui.button("Aceptar", ImGui.getWindowWidth() - 16, 20) || ImGui.isKeyPressed(GLFW_KEY_ENTER)) close();

        ImGui.end();

    }

}
