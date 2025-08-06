package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import org.aoclient.engine.game.Messages;

import java.nio.charset.StandardCharsets;

import static org.aoclient.engine.game.Messages.MessageKey.ACCEPT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;

/**
 * Formulario para mostrar mensajes informativos.
 * <p>
 * Esta clase implementa un cuadro de dialogo simple que muestra un mensaje al usuario y proporciona un boton "Aceptar" para
 * cerrarlo. El mensaje se muestra con formato de texto ajustado para facilitar su lectura.
 * <p>
 * {@code FMessage} puede ser utilizado para notificaciones del sistema, mensajes de error, confirmaciones y cualquier
 * comunicacion importante que requiera la atencion del usuario. El cuadro de dialogo se puede cerrar tanto con el boton "Aceptar"
 * como presionando la tecla Enter.
 */

public final class FMessage extends Form {

    private final String txtMessage;

    public FMessage() {
        this.txtMessage = "";
    }

    public FMessage(String text) {
        this.txtMessage = new String(text.getBytes(), StandardCharsets.UTF_8);
    }

    @Override
    public void render() {
        ImGui.setNextWindowFocus(); // dale foco solo a este FRM
        ImGui.setNextWindowSize(250, 230, ImGuiCond.Always);

        // Start Custom window
        ImGui.begin(this.getClass().getSimpleName(), ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.AlwaysAutoResize);

        ImGui.textWrapped(this.txtMessage);

        ImGui.setCursorPos(8, ImGui.getWindowHeight() - 40);
        ImGui.separator();

        ImGui.setCursorPos(8, ImGui.getWindowHeight() - 30);
        if (ImGui.button(Messages.get(ACCEPT), ImGui.getWindowWidth() - 16, 20) || ImGui.isKeyPressed(GLFW_KEY_ENTER))
            close();

        ImGui.end();

    }

}
