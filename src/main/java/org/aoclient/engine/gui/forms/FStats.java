package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.*;

import java.io.IOException;

import static org.aoclient.engine.Sound.SND_CLICK;
import static org.aoclient.engine.Sound.playSound;
import static org.aoclient.network.protocol.Protocol.*;

public class FStats extends Form {
    private final int backgroundImage;

    public FStats() {
        try {
            backgroundImage = loadTexture("VentanaEstadisticas");
            writeRequestAttributes();
            writeRequestFame();
            writeRequestSkills();
            writeRequestMiniStats();
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
        drawSkills();
        drawStatistics();

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

    private void drawReputations() {
        short y = 158;
        for (E_Reputation reputation : E_Reputation.values()) {
            if (reputation.ordinal() == E_Reputation.LADRON.ordinal()) continue;
            ImGui.setCursorPos(100, y += 17);
            ImGui.text(String.valueOf(User.get().getReputations()[reputation.ordinal()]));
        }
        ImGui.setCursorPos(100, y += 17);
        ImGui.text(User.get().isCriminal() ? "Criminal" : "Ciudadano");
    }

    private void drawSkills() {
        float y = 40;
        for (int skill : User.get().getSkills()) {
            ImGui.setCursorPos(320, y += 17.25f);
            ImGui.text(String.valueOf(skill));
        }
    }

    private void drawStatistics() {
        float y = 300;
        for (E_KillCounters counter : E_KillCounters.values()) {
            ImGui.setCursorPos(160, y += 15.5f);
            ImGui.text(String.valueOf(User.get().getKillCounter((byte) counter.ordinal())));
        }
        ImGui.setCursorPos(65, y += 15);
        ImGui.text(E_Roles.values()[User.get().getRole()].name());
        ImGui.setCursorPos(160, y += 15);
        ImGui.text(String.valueOf(User.get().getJailTime()));
    }
}
