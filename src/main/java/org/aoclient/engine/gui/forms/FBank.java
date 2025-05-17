package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import org.aoclient.engine.game.User;
import org.aoclient.engine.game.inventory.NPCInventory;
import org.aoclient.engine.game.inventory.UserInventory;

import java.io.IOException;

import static org.aoclient.engine.Sound.SND_CLICK;
import static org.aoclient.engine.Sound.playSound;
import static org.aoclient.network.protocol.Protocol.*;

public class FBank extends Form{
    private final ImString cant = new ImString("1");
    public static NPCInventory invNPC = new NPCInventory(true);
    public static UserInventory invUser = User.get().getUserInventory().clone();


    public FBank() {
        try {
            this.backgroundImage = loadTexture("Boveda");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        invUser.transformInvComerce(true);
    }

    @Override
    public void render() {
        ImGui.setNextWindowSize(461, 530, ImGuiCond.Always);
        ImGui.begin(this.getClass().getSimpleName(), ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoDecoration |
                ImGuiWindowFlags.NoBackground | ImGuiWindowFlags.NoMove);

        ImGui.setCursorPos(5, 0);
        ImGui.image(backgroundImage, 461, 530);

        ImGui.setCursorPos(415, 28);
        if (ImGui.invisibleButton("Close", 30, 30)) {
            writeBankEnd();
            this.close();
        }

        ImGui.setCursorPos(229, 232);
        if (ImGui.invisibleButton("Depositar", 17, 17)) {
            playSound(SND_CLICK);
            writeBankDeposit(invUser.getSlotSelected() + 1, Integer.parseInt(cant.get()));
        }

        ImGui.setCursorPos(229, 296);

        if (ImGui.invisibleButton("Retirar", 17, 17)) {
            playSound(SND_CLICK);
            writeBankExtractItem(invNPC.getSlotSelected() + 1, Integer.parseInt(cant.get()));
        }

        ImGui.setCursorPos(218, 262);
        ImGui.pushItemWidth(42);
        ImGui.pushID("cantidad");
        ImGui.inputText("", cant, ImGuiInputTextFlags.CharsDecimal | ImGuiInputTextFlags.CallbackResize);
        ImGui.popID();
        ImGui.popItemWidth();

        this.checkInventoryEvents();

        invNPC.drawInventory();
        invUser.drawInventory();


        ImGui.end();
    }

    private void checkInventoryEvents() {
        ImVec2 mousePos = ImGui.getMousePos();      // posición global del mouse
        ImVec2 windowPos = ImGui.getWindowPos();    // posición global de la ventana actual
        ImVec2 localPos = new ImVec2(
                mousePos.x - windowPos.x,
                mousePos.y - windowPos.y
        );

        if (invNPC.inInventoryArea(localPos.x, localPos.y)) {
            if (ImGui.isMouseClicked(0)) { // click izq
                invNPC.clickInventory(localPos.x, localPos.y);
            }
        }

        if (invUser.inInventoryArea(localPos.x, localPos.y)) {
            if (ImGui.isMouseClicked(0)) { // click izq
                invUser.clickInventory(localPos.x, localPos.y);
            }
        }
    }
}
