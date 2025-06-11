package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import org.aoclient.engine.gui.widgets.ImageButton3State;

import java.io.IOException;

import static org.aoclient.engine.Sound.SND_CLICK;
import static org.aoclient.engine.Sound.playSound;

public class FGuildAdm extends Form {
    private String[] guildsList = new String[0];
    private int selectedGuildIndex = -1;
    
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
        ImGui.setNextWindowSize(271, 370, ImGuiCond.Always);
        ImGui.begin(this.getClass().getSimpleName(), ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoDecoration |
                ImGuiWindowFlags.NoBackground | ImGuiWindowFlags.NoMove);

        ImGui.setCursorPos(5, 0);
        ImGui.image(backgroundImage, 271, 370);
        
        // Guild List
        ImGui.setCursorPos(33, 38);
        ImGui.beginChild("guildList", 205, 236, true);
        for (int i = 0; i < guildsList.length; i++) {
            String guild = guildsList[i];
            if (guild != null && !guild.trim().isEmpty()) {
                boolean isSelected = (i == selectedGuildIndex);
                if (ImGui.selectable(guild, isSelected)) {
                    selectedGuildIndex = i;
                }
                // Resaltar el elemento seleccionado
                if (isSelected) {
                    ImGui.setItemDefaultFocus();
                }
            }
        }
        ImGui.endChild();

        drawButtons();

        ImGui.end();

    }

    private void drawButtons() {
        if (btnClose.render()) {
            playSound(SND_CLICK);
            this.close();
        }

        if (btnDetails.render()) {
            playSound(SND_CLICK);
        }
    }


}
