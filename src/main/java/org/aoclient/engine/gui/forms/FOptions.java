package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import org.aoclient.engine.Window;
import org.aoclient.engine.game.Rain;
import org.aoclient.engine.game.User;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.aoclient.engine.audio.Sound.*;
import static org.aoclient.engine.utils.GameData.options;

/**
 * <p>
 * Proporciona una interfaz grafica completa para que el usuario pueda ver y modificar las diferentes opciones de configuracion.
 * Permite gestionar ajustes como la pantalla completa, sincronizacion vertical, activacion/desactivacion de musica y sonidos.
 * <p>
 * Incluye tambien una serie de botones que dan acceso a otras funcionalidades relacionadas con la configuracion, como la
 * configuracion de teclas, visualizacion del mapa, acceso al manual, soporte, mensajes personalizados, cambio de contrasena,
 * radio y tutorial.
 * <p>
 * El formulario se encarga de aplicar los cambios de configuracion inmediatamente cuando el usuario modifica las opciones, y
 * guarda los ajustes en un archivo de configuracion cuando se cierra. Mantiene una interfaz cohesiva y uniforme con el resto de
 * elementos.
 */

public final class FOptions extends Form {

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
            options.setFullscreen(!options.isFullscreen());
            Window.INSTANCE.toggleWindow();
        }

        if (ImGui.checkbox("Sincronizacion Vertical", options.isVsync())) {
            options.setVsync(!options.isVsync());
            Window.INSTANCE.toggleWindow();
        }

        ImGui.separator();

        if (ImGui.checkbox("Musica", options.isMusic())) {
            options.setMusic(!options.isMusic());
            stopMusic();
        }

        if (ImGui.checkbox("Audio", options.isSound())) {
            options.setSound(!options.isSound());
            Rain.INSTANCE.stopSounds();
        }

        ImGui.separator();

        if (ImGui.checkbox("Cursores graficos", options.isCursorGraphic())) {
            options.setCursorGraphic(!options.isCursorGraphic());
        }


        this.drawButtons();

        ImGui.end();
    }

    private void drawButtons() {
        ImGui.setCursorPos(6, 344);
        if (ImGui.button("Configurar Teclas", 170, 20)) {
            playSound(SND_CLICK);
            IM_GUI_SYSTEM.show(new FBindKeys());
        }

        ImGui.setCursorPos(6, 368);
        if (ImGui.button("Mapa", 170, 20)) {
            playSound(SND_CLICK);
            IM_GUI_SYSTEM.show(new FMapa());
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
            IM_GUI_SYSTEM.show(new FNewPassword());
        }

        ImGui.setCursorPos(180, 392);
        if (ImGui.button("Radio", 170, 20)) playSound(SND_CLICK);

        ImGui.setCursorPos(180, 416);
        if (ImGui.button("Tutorial", 170, 20)) {
            playSound(SND_CLICK);
            IM_GUI_SYSTEM.show(new FTutorial());
        }

        ImGui.setCursorPos(134, 440);
        if (ImGui.button("Salir", 170, 20)) {
            options.save();
            this.close();
        }
    }

}
