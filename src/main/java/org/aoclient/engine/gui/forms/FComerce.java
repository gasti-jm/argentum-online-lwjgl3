package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import org.aoclient.engine.game.inventory.NPCInventory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import static org.aoclient.network.Protocol.writeCommerceEnd;

public class FComerce extends Form {
    public static NPCInventory invNPC = new NPCInventory();

    public FComerce() {

        try {
            this.backgroundImage = loadTexture("VentanaComercio");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render() {
        ImGui.setNextWindowSize(462, 486, ImGuiCond.Always);
        ImGui.begin(this.getClass().getSimpleName(), ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoDecoration |
                ImGuiWindowFlags.NoBackground | ImGuiWindowFlags.NoMove);

        ImGui.setCursorPos(5, 0);
        ImGui.image(backgroundImage, 462, 486);

        ImGui.setCursorPos(405, 24);
        if (ImGui.invisibleButton("Close", 30, 30)) {
            writeCommerceEnd();
            this.close();
        }

        invNPC.drawInventory();

        ImGui.end();
    }
}
