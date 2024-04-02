package org.aoclient.engine.gui.forms;

import imgui.ImDrawList;
import imgui.ImGui;

import imgui.flag.*;
import imgui.type.ImInt;
import imgui.type.ImString;
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
    private final ImString sendText     = new ImString();
    private final User dataUser         = User.get();
    private boolean viewInventory;
    private int backgroundInventorySpells;

    public FMain() {
        this.formName = "frmMain";
        this.viewInventory = true;
        User.get().getUserInventory().setVisible(true);

        Console.get().clearConsole();

        try {
            this.backgroundImage = loadTexture(ImageIO.read(new File("resources/gui/VentanaPrincipal.png")));
            this.backgroundInventorySpells = loadTexture(ImageIO.read(new File("resources/gui/CentroHechizos.jpg")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render() {
        ImGui.setNextWindowSize(Window.get().getWidth() + 10, Window.get().getHeight() + 5, ImGuiCond.Once);
        ImGui.setNextWindowPos(-5, -1, ImGuiCond.Once);

        // Start Custom window
        ImGui.begin(formName, ImGuiWindowFlags.NoTitleBar |
                ImGuiWindowFlags.NoMove |
                ImGuiWindowFlags.NoFocusOnAppearing |
                ImGuiWindowFlags.NoDecoration |
                ImGuiWindowFlags.NoBackground |
                ImGuiWindowFlags.NoResize |
                ImGuiWindowFlags.NoSavedSettings |
                ImGuiWindowFlags.NoBringToFrontOnFocus);


        ImGui.getWindowDrawList().addImage(backgroundImage, 0, 0, Window.get().getWidth(), Window.get().getHeight());

        this.drawShapes();

        // LBL FPS
        final String txtFPS = String.valueOf(FPS);
        ImGui.setCursorPos(448, 4);
        ImGui.pushStyleVar(ImGuiStyleVar.SelectableTextAlign, 0.5f, 0.5f);
        ImGui.pushStyleColor(ImGuiCol.HeaderHovered, ImGui.getColorU32(0f, 0f, 0f, 0f));
        ImGui.pushStyleColor(ImGuiCol.HeaderActive, ImGui.getColorU32(0f, 0f, 0f, 0f));
        ImGui.selectable(txtFPS, false, ImGuiSelectableFlags.None, 28, 10);
        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popStyleVar();

        // lblEnergia
        ImGui.setCursorPos(591, 453);
        ImGui.pushStyleVar(ImGuiStyleVar.SelectableTextAlign, 0.5f, 0.5f);
        ImGui.pushStyleColor(ImGuiCol.HeaderHovered, ImGui.getColorU32(0f, 0f, 0f, 0f));
        ImGui.pushStyleColor(ImGuiCol.HeaderActive, ImGui.getColorU32(0f, 0f, 0f, 0f));
        ImGui.selectable(dataUser.getUserMinSTA() + "/" + dataUser.getUserMaxSTA(), false, ImGuiSelectableFlags.None, 69, 10);
        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popStyleVar();

        // lblMana
        ImGui.setCursorPos(591, 477);
        ImGui.pushStyleVar(ImGuiStyleVar.SelectableTextAlign, 0.5f, 0.5f);
        ImGui.pushStyleColor(ImGuiCol.HeaderHovered, ImGui.getColorU32(0f, 0f, 0f, 0f));
        ImGui.pushStyleColor(ImGuiCol.HeaderActive, ImGui.getColorU32(0f, 0f, 0f, 0f));
        ImGui.selectable(dataUser.getUserMinMAN() + "/" + dataUser.getUserMaxMAN(), false, ImGuiSelectableFlags.None, 69, 10);
        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popStyleVar();

        // lblHP
        ImGui.setCursorPos(591, 498);
        ImGui.pushStyleVar(ImGuiStyleVar.SelectableTextAlign, 0.5f, 0.5f);
        ImGui.pushStyleColor(ImGuiCol.HeaderHovered, ImGui.getColorU32(0f, 0f, 0f, 0f));
        ImGui.pushStyleColor(ImGuiCol.HeaderActive, ImGui.getColorU32(0f, 0f, 0f, 0f));
        ImGui.selectable(dataUser.getUserMinHP() + "/" + dataUser.getUserMaxHP(), false, ImGuiSelectableFlags.None, 69, 10);
        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popStyleVar();

        // lblHambre
        ImGui.setCursorPos(591, 521);
        ImGui.pushStyleVar(ImGuiStyleVar.SelectableTextAlign, 0.5f, 0.5f);
        ImGui.pushStyleColor(ImGuiCol.HeaderHovered, ImGui.getColorU32(0f, 0f, 0f, 0f));
        ImGui.pushStyleColor(ImGuiCol.HeaderActive, ImGui.getColorU32(0f, 0f, 0f, 0f));
        ImGui.selectable(dataUser.getUserMinHAM() + "/" + dataUser.getUserMaxHAM(), false, ImGuiSelectableFlags.None, 69, 10);
        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popStyleVar();

        // lblSed
        ImGui.setCursorPos(591, 542);
        ImGui.pushStyleVar(ImGuiStyleVar.SelectableTextAlign, 0.5f, 0.5f);
        ImGui.pushStyleColor(ImGuiCol.HeaderHovered, ImGui.getColorU32(0f, 0f, 0f, 0f));
        ImGui.pushStyleColor(ImGuiCol.HeaderActive, ImGui.getColorU32(0f, 0f, 0f, 0f));
        ImGui.selectable(dataUser.getUserMinAGU() + "/" + dataUser.getUserMaxAGU(), false, ImGuiSelectableFlags.None, 69, 10);
        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popStyleVar();

        // lblLvl
        ImGui.setCursorPos(625, 78);
        ImGui.text("Nivel: " + dataUser.getUserLvl());

        //lblPorcLvl
        ImGui.setCursorPos(624, 90);
        if (dataUser.getUserPasarNivel() > 0) {
            ImGui.textColored(0.5f, 1, 1, 1,
                    "[" + Math.round((float) (dataUser.getUserExp() * 100) / dataUser.getUserPasarNivel()) + "%]");
        } else {
            ImGui.textColored(0.5f, 1, 1, 1,"[N/A]");
        }

        //lblExp
        ImGui.setCursorPos(625, 102);
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
        ImGui.setCursorPos(88, 579);
        ImGui.pushStyleVar(ImGuiStyleVar.SelectableTextAlign, 0.5f, 0.5f);
        ImGui.pushStyleColor(ImGuiCol.HeaderHovered, ImGui.getColorU32(0f, 0f, 0f, 0f));
        ImGui.pushStyleColor(ImGuiCol.HeaderActive, ImGui.getColorU32(0f, 0f, 0f, 0f));
        ImGui.pushStyleColor(ImGuiCol.Text, ImGui.getColorU32(1f, 0f, 0f, 1f));
        ImGui.selectable(dataUser.getUserArmourEqpDef(), false, ImGuiSelectableFlags.None, 45, 10);
        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popStyleVar();

        // lblShielder (color)
        ImGui.setCursorPos(354, 579);
        ImGui.pushStyleVar(ImGuiStyleVar.SelectableTextAlign, 0.5f, 0.5f);
        ImGui.pushStyleColor(ImGuiCol.HeaderHovered, ImGui.getColorU32(0f, 0f, 0f, 0f));
        ImGui.pushStyleColor(ImGuiCol.HeaderActive, ImGui.getColorU32(0f, 0f, 0f, 0f));
        ImGui.pushStyleColor(ImGuiCol.Text, ImGui.getColorU32(1f, 0f, 0f, 1f));
        ImGui.selectable(dataUser.getUserShieldEqpDef(), false, ImGuiSelectableFlags.None, 45, 10);
        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popStyleVar();

        // lblHelm (color)
        ImGui.setCursorPos(206, 579);
        ImGui.pushStyleVar(ImGuiStyleVar.SelectableTextAlign, 0.5f, 0.5f);
        ImGui.pushStyleColor(ImGuiCol.HeaderHovered, ImGui.getColorU32(0f, 0f, 0f, 0f));
        ImGui.pushStyleColor(ImGuiCol.HeaderActive, ImGui.getColorU32(0f, 0f, 0f, 0f));
        ImGui.pushStyleColor(ImGuiCol.Text, ImGui.getColorU32(1f, 0f, 0f, 1f));
        ImGui.selectable(dataUser.getUserHelmEqpDef(), false, ImGuiSelectableFlags.None, 45, 10);
        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popStyleVar();

        // lblWeapon (color)
        ImGui.setCursorPos(472, 579);
        ImGui.pushStyleVar(ImGuiStyleVar.SelectableTextAlign, 0.5f, 0.5f);
        ImGui.pushStyleColor(ImGuiCol.HeaderHovered, ImGui.getColorU32(0f, 0f, 0f, 0f));
        ImGui.pushStyleColor(ImGuiCol.HeaderActive, ImGui.getColorU32(0f, 0f, 0f, 0f));
        ImGui.pushStyleColor(ImGuiCol.Text, ImGui.getColorU32(1f, 0f, 0f, 1f));
        ImGui.selectable(dataUser.getUserWeaponEqpHit(), false, ImGuiSelectableFlags.None, 45, 10);
        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popStyleVar();

        // lblCoords (color)
        ImGui.setCursorPos(590, 574);
        ImGui.pushStyleVar(ImGuiStyleVar.SelectableTextAlign, 0.5f, 0.5f);
        ImGui.pushStyleColor(ImGuiCol.HeaderHovered, ImGui.getColorU32(0f, 0f, 0f, 0f));
        ImGui.pushStyleColor(ImGuiCol.HeaderActive, ImGui.getColorU32(0f, 0f, 0f, 0f));
        ImGui.pushStyleColor(ImGuiCol.Text, ImGui.getColorU32(1f, 1f, 0f, 1f));

        ImGui.selectable(dataUser.getUserMap() + " X:" + User.get().getUserPos().getX() + " Y:" + User.get().getUserPos().getY(),
                false, ImGuiSelectableFlags.None, 90, 12);

        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popStyleVar();

        // lblName
        ImGui.setCursorPos(584, 24);
        ImGui.textColored(1, 0.0f, 0.0f, 1, dataUser.getUserName());

        // btnOptions
        ImGui.setCursorPos(681, 485);
        if(ImGui.invisibleButton("viewOptions", 95, 22)) {
            ImGUISystem.get().checkAddOrChange("frmOpciones", new FOptions());
        }

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

        // txtSend
        if(dataUser.isTalking()) {
            ImGui.setCursorPos(15, 123);

            ImGui.pushItemWidth(546);
                ImGui.pushStyleColor(ImGuiCol.FrameBg, 0f, 0f,0f, 1);

                    ImGui.setKeyboardFocusHere();
                    ImGui.pushID("sendText");
                        ImGui.inputText("", sendText, ImGuiInputTextFlags.CallbackResize);
                    ImGui.popID();

                ImGui.popStyleColor();
            ImGui.popItemWidth();
        }

        // para hacer click derecho y abrir el frm
        if (ImGui.beginPopupContextItem("Tirar Oro")) {
            ImGUISystem.get().checkAddOrChange("frmCantidad", new FCantidad(true));
            ImGui.endPopup();
        }

        ImGui.setCursorPos(592, 128);
        if (ImGui.invisibleButton("ViewInvetory", 93, 30)) {
            User.get().getUserInventory().setVisible(true);
            this.viewInventory = true;
        }

        ImGui.setCursorPos(688, 128);
        if (ImGui.invisibleButton("ViewInvetorySpells", 75, 30)) {
            User.get().getUserInventory().setVisible(false);
            this.viewInventory = false;
        }

        /////// Inventory
        if(viewInventory) {
            User.get().getUserInventory().drawInventory();
        } else {
            ImGui.setCursorPos(586, 126);
            ImGui.image(backgroundInventorySpells, 198, 282);
            User.get().getInventorySpells().draw();
        }

        User.get().getUserInventory().updateTimers();

        /////// Console
        Console.get().drawConsole();

        ImGui.end();
    }

    private void drawShapes() {
        final ImDrawList drawList   = ImGui.getWindowDrawList();
        final int shpColor          = ImGui.getColorU32(0, 0, 0, 1);

        // shpEnergia
        float bWidth = (((float) (dataUser.getUserMinSTA()) / ((float) dataUser.getUserMaxSTA())) * 75);
        drawList.addRectFilled(
                (584 + bWidth), 453,
                584 + 75,
                453 + 12, shpColor
        );

        // shpMana
        bWidth = (((float) (dataUser.getUserMinMAN()) / ((float) dataUser.getUserMaxMAN())) * 75);
        drawList.addRectFilled(
                (584 + bWidth), 477,
                584 + 75,
                477 + 12, shpColor
        );

        // shpVida
        bWidth = (((float) (dataUser.getUserMinHP()) / ((float) dataUser.getUserMaxHP())) * 75);
        drawList.addRectFilled(
                (584 + bWidth), 498,
                584 + 75,
                498 + 12, shpColor
        );

        // shpHambre
        bWidth = (((float) (dataUser.getUserMinHAM()) / ((float) dataUser.getUserMaxHAM())) * 75);
        drawList.addRectFilled(
                (584 + bWidth), 521,
                584 + 75,
                521 + 12, shpColor
        );

        // shpSed
        bWidth = (((float) (dataUser.getUserMinAGU()) / ((float) dataUser.getUserMaxAGU())) * 75);
        drawList.addRectFilled(
                (584 + bWidth), 542,
                584 + 75,
                542 + 12, shpColor
        );

    }

    public void clearSendTxt() {
        this.sendText.set("");
    }

    public String getSendText() {
        return this.sendText.get();
    }
}
