package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.E_Attributes;
import org.aoclient.engine.game.models.E_Reputation;

import java.io.IOException;

import static org.aoclient.engine.Sound.SND_CLICK;
import static org.aoclient.engine.Sound.playSound;
import static org.aoclient.network.protocol.Protocol.writeRequestAttributes;
import static org.aoclient.network.protocol.Protocol.writeRequestFame;

public class FStats extends Form {
    private final int backgroundImage;

    public FStats() {
        try {
            backgroundImage = loadTexture("VentanaEstadisticas");
            writeRequestAttributes();
            writeRequestFame();
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
        drawReputations();

        ImGui.setCursorPos(12, 416);
        if (ImGui.button("Cerrar", 438, 24)) {
            playSound(SND_CLICK);
            close();
        }

        ImGui.end();
    }

    private void drawAttributes() {
        short y = 40;
        for (E_Attributes attributes : E_Attributes.values()) {
            ImGui.setCursorPos(100, y += 17);
            ImGui.text(String.valueOf(User.get().getAttributes()[attributes.ordinal()]));
        }
    }

    public void drawReputations() {
        short y = 158;
        for (E_Reputation reputation : E_Reputation.values()) {
            if (reputation.ordinal() == E_Reputation.LADRON.ordinal()) continue;
            ImGui.setCursorPos(100, y += 17);
            ImGui.text(String.valueOf(User.get().getReputations()[reputation.ordinal()]));
        }
        ImGui.setCursorPos(100, y += 17);
        ImGui.text(User.get().getIsCriminal() ? "Criminal" : "Ciudadano");
    }
}
