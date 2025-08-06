package org.aoclient.engine.gui.forms;

import imgui.ImDrawList;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImInt;
import imgui.type.ImString;
import org.aoclient.engine.Window;
import org.aoclient.engine.game.Messages;
import org.aoclient.engine.game.models.City;
import org.aoclient.engine.game.models.Role;
import org.aoclient.engine.game.models.Direction;
import org.aoclient.engine.game.models.Race;
import org.aoclient.engine.gui.widgets.ImageButton3State;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.engine.renderer.Surface;
import org.aoclient.engine.renderer.Texture;
import org.aoclient.engine.utils.inits.BodyData;
import org.aoclient.network.Connection;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.aoclient.engine.audio.Sound.*;
import static org.aoclient.engine.game.Messages.MessageKey.GENDER_FEMININE;
import static org.aoclient.engine.game.Messages.MessageKey.GENDER_MALE;
import static org.aoclient.engine.game.models.Character.*;
import static org.aoclient.engine.renderer.Drawn.*;
import static org.aoclient.engine.utils.GameData.*;
import static org.aoclient.network.protocol.Protocol.loginNewChar;
import static org.aoclient.network.protocol.Protocol.throwDices;

/**
 * Formulario para la creacion de nuevos personajes.
 * <p>
 * La clase {@code FCreateCharacter} implementa la interfaz grafica que permite a los usuarios crear un nuevo personaje. Esta
 * pantalla proporciona todas las opciones necesarias para personalizar completamente un personaje antes de ingresar al mundo.
 * <p>
 * El formulario ofrece las siguientes funcionalidades principales:
 * <ul>
 * <li>Campos para ingresar datos de cuenta (nombre, contraseña, correo electronico)
 * <li>Selectores para elegir raza, clase, genero y ciudad de origen
 * <li>Visualizacion en tiempo real del personaje con la apariencia seleccionada
 * <li>Sistema de navegacion para cambiar entre diferentes cabezas disponibles
 * <li>Control para cambiar la direccion de visualizacion del personaje
 * <li>Boton para "tirar dados" y generar atributos de personaje aleatorios
 * <li>Validaciones de datos y envio de informacion al servidor
 * </ul>
 * <p>
 * Esta clase maneja tanto la presentacion visual de las opciones como la logica de validacion y la comunicacion con el servidor
 * para registrar el nuevo personaje, permitiendo a los nuevos jugadores personalizar su experiencia de juego desde el primer
 * momento.
 */

public final class FCreateCharacter extends Form {

    // Necesito hacer esto para dibujar despues el cuerpo y cabeza por encima de la interfaz
    private Texture background;

    // Botones con 3 estados
    private ImageButton3State btnVolver;
    private ImageButton3State btnCrearPj;

    // Text Boxes
    private final ImString txtNombre = new ImString(20);
    private final ImString txtPassword = new ImString();
    private final ImString txtConfirmPassword = new ImString();
    private final ImString txtMail = new ImString();

    // Combo Boxes
    private final ImInt currentItemHogar = new ImInt(0);
    private final String[] strCities = new String[City.values().length];

    private final ImInt currentItemRaza = new ImInt(0);
    private final String[] strRazas = new String[Race.values().length];

    private final ImInt currentItemClass = new ImInt(0);
    private final String[] role = new String[Role.values().length];

    private final ImInt currentItemGenero = new ImInt(0);
    private final String[] strGenero = new String[2];
    private RGBColor color;
    private RECT characterPos;
    // para el dibujado
    private int dir;
    private int userHead;
    private int userBody;
    private BodyData bodyGraphic;

    // --- NUEVO: ATRIBUTOS PARA LOS LABELS DE LOS ATRIBUTOS ---
    private int fuerza = 0;
    private int agilidad = 0;
    private int inteligencia = 0;
    private int carisma = 0;
    private int constitucion = 0;

    /**
     * Al crear un pj tenemos una pequeña demora, ya que el cliente se conecta al servidor. Pero si el usuario
     * empieza a mandar varias peticiones el buffer se rompe y no te termina logiando o logias y se te desconecta.
     */
    public static boolean sendCreate = false;

    public void setAtributos(int fuerza, int agilidad, int inteligencia, int carisma, int constitucion) {
        this.fuerza = fuerza;
        this.agilidad = agilidad;
        this.inteligencia = inteligencia;
        this.carisma = carisma;
        this.constitucion = constitucion;
    }

    public FCreateCharacter() {
        try {
            this.background = Surface.INSTANCE.createTexture("gui.ao", "VentanaCrearPersonaje", true);
            this.userHead = HUMANO_H_PRIMER_CABEZA;
            this.userBody = HUMANO_H_CUERPO_DESNUDO;
            this.dir = Direction.DOWN.getId();
            this.bodyGraphic = new BodyData(bodyData[userBody]);
            this.color = new RGBColor(1, 1, 1);
            this.characterPos = new RECT();

            // pos
            this.characterPos.left = 472;
            this.characterPos.top = 425;
            // size
            this.characterPos.right = 41;
            this.characterPos.bottom = 65;

            this.loadComboBoxes();
            this.giveBodyAndHead();


            btnVolver = new ImageButton3State(
                    loadTexture("BotonVolverRollover"),
                    loadTexture("BotonVolverRollover"),
                    loadTexture("BotonVolverClick"),
                    94, 546, 86, 30
            );

            btnCrearPj = new ImageButton3State(
                    loadTexture("BotonCrearPersonajeRollover"),
                    loadTexture("BotonCrearPersonajeRollover"),
                    loadTexture("BotonCrearPersonajeRollover"),
                    611, 546, 174, 29
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Carga los datos que tendran almacenados los combo boxes.
     */
    private void loadComboBoxes() {
        // Esto hay que internacionalizarlo en algun momento, es puro hardcodeo.
        // Hogar
        for (int i = 0; i < City.values().length; i++) {
            String name = City.values()[i].name().toLowerCase();
            strCities[i] = name.substring(0, 1).toUpperCase() + name.substring(1);
        }

        // Raza
        for (int i = 0; i < Race.values().length; i++)
            strRazas[i] = Race.values()[i].getName();
        // Clases
        for (int i = 0; i < Role.values().length; i++)
            role[i] = Role.values()[i].getName();

        strGenero[0] = Messages.get(GENDER_MALE);
        strGenero[1] = Messages.get(GENDER_FEMININE);
    }

    @Override
    public void render() {
        geometryBoxRenderGUI(background, 0, 0, 1.0f);
        this.updateHeadSelection();

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


        final ImDrawList drawList = ImGui.getWindowDrawList();
        final int shpColor = ImGui.getColorU32(1f, 0f, 0f, 1f);

        // btnVolver
        if(btnVolver.render()) {
            playSound(SND_CLICK);
            this.buttonGoBack();
        }

        // btnCreateCharacter
        if(btnCrearPj.render()) {
            playSound(SND_CLICK);
            this.buttonCreateCharacter();
        }

        //  btnTirarDados
        ImGui.setCursorPos(13, 185);
        if (ImGui.invisibleButton("btnTirarDados", 60, 60)) this.buttonThrowDices();

        // txtNombre
        ImGui.setCursorPos(238, 86);
        ImGui.pushItemWidth(337);
        ImGui.pushStyleColor(ImGuiCol.FrameBg, 0, 0, 0, 1);
        ImGui.pushID("txtNombre");
        ImGui.inputText("", txtNombre, ImGuiInputTextFlags.CallbackCharFilter);
        ImGui.popID();
        ImGui.popStyleColor();
        ImGui.popItemWidth();

        // txtPasswd
        ImGui.setCursorPos(238, 120);
        ImGui.pushItemWidth(160);
        ImGui.pushStyleColor(ImGuiCol.FrameBg, 0, 0, 0, 1);
        ImGui.pushID("txtPassword");
        ImGui.inputText("", txtPassword, ImGuiInputTextFlags.Password | ImGuiInputTextFlags.CallbackResize);
        ImGui.popID();
        ImGui.popStyleColor();
        ImGui.popItemWidth();

        // txtPasswd
        ImGui.setCursorPos(414, 120);
        ImGui.pushItemWidth(160);
        ImGui.pushStyleColor(ImGuiCol.FrameBg, 0, 0, 0, 1);
        ImGui.pushID("txtConfirmPassword");
        ImGui.inputText("", txtConfirmPassword, ImGuiInputTextFlags.Password | ImGuiInputTextFlags.CallbackResize);
        ImGui.popID();
        ImGui.popStyleColor();
        ImGui.popItemWidth();

        // txtMail
        ImGui.setCursorPos(238, 152);
        ImGui.pushItemWidth(337);
        ImGui.pushStyleColor(ImGuiCol.FrameBg, 0, 0, 0, 1);
        ImGui.pushID("txtMail");
        ImGui.inputText("", txtMail, ImGuiInputTextFlags.CallbackResize);
        ImGui.popID();
        ImGui.popStyleColor();
        ImGui.popItemWidth();

        // cbHogar
        ImGui.setCursorPos(408, 199);
        ImGui.pushItemWidth(175);
        ImGui.pushID("cbHogar");
        ImGui.combo("", currentItemHogar, strCities, strCities.length);
        ImGui.popID();
        ImGui.popItemWidth();

        // cbRaza
        ImGui.setCursorPos(408, 233);
        ImGui.pushItemWidth(175);
        ImGui.pushID("cbRaza");
        if (ImGui.combo("", currentItemRaza, strRazas, strRazas.length)) this.giveBodyAndHead();

        ImGui.popID();
        ImGui.popItemWidth();

        // cbClase
        ImGui.setCursorPos(408, 269);
        ImGui.pushItemWidth(175);
        ImGui.pushID("cbClase");
        ImGui.combo("", currentItemClass, role, role.length);
        ImGui.popID();
        ImGui.popItemWidth();

        // cbGenero
        ImGui.setCursorPos(408, 304);
        ImGui.pushItemWidth(175);
        ImGui.pushID("cbGenero");
        if (ImGui.combo("", currentItemGenero, strGenero, strGenero.length)) this.giveBodyAndHead();

        ImGui.popID();
        ImGui.popItemWidth();

        // HeadPJ(0)
        ImGui.setCursorPos(410, 395);
        if (ImGui.invisibleButton("leftChangeHead", 16, 15)) this.headPJButton(0);

        // HeadPJ(1)
        ImGui.setCursorPos(569, 395);
        if (ImGui.invisibleButton("rightChangeHead", 16, 15)) this.headPJButton(1);

        // DirPJ(0)
        ImGui.setCursorPos(477, 488);
        if (ImGui.invisibleButton("leftDir", 16, 15)) dirPJButton(0);

        // DirPJ(1)
        ImGui.setCursorPos(502, 488);
        if (ImGui.invisibleButton("rightDir", 16, 15)) dirPJButton(1);

        //480, 392
        drawList.addRect(
                479, 391,
                505,
                418, shpColor
        );

        // Mostrar atributos
        ImGui.setCursorPos(307, 232);
        ImGui.text(String.valueOf(fuerza));
        ImGui.setCursorPos(307, 253);
        ImGui.text(String.valueOf(agilidad));
        ImGui.setCursorPos(307, 275);
        ImGui.text(String.valueOf(inteligencia));
        ImGui.setCursorPos(307, 298);
        ImGui.text(String.valueOf(carisma));
        ImGui.setCursorPos(307, 320);
        ImGui.text(String.valueOf(constitucion));

        ImGui.end();
    }

    private void dirPJButton(int index) {
        switch (index) {
            case 0:
                this.dir = checkDir(dir + 1);
                break;
            case 1:
                this.dir = checkDir(dir - 1);
        }
    }

    private int checkDir(int direction) {
        if (direction > Direction.LEFT.getId()) direction = Direction.UP.getId();
        if (direction < Direction.UP.getId()) direction = Direction.LEFT.getId();
        this.bodyGraphic.getWalk(dir).setFrameCounter(1);
        this.bodyGraphic.getWalk(dir).setStarted(false);
        return direction;
    }

    private void headPJButton(int index) {
        switch (index) {
            case 0:
                this.userHead = checkCabeza(userHead + 1);
                break;
            case 1:
                this.userHead = checkCabeza(userHead - 1);
        }
    }

    private void updateHeadSelection() {
        int head = this.userHead;
        drawHead(checkCabeza(head), 2);
        head++;
        drawHead(checkCabeza(head), 3);
        head++;
        drawHead(checkCabeza(head), 4);
        head = this.userHead;
        head--;
        drawHead(checkCabeza(head), 1);
        head--;
        drawHead(checkCabeza(head), 0);
    }

    private void drawHead(int head, int index) {
        final int headGraphic = headData[head].getHead(dir).getGrhIndex();
        this.bodyGraphic.getWalk(dir).setStarted(true);

        switch (index) {
            case 0:
                drawGrhIndexNoBatch(headGraphic, 429, 395, color);
                break;
            case 1:
                drawGrhIndexNoBatch(headGraphic, 456, 395, color);
                break;
            case 2:
                drawGrhIndexNoBatch(headGraphic, 483, 395, color);

                drawTextureNoBatch(bodyGraphic.getWalk(dir),
                        (characterPos.left + (characterPos.right / 2)) - (grhData[bodyGraphic.getWalk(dir).getGrhIndex()].getPixelWidth() / 2) - 4,
                        450,
                        true, true, false, 1.0f, color);
                // gnomo o enano
                if (currentItemRaza.get() == 3 || currentItemRaza.get() == 4) {
                    drawGrhIndexNoBatch(headGraphic,
                            492 - (grhData[headGraphic].getPixelWidth() / 2),
                            487 - (grhData[headGraphic].getPixelHeight()), color);
                } else {
                    drawGrhIndexNoBatch(headGraphic,
                            492 - (grhData[headGraphic].getPixelWidth() / 2),
                            478 - (grhData[headGraphic].getPixelHeight()), color);
                }
                break;
            case 3:
                drawGrhIndexNoBatch(headGraphic, 510, 395, color);
                break;

            case 4:
                drawGrhIndexNoBatch(headGraphic, 537, 395, color);
                break;
        }
    }

    private int checkCabeza(int head) {
        int retHead = head;
        Race razaSelected = Race.values()[currentItemRaza.get()];

        switch (currentItemGenero.get()) {
            case 0: // hombre
                switch (razaSelected) {
                    case HUMAN:
                        if (head > HUMANO_H_ULTIMA_CABEZA) retHead = HUMANO_H_PRIMER_CABEZA + (head - HUMANO_H_ULTIMA_CABEZA) - 1;
                        else if (head < HUMANO_H_PRIMER_CABEZA)
                            retHead = HUMANO_H_ULTIMA_CABEZA - (HUMANO_H_PRIMER_CABEZA - head) + 1;
                        break;
                    case ELF:
                        if (head > ELFO_H_ULTIMA_CABEZA)
                            retHead = ELFO_H_PRIMER_CABEZA + (head - ELFO_H_ULTIMA_CABEZA) - 1;
                        else if (head < ELFO_H_PRIMER_CABEZA)
                            retHead = ELFO_H_ULTIMA_CABEZA - (ELFO_H_PRIMER_CABEZA - head) + 1;
                        break;
                    case DROW_ELF:
                        if (head > DROW_H_ULTIMA_CABEZA)
                            retHead = DROW_H_PRIMER_CABEZA + (head - DROW_H_ULTIMA_CABEZA) - 1;
                        else if (head < DROW_H_PRIMER_CABEZA)
                            retHead = DROW_H_ULTIMA_CABEZA - (DROW_H_PRIMER_CABEZA - head) + 1;
                        break;
                    case DWARF:
                        if (head > ENANO_H_ULTIMA_CABEZA)
                            retHead = ENANO_H_PRIMER_CABEZA + (head - ENANO_H_ULTIMA_CABEZA) - 1;
                        else if (head < ENANO_H_PRIMER_CABEZA)
                            retHead = ENANO_H_ULTIMA_CABEZA - (ENANO_H_PRIMER_CABEZA - head) + 1;
                        break;
                    case GNOME:
                        if (head > GNOMO_H_ULTIMA_CABEZA)
                            retHead = GNOMO_H_PRIMER_CABEZA + (head - GNOMO_H_ULTIMA_CABEZA) - 1;
                        else if (head < GNOMO_H_PRIMER_CABEZA)
                            retHead = GNOMO_H_ULTIMA_CABEZA - (GNOMO_H_PRIMER_CABEZA - head) + 1;
                }
                break;
            case 1: // mujer
                switch (razaSelected) {
                    case HUMAN:
                        if (head > HUMANO_M_ULTIMA_CABEZA)
                            retHead = HUMANO_M_PRIMER_CABEZA + (head - HUMANO_M_ULTIMA_CABEZA) - 1;
                        else if (head < HUMANO_M_PRIMER_CABEZA)
                            retHead = HUMANO_M_ULTIMA_CABEZA - (HUMANO_M_PRIMER_CABEZA - head) + 1;
                        break;
                    case ELF:
                        if (head > ELFO_M_ULTIMA_CABEZA)
                            retHead = ELFO_M_PRIMER_CABEZA + (head - ELFO_M_ULTIMA_CABEZA) - 1;
                        else if (head < ELFO_M_PRIMER_CABEZA)
                            retHead = ELFO_M_ULTIMA_CABEZA - (ELFO_M_PRIMER_CABEZA - head) + 1;
                        break;
                    case DROW_ELF:
                        if (head > DROW_M_ULTIMA_CABEZA)
                            retHead = DROW_M_PRIMER_CABEZA + (head - DROW_M_ULTIMA_CABEZA) - 1;
                        else if (head < DROW_M_PRIMER_CABEZA)
                            retHead = DROW_M_ULTIMA_CABEZA - (DROW_M_PRIMER_CABEZA - head) + 1;
                        break;
                    case DWARF:
                        if (head > ENANO_M_ULTIMA_CABEZA)
                            retHead = ENANO_M_PRIMER_CABEZA + (head - ENANO_M_ULTIMA_CABEZA) - 1;
                        else if (head < ENANO_M_PRIMER_CABEZA)
                            retHead = ENANO_M_ULTIMA_CABEZA - (ENANO_M_PRIMER_CABEZA - head) + 1;
                        break;
                    case GNOME:
                        if (head > GNOMO_M_ULTIMA_CABEZA)
                            retHead = GNOMO_M_PRIMER_CABEZA + (head - GNOMO_M_ULTIMA_CABEZA) - 1;
                        else if (head < GNOMO_M_PRIMER_CABEZA)
                            retHead = GNOMO_M_ULTIMA_CABEZA - (GNOMO_M_PRIMER_CABEZA - head) + 1;
                }
        }

        return retHead;
    }

    private void buttonCreateCharacter() {
        if (sendCreate) return;

        final int userRaza = currentItemRaza.get() + 1;
        final int userSexo = currentItemGenero.get() + 1;
        final int userClase = currentItemClass.get() + 1;
        final int userHogar = currentItemHogar.get() + 1;

        if (!checkData()) return;

        new Thread(() -> {
            if (Connection.INSTANCE.connect())
                loginNewChar(txtNombre.get(), txtPassword.get(), userRaza, userSexo, userClase, userHead, txtMail.get(), userHogar);
        }).start();

        USER.setUserName(txtNombre.get());

        // listo basta no mandes mas peticiones al servidor.
        sendCreate = true;
    }

    private void buttonThrowDices() {
        new Thread(() -> {
            if (Connection.INSTANCE.connect()) throwDices();
        }).start();
    }

    private void buttonGoBack() {
        IM_GUI_SYSTEM.show(new FConnect());
        playMusic("6.ogg");

        btnVolver.delete();
        btnCrearPj.delete();
        this.close();
    }

    private void giveBodyAndHead() {
        Race razaSelected = Race.values()[currentItemRaza.get()];

        switch (currentItemGenero.get()) {
            case 0: // hombre
                switch (razaSelected) {
                    case HUMAN:
                        this.userHead = HUMANO_H_PRIMER_CABEZA;
                        this.userBody = HUMANO_H_CUERPO_DESNUDO;
                        break;
                    case ELF:
                        this.userHead = ELFO_H_PRIMER_CABEZA;
                        this.userBody = ELFO_H_CUERPO_DESNUDO;
                        break;
                    case DROW_ELF:
                        this.userHead = DROW_H_PRIMER_CABEZA;
                        this.userBody = DROW_H_CUERPO_DESNUDO;
                        break;
                    case DWARF:
                        this.userHead = ENANO_H_PRIMER_CABEZA;
                        this.userBody = ENANO_H_CUERPO_DESNUDO;
                        break;
                    case GNOME:
                        this.userHead = GNOMO_H_PRIMER_CABEZA;
                        this.userBody = GNOMO_H_CUERPO_DESNUDO;
                        break;
                    default:
                        this.userHead = 0;
                        this.userBody = 0;
                }
                break;

            case 1: // mujer
                switch (razaSelected) {
                    case HUMAN:
                        this.userHead = HUMANO_M_PRIMER_CABEZA;
                        this.userBody = HUMANO_M_CUERPO_DESNUDO;
                        break;
                    case ELF:
                        this.userHead = ELFO_M_PRIMER_CABEZA;
                        this.userBody = ELFO_M_CUERPO_DESNUDO;
                        break;
                    case DROW_ELF:
                        this.userHead = DROW_M_PRIMER_CABEZA;
                        this.userBody = DROW_M_CUERPO_DESNUDO;
                        break;
                    case DWARF:
                        this.userHead = ENANO_M_PRIMER_CABEZA;
                        this.userBody = ENANO_M_CUERPO_DESNUDO;
                        break;
                    case GNOME:
                        this.userHead = GNOMO_M_PRIMER_CABEZA;
                        this.userBody = GNOMO_M_CUERPO_DESNUDO;
                        break;
                    default:
                        this.userHead = 0;
                        this.userBody = 0;
                }
                break;

            default:
                this.userHead = 0;
                this.userBody = 0;
        }

        this.bodyGraphic = new BodyData(bodyData[userBody]);
        this.bodyGraphic.getWalk(dir).setFrameCounter(1);
        this.bodyGraphic.getWalk(dir).setStarted(false);
    }

    private boolean checkData() {
        if (txtNombre.get().isEmpty() || txtPassword.get().isEmpty() || txtMail.get().isEmpty()) {
            IM_GUI_SYSTEM.show(new FMessage("Por favor, rellene todos los campos."));
            return false;
        }

        if (txtNombre.get().endsWith(" ")) {
            IM_GUI_SYSTEM.show(new FMessage("Nombre invalido, se ha removido un espacio en blanco al final del nombre."));
            txtNombre.set(txtNombre.get().substring(0, txtNombre.get().length() - 1));
            return false;
        }

        if (!txtPassword.get().equals(txtConfirmPassword.get())) {
            IM_GUI_SYSTEM.show(new FMessage("Las contraseñas no son iguales. Recuerde que no se permite caracteres especiales."));
            return false;
        }

        if (!checkEmail(txtMail.get())) {
            IM_GUI_SYSTEM.show(new FMessage("Direccion de correo electronico invalido."));
            return false;
        }

        if (checkSpecialChar(txtPassword.get()) || txtPassword.get().contains(" ")) {
            IM_GUI_SYSTEM.show(new FMessage("Password invalido, no puede contener caracteres especiales ni espacios en blanco."));
            return false;
        }

        return true;
    }

    private boolean checkEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean checkSpecialChar(String text) {
        String regex = "[^\\p{L}0-9\\s]";
        return text.matches(".*" + regex + ".*");
    }

    static class RECT {
        private int top, left, right, bottom;
    }

}
