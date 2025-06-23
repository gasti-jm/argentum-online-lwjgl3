package org.aoclient.engine.gui.forms;

import imgui.ImDrawList;
import imgui.ImGui;
import imgui.flag.*;
import imgui.type.ImString;
import org.aoclient.engine.Engine;
import org.aoclient.engine.Window;
import org.aoclient.engine.game.Console;
import org.aoclient.network.protocol.Protocol;

import java.io.IOException;

import static org.aoclient.engine.Sound.SND_CLICK;
import static org.aoclient.engine.Sound.playSound;
import static org.aoclient.engine.utils.Time.FPS;

/**
 * Formulario principal que proporciona la interfaz de usuario durante la partida activa.
 * <p>
 * La clase {@code FMain} representa la pantalla principal que se muestra una vez que el usuario ha iniciado sesion correctamente
 * y esta jugando activamente. Esta clase extiende {@link Form} y actua como el nucleo de la interfaz grafica durante el
 * gameplay.
 * <p>
 * Esta interfaz principal contiene todos los elementos esenciales para que el jugador interactue con el mundo , incluyendo:
 * <ul>
 * <li>Estadisticas del personaje (vida, mana, energia, hambre, sed, etc.)
 * <li>Gestion del inventario y hechizos
 * <li>Visualizacion de oro y experiencia
 * <li>Consola de mensajes del sistema
 * <li>Chat para comunicacion con otros jugadores
 * <li>Acceso a configuraciones y otras funcionalidades
 * </ul>
 * <p>
 * Ademas de mostrar informacion, esta clase maneja la entrada de texto para comandos y chat, procesando las interacciones del
 * usuario y comunicandose con el servidor cuando es necesario a traves del sistema de protocolos de red.
 */

public final class FMain extends Form {

    private final ImString sendText = new ImString();
    private final int backgroundInventorySpells;
    private boolean viewInventory;

    private static final int STAT_WIDTH = 69;
    private static final int STAT_HEIGHT = 10;
    private static final float[] STAT_ALIGN = {0.5f, 0.5f};
    private static final int TRANSPARENT_COLOR = ImGui.getColorU32(0f, 0f, 0f, 0f);

    public FMain() {
        this.viewInventory = true;
        USER.getUserInventory().setVisible(true);

        Console.INSTANCE.clearConsole();

        try {
            this.backgroundImage = loadTexture("VentanaPrincipal");
            this.backgroundInventorySpells = loadTexture("CentroHechizos");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render() {
        ImGui.setNextWindowSize(Window.INSTANCE.getWidth() + 10, Window.INSTANCE.getHeight() + 5, ImGuiCond.Once);
        ImGui.setNextWindowPos(-5, -1, ImGuiCond.Once);

        // Start Custom window
        ImGui.begin(this.getClass().getSimpleName(), ImGuiWindowFlags.NoTitleBar |
                ImGuiWindowFlags.NoMove |
                ImGuiWindowFlags.NoFocusOnAppearing |
                ImGuiWindowFlags.NoDecoration |
                ImGuiWindowFlags.NoBackground |
                ImGuiWindowFlags.NoResize |
                ImGuiWindowFlags.NoSavedSettings |
                ImGuiWindowFlags.NoBringToFrontOnFocus);


        ImGui.getWindowDrawList().addImage(backgroundImage, 0, 0, Window.INSTANCE.getWidth(), Window.INSTANCE.getHeight());

        this.drawShapes();

        // SKILLS BUTTON
        ImGui.setCursorPos(670, 45);
        if (ImGui.invisibleButton("viewSkills", 30, 30)) {
            playSound(SND_CLICK);
            IM_GUI_SYSTEM.show(new FSkills());
        }

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
        drawStat(591, 453, USER.getUserMinSTA() + "/" + USER.getUserMaxSTA());

        // lblMana
        drawStat(591, 477, USER.getUserMinMAN() + "/" + USER.getUserMaxMAN());

        // lblHP
        drawStat(591, 498, USER.getUserMinHP() + "/" + USER.getUserMaxHP());

        // lblHambre
        drawStat(591, 521, USER.getUserMinHAM() + "/" + USER.getUserMaxHAM());

        // lblSed
        ImGui.setCursorPos(591, 542);
        ImGui.pushStyleVar(ImGuiStyleVar.SelectableTextAlign, 0.5f, 0.5f);
        ImGui.pushStyleColor(ImGuiCol.HeaderHovered, ImGui.getColorU32(0f, 0f, 0f, 0f));
        ImGui.pushStyleColor(ImGuiCol.HeaderActive, ImGui.getColorU32(0f, 0f, 0f, 0f));
        ImGui.selectable(USER.getUserMinAGU() + "/" + USER.getUserMaxAGU(), false, ImGuiSelectableFlags.None, 69, 10);
        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popStyleVar();

        // lblLvl
        ImGui.setCursorPos(625, 78);
        ImGui.text("Nivel: " + USER.getUserLvl());

        //lblPorcLvl
        ImGui.setCursorPos(624, 90);
        if (USER.getUserPasarNivel() > 0) {
            ImGui.textColored(0.5f, 1, 1, 1,
                    "[" + Math.round((float) (USER.getUserExp() * 100) / USER.getUserPasarNivel()) + "%]");
        } else ImGui.textColored(0.5f, 1, 1, 1, "[N/A]");


        //lblExp
        ImGui.setCursorPos(625, 102);
        ImGui.text("Exp: " + USER.getUserExp() + "/" + USER.getUserPasarNivel());

        // gldLbl (color)
        ImGui.setCursorPos(730, 419);
        ImGui.textColored(1, 1, 0.0f, 1, String.valueOf(USER.getUserGLD()));

        // lblDext (color)
        ImGui.setCursorPos(613, 413);
        ImGui.textColored(1, 0.5f, 0.0f, 1, String.valueOf(USER.getUserDext()));

        // lblStrg (color)
        ImGui.setCursorPos(650, 413);
        ImGui.textColored(0.1f, 0.6f, 0.1f, 1, String.valueOf(USER.getUserStrg()));

        // lblArmor (color)
        ImGui.setCursorPos(88, 579);
        ImGui.pushStyleVar(ImGuiStyleVar.SelectableTextAlign, 0.5f, 0.5f);
        ImGui.pushStyleColor(ImGuiCol.HeaderHovered, ImGui.getColorU32(0f, 0f, 0f, 0f));
        ImGui.pushStyleColor(ImGuiCol.HeaderActive, ImGui.getColorU32(0f, 0f, 0f, 0f));
        ImGui.pushStyleColor(ImGuiCol.Text, ImGui.getColorU32(1f, 0f, 0f, 1f));
        ImGui.selectable(USER.getUserArmourEqpDef(), false, ImGuiSelectableFlags.None, 45, 10);
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
        ImGui.selectable(USER.getUserShieldEqpDef(), false, ImGuiSelectableFlags.None, 45, 10);
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
        ImGui.selectable(USER.getUserHelmEqpDef(), false, ImGuiSelectableFlags.None, 45, 10);
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
        ImGui.selectable(USER.getUserWeaponEqpHit(), false, ImGuiSelectableFlags.None, 45, 10);
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

        ImGui.selectable(USER.getUserMap() + " X:" + USER.getUserPos().getX() + " Y:" + USER.getUserPos().getY(),
                false, ImGuiSelectableFlags.None, 90, 12);

        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popStyleVar();

        // lblName
        ImGui.setCursorPos(584, 24);
        ImGui.textColored(1, 0.0f, 0.0f, 1, USER.getUserName());

        // btnOptions
        ImGui.setCursorPos(681, 485);
        if (ImGui.invisibleButton("viewOptions", 95, 22)) {
            playSound(SND_CLICK);
            IM_GUI_SYSTEM.show(new FOptions());
        }

        // STATS BUTTON
        ImGui.setCursorPos(681, 510);
        if (ImGui.invisibleButton("viewStats", 95, 22)) {
            playSound(SND_CLICK);
            IM_GUI_SYSTEM.show(new FStats());
        }

        // btnClanes
        ImGui.setCursorPos(681, 530);
        if (ImGui.invisibleButton("viewGuild", 95, 22)) {
            playSound(SND_CLICK);
            Protocol.writeRequestGuildLeaderInfo();
        }

        // btnClose
        ImGui.setCursorPos(775, 3);
        if (ImGui.invisibleButton("close", 17, 17)) {
            playSound(SND_CLICK);
            Engine.closeClient();
        }

        // btnMinimizar
        ImGui.setCursorPos(755, 3);
        if (ImGui.invisibleButton("minimizar", 17, 17)) {
            playSound(SND_CLICK);
            Window.INSTANCE.minimizar();
        }

        // btnGLD
        ImGui.setCursorPos(710, 417);
        ImGui.invisibleButton("Tirar Oro", 17, 17);

        // txtSend
        if (USER.isTalking()) {
            ImGui.setCursorPos(15, 123);

            ImGui.pushItemWidth(546);
            ImGui.pushStyleColor(ImGuiCol.FrameBg, 0f, 0f, 0f, 1);

            ImGui.setKeyboardFocusHere();
            ImGui.pushID("sendText");
            ImGui.inputText("", sendText, ImGuiInputTextFlags.CallbackResize);
            ImGui.popID();

            ImGui.popStyleColor();
            ImGui.popItemWidth();
        }

        // para hacer click derecho y abrir el frm
        if (ImGui.beginPopupContextItem("Tirar Oro")) {
            playSound(SND_CLICK);
            IM_GUI_SYSTEM.show(new FCantidad(true));
            ImGui.endPopup();
        }

        ImGui.setCursorPos(592, 128);
        if (ImGui.invisibleButton("ViewInvetory", 93, 30)) {
            playSound(SND_CLICK);
            USER.getUserInventory().setVisible(true);
            this.viewInventory = true;
        }

        ImGui.setCursorPos(688, 128);
        if (ImGui.invisibleButton("ViewInvetorySpells", 75, 30)) {
            playSound(SND_CLICK);
            USER.getUserInventory().setVisible(false);
            this.viewInventory = false;
        }

        /////// Inventory
        if (viewInventory) USER.getUserInventory().drawInventory();
        else {
            ImGui.setCursorPos(586, 126);
            ImGui.image(backgroundInventorySpells, 198, 282);
            USER.getInventorySpells().draw();
        }

        USER.getUserInventory().updateTimers();

        /////// Console
        Console.INSTANCE.drawConsole();

        ImGui.end();
    }

    private void drawStat(int x, int y, String text) {
        ImGui.setCursorPos(x, y);
        ImGui.pushStyleVar(ImGuiStyleVar.SelectableTextAlign, STAT_ALIGN[0], STAT_ALIGN[1]);
        ImGui.pushStyleColor(ImGuiCol.HeaderHovered, TRANSPARENT_COLOR);
        ImGui.pushStyleColor(ImGuiCol.HeaderActive, TRANSPARENT_COLOR);
        ImGui.selectable(text, false, ImGuiSelectableFlags.None, STAT_WIDTH, STAT_HEIGHT);
        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popStyleVar();
    }

    private void drawShapes() {
        final ImDrawList drawList = ImGui.getWindowDrawList();
        final int shpColor = ImGui.getColorU32(0, 0, 0, 1);

        // shpEnergia
        float bWidth = (((float) (USER.getUserMinSTA()) / ((float) USER.getUserMaxSTA())) * 75);
        drawList.addRectFilled(
                (584 + bWidth), 453,
                584 + 75,
                453 + 12, shpColor
        );

        // shpMana
        bWidth = (((float) (USER.getUserMinMAN()) / ((float) USER.getUserMaxMAN())) * 75);
        drawList.addRectFilled(
                (584 + bWidth), 477,
                584 + 75,
                477 + 12, shpColor
        );

        // shpVida
        bWidth = (((float) (USER.getUserMinHP()) / ((float) USER.getUserMaxHP())) * 75);
        drawList.addRectFilled(
                (584 + bWidth), 498,
                584 + 75,
                498 + 12, shpColor
        );

        // shpHambre
        bWidth = (((float) (USER.getUserMinHAM()) / ((float) USER.getUserMaxHAM())) * 75);
        drawList.addRectFilled(
                (584 + bWidth), 521,
                584 + 75,
                521 + 12, shpColor
        );

        // shpSed
        bWidth = (((float) (USER.getUserMinAGU()) / ((float) USER.getUserMaxAGU())) * 75);
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
