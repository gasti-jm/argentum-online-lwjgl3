package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.E_Attributes;

import java.io.IOException;

import static org.aoclient.engine.Sound.SND_CLICK;
import static org.aoclient.engine.Sound.playSound;
import static org.aoclient.network.protocol.Protocol.writeRequestAttributes;

public class FStats extends Form {
    private final int backgroundImage;

    public FStats() {
        try {
            backgroundImage = loadTexture("VentanaEstadisticas");
            writeRequestAttributes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render() {
        ImGui.setNextWindowSize(466, 447, ImGuiCond.Always);
        ImGui.begin(this.getClass().getSimpleName(), ImGuiWindowFlags.NoTitleBar
                | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoDecoration | ImGuiWindowFlags.NoMove);

        ImGui.setWindowFocus();
        ImGui.setCursorPos(0, 0);
        ImGui.image(backgroundImage, 466, 447);

        drawAttributes();

        ImGui.setCursorPos(12, 416);
        if (ImGui.button("Cerrar", 438, 24)) {
            playSound(SND_CLICK);
            close();
        }

        ImGui.end();
    }

    private void drawAttributes() {
        short y = 45;
        for (E_Attributes attributes : E_Attributes.values()) {
            ImGui.setCursorPos(150, y += 15);
            ImGui.text(String.valueOf(User.get().getAttributes()[attributes.ordinal()]));
        }
    }
}
