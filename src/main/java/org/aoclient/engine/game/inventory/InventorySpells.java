package org.aoclient.engine.game.inventory;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImInt;

import java.util.ArrayList;
import java.util.List;

public class InventorySpells {
    private static final int MAX_INVENTORY_SPELLS = 35;


    private List<String> spellsAdded = new ArrayList<>();
    private String[] spells;
    private final ImInt currentItemSpell = new ImInt(0);

    public InventorySpells() {
        this.spells = new String[MAX_INVENTORY_SPELLS];

        for (int i = 0; i < MAX_INVENTORY_SPELLS; i++) {
            spells[i] = "";
        }
    }

    public void draw() {

        ImGui.setNextWindowPos(595, 159);
        ImGui.setNextWindowSize(250, 250, ImGuiCond.Once);
        ImGui.begin("InventorySpells", ImGuiWindowFlags.NoTitleBar
                | ImGuiWindowFlags.NoBackground
                | ImGuiWindowFlags.NoResize
                | ImGuiWindowFlags.NoSavedSettings);

        ImGui.setCursorPos(5, 1);
        ImGui.pushID("spells");
        ImGui.listBox("", currentItemSpell, spells);
        ImGui.popID();


        ImGui.end();

    }

    public void addSpell(String spell) {
        if (spellsAdded.size() == MAX_INVENTORY_SPELLS - 1) {
            spellsAdded.add(spell);

            for (int i = 0; i < MAX_INVENTORY_SPELLS; i++) {
                spells[i] = spellsAdded.get(i);
            }

            spellsAdded.clear();
        } else {
            spellsAdded.add(spell);
        }
    }
}
