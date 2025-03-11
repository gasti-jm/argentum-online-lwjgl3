package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import org.aoclient.engine.Window;
import org.aoclient.engine.gui.ImGUISystem;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.aoclient.engine.Sound.SND_CLICK;
import static org.aoclient.engine.Sound.playSound;
import static org.aoclient.engine.utils.GameData.options;

public class FOptions extends Form {

    public FOptions() {
        try {
            this.backgroundImage = loadTexture("VentanaOpciones");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render() {
        ImGui.setNextWindowSize(355, 480, ImGuiCond.Always);
        ImGui.begin(this.getClass().getSimpleName(), ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize);

        ImGui.text("Opciones:");
        ImGui.separator();

        if (ImGui.checkbox("Pantalla Completa", options.isFullscreen())) {
            if (options.isFullscreen()) options.setFullscreen(false);
            else options.setFullscreen(true);
            Window.get().toggleWindow();
        }

        if (ImGui.checkbox("Sincronizacion Vertical", options.isVsync())) {
            if (options.isVsync()) options.setVsync(false);
            else options.setVsync(true);
            Window.get().toggleWindow();
        }

        ImGui.separator();

        if (ImGui.checkbox("Musica", options.isMusic())) {
            if (options.isMusic()) options.setMusic(false);
            else options.setMusic(true);
        }

        if (ImGui.checkbox("Audio", options.isSound())) {
            if (options.isSound()) options.setSound(false);
            else options.setSound(true);
        }

        this.drawButtons();

        ImGui.end();
    }

    private void drawButtons() {
        ImGui.setCursorPos(6, 344);
        if (ImGui.button("Configurar Teclas", 170, 20)) playSound(SND_CLICK);

        ImGui.setCursorPos(6, 368);
        if (ImGui.button("Mapa", 170, 20)) {
            playSound(SND_CLICK);
            ImGUISystem.get().show(new FMapa());
        }

        ImGui.setCursorPos(6, 392);
        if (ImGui.button("Manual", 170, 20)) playSound(SND_CLICK);

        ImGui.setCursorPos(6, 416);
        if (ImGui.button("Soporte", 170, 20)) playSound(SND_CLICK);

        ImGui.setCursorPos(180, 344);
        if (ImGui.button("Mensajes Personalizados", 170, 20)) playSound(SND_CLICK);

        ImGui.setCursorPos(180, 368);
        if (ImGui.button(new String("Cambiar Contraseña".getBytes(), StandardCharsets.UTF_8), 170, 20)) {
            playSound(SND_CLICK);
            ImGUISystem.get().show(new FNewPassword());
        }

        ImGui.setCursorPos(180, 392);
        if (ImGui.button("Radio", 170, 20)) playSound(SND_CLICK);

        ImGui.setCursorPos(180, 416);
        if (ImGui.button("Tutorial", 170, 20)) playSound(SND_CLICK);

        ImGui.setCursorPos(134, 440);
        if (ImGui.button("Salir", 170, 20)) {
            options.saveOptions();
            this.close();
        }
    }

}
