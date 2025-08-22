package org.aoclient.engine.gui.forms;

import imgui.ImDrawList;
import imgui.ImGui;
import imgui.flag.*;
import imgui.type.ImString;
import org.aoclient.engine.Engine;
import org.aoclient.engine.Window;
import org.aoclient.engine.game.console.Console;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.widgets.ImageButton3State;
import org.aoclient.network.protocol.Protocol;

import java.io.IOException;

import static org.aoclient.engine.audio.Sound.SND_CLICK;
import static org.aoclient.engine.audio.Sound.playSound;
import static org.aoclient.engine.gui.forms.FCreateCharacter.sendCreate;
import static org.aoclient.engine.utils.Time.FPS;

/**
 * Formulario principal que proporciona la interfaz de usuario durante la partida activa.
 * <p>
 * La clase {@code FMain} representa la pantalla principal que se muestra una vez que el usuario ha iniciado sesión correctamente
 * y está jugando activamente. Esta clase extiende {@link Form} y actúa como el núcleo de la interfaz gráfica durante el gameplay.
 * <p>
 * Funcionalidades principales:
 * <ul>
 *   <li>Muestra estadísticas del personaje (vida, maná, energía, hambre, sed, etc.).</li>
 *   <li>Permite la gestión del inventario y hechizos, alternando entre ambas vistas.</li>
 *   <li>Visualiza oro, experiencia y nivel del personaje.</li>
 *   <li>Integra una consola de mensajes del sistema y un chat para comunicación con otros jugadores.</li>
 *   <li>Incluye botones para acceder a habilidades, estadísticas, clanes, opciones y manejo de oro.</li>
 *   <li>Permite el cierre y minimizado de la ventana principal del cliente.</li>
 *   <li>Gestiona la entrada de texto para comandos y chat, y procesa las interacciones del usuario.</li>
 *   <li>Comunica acciones relevantes con el servidor mediante el sistema de protocolos de red.</li>
 * </ul>
 * <p>
 * La clase está organizada en métodos privados que separan la lógica de renderizado en secciones específicas para mejorar la legibilidad y el mantenimiento.
 * <p>
 * <b>Nota:</b> Todos los elementos gráficos se dibujan usando ImGui y se posicionan manualmente según el layout de la interfaz.
 */

public final class FMain extends Form {

    private final ImString sendText = new ImString();
    private final int backgroundInventorySpells;
    private boolean viewInventory;

    private static final int STAT_WIDTH = 69;
    private static final int STAT_HEIGHT = 10;
    private static final float[] STAT_ALIGN = {0.5f, 0.5f};
    private static final int TRANSPARENT_COLOR = ImGui.getColorU32(0f, 0f, 0f, 0f);

    private ImageButton3State btnMap;
    private ImageButton3State btnParty;
    private ImageButton3State btnOptions;
    private ImageButton3State btnStats;
    private ImageButton3State btnGuild;

    public FMain() {
        sendCreate = false; // una vez logiado resetiamos esto para que se pueda volver a crear pj.
        this.viewInventory = true;
        USER.getUserInventory().setVisible(true);

        Console.INSTANCE.clearConsole();

        try {
            this.backgroundImage = loadTexture("VentanaPrincipal");
            this.backgroundInventorySpells = loadTexture("CentroHechizos");

            // Instanciación de botones con 3 estados:
            btnMap = new ImageButton3State(
                    loadTexture("BotonMapaNormal"),
                    loadTexture("BotonMapaRollover"),
                    loadTexture("BotonMapaClick"),
                    682, 445, 93, 20
            );
            btnParty = new ImageButton3State(
                    loadTexture("BotonGrupoNormal"),
                    loadTexture("BotonGrupoRollover"),
                    loadTexture("BotonGrupoClick"),
                    681, 466, 94, 21
            );
            btnOptions = new ImageButton3State(
                    loadTexture("BotonOpcionesNormal"),
                    loadTexture("BotonOpcionesRollover"),
                    loadTexture("BotonOpcionesClick"),
                    681, 485, 95, 22
            );
            btnStats = new ImageButton3State(
                    loadTexture("BotonEstadisticasNormal"),
                    loadTexture("BotonEstadisticasRollover"),
                    loadTexture("BotonEstadisticasClick"),
                    681, 507, 95, 24
            );
            btnGuild = new ImageButton3State(
                    loadTexture("BotonClanesNormal"),
                    loadTexture("BotonClanesRollover"),
                    loadTexture("BotonClanesClick"),
                    683, 532, 92, 26
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render() {
        ImGui.setNextWindowSize(Window.INSTANCE.getWidth() + 10, Window.INSTANCE.getHeight() + 5, ImGuiCond.Once);
        ImGui.setNextWindowPos(-5, -1, ImGuiCond.Once);

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
        this.renderStats();
        this.renderLevelInfo();
        this.renderEquipmentInfo();
        this.renderFPS();
        this.drawButtons();
        this.renderInventorySection();
        USER.getUserInventory().updateTimers();
        Console.INSTANCE.drawConsole();
        ImGui.end();
    }

    /**
     * Dibuja un stat o valor de equipo con colores personalizados.
     */
    private void drawColoredStat(int x, int y, String text, float r, float g, float b, float a, int width, int height) {
        ImGui.setCursorPos(x, y);
        ImGui.pushStyleVar(ImGuiStyleVar.SelectableTextAlign, 0.5f, 0.5f);
        ImGui.pushStyleColor(ImGuiCol.HeaderHovered, TRANSPARENT_COLOR);
        ImGui.pushStyleColor(ImGuiCol.HeaderActive, TRANSPARENT_COLOR);
        ImGui.pushStyleColor(ImGuiCol.Text, ImGui.getColorU32(r, g, b, a));
        ImGui.selectable(text, false, ImGuiSelectableFlags.None, width, height);
        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popStyleVar();
    }

    /**
     * Dibuja un botón invisible en la posición y tamaño indicados. Devuelve true si fue presionado.
     */
    private boolean drawButton(int x, int y, int w, int h, String label) {
        ImGui.setCursorPos(x, y);
        return ImGui.invisibleButton(label, w, h);
    }

    // Sección de estadísticas (vida, mana, energía, hambre, sed)
    private void renderStats() {
        drawColoredStat(591, 453, USER.getUserMinSTA() + "/" + USER.getUserMaxSTA(), 1, 1, 1, 1, 69, 10);
        drawColoredStat(591, 477, USER.getUserMinMAN() + "/" + USER.getUserMaxMAN(), 1, 1, 1, 1, 69, 10);
        drawColoredStat(591, 498, USER.getUserMinHP() + "/" + USER.getUserMaxHP(), 1, 1, 1, 1, 69, 10);
        drawColoredStat(591, 521, USER.getUserMinHAM() + "/" + USER.getUserMaxHAM(), 1, 1, 1, 1, 69, 10);
        drawColoredStat(591, 542, USER.getUserMinAGU() + "/" + USER.getUserMaxAGU(), 1, 1, 1, 1, 69, 10);
    }

    // Información de nivel, experiencia y nombre
    private void renderLevelInfo() {
        ImGui.setCursorPos(625, 78);
        ImGui.text("Nivel: " + USER.getUserLvl());
        ImGui.setCursorPos(624, 90);
        if (USER.getUserPasarNivel() > 0) {
            ImGui.textColored(0.5f, 1, 1, 1,
                    "[" + Math.round((float) (USER.getUserExp() * 100) / USER.getUserPasarNivel()) + "%]");
        } else ImGui.textColored(0.5f, 1, 1, 1, "[N/A]");
        ImGui.setCursorPos(625, 102);
        ImGui.text("Exp: " + USER.getUserExp() + "/" + USER.getUserPasarNivel());
        ImGui.setCursorPos(584, 24);
        ImGui.textColored(1, 0.0f, 0.0f, 1, USER.getUserName());
    }

    // Información de equipo y stats secundarios
    private void renderEquipmentInfo() {
        drawColoredStat(730, 419, String.valueOf(USER.getUserGLD()), 1, 1, 0.0f, 1, 45, 10);
        drawColoredStat(598, 413, String.valueOf(USER.getUserDext()), 1, 0.5f, 0.0f, 1, 45, 10);
        drawColoredStat(635, 413, String.valueOf(USER.getUserStrg()), 0.1f, 0.6f, 0.1f, 1, 45, 10);
        drawColoredStat(88, 579, USER.getUserArmourEqpDef(), 1f, 0f, 0f, 1f, 45, 10);
        drawColoredStat(354, 579, USER.getUserShieldEqpDef(), 1f, 0f, 0f, 1f, 45, 10);
        drawColoredStat(206, 579, USER.getUserHelmEqpDef(), 1f, 0f, 0f, 1f, 45, 10);
        drawColoredStat(472, 579, USER.getUserWeaponEqpHit(), 1f, 0f, 0f, 1f, 45, 10);
        drawColoredStat(590, 574, USER.getUserMap() + " X:" + USER.getUserPos().getX() + " Y:" + USER.getUserPos().getY(), 1f, 1f, 0f, 1f, 90, 12);
    }

    // FPS
    private void renderFPS() {
        final String txtFPS = String.valueOf(FPS);
        ImGui.setCursorPos(448, 4);
        ImGui.pushStyleVar(ImGuiStyleVar.SelectableTextAlign, 0.5f, 0.5f);
        ImGui.pushStyleColor(ImGuiCol.HeaderHovered, TRANSPARENT_COLOR);
        ImGui.pushStyleColor(ImGuiCol.HeaderActive, TRANSPARENT_COLOR);
        ImGui.selectable(txtFPS, false, ImGuiSelectableFlags.None, 28, 10);
        ImGui.popStyleColor();
        ImGui.popStyleColor();
        ImGui.popStyleVar();
    }

    // Botones principales
    private void drawButtons() {
        /*-=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=-
                         WINDOWS CONTROLS BUTTONS
        -=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=-*/
        if (drawButton(775, 3, 17, 17, "close")) {
            playSound(SND_CLICK);
            Engine.closeClient();
        }
        if (drawButton(755, 3, 17, 17, "minimizar")) {
            playSound(SND_CLICK);
            Window.INSTANCE.minimizar();
        }
        /*-=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=-
                                 SKILL BUTTONS
        -=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=-*/
        if (drawButton(670, 45, 30, 30, "viewSkills")) {
            playSound(SND_CLICK);
            IM_GUI_SYSTEM.show(new FSkills());
        }
        /*-=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=-
                                MENU BUTTONS
        -=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=-*/
        if (btnMap.render()){
            playSound(SND_CLICK);
            IM_GUI_SYSTEM.show(new FMapa());
        }
        if (btnParty.render()) {
            playSound(SND_CLICK);
            //TODO: FParty
        }
        if (btnOptions.render()) {
            playSound(SND_CLICK);
            IM_GUI_SYSTEM.show(new FOptions());
        }
        if(btnStats.render()) {
            playSound(SND_CLICK);
            IM_GUI_SYSTEM.show(new FStats());
        }
        if(btnGuild.render()){
            playSound(SND_CLICK);
            Protocol.requestGuildLeaderInfo();
        }

        /*-=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=-
                       INVENTORY AND SPELLS BUTTONS
        -=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=-*/

        if (drawButton(592, 128, 93, 30, "ViewInvetory")) {
            playSound(SND_CLICK);
            USER.getUserInventory().setVisible(true);
            this.viewInventory = true;
        }
        if (drawButton(688, 128, 75, 30, "ViewInvetorySpells")) {
            playSound(SND_CLICK);
            USER.getUserInventory().setVisible(false);
            this.viewInventory = false;
        }

        if (ImGUISystem.INSTANCE.isMainLast()) {
            drawButton(710, 417, 17, 17, "Tirar Oro");
            if (ImGui.beginPopupContextItem("Tirar Oro")) {
                playSound(SND_CLICK);
                IM_GUI_SYSTEM.show(new FCantidad(true));
                ImGui.endPopup();
            }
        }

        /*-=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=-
                                CHAT BUTTONS
        -=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=--=-*/
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
    }

    // Inventario y hechizos
    private void renderInventorySection() {
        if (viewInventory) USER.getUserInventory().drawInventory();
        else {
            ImGui.setCursorPos(586, 126);
            ImGui.image(backgroundInventorySpells, 198, 282);
            USER.getInventorySpells().draw();
        }
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
