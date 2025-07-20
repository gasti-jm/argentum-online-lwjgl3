package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import org.aoclient.engine.game.inventory.NPCInventory;
import org.aoclient.engine.game.inventory.UserInventory;

import java.io.IOException;

import static org.aoclient.engine.audio.Sound.SND_CLICK;
import static org.aoclient.engine.audio.Sound.playSound;
import static org.aoclient.network.protocol.Protocol.*;

public final class FBank extends Form {

    public static NPCInventory invNPC = new NPCInventory(true);
    public static UserInventory invUser = USER.getUserInventory().clone();
    public static int goldDeposited;
    private final ImString cant = new ImString("1");
    private final ImString cantGOLD = new ImString("1");
    private String lblName = "";
    private String lblMin = "";
    private String lblMax = "";

    public FBank(int goldDeposited) {
        FBank.goldDeposited = goldDeposited;

        try {
            this.backgroundImage = loadTexture("Boveda");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        invUser.transformInvComerce(true);
    }

    @Override
    public void render() {
        ImGui.setNextWindowFocus(); // dale foco solo a este FRM

        ImGui.setNextWindowSize(461, 530, ImGuiCond.Always);
        ImGui.begin(this.getClass().getSimpleName(), ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoDecoration |
                ImGuiWindowFlags.NoBackground | ImGuiWindowFlags.NoMove);

        this.checkMoveFrm();

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
            bankEnd();
            this.close();
        }

        ImGui.setCursorPos(229, 232);
        if (ImGui.invisibleButton("Depositar", 17, 17)) {
            playSound(SND_CLICK);
            if (!cant.isEmpty()) {
                bankDeposit(invUser.getSlotSelected() + 1, Integer.parseInt(cant.get()));
            }

        }

        ImGui.setCursorPos(229, 296);
        if (ImGui.invisibleButton("Retirar", 17, 17)) {
            playSound(SND_CLICK);

            if (!cant.isEmpty()) {
                bankExtractItem(invNPC.getSlotSelected() + 1, Integer.parseInt(cant.get()));
            }
        }

        ImGui.setCursorPos(109, 63);
        if (ImGui.invisibleButton("DepositarGOLD", 62, 70)) {
            playSound(SND_CLICK);

            if (!cantGOLD.isEmpty()) {
                depositGold(Integer.parseInt(cantGOLD.get()));
            }
        }

        ImGui.setCursorPos(318, 63);
        if (ImGui.invisibleButton("RetirarGOLD", 63, 51)) {
            playSound(SND_CLICK);

            if (!cantGOLD.isEmpty()) {
                extractGold(Integer.parseInt(cantGOLD.get()));
            }
        }

        ImGui.setCursorPos(155, 432);
        ImGui.text(lblName);

        ImGui.setCursorPos(155, 466 - 5);
        ImGui.text(lblMax);

        ImGui.setCursorPos(155, 483 - 5);
        ImGui.text(lblMin);


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
                updateLabels(false);

            }
        }

        if (invUser.inInventoryArea(localPos.x, localPos.y)) {
            if (ImGui.isMouseClicked(0)) { // click izq
                invUser.clickInventory(localPos.x, localPos.y);
                updateLabels(true);
            }
        }
    }

    private void updateLabels(boolean userInv) {
        if (userInv) {
            if (invUser.getObjType(invUser.getSlotSelected()) == null) {
                lblName = "";
                lblMin = "";
                lblMax = "";
                return;
            }

            lblName = invUser.getItemName(invUser.getSlotSelected());

            switch (invUser.getObjType(invUser.getSlotSelected())) {
                case WEAPON:
                case ARROW:
                    lblMax = "Máx Golpe: " + invUser.getMaxHit(invUser.getSlotSelected());
                    lblMin = "Min Golpe: " + invUser.getMinHit(invUser.getSlotSelected());
                    break;

                case ARMOR:
                case SHIELD:
                case HELMET:
                    lblMax = "Máx Defensa: " + invUser.getMaxDef(invUser.getSlotSelected());
                    lblMin = "Min Defensa: " + invUser.getMinDef(invUser.getSlotSelected());
                    break;

                default:
                    lblMin = "";
                    lblMax = "";
            }

        } else { // npc inv
            if (invNPC.getObjType(invNPC.getSlotSelected()) == null) {
                lblName = "";
                lblMin = "";
                lblMax = "";
                return;
            }


            lblName = invNPC.getItemName(invNPC.getSlotSelected());

            switch (invNPC.getObjType(invNPC.getSlotSelected())) {
                case WEAPON:
                case ARROW:
                    lblMax = "Máx Golpe:" + invNPC.getMaxHit(invNPC.getSlotSelected());
                    lblMin = "Min Golpe:" + invNPC.getMinHit(invNPC.getSlotSelected());
                    break;

                case ARMOR:
                case SHIELD:
                case HELMET:
                    lblMax = "Máx Defensa:" + invNPC.getMaxDef(invNPC.getSlotSelected());
                    lblMin = "Min Defensa:" + invNPC.getMinDef(invNPC.getSlotSelected());
                    break;

                default:
                    lblMin = "";
                    lblMax = "";
            }

        }
    }
}
