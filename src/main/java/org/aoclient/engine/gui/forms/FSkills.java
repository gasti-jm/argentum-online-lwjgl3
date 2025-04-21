package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import org.aoclient.engine.game.models.E_Skills;

import java.io.IOException;

import static org.aoclient.engine.Sound.SND_CLICK;
import static org.aoclient.engine.Sound.playSound;

/**
 * Formulario para la visualizacion y gestion de habilidades (skills) del personaje.
 */

public class FSkills extends Form {

    private String hoverSkillDescription = "";

    public FSkills() {
        try {
            this.backgroundImage = loadTexture("VentanaSkills");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render() {
        ImGui.setNextWindowSize(600, 450, ImGuiCond.Always);
        ImGui.begin(this.getClass().getSimpleName(), ImGuiWindowFlags.NoTitleBar
                | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoDecoration | ImGuiWindowFlags.NoMove);

        ImGui.setWindowFocus();
        ImGui.setCursorPos(0, 0);
        ImGui.image(backgroundImage, 600, 450);

        ImGui.setCursorPos(320, 15);
        ImGui.text(String.valueOf(User.get().getFreeSkillPoints()));

        this.drawSkills();
        this.drawSkillsHovers();

        ImGui.setCursorPos(50, 330);
        ImGui.beginChild("SkillDescription", 520, 75);
        ImGui.pushTextWrapPos(ImGui.getCursorPosX() + 500);
        ImGui.textWrapped(this.hoverSkillDescription);
        ImGui.popTextWrapPos();
        ImGui.endChild();

        ImGui.setCursorPos(40, 410);
        if (ImGui.button("Cancelar", 97, 24)) {
            playSound(SND_CLICK);
            this.close();
        }

        ImGui.setCursorPos(460, 410);
        if (ImGui.button("Aceptar", 97, 24)) {
            playSound(SND_CLICK);
            this.close();
        }

        ImGui.end();
    }

    private void drawSkills() {
        int y = 30;
        int y2 = 30;
        for (E_Skills skill : E_Skills.values()) {
            if (skill.getValue() <= (E_Skills.values().length / 2)) {
                ImGui.setCursorPos(220, y += 24);
            } else {
                ImGui.setCursorPos(480, y2 += 24);
            }
            ImGui.arrowButton("FSkill_" + skill.getValue() + "_sub", 0);
            ImGui.sameLine(0, 10);
            ImGui.text(String.valueOf(skill.getValue()));
            ImGui.sameLine(0, 10);
            ImGui.arrowButton("FSkill_" + skill.getValue() + "_add", 1);
        }
    }

    private void drawSkillsHovers() {
        this.hoverSkillDescription = "";

        int y = 30;
        int y2 = 30;
        for (E_Skills skill : E_Skills.values()) {
            if (skill.getValue() <= (E_Skills.values().length / 2)) {
                ImGui.setCursorPos(40, y += 24);
            } else {
                ImGui.setCursorPos(320, y2 += 24);
            }
            ImGui.invisibleButton("FSkill_" + skill.getValue() + "_hover", 132, 18);
            if (ImGui.isItemHovered()) {
                this.hoverSkillDescription = skill.getDescription();
            }
        }
    }

}
