package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import org.aoclient.engine.game.Messages;
import org.aoclient.engine.game.console.Console;
import org.aoclient.engine.game.console.FontStyle;
import org.aoclient.engine.game.inventory.NPCInventory;
import org.aoclient.engine.game.inventory.UserInventory;
import org.aoclient.engine.renderer.RGBColor;

import java.io.IOException;

import static org.aoclient.engine.audio.Sound.SND_CLICK;
import static org.aoclient.engine.audio.Sound.playSound;
import static org.aoclient.engine.game.Messages.MessageKey.ORO_INSUFICIENTE;
import static org.aoclient.network.protocol.Protocol.*;

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

public final class FComerce extends Form {

    private final ImString cant = new ImString("1");
    public static NPCInventory invNPC = new NPCInventory(false);
    public static UserInventory invUser = USER.getUserInventory().clone();

    public FComerce() {
        try {
            this.backgroundImage = loadTexture("VentanaComercio");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        invUser.transformInvComerce(false);
    }

    @Override
    public void render() {
        ImGui.setNextWindowFocus(); // le damos foco.
        ImGui.setNextWindowSize(462, 486, ImGuiCond.Always);
        ImGui.begin(this.getClass().getSimpleName(), ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoDecoration |
                ImGuiWindowFlags.NoBackground | ImGuiWindowFlags.NoMove);

        // para poder mover el frm.
        this.checkMoveFrm();

        ImGui.setCursorPos(5, 0);
        ImGui.image(backgroundImage, 462, 486);

        ImGui.setCursorPos(405, 24);
        if (ImGui.invisibleButton("Close", 30, 30)) {
            commerceEnd();
            this.close();
        }

        ImGui.setCursorPos(34, 402);
        if (ImGui.button("Comprar", 172, 31)) {
            playSound(SND_CLICK);

            if (USER.getUserGLD() >= invNPC.getValue(invNPC.getSlotSelected())) {
                if (!cant.isEmpty()) {
                    commerceBuy(invNPC.getSlotSelected() + 1, Integer.parseInt(cant.get()));
                }
            } else {
                Console.INSTANCE.addMsgToConsole(Messages.get(ORO_INSUFICIENTE), FontStyle.BOLD, new RGBColor(1f, 0.1f, 0.1f));
            }

        }

        ImGui.setCursorPos(256, 402);
        if (ImGui.button("Vender", 172, 31)) {
            playSound(SND_CLICK);
            if (!cant.isEmpty()) {
                commerceSell(invUser.getSlotSelected() + 1, Integer.parseInt(cant.get()));
            }
        }


        ImGui.setCursorPos(215, 438);
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
