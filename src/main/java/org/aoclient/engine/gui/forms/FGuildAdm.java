package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import org.aoclient.engine.gui.widgets.ImageButton3State;

import java.io.IOException;

import static org.aoclient.engine.Sound.SND_CLICK;
import static org.aoclient.engine.Sound.playSound;

public class FGuildAdm extends Form {

    private String[] guildsList = new String[0];
    private int selectedGuildIndex = -1;

    private ImString filterText = new ImString(100);
    
    // Botones gráficos de 3 estados
    private ImageButton3State btnClose;
    private ImageButton3State btnDetails;

    public FGuildAdm() {
        try {
            this.backgroundImage = loadTexture("VentanaListaClanes");

            // Instanciación de botones con 3 estados (usa los tamaños y posiciones existentes)
            btnClose = new ImageButton3State(
                    loadTexture("BotonCerrarListaClanes"),
                    loadTexture("BotonCerrarRolloverListaClanes"),
                    loadTexture("BotonCerrarClickListaClanes"),
                    32, 335, 57, 25);

            btnDetails = new ImageButton3State(
                    loadTexture("BotonDetallesListaClanes"),
                    loadTexture("BotonDetallesRolloverListaClanes"),
                    loadTexture("BotonDetallesClickListaClanes"),
                    152, 335, 89, 25);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setGuildsList(String[] guilds) {
        if (guilds != null) {
            this.guildsList = guilds;
        }
    }

    @Override
    public void render() {
        ImGui.setNextWindowFocus(); // le damos foco.
        ImGui.setNextWindowSize(280, 370, ImGuiCond.Always);
        ImGui.begin(this.getClass().getSimpleName(), ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoDecoration |
                ImGuiWindowFlags.NoBackground | ImGuiWindowFlags.NoMove);

        this.checkMoveFrm();

        ImGui.setCursorPos(5, 0);
        ImGui.image(backgroundImage, 271, 370);

        drawButtons();

        ImGui.end();

    }

    private void drawButtons() {

        // Campo de texto para filtrar
        ImGui.setCursorPos(38, 310);
        ImGui.setNextItemWidth(207);
        ImGui.inputText("##filter", filterText, ImGuiInputTextFlags.None);

        // Guild List (filtrada)
        ImGui.setCursorPos(38, 38);
        ImGui.beginChild("guildList", 205, 236, true);

        String filter = filterText.get().toLowerCase();

        for (int i = 0; i < guildsList.length; i++) {
            String guild = guildsList[i];
            if (guild != null && !guild.trim().isEmpty()) {
                if (!filter.isEmpty() && !guild.toLowerCase().contains(filter)) {
                    continue;
                }

                boolean isSelected = (i == selectedGuildIndex);
                if (ImGui.selectable(guild, isSelected)) {
                    selectedGuildIndex = i;
                }
                if (isSelected) {
                    ImGui.setItemDefaultFocus();
                }
            }
        }
        ImGui.endChild();

        if (btnClose.render()) {
            playSound(SND_CLICK);
            this.close();
        }

        if (btnDetails.render()) {
            playSound(SND_CLICK);
        }
    }


}
