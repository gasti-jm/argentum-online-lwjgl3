package org.aoclient.engine.game;

import imgui.ImGui;
import org.aoclient.engine.game.console.Console;
import org.aoclient.engine.game.console.FontStyle;
import org.aoclient.engine.game.models.SMType;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.engine.renderer.Texture;
import org.aoclient.engine.renderer.TextureManager;

import static org.aoclient.engine.audio.Sound.SND_CLICK;
import static org.aoclient.engine.audio.Sound.playSound;
import static org.aoclient.engine.gui.forms.Form.imageButtonRegion;
import static org.aoclient.engine.gui.forms.Form.imageRegion;
import static org.aoclient.engine.utils.GameData.grhData;
import static org.aoclient.network.protocol.Protocol.resucitationToggle;
import static org.aoclient.network.protocol.Protocol.safeToggle;

// lo creamos en fMAIN
public class SMSystem {
    private final int x, y, w, h; // posicion y size del boton
    private final SMType type;
    private boolean active;
    private Texture SMTex;

    public SMSystem(int x, int y, SMType type, boolean active) {
        this.x = x;
        this.y = y;
        this.w = 28;
        this.h = 30;

        this.type = type;
        this.active = active;

        // precargamos la textura
        TextureManager.requestTexture(type.getGrhIndexON());
        TextureManager.requestTexture(type.getGrhIndexOFF());

        if (active) {
            SMTex = TextureManager.getTexture(grhData[type.getGrhIndexON()].getFileNum());
        } else {
            SMTex = TextureManager.getTexture(grhData[type.getGrhIndexOFF()].getFileNum());
        }
    }

    // dibuajamos en imgui.
    public void draw() {
        // que grhindex tenemos que mostrar?
        final int currentGrh = active? type.getGrhIndexON() : type.getGrhIndexOFF();

        // mostramos una imagen primero
        ImGui.setCursorPos(x, y);
        if (SMTex != null) {
            imageRegion(SMTex, grhData[currentGrh].getsX(), grhData[currentGrh].getsY(), w, h);
        }

        ImGui.setCursorPos(x, y);
        if (ImGui.invisibleButton(type.toString(), w, h)) {
            action();
        }

        // update texture
        SMTex = TextureManager.getTexture(grhData[currentGrh].getFileNum());

        updateToolTip();
    }



    private void updateToolTip() {
        switch (type) {
            case sResucitation:
                if (active){
                    if (ImGui.isItemHovered()) ImGui.setTooltip(Messages.get(Messages.MessageKey.SEGURO_RESU_ON));
                } else {
                    if (ImGui.isItemHovered()) ImGui.setTooltip(Messages.get(Messages.MessageKey.SEGURO_RESU_OFF));
                }
                break;

            case sSafemode:
                if (active){
                    if (ImGui.isItemHovered()) ImGui.setTooltip(Messages.get(Messages.MessageKey.SEGURO_ACTIVADO));
                } else {
                    if (ImGui.isItemHovered()) ImGui.setTooltip(Messages.get(Messages.MessageKey.SEGURO_DESACTIVADO));
                }
                break;

        }
    }

    // accion segun el tipo que sea.
    private void action() {
        // sonido
        playSound(SND_CLICK);

        // accion segun el tipo
        switch (type){
            case sResucitation:
                // el servidor ya devuelve mensaje.
                resucitationToggle();
                break;

            case sSafemode:
                // el servidor ya devuelve mensaje.
                safeToggle();
                break;

                // TODO: Agregar al sistema de internacionalizacion, no puede quedar hardcodeado esto.
            case mWork:
                if (active) {
                    Console.INSTANCE.addMsgToConsole("Macro de trabajo desactivado.", FontStyle.BOLD, new RGBColor(1, 0, 0));
                } else {
                    Console.INSTANCE.addMsgToConsole("Macro de trabajo activado.", FontStyle.BOLD, new RGBColor(0, 1, 0));
                }
                break;

            case mSpells:
                if (active) {
                    Console.INSTANCE.addMsgToConsole("Macro de hechizos desactivado.", FontStyle.BOLD, new RGBColor(1, 0, 0));
                } else {
                    Console.INSTANCE.addMsgToConsole("Macro de hechizos activado.", FontStyle.BOLD, new RGBColor(0, 1, 0));
                }
                break;

        }

        active = !active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
