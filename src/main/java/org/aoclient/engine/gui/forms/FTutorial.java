package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;

import java.io.IOException;

import static org.aoclient.engine.Sound.SND_CLICK;
import static org.aoclient.engine.Sound.playSound;

public class FTutorial extends Form {

    // IDs de textura para los botones siguiente (normal, rollover y click)
    private int botonSiguienteTextureId;
    private int botonSiguienteRolloverTextureId;
    private int botonSiguienteClickTextureId;
    // IDs de textura para el botón anterior (normal, rollover y click)
    private int botonAnteriorTextureId;
    private int botonAnteriorRolloverTextureId;
    private int botonAnteriorClickTextureId;

    public FTutorial() {
        try {
            this.backgroundImage = loadTexture("VentanaTutorial");
            // Carga las texturas de los botones siguiente
            this.botonSiguienteTextureId = loadTexture("BotonSiguienteTutorial");
            this.botonSiguienteRolloverTextureId = loadTexture("BotonSiguienteRolloverTutorial");
            this.botonSiguienteClickTextureId = loadTexture("BotonSiguienteClickTutorial");
            // Carga las texturas de los botones anterior
            this.botonAnteriorTextureId = loadTexture("BotonAnteriorTutorial");
            this.botonAnteriorRolloverTextureId = loadTexture("BotonAnteriorRolloverTutorial");
            this.botonAnteriorClickTextureId = loadTexture("BotonAnteriorClickTutorial");
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

        // --- Botón siguiente con imagen y 3 estados ---
        ImGui.setCursorPos(416, 468);
        float width = 97, height = 24;
        ImGui.pushStyleColor(ImGuiCol.Button, 0f, 0f, 0f, 0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0f, 0f, 0f, 0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0f, 0f, 0f, 0f);
        ImGui.pushStyleVar(ImGuiStyleVar.FrameBorderSize, 0f);

        int textureIdToUse = botonSiguienteTextureId;
        boolean clicked = ImGui.imageButton(textureIdToUse, width, height);
        if (ImGui.isItemActive()) {
            ImGui.setCursorPos(419, 470); // Ajusta si es necesario
            ImGui.image(botonSiguienteClickTextureId, width, height);
            if (ImGui.isMouseClicked(0)) {
                playSound(SND_CLICK);
            }
        } else if (ImGui.isItemHovered()) {
            ImGui.setCursorPos(419, 470); // Ajusta si es necesario
            ImGui.image(botonSiguienteRolloverTextureId, width, height);
            if (ImGui.isMouseClicked(0)) {
                playSound(SND_CLICK);
            }
        } else if (clicked) {
            playSound(SND_CLICK);
        }

        ImGui.popStyleVar();
        ImGui.popStyleColor(3);

        // --- Botón anterior con imagen y 3 estados ---
        ImGui.setCursorPos(63, 468);
        float widthAnterior = 97, heightAnterior = 24;
        ImGui.pushStyleColor(ImGuiCol.Button, 0f, 0f, 0f, 0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0f, 0f, 0f, 0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0f, 0f, 0f, 0f);
        ImGui.pushStyleVar(ImGuiStyleVar.FrameBorderSize, 0f);

        int anteriorTextureIdToUse = botonAnteriorTextureId;
        boolean anteriorClicked = ImGui.imageButton(anteriorTextureIdToUse, widthAnterior, heightAnterior);
        if (ImGui.isItemActive()) {
            ImGui.setCursorPos(66, 470); // Ajusta si es necesario
            ImGui.image(botonAnteriorClickTextureId, widthAnterior, heightAnterior);
            if (ImGui.isMouseClicked(0)) {
                playSound(SND_CLICK);
            }
        } else if (ImGui.isItemHovered()) {
            ImGui.setCursorPos(66, 470); // Ajusta si es necesario
            ImGui.image(botonAnteriorRolloverTextureId, widthAnterior, heightAnterior);
            if (ImGui.isMouseClicked(0)) {
                playSound(SND_CLICK);
            }
        } else if (anteriorClicked) {
            playSound(SND_CLICK);
        }

        ImGui.popStyleVar();
        ImGui.popStyleColor(3);
    }
}
