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

public final class FBank extends Form{
    private final ImString cant = new ImString("1");
    private final ImString cantGOLD = new ImString("1");
    public static NPCInventory invNPC = new NPCInventory(true);
    public static UserInventory invUser = User.get().getUserInventory().clone();
    private int goldDeposited;

    public FBank(int goldDeposited) {
        this.goldDeposited = goldDeposited;

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

        ImGui.setCursorPos(218, 262);
        ImGui.pushItemWidth(42);
        ImGui.pushID("cantidad");
        ImGui.inputText("", cant, ImGuiInputTextFlags.CharsDecimal | ImGuiInputTextFlags.CallbackResize);
        ImGui.popID();
        ImGui.popItemWidth();

        ImGui.setCursorPos(240, 94);
        ImGui.pushItemWidth(69);
        ImGui.pushID("cantidadGOLD");
        ImGui.inputText("", cantGOLD, ImGuiInputTextFlags.CharsDecimal | ImGuiInputTextFlags.CallbackResize);
        ImGui.popID();
        ImGui.popItemWidth();

        ImGui.setCursorPos(245, 63);
        ImGui.textColored(1, 1, 0.0f, 1, String.valueOf(goldDeposited));

        // BOTONES
        ImGui.setCursorPos(415, 28);
        if (ImGui.invisibleButton("Close", 30, 30)) {
            writeBankEnd();
            this.close();
        }

        ImGui.setCursorPos(229, 232);
        if (ImGui.invisibleButton("Depositar", 17, 17)) {
            playSound(SND_CLICK);
            if(!cant.isEmpty()) {
                writeBankDeposit(invUser.getSlotSelected() + 1, Integer.parseInt(cant.get()));
            }

        }

        ImGui.setCursorPos(229, 296);
        if (ImGui.invisibleButton("Retirar", 17, 17)) {
            playSound(SND_CLICK);

            if(!cant.isEmpty()) {
                writeBankExtractItem(invNPC.getSlotSelected() + 1, Integer.parseInt(cant.get()));
            }
        }

        ImGui.setCursorPos(109, 63);
        if (ImGui.invisibleButton("DepositarGOLD", 62, 70)) {
            playSound(SND_CLICK);

            if(!cantGOLD.isEmpty()) {
                writeBankDepositGold(Integer.parseInt(cantGOLD.get()));
            }
        }

        ImGui.setCursorPos(318, 63);
        if (ImGui.invisibleButton("RetirarGOLD", 63, 51)) {
            playSound(SND_CLICK);

            if(!cantGOLD.isEmpty()) {
                writeBankExtractGold(Integer.parseInt(cantGOLD.get()));
            }
        }

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
