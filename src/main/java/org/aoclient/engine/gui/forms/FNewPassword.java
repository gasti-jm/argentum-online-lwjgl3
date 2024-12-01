package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import org.aoclient.engine.gui.ImGUISystem;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.aoclient.engine.Sound.SND_CLICK;
import static org.aoclient.engine.Sound.playSound;
import static org.aoclient.network.Protocol.writeChangePassword;

public class FNewPassword extends Form {

    private final ImString txtPassword = new ImString();

    private final ImString txtNewPassword = new ImString();

    private final ImString txtNewrePassword = new ImString();

    public FNewPassword() {
        try {
            this.backgroundImage = loadTexture(ImageIO.read(new File("resources/gui/VentanaCambiarcontrasenia.jpg")));

        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    @Override
    public void render() {

        ImGui.setNextWindowSize(317, 237, ImGuiCond.Always);
        ImGui.begin(this.getClass().getSimpleName(), ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize);

        //ImGui.getWindowDrawList().addImage(backgroundImage, 0, 0, Window.get().getWidth(), Window.get().getHeight());

        ImGui.text(new String("Cambiar Contraseña:".getBytes(), StandardCharsets.UTF_8));
        ImGui.separator();

        // txtPassword
        //ImGui.setCursorPos(100, 40);
        ImGui.text(new String("Contraseña:".getBytes(), StandardCharsets.UTF_8));
        ImGui.pushItemWidth(150);
            //ImGui.pushStyleColor(ImGuiCol.FrameBg, 255, 0,0, 1);
                ImGui.pushID("txtPassword");
                    ImGui.inputText("", txtPassword, ImGuiInputTextFlags.Password | ImGuiInputTextFlags.CallbackResize);
                ImGui.popID();
            //ImGui.popStyleColor();
        ImGui.popItemWidth();

        // txtNewPasswd
        ImGui.text(new String("Nueva Contraseña:".getBytes(), StandardCharsets.UTF_8));
        ImGui.pushItemWidth(150);
            //ImGui.pushStyleColor(ImGuiCol.FrameBg, 0, 0, 0, 1);
                ImGui.pushID("txtNewPassword");
                    ImGui.inputText("", txtNewPassword, ImGuiInputTextFlags.Password | ImGuiInputTextFlags.CallbackResize);
                ImGui.popID();
            //ImGui.popStyleColor();
        ImGui.popItemWidth();

        // txtNewRePasswd
        ImGui.text(new String("Repita la Contraseña:".getBytes(), StandardCharsets.UTF_8));
        ImGui.pushItemWidth(150);
            //ImGui.pushStyleColor(ImGuiCol.FrameBg, 0, 0, 0, 1);
                ImGui.pushID("txtNewrePassword");
                    ImGui.inputText("", txtNewrePassword, ImGuiInputTextFlags.Password | ImGuiInputTextFlags.CallbackResize);
                ImGui.popID();
            //ImGui.popStyleColor();
        ImGui.popItemWidth();

        this.drawButtons();

        ImGui.end();

    }

    private void drawButtons() {

        //Botón Salir
        ImGui.setCursorPos(25, 200);
        if(ImGui.button("Salir", 110, 20)) {
            playSound(SND_CLICK);
            close();
        }

        //Botón Cambiar Contraseña
        ImGui.setCursorPos(180, 200);
        if(ImGui.button("Cambiar", 110, 20)) {
            playSound(SND_CLICK);

            if (this.txtPassword.get().isEmpty() || this.txtNewPassword.get().isEmpty() || this.txtNewrePassword.get().isEmpty()) {
                ImGUISystem.get().show(new FMessage("Por favor, completa todos los campos."));

            } else if (!this.txtNewPassword.get().equals(this.txtNewrePassword.get())) {
                ImGUISystem.get().show(new FMessage("Las contraseñas no coinciden."));

            } else { // Si está correcto enviamos paquete para cambiar la contraseña.
                writeChangePassword(this.txtPassword.get(),this.txtNewPassword.get());

            }

        }

    }

}
