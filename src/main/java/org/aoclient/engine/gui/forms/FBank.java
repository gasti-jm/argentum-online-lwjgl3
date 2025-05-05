package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;

import java.io.IOException;

import static org.aoclient.network.protocol.Protocol.writeBankEnd;

public class FBank extends Form{

    public FBank() {
        try {
            this.backgroundImage = loadTexture("Boveda");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render() {
        ImGui.setNextWindowSize(461, 530, ImGuiCond.Always);
        ImGui.begin(this.getClass().getSimpleName(), ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoDecoration |
                ImGuiWindowFlags.NoBackground | ImGuiWindowFlags.NoMove);

        ImGui.setCursorPos(5, 0);
        ImGui.image(backgroundImage, 461, 530);

        ImGui.setCursorPos(415, 28);
        if (ImGui.invisibleButton("Close", 30, 30)) {
            writeBankEnd();
            this.close();
        }


        ImGui.end();
    }
}
