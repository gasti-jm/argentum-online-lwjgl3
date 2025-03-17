package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import org.aoclient.engine.game.inventory.NPCInventory;

import java.io.IOException;

import static org.aoclient.network.Protocol.writeCommerceEnd;

/**
 * Formulario de comercio con NPCs.
 * <p>
 * La clase {@code FComerce} implementa la interfaz grafica que permite a los jugadores interactuar comercialmente con los
 * diferentes NPCs mercaderes presentes en el mundo. Esta ventana se abre cuando el jugador inicia una transaccion comercial con
 * un vendedor.
 * <p>
 * El formulario proporciona las siguientes funcionalidades:
 * <ul>
 * <li>Visualizacion de los objetos disponibles en el inventario del NPC
 * <li>Informacion sobre precios y cantidades de los articulos
 * <li>Interfaz para realizar operaciones de compra
 * <li>Control para cerrar la ventana de comercio cuando se desee terminar la transaccion
 * </ul>
 * <p>
 * La clase mantiene un inventario estatico para el NPC con el que se esta comerciando, permitiendo que el jugador vea los
 * articulos que este ofrece. Al finalizar el comercio, se envia un mensaje al servidor para informar que la transaccion ha
 * concluido.
 */

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
