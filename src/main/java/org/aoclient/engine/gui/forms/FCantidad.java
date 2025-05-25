package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;

import java.io.IOException;

import static org.aoclient.engine.game.inventory.UserInventory.FLAGORO;
import static org.aoclient.network.protocol.Protocol.writeDrop;

/**
 * Formulario para especificar cantidades al tirar objetos o oro al suelo.
 * <p>
 * La clase {@code FCantidad} implementa una interfaz grafica que permite al jugador indicar la cantidad exacta de un objeto o de
 * oro que desea tirar al suelo. Este formulario aparece como una ventana emergente cuando el usuario inicia la accion de tirar un
 * item de su inventario o dinero.
 * <p>
 * El formulario proporciona las siguientes funcionalidades:
 * <ul>
 * <li>Campo de texto para introducir la cantidad numerica deseada
 * <li>Boton para tirar la cantidad especificada (limitada a un maximo de 10,000)
 * <li>Boton para tirar la cantidad total disponible del objeto o todo el oro
 * <li>Validacion de entrada para asegurar que la cantidad sea un valor numerico valido
 * </ul>
 * <p>
 * Esta clase puede funcionar en dos modos diferentes: para tirar oro (indicado por el flag {@code dropOro}) o para tirar un item
 * del inventario seleccionado actualmente. Cuando el formulario se confirma, se envia la orden correspondiente al servidor a
 * traves del protocolo de red.
 */

public final class FCantidad extends Form {

    private final ImString cant = new ImString("1");
    private final boolean dropOro;

    public FCantidad() {
        this.dropOro = false;

        try {
            this.backgroundImage = loadTexture("VentanaTirarOro");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public FCantidad(boolean dropOro) {
        this.dropOro = dropOro;
        try {
            this.backgroundImage = loadTexture("VentanaTirarOro");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render() {
        ImGui.setNextWindowSize(225, 100, ImGuiCond.Always);
        ImGui.begin(this.getClass().getSimpleName(), ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoDecoration |
                ImGuiWindowFlags.NoBackground);

        ImGui.setCursorPos(5, 0);
        ImGui.image(backgroundImage, 216, 100);

        ImGui.setCursorPos(35, 30);
        ImGui.pushItemWidth(150);
        ImGui.pushID("cantidad");
        ImGui.inputText("", cant, ImGuiInputTextFlags.CharsDecimal | ImGuiInputTextFlags.CallbackResize);
        ImGui.popID();
        ImGui.popItemWidth();

        // buttons
        ImGui.setCursorPos(20, 65);
        if (ImGui.button("Tirar", 89, 25)) {
            if (!cant.get().isEmpty()) {
                final int cantToDrop = Integer.parseInt(cant.get());
                if (cantToDrop > 0 && cantToDrop <= 10000) {
                    if (dropOro) writeDrop(FLAGORO, cantToDrop);
                    else writeDrop(USER.getUserInventory().getSlotSelected() + 1, cantToDrop);
                }
            }
            this.close();
        }

        ImGui.setCursorPos(118, 65);
        if (ImGui.button("Tirar todo", 89, 25)) {
            if (!cant.get().isEmpty()) {
                final int cantToDrop = Integer.parseInt(cant.get());
                if (cantToDrop > 0 && cantToDrop <= 10000) {
                    if (dropOro) writeDrop(FLAGORO, Math.min(USER.getUserGLD(), 10000));
                    else
                        writeDrop(USER.getUserInventory().getSlotSelected() + 1, USER.getUserInventory().getAmountSlotSelected());
                }
            }
            this.close();
        }
        ImGui.end();
    }

}
