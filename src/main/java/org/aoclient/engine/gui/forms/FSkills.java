package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import org.aoclient.engine.game.models.E_Skills;

import java.io.IOException;

import static org.aoclient.engine.Sound.SND_CLICK;
import static org.aoclient.engine.Sound.playSound;
import static org.aoclient.network.protocol.Protocol.writeModifySkills;
import static org.aoclient.network.protocol.Protocol.writeRequestSkills;

/**
 * Formulario para la visualizacion y gestion de habilidades (skills) del personaje.
 */

public final class FSkills extends Form {

    private String hoverSkillDescription = "";
    private final int[] userSkills = USER.getSkills().clone();
    private final int userFreeSkillsPoints = USER.getFreeSkillPoints();

    public FSkills() {
        try {
            backgroundImage = loadTexture("VentanaSkills");
            writeRequestSkills();
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
        ImGui.text(String.valueOf(USER.getFreeSkillPoints()));

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
            this.rollbackChanges();
            playSound(SND_CLICK);
            this.close();
        }

        ImGui.setCursorPos(460, 410);
        if (ImGui.button("Aceptar", 97, 24)) {
            this.saveChanges();
            playSound(SND_CLICK);
            this.close();
        }

        ImGui.end();
    }

    private boolean isLeftColumn(E_Skills skill) {
        return skill.getValue() <= (E_Skills.values().length / 2);
    }

    private void drawSkillInput(E_Skills skill) {
        byte spacing = 10;
        if (ImGui.arrowButton("FSkill_" + skill.getValue() + "_sub", 0)) {
            this.minusSkill(skill.getValue());
        }
        ImGui.sameLine(0, spacing);
        ImGui.text(String.valueOf(USER.getSkill(skill.getValue())));
        ImGui.sameLine(0, spacing);
        if (ImGui.arrowButton("FSkill_" + skill.getValue() + "_add", 1)) {
            this.plusSkill(skill.getValue());
        }
    }

    private void drawSkills() {
        int leftY = 30;
        int rightY = 30;
        byte rowHeight = 24;
        for (E_Skills skill : E_Skills.values()) {
            if (this.isLeftColumn(skill)) {
                ImGui.setCursorPos(220, leftY += rowHeight);
            } else {
                ImGui.setCursorPos(480, rightY += rowHeight);
            }
            this.drawSkillInput(skill);
        }
    }

    private void drawSkillsHovers() {
        this.hoverSkillDescription = "";
        int leftY = 30;
        int rightY = 30;
        int rowHeight = 24;
        for (E_Skills skill : E_Skills.values()) {
            if (this.isLeftColumn(skill)) {
                ImGui.setCursorPos(40, leftY += rowHeight);
            } else {
                ImGui.setCursorPos(320, rightY += rowHeight);
            }
            ImGui.invisibleButton("FSkill_" + skill.getValue() + "_hover", 132, 18);
            if (ImGui.isItemHovered()) {
                this.hoverSkillDescription = skill.getDescription();
            }
        }
    }

    private void plusSkill(int skill) {
        int freeSkillPts = USER.getFreeSkillPoints();
        if (freeSkillPts > 0 && USER.getSkill(skill) + 1 <= 100) {
            USER.setSkill(skill, USER.getSkill(skill) + 1);
            USER.setFreeSkillPoints(freeSkillPts - 1);
        }
    }

    private void minusSkill(int skill) {
        int result = (USER.getSkill(skill) - 1);
        if (result >= this.userSkills[skill - 1]) {
            USER.setSkill(skill, USER.getSkill(skill) - 1);
            USER.setFreeSkillPoints(USER.getFreeSkillPoints() + 1);
        }
    }

    private void rollbackChanges() {
        USER.setFreeSkillPoints(this.userFreeSkillsPoints);
        USER.setSkills(this.userSkills);
    }

    private void saveChanges() {
        int[] modifiedSkills = new int[E_Skills.values().length];
        for (E_Skills skill : E_Skills.values()) {
            int skillIndex = skill.getValue() - 1;
            modifiedSkills[skillIndex] = USER.getSkill(skill.getValue()) - this.userSkills[skillIndex];
        }
        writeModifySkills(modifiedSkills);
    }
}