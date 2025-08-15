package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImInt;
import org.aoclient.network.protocol.Protocol;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;

public class FMSG extends Form {

    private String[] MSGList;
    private ImInt selectedIndex = new ImInt(0);

    public FMSG(String[] msgList) {
        super();
        this.MSGList = msgList;
    }

    @Override
    public void render() {
        ImGui.setNextWindowSize(220, 300, ImGuiCond.Always);
        ImGui.begin(this.getClass().getSimpleName(), ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.AlwaysAutoResize);

        ImGui.setCursorPos(5, 0);

        drawButtons();

        ImGui.end();
    }

    private void drawButtons() {
        ImGui.textWrapped("Selecciona el Mensaje:");
        ImGui.separator();

        // Construir lista filtrada (solo entradas no vacias)
        int[] filteredIdx = new int[MSGList == null ? 0 : MSGList.length];
        String[] filteredTxt = new String[MSGList == null ? 0 : MSGList.length];
        int count = 0;
        if (MSGList != null) {
            for (int i = 0; i < MSGList.length; i++) {
                String t = MSGList[i];
                if (t != null && !t.trim().isEmpty()) {
                    filteredIdx[count] = i;        // indice original
                    filteredTxt[count] = t.trim(); // texto visible
                    count++;
                }
            }
        }

        if (count == 0) {
            ImGui.text("No hay mensajes disponibles.");
            if (ImGui.button("Salir", ImGui.getWindowWidth() - 16, 20)) {
                close();
            }
            return;
        }

        // Ajustar seleccion a rango de la lista filtrada
        if (selectedIndex.get() >= count) selectedIndex.set(Math.max(0, count - 1));

        if (ImGui.beginListBox("##messageList", -1f, 170f)) {
            for (int fi = 0; fi < count; fi++) {
                String itemText = filteredTxt[fi];
                String label = itemText + "##msg" + filteredIdx[fi]; // ID unico por indice original
                boolean isSelected = (selectedIndex.get() == fi);
                if (ImGui.selectable(label, isSelected)) {
                    selectedIndex.set(fi);
                }
                if (isSelected) {
                    ImGui.setItemDefaultFocus();
                }
            }
            ImGui.endListBox();
        }

        if (ImGui.button("Ir", ImGui.getWindowWidth() - 16, 20)) {
            int sel = Math.max(0, Math.min(selectedIndex.get(), count - 1));
            int origIdx = filteredIdx[sel];
            String raw = MSGList[origIdx];
            String name = extractNameFromLine(raw);
            if (!name.isEmpty()) {
                Protocol.teleportToPlayer(name);
            }
        }
        if (ImGui.button("Traer", ImGui.getWindowWidth() - 16, 20)) {
            int sel = Math.max(0, Math.min(selectedIndex.get(), count - 1));
            int origIdx = filteredIdx[sel];
            String raw = MSGList[origIdx];
            String name = extractNameFromLine(raw);
            if (!name.isEmpty()) {
                Protocol.summon(name);
            }
        }
        if (ImGui.button("Borrar", ImGui.getWindowWidth() - 16, 20)) {
            int sel = Math.max(0, Math.min(selectedIndex.get(), count - 1));
            int origIdx = filteredIdx[sel];
            String raw = MSGList[origIdx]; // e.g. "13:47:25 BETATESTER"
            String name = extractNameFromLine(raw);
            if (!name.isEmpty()) {
                Protocol.SOSRemove(name);
                // eliminar elemento de la lista y ajustar seleccion (basado en indice original)
                MSGList = removeAt(MSGList, origIdx);
                // recalcular cuantos quedan no vacios para ajustar seleccion
                int newCount = 0;
                if (MSGList != null) {
                    for (String t : MSGList) if (t != null && !t.trim().isEmpty()) newCount++;
                }
                if (newCount == 0) {
                    selectedIndex.set(0);
                } else {
                    selectedIndex.set(Math.min(sel, newCount - 1));
                }
            }
        }
        if (ImGui.button("Salir", ImGui.getWindowWidth() - 16, 20)) {
            close();
        }
    }

    // Extrae el nombre de una línea con formato "HH:mm:ss NOMBRE".
    private String extractNameFromLine(String text) {
        if (text == null) return "";
        String trimmed = text.trim();
        if (trimmed.isEmpty()) return "";
        String[] parts = trimmed.split("\\s+", 2);
        if (parts.length == 2) {
            return parts[1].trim();
        }
        return trimmed; // fallback por si no hay hora
    }

    private String[] removeAt(String[] arr, int index) {
        if (arr == null || arr.length == 0 || index < 0 || index >= arr.length) return new String[0];
        String[] out = new String[arr.length - 1];
        for (int i = 0, j = 0; i < arr.length; i++) {
            if (i == index) continue;
            out[j++] = arr[i];
        }
        return out;
    }

}
