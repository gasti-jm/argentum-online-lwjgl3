package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;

import java.io.IOException;

import static org.aoclient.engine.Sound.SND_CLICK;
import static org.aoclient.engine.Sound.playSound;

public class FTutorial extends Form {

    public FTutorial() {
        try {
            this.backgroundImage = loadTexture("VentanaTutorial");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render() {
        ImGui.setNextWindowSize(583, 509, ImGuiCond.Always);
        ImGui.begin(this.getClass().getSimpleName(),
                ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize
                        | ImGuiWindowFlags.NoDecoration | ImGuiWindowFlags.NoBackground);

        ImGui.setCursorPos(5, 0);
        ImGui.image(backgroundImage, 583, 509);

        drawButtons();

        ImGui.end();
    }

    private void drawButtons() {

        ImGui.setCursorPos(562, 4);
        if (ImGui.invisibleButton("close", 17, 17)) {
            playSound(SND_CLICK);
            close();
        }

        ImGui.setCursorPos(416, 468);
        if (ImGui.invisibleButton("siguiente", 17, 17)) {
            playSound(SND_CLICK);
        }

        ImGui.setCursorPos(63, 468);
        if (ImGui.invisibleButton("anterior", 17, 17)) {
            playSound(SND_CLICK);
        }

    }

}
