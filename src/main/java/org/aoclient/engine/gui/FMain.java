package org.aoclient.engine.gui;

import imgui.ImGui;
import imgui.enums.ImGuiCond;
import imgui.enums.ImGuiWindowFlags;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import static org.aoclient.engine.utils.Time.FPS;

public class FMain extends FormGUI{

    public FMain() {
        this.formName = "frmMain";

        try {
            this.backgroundImage = loadTexture(ImageIO.read(new File("resources/gui/VentanaPrincipal.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render() {
        ImGui.setNextWindowSize(810, 605, ImGuiCond.Once);
        ImGui.setNextWindowPos(-5, -1, ImGuiCond.Once);

        // Start Custom window
        ImGui.begin(formName, ImGuiWindowFlags.NoTitleBar |
                ImGuiWindowFlags.NoMove |
                ImGuiWindowFlags.NoFocusOnAppearing |
                ImGuiWindowFlags.NoDecoration |
                ImGuiWindowFlags.NoBackground |
                ImGuiWindowFlags.NoResize |
                ImGuiWindowFlags.NoCollapse |
                ImGuiWindowFlags.NoSavedSettings |
                ImGuiWindowFlags.NoBringToFrontOnFocus);

        ImGui.getWindowDrawList().addImage(backgroundImage, 0, 0, 800, 600);

        // LBL FPS
        final String txtFPS = String.valueOf(FPS);
        ImGui.setCursorPos(459 - (txtFPS.length() * 2), 4);
        ImGui.text(txtFPS);

        // lblEnergia
        ImGui.setCursorPos(589, 453);
        ImGui.text("000/000");

        // lblMana
        ImGui.setCursorPos(589, 477);
        ImGui.text("0000/0000");

        // lblHP
        ImGui.setCursorPos(589, 498);
        ImGui.text("000/000");

        // lblHambre
        ImGui.setCursorPos(589, 521);
        ImGui.text("000/100");

        // lblSed
        ImGui.setCursorPos(589, 542);
        ImGui.text("000/100");

        // lblLvl
        ImGui.setCursorPos(615, 78);
        ImGui.text("000/000");

        //lblPorcLvl
        ImGui.setCursorPos(615, 90);
        ImGui.text("000000/000000");

        //lblExp
        ImGui.setCursorPos(615, 102);
        ImGui.text("000000/000000");

        // gldLbl (color)
        ImGui.setCursorPos(724, 419);
        ImGui.text("1.000.000");

        // lblDext (color)
        ImGui.setCursorPos(607, 412);
        ImGui.text("35");

        // lblStrg (color)
        ImGui.setCursorPos(645, 412);
        ImGui.text("35");

        // lblArmor (color)
        ImGui.setCursorPos(104, 579);
        ImGui.text("0/0");

        // lblShielder (color)
        ImGui.setCursorPos(370, 579);
        ImGui.text("0/0");

        // lblHelm (color)
        ImGui.setCursorPos(222, 579);
        ImGui.text("0/0");

        // lblWeapon (color)
        ImGui.setCursorPos(488, 579);
        ImGui.text("0/0");

        // lblCoords (color)
        ImGui.setCursorPos(630, 571);
        ImGui.text("Map: 1, X: 50, Y: 50");

        // lblName
        ImGui.setCursorPos(584, 24);
        ImGui.text("BETATESTER");

        ImGui.end();
    }
}
