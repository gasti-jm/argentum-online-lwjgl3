package org.aoclient.engine.gui.forms;

import imgui.ImGui;

import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import org.aoclient.engine.game.User;

import static org.aoclient.connection.Protocol.writeDrop;
import static org.aoclient.engine.game.inventory.UserInventory.FLAGORO;

public final class FCantidad extends Form {
    private final ImString cant = new ImString("1");
    private final boolean dropOro;

    public FCantidad() {
        this.formName = "frmCantidad";
        this.dropOro = false;
    }

    public FCantidad(boolean dropOro) {
        this.formName = "frmCantidad";
        this.dropOro = dropOro;
    }

    @Override
    public void render() {
        ImGui.setNextWindowSize(216, 98, ImGuiCond.Always);

        ImGui.begin(formName, ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize);



        //input text
        ImGui.setCursorPos(30, 15);
        ImGui.text("Cantidad:");

        ImGui.setCursorPos(30, 30);
        ImGui.pushItemWidth(150);
            ImGui.pushID("cantidad");
                ImGui.inputText("", cant, ImGuiInputTextFlags.CharsDecimal | ImGuiInputTextFlags.CallbackResize);
            ImGui.popID();
        ImGui.popItemWidth();


        ImGui.separator();

        // buttons
        ImGui.setCursorPos(14, 65);
        if (ImGui.button("Tirar", 89, 25)) {

            if (!cant.get().isEmpty()) {
                final int cantToDrop = Integer.parseInt(cant.get());

                if (cantToDrop > 0 && cantToDrop <= 10000) {
                    if (dropOro) {
                        writeDrop(FLAGORO, cantToDrop);
                    } else {
                        writeDrop(User.get().getUserInventory().getSlotSelected() + 1, cantToDrop);
                    }
                }
            }

            this.close();
        }

        ImGui.setCursorPos(112, 65);
        if (ImGui.button("Tirar todo", 89, 25)) {

            if (!cant.get().isEmpty()) {
                final int cantToDrop = Integer.parseInt(cant.get());

                if (cantToDrop > 0 && cantToDrop <= 10000) {
                    if (dropOro) {

                        if (User.get().getUserGLD() > 10000) {
                            writeDrop(FLAGORO, 10000);
                        } else {
                            writeDrop(FLAGORO, User.get().getUserGLD());
                        }

                    } else {
                        writeDrop(User.get().getUserInventory().getSlotSelected() + 1,
                                    User.get().getUserInventory().getAmountSlotSelected());
                    }
                }
            }


            this.close();
        }

        ImGui.end();
    }


}
