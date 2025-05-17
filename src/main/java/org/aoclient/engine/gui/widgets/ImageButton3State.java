package org.aoclient.engine.gui.widgets;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;

/**
 * Utilidad para dibujar un botón con 3 estados gráficos (normal, rollover, click) usando ImGui.
 * Permite centralizar la lógica visual y simplificar el código de los formularios.
 */
public class ImageButton3State {
    private final int normalId;
    private final int hoverId;
    private final int clickId;
    private final float width, height;
    private final int x, y;

    /**
     * Crea un botón gráfico de 3 estados.
     * @param normalId ID de textura para el estado normal
     * @param hoverId ID de textura para el estado rollover
     * @param clickId ID de textura para el estado click
     * @param x Posición X relativa en la ventana ImGui
     * @param y Posición Y relativa en la ventana ImGui
     * @param width Ancho del botón
     * @param height Alto del botón
     */
    public ImageButton3State(int normalId, int hoverId, int clickId, int x, int y, float width, float height) {
        this.normalId = normalId;
        this.hoverId = hoverId;
        this.clickId = clickId;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Dibuja el botón en la posición y tamaño especificados.
     * @return true si el botón fue clickeado
     */
    public boolean render() {
        ImGui.setCursorPos(x, y);
        ImGui.pushStyleColor(ImGuiCol.Button, 0f, 0f, 0f, 0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0f, 0f, 0f, 0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0f, 0f, 0f, 0f);
        ImGui.pushStyleVar(ImGuiStyleVar.FrameBorderSize, 0f);

        boolean clicked = ImGui.imageButton(normalId, width, height);
        if (ImGui.isItemActive()) {
            ImGui.setCursorPos(x, y);
            ImGui.image(clickId, width, height);
            if (ImGui.isMouseClicked(0)) {
                ImGui.popStyleVar();
                ImGui.popStyleColor(3);
                return true;
            }
        } else if (ImGui.isItemHovered()) {
            ImGui.setCursorPos(x, y);
            ImGui.image(hoverId, width, height);
            if (ImGui.isMouseClicked(0)) {
                ImGui.popStyleVar();
                ImGui.popStyleColor(3);
                return true;
            }
        } else if (clicked) {
            ImGui.popStyleVar();
            ImGui.popStyleColor(3);
            return true;
        }

        ImGui.popStyleVar();
        ImGui.popStyleColor(3);
        return false;
    }
}
