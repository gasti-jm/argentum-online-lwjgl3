package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import org.aoclient.engine.gui.widgets.ImageButton3State;

import java.io.IOException;

import static org.aoclient.engine.Sound.SND_CLICK;
import static org.aoclient.engine.Sound.playSound;

public class FBindKeys extends Form{
    // apa las papas.

    private ImageButton3State btnSave;
    private ImageButton3State btnDefaultKeys;


    public FBindKeys() {
        try {
            this.backgroundImage = loadTexture("VentanaConfigurarTeclas");


            btnSave = new ImageButton3State(
                    loadTexture("BotonGuardarConfigKey"),
                    loadTexture("BotonGuardarRolloverConfigKey"),
                    loadTexture("BotonGuardarClickConfigKey"),
                    312, 448, 177, 25
            );

            btnDefaultKeys = new ImageButton3State(
                    loadTexture("BotonDefaultKeys"),
                    loadTexture("BotonDefaultKeysRollover"),
                    loadTexture("BotonDefaultKeysClick"),
                    64, 448, 177, 25
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void render() {
        ImGui.setNextWindowFocus(); // dale foco solo a este FRM

        ImGui.setNextWindowSize(560, 500, ImGuiCond.Always);
        ImGui.begin(this.getClass().getSimpleName(), ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoDecoration |
                ImGuiWindowFlags.NoBackground | ImGuiWindowFlags.NoMove);

        this.checkMoveFrm();

        ImGui.setCursorPos(5, 0);
        ImGui.image(backgroundImage, 548, 500);

        if(btnDefaultKeys.render()) buttonDefault();
        if(btnSave.render()) buttonSave();

        ImGui.end();
    }

    private void buttonDefault() {
        playSound(SND_CLICK);

        // set Default keys...
        // saveOptions();

        btnDefaultKeys.delete();
        btnSave.delete();
        close();
    }

    private void buttonSave() {
        playSound(SND_CLICK);

        // creamos una funcion que permita guardar las teclas segun lo configurado.
        // saveOptions();


        btnDefaultKeys.delete();
        btnSave.delete();
        close();
    }
}
