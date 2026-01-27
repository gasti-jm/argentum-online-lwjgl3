package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import org.aoclient.engine.game.models.Skill;

import java.io.IOException;

import static org.aoclient.engine.audio.Sound.SND_CLICK;
import static org.aoclient.engine.audio.Sound.playSound;
import static org.aoclient.network.protocol.Protocol.modifySkills;
import static org.aoclient.network.protocol.Protocol.requestSkills;

/**
 * Formulario para la visualizacion y gestion de habilidades (skills) del personaje.
 */

public final class FSkills extends Form {

    private String hoverSkillDescription = "";
    private final int[] userSkills = USER.getSkills().clone();
    private final int[] editSkills = USER.getSkills().clone();
    private final int userFreeSkillsPoints = USER.getFreeSkillPoints();
    private int editFreeSkillPoints = USER.getFreeSkillPoints();
    private boolean visibleButtonSave;

    public FSkills() {
        this.visibleButtonSave = false;

        try {
            backgroundImage = loadTexture("VentanaSkills");
            requestSkills();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render() {
        ImGui.setNextWindowSize(600, 450, ImGuiCond.Always);
        ImGui.begin(this.getClass().getSimpleName(), ImGuiWindowFlags.NoTitleBar
                | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoDecoration | ImGuiWindowFlags.NoMove);

        this.checkMoveFrm();

        ImGui.setWindowFocus();
        ImGui.setCursorPos(0, 0);
        ImGui.image(backgroundImage, 600, 450);

        ImGui.setCursorPos(320, 15);
        ImGui.text(String.valueOf(editFreeSkillPoints));

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

        if (visibleButtonSave) {
            ImGui.setCursorPos(460, 410);
            if (ImGui.button("Guardar", 97, 24)) {
                this.saveChanges();
                playSound(SND_CLICK);
                this.close();
            }
        }

        ImGui.end();
    }

    private boolean isLeftColumn(Skill skill) {
        return skill.getId() <= (Skill.values().length / 2);
    }

    private void drawSkillInput(Skill skill) {
        byte spacing = 10;
        if (ImGui.arrowButton("FSkill_" + skill.getId() + "_sub", 0)) {
            this.minusSkill(skill.getId());
        }
        ImGui.sameLine(0, spacing);
        ImGui.text(String.valueOf(editSkills[skill.getId() - 1]));
        ImGui.sameLine(0, spacing);
        if (ImGui.arrowButton("FSkill_" + skill.getId() + "_add", 1)) {
            this.plusSkill(skill.getId());
        }
    }

    private void drawSkills() {
        int leftY = 30;
        int rightY = 30;
        byte rowHeight = 24;
        for (Skill skill : Skill.values()) {
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
        for (Skill skill : Skill.values()) {
            if (this.isLeftColumn(skill)) {
                ImGui.setCursorPos(40, leftY += rowHeight);
            } else {
                ImGui.setCursorPos(320, rightY += rowHeight);
            }
            ImGui.invisibleButton("FSkill_" + skill.getId() + "_hover", 132, 18);
            if (ImGui.isItemHovered()) {
                this.hoverSkillDescription = skill.getDescription();
            }
        }
    }

    private void plusSkill(int skill) {
        if (editFreeSkillPoints > 0 && editSkills[skill - 1] < 100) {
            this.visibleButtonSave = true;
            editSkills[skill - 1]++;
            editFreeSkillPoints--;
        }
    }

    private void minusSkill(int skill) {
        if (editSkills[skill - 1] > userSkills[skill - 1]) {
            this.visibleButtonSave = true;
            editSkills[skill - 1]--;
            editFreeSkillPoints++;
        }
    }

    private void saveChanges() {
        int[] modifiedSkills = new int[Skill.values().length];
        for (Skill skill : Skill.values()) {
            int skillIndex = skill.getId() - 1;
            modifiedSkills[skillIndex] = editSkills[skillIndex] - this.userSkills[skillIndex];
        }
        modifySkills(modifiedSkills);
    }
}