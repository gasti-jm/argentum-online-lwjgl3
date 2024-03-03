package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.ImString;
import imgui.enums.ImGuiCol;
import imgui.enums.ImGuiCond;
import imgui.enums.ImGuiInputTextFlags;
import imgui.enums.ImGuiWindowFlags;
import org.aoclient.engine.Engine;
import org.aoclient.engine.Window;
import org.aoclient.engine.game.Console;
import org.aoclient.engine.game.User;
import org.aoclient.engine.gui.ImGUISystem;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import static org.aoclient.engine.utils.Time.FPS;

public final class FMain extends Form {
    private final ImString sendText = new ImString();


    public FMain() {
        this.formName = "frmMain";

        Console.get().clearConsole();

        try {
            this.backgroundImage = loadTexture(ImageIO.read(new File("resources/gui/VentanaPrincipal.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render() {
        final User dataUser = User.get();

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
        ImGui.setCursorPos(centerTextValue(459, txtFPS.length()), 4);
        ImGui.text(txtFPS);

        // lblEnergia
        ImGui.setCursorPos(589, 453);
        ImGui.text(dataUser.getUserMinSTA() + "/" + dataUser.getUserMaxSTA());

        // lblMana
        ImGui.setCursorPos(589, 477);
        ImGui.text(dataUser.getUserMinMAN() + "/" + dataUser.getUserMaxMAN());

        // lblHP
        ImGui.setCursorPos(589, 498);
        ImGui.text(dataUser.getUserMinHP() + "/" + dataUser.getUserMaxHP());

        // lblHambre
        ImGui.setCursorPos(589, 521);
        ImGui.text(dataUser.getUserMinHAM() + "/" + dataUser.getUserMaxHAM());

        // lblSed
        ImGui.setCursorPos(589, 542);
        ImGui.text(dataUser.getUserMinAGU() + "/" + dataUser.getUserMaxAGU());

        // lblLvl
        ImGui.setCursorPos(615, 78);
        ImGui.text("Nivel: " + dataUser.getUserLvl());

        //lblPorcLvl
        ImGui.setCursorPos(615, 90);
        if (dataUser.getUserPasarNivel() > 0) {
            ImGui.textColored(0.5f, 1, 1, 1,
                    "[" + Math.round((float) (dataUser.getUserExp() * 100) / dataUser.getUserPasarNivel()) + "%]");
        } else {
            ImGui.textColored(0.5f, 1, 1, 1,"[N/A]");
        }

        //lblExp
        ImGui.setCursorPos(615, 102);
        ImGui.text("Exp: " + dataUser.getUserExp() + "/" + dataUser.getUserPasarNivel());

        // gldLbl (color)
        ImGui.setCursorPos(730, 419);
        ImGui.textColored(1, 1, 0.0f, 1, String.valueOf(dataUser.getUserGLD()));

        // lblDext (color)
        ImGui.setCursorPos(613, 413);
        ImGui.textColored(1, 0.5f, 0.0f, 1, String.valueOf(dataUser.getUserDext()));

        // lblStrg (color)
        ImGui.setCursorPos(650, 413);
        ImGui.textColored(0.1f, 0.6f, 0.1f, 1, String.valueOf(dataUser.getUserStrg()));

        // lblArmor (color)
        ImGui.setCursorPos(104, 579);
        ImGui.textColored(1, 0.0f, 0.0f, 1, dataUser.getUserArmourEqpDef());

        // lblShielder (color)
        ImGui.setCursorPos(370, 579);
        ImGui.textColored(1, 0.0f, 0.0f, 1, dataUser.getUserShieldEqpDef());

        // lblHelm (color)
        ImGui.setCursorPos(222, 579);
        ImGui.textColored(1, 0.0f, 0.0f, 1, dataUser.getUserHelmEqpDef());

        // lblWeapon (color)
        ImGui.setCursorPos(488, 579);
        ImGui.textColored(1, 0.0f, 0.0f, 1, dataUser.getUserWeaponEqpHit());

        // lblCoords (color)
        ImGui.setCursorPos(630, 571);
        ImGui.textColored(1, 1, 0.0f, 1,
                dataUser.getUserMap() + " X: " + User.get().getUserPos().getX() + " Y: " + User.get().getUserPos().getY());

        // lblName
        ImGui.setCursorPos(584, 24);
        ImGui.textColored(1, 0.0f, 0.0f, 1, dataUser.getUserName());

        // btnClose
        ImGui.setCursorPos(775, 3);
        if(ImGui.invisibleButton("close", 17, 17)) {
            Engine.closeClient();
        }

        // btnMinimizar
        ImGui.setCursorPos(755, 3);
        if(ImGui.invisibleButton("minimizar", 17, 17)) {
            Window.get().minimizar();
        }

        // btnGLD
        ImGui.setCursorPos(710, 417);
        ImGui.invisibleButton("Tirar Oro", 17, 17);

        // para hacer click derecho y abrir el frm
        if (ImGui.beginPopupContextItem("Tirar Oro")) {
            ImGUISystem.get().checkAddOrChange("frmCantidad", new FCantidad(true));
            ImGui.endPopup();
        }

        // txtSend
        if(dataUser.isTalking()) {
            ImGui.setCursorPos(15, 123);

            ImGui.pushItemWidth(546);
                ImGui.pushStyleColor(ImGuiCol.FrameBg, 0, 0,0, 1);

                    ImGui.inputText("", sendText, ImGuiInputTextFlags.CallbackResize);
                    ImGui.setKeyboardFocusHere();

                ImGui.popStyleColor();
            ImGui.popItemWidth();
        }


        /////// console
        Console.get().drawConsole();

        ImGui.end();
    }

    public void clearSendTxt() {
        this.sendText.set("");
    }

    public String getSendText() {
        return this.sendText.get();
    }
}
