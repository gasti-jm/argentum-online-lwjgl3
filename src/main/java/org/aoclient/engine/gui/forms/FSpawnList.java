package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImInt;
import org.aoclient.engine.game.User;
import org.aoclient.network.protocol.Protocol;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;

public final class FSpawnList extends Form {

    private String[] CreatureList;
    private ImInt selectedIndex = new ImInt(0);

    public FSpawnList(String[] creatureList) {
        super();
        this.CreatureList = creatureList;
    }

    @Override
    public void render() {
        ImGui.setNextWindowSize(220, 250, ImGuiCond.Always);
        ImGui.begin(this.getClass().getSimpleName(),ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.AlwaysAutoResize);

        ImGui.setCursorPos(5, 0);

        drawButtons();

        ImGui.end();
    }

    private void drawButtons() {

        ImGui.textWrapped("Selecciona la criatura:");
        ImGui.separator();

        // Mostrar lista de criaturas
        if (CreatureList != null && CreatureList.length > 0) {
            if (ImGui.beginListBox("##creatureList", -1, 170)) {
                for (int i = 0; i < CreatureList.length; i++) {
                    boolean isSelected = (selectedIndex.get() == i);
                    if (ImGui.selectable(CreatureList[i], isSelected)) {
                        selectedIndex.set(i);
                    }
                    if (isSelected) {
                        ImGui.setItemDefaultFocus();
                    }
                }
                ImGui.endListBox();
            }
        } else {
            ImGui.text("No hay criaturas disponibles.");
        }
        
        if (ImGui.button("Invocar", ImGui.getWindowWidth() - 16, 20) || ImGui.isKeyPressed(GLFW_KEY_ENTER)) {
            Protocol.spawnCreature((short) (selectedIndex.get() + 1));
        }
        if (ImGui.button("Salir", ImGui.getWindowWidth() - 16, 20) || ImGui.isKeyPressed(GLFW_KEY_ENTER)) {
            close();
        }
    }

}
