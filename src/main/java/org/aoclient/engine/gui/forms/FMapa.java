package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;

import java.io.IOException;

import static org.aoclient.engine.Sound.SND_CLICK;
import static org.aoclient.engine.Sound.playSound;

/**
 * Formulario que muestra el mapa del mundo.
 * <p>
 * Esta clase implementa una ventana interactiva que presenta un mapa visual del mundo, permitiendo a los jugadores orientarse y
 * ubicarse en el universo virtual de Argentum Online.
 * <p>
 * FMapa ofrece dos vistas diferentes del mundo:
 * <ul>
 * <li>Mapa del mundo exterior (overworld): muestra las ciudades, rutas y areas principales
 * <li>Mapa de dungeons: muestra las areas subterraneas y zonas peligrosas
 * </ul>
 * <p>
 * La interfaz permite al usuario alternar entre estas dos vistas mediante botones interactivos y cerrar el mapa cuando ya no es
 * necesario.
 */

public final class FMapa extends Form {

    private boolean overWorld;

    public FMapa() {
        try {
            this.backgroundImage = loadTexture("Mapa1");
            this.overWorld = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render() {
        ImGui.setNextWindowFocus();
        ImGui.setNextWindowSize(573, 528, ImGuiCond.Always);
        ImGui.begin(this.getClass().getSimpleName(),
                ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize
                        | ImGuiWindowFlags.NoDecoration | ImGuiWindowFlags.NoBackground | ImGuiWindowFlags.NoMove);

        this.checkMoveFrm();

        ImGui.setCursorPos(5, 0);
        ImGui.image(backgroundImage, 573, 528);

        drawButtons();

        ImGui.end();
    }

    private void drawButtons() {
        // Dependiendo de que mapa estemos viendo ahora, renderizamos el boton de arriba o el de abajo.
        if (this.overWorld) {
            ImGui.setCursorPos(266, 504);
            if (ImGui.invisibleButton("overWorldView", 55, 17)) {
                playSound(SND_CLICK);

                try {
                    this.backgroundImage = loadTexture("Mapa2");
                    this.overWorld = false;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }

            // Botón cerrar (solo se muestra con el Mapa1)
            ImGui.setCursorPos(542, 14);
            if (ImGui.invisibleButton("closeMap", 17, 17)) {
                playSound(SND_CLICK);
                close();
            }

        } else {
            ImGui.setCursorPos(266, 8);
            if (ImGui.invisibleButton("dungeonView", 55, 17)) {
                playSound(SND_CLICK);
                try {
                    this.backgroundImage = loadTexture("Mapa1");
                    this.overWorld = true;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
