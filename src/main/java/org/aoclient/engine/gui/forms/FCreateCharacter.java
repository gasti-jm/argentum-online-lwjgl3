package org.aoclient.engine.gui.forms;

import imgui.ImDrawList;
import imgui.ImGui;

import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImInt;
import imgui.type.ImString;
import org.aoclient.network.SocketConnection;
import org.aoclient.engine.Window;
import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.E_Cities;
import org.aoclient.engine.game.models.E_Class;
import org.aoclient.engine.game.models.E_Heading;
import org.aoclient.engine.game.models.E_Raza;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.engine.renderer.Surface;
import org.aoclient.engine.renderer.Texture;
import org.aoclient.engine.utils.inits.BodyData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.aoclient.engine.Sound.*;
import static org.aoclient.engine.game.models.Character.*;
import static org.aoclient.engine.renderer.Drawn.*;
import static org.aoclient.engine.utils.GameData.*;
import static org.aoclient.network.Protocol.*;

public final class FCreateCharacter extends Form{
    // Necesito hacer esto para dibujar despues el cuerpo y cabeza por encima de la interfaz
    private final Texture background;

    // Text Boxes
    private final ImString txtNombre = new ImString(20);
    private final ImString txtPassword = new ImString();
    private final ImString txtConfirmPassword = new ImString();
    private final ImString txtMail = new ImString();

    // Combo Boxes
    private final ImInt currentItemHogar = new ImInt(0);
    private final String[] strCities = new String[E_Cities.values().length];

    private final ImInt currentItemRaza = new ImInt(0);
    private final String[] strRazas = new String[E_Raza.values().length];

    private final ImInt currentItemClass = new ImInt(0);
    private final String[] strClass = new String[E_Class.values().length];

    private final ImInt currentItemGenero = new ImInt(0);
    private final String[] strGenero = {"Hombre", "Mujer"};

    // para el dibujado
    private int dir;
    private final RGBColor color;

    private int userHead;
    private int userBody;
    private BodyData bodyGraphic;

    static class RECT {
        private int top, left, right, bottom;
    }
    private RECT characterPos;

    public FCreateCharacter(){
        this.background     = Surface.get().createTexture("resources/gui/VentanaCrearPersonaje.jpg", true);
        this.userHead       = HUMANO_H_PRIMER_CABEZA;
        this.userBody       = HUMANO_H_CUERPO_DESNUDO;
        this.dir            = E_Heading.SOUTH.value;
        this.bodyGraphic    = new BodyData(bodyData[userBody]);
        this.color          = new RGBColor(1,1,1);
        this.characterPos   = new RECT();

        // pos
        this.characterPos.left = 472;
        this.characterPos.top = 425;
        // size
        this.characterPos.right = 41;
        this.characterPos.bottom = 65;


        this.loadComboBoxes();
        this.giveBodyAndHead();
    }

    /**
     * Carga los datos que tendran almacenados los combo boxes.
     */
    private void loadComboBoxes() {
        // Esto hay que internacionalizarlo en algun momento, es puro hardcodeo.

        // Hogar
        for (int i = 0; i < E_Cities.values().length; i++){
            strCities[i] = E_Cities.values()[i].name();
        }

        // Raza
        for (int i = 0; i < E_Raza.values().length; i++){
            strRazas[i] = E_Raza.values()[i].name().replace("_", " "); // para quitar el "_" del enum
        }

        // Clases
        for (int i = 0; i < E_Class.values().length; i++){
            strClass[i] = E_Class.values()[i].name();
        }

    }

    @Override
    public void render() {
        geometryBoxRenderGUI(background, 0, 0, 1.0f);
        this.updateHeadSelection();

        ImGui.setNextWindowSize(Window.get().getWidth() + 10, Window.get().getHeight() + 5, ImGuiCond.Once);
        ImGui.setNextWindowPos(-5, -1, ImGuiCond.Once);

        // Start Custom window
        ImGui.begin(this.getClass().getSimpleName(), ImGuiWindowFlags.NoTitleBar |
                ImGuiWindowFlags.NoMove |
                ImGuiWindowFlags.NoFocusOnAppearing |
                ImGuiWindowFlags.NoDecoration |
                ImGuiWindowFlags.NoBackground |
                ImGuiWindowFlags.NoResize |
                ImGuiWindowFlags.NoCollapse |
                ImGuiWindowFlags.NoSavedSettings |
                ImGuiWindowFlags.NoBringToFrontOnFocus);


        final ImDrawList drawList   = ImGui.getWindowDrawList();
        final int shpColor          = ImGui.getColorU32(1f, 0f, 0f, 1f);

        // btnVolver
        ImGui.setCursorPos(92, 548);
        if (ImGui.invisibleButton("btnVolver", 86, 30)) {
            this.buttonGoBack();
        }

        // btnCreateCharacter
        ImGui.setCursorPos(610,546);
        if(ImGui.invisibleButton("btnCreate", 174, 29)) {
            this.buttonCreateCharacter();
        }

        //  btnTirarDados
        ImGui.setCursorPos(13, 185);
        if (ImGui.invisibleButton("btnTirarDados", 60, 60)) {
            this.buttonThrowDices();
        }


        // txtNombre
        ImGui.setCursorPos(238, 86);
        ImGui.pushItemWidth(337);
            ImGui.pushStyleColor(ImGuiCol.FrameBg, 0, 0,0, 1);
                ImGui.pushID("txtNombre");
                    ImGui.inputText("", txtNombre, ImGuiInputTextFlags.CallbackCharFilter);
                ImGui.popID();
            ImGui.popStyleColor();
        ImGui.popItemWidth();

        // txtPasswd
        ImGui.setCursorPos(238, 120);
        ImGui.pushItemWidth(160);
            ImGui.pushStyleColor(ImGuiCol.FrameBg, 0, 0,0, 1);
                ImGui.pushID("txtPassword");
                    ImGui.inputText("", txtPassword, ImGuiInputTextFlags.Password | ImGuiInputTextFlags.CallbackResize);
                ImGui.popID();
            ImGui.popStyleColor();
        ImGui.popItemWidth();

        // txtPasswd
        ImGui.setCursorPos(414, 120);
        ImGui.pushItemWidth(160);
            ImGui.pushStyleColor(ImGuiCol.FrameBg, 0, 0,0, 1);
                ImGui.pushID("txtConfirmPassword");
                    ImGui.inputText("", txtConfirmPassword, ImGuiInputTextFlags.Password | ImGuiInputTextFlags.CallbackResize);
                ImGui.popID();
            ImGui.popStyleColor();
        ImGui.popItemWidth();

        // txtMail
        ImGui.setCursorPos(238, 152);
        ImGui.pushItemWidth(337);
            ImGui.pushStyleColor(ImGuiCol.FrameBg, 0, 0,0, 1);
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
                if(ImGui.combo("", currentItemRaza, strRazas, strRazas.length)) {
                    this.giveBodyAndHead();
                }
            ImGui.popID();
        ImGui.popItemWidth();

        // cbClase
        ImGui.setCursorPos(408, 269);
        ImGui.pushItemWidth(175);
            ImGui.pushID("cbClase");
                ImGui.combo("", currentItemClass, strClass, strClass.length);
            ImGui.popID();
        ImGui.popItemWidth();

        // cbGenero
        ImGui.setCursorPos(408, 304);
        ImGui.pushItemWidth(175);
            ImGui.pushID("cbGenero");
                if(ImGui.combo("", currentItemGenero, strGenero, strGenero.length)) {
                    this.giveBodyAndHead();
                }
            ImGui.popID();
        ImGui.popItemWidth();

        // HeadPJ(0)
        ImGui.setCursorPos(410, 395);
        if(ImGui.invisibleButton("leftChangeHead", 16, 15)) {
            this.headPJButton(0);
        }

        // HeadPJ(1)
        ImGui.setCursorPos(569, 395);
        if(ImGui.invisibleButton("rightChangeHead", 16, 15)) {
            this.headPJButton(1);
        }


        // DirPJ(0)
        ImGui.setCursorPos(477, 488);
        if(ImGui.invisibleButton("leftDir", 16, 15)) {
            dirPJButton(0);
        }

        // DirPJ(1)
        ImGui.setCursorPos(502, 488);
        if(ImGui.invisibleButton("rightDir", 16, 15)) {
            dirPJButton(1);
        }

        //480, 392
        drawList.addRect(
                479, 391,
                505,
                418, shpColor
        );


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
        if(direction > E_Heading.WEST.value) direction = E_Heading.NORTH.value;
        if(direction < E_Heading.NORTH.value) direction = E_Heading.WEST.value;

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
        drawHead(checkCabeza(head), 2); head++;
        drawHead(checkCabeza(head), 3); head++;
        drawHead(checkCabeza(head), 4);

        head = this.userHead; head--;
        drawHead(checkCabeza(head), 1); head--;
        drawHead(checkCabeza(head), 0);
    }

    private void drawHead(int head, int index) {
        final int headGraphic = headData[head].getHead(dir).getGrhIndex();
        this.bodyGraphic.getWalk(dir).setStarted(true);

        switch (index) {
            case 0:
                drawGrhIndex(headGraphic, 429, 395, color);
                break;
            case 1:
                drawGrhIndex(headGraphic, 456, 395, color);
                break;

            case 2:
                drawGrhIndex(headGraphic, 483, 395, color);

                drawTexture(bodyGraphic.getWalk(dir),
                        (characterPos.left + (characterPos.right / 2)) - (grhData[bodyGraphic.getWalk(dir).getGrhIndex()].getPixelWidth() / 2) - 4,
                        450,
                        true, true, false, 1.0f, color);

                // gnomo o enano
                if (currentItemRaza.get() == 3 || currentItemRaza.get() == 4) {
                    drawGrhIndex(headGraphic,
                            492 - (grhData[headGraphic].getPixelWidth() / 2),
                            487 - (grhData[headGraphic].getPixelHeight()), color);
                } else {
                    drawGrhIndex(headGraphic,
                            492 - (grhData[headGraphic].getPixelWidth() / 2),
                            478 - (grhData[headGraphic].getPixelHeight()), color);
                }


                break;
            case 3:
                drawGrhIndex(headGraphic, 510, 395, color);
                break;

            case 4:
                drawGrhIndex(headGraphic, 537, 395, color);
                break;
        }
    }

    private int checkCabeza(int head) {
        int retHead = head;
        E_Raza razaSelected = E_Raza.values()[currentItemRaza.get()];

        switch (currentItemGenero.get()) {
            case 0: // hombre
                switch (razaSelected) {
                    case Humano:
                        if(head > HUMANO_H_ULTIMA_CABEZA) {
                            retHead = HUMANO_H_PRIMER_CABEZA + (head - HUMANO_H_ULTIMA_CABEZA) - 1;
                        } else if(head < HUMANO_H_PRIMER_CABEZA) {
                            retHead =  HUMANO_H_ULTIMA_CABEZA - (HUMANO_H_PRIMER_CABEZA - head) + 1;
                        }
                        break;

                    case Elfo:
                        if (head > ELFO_H_ULTIMA_CABEZA) {
                            retHead = ELFO_H_PRIMER_CABEZA + (head - ELFO_H_ULTIMA_CABEZA) - 1;
                        } else if(head < ELFO_H_PRIMER_CABEZA) {
                            retHead = ELFO_H_ULTIMA_CABEZA - (ELFO_H_PRIMER_CABEZA - head) + 1;
                        }
                        break;

                    case Elfo_Drow:
                        if (head > DROW_H_ULTIMA_CABEZA) {
                            retHead = DROW_H_PRIMER_CABEZA + (head - DROW_H_ULTIMA_CABEZA) - 1;
                        } else if (head < DROW_H_PRIMER_CABEZA) {
                            retHead = DROW_H_ULTIMA_CABEZA - (DROW_H_PRIMER_CABEZA - head) + 1;
                        }
                        break;

                    case Enano:
                        if (head > ENANO_H_ULTIMA_CABEZA) {
                            retHead = ENANO_H_PRIMER_CABEZA + (head - ENANO_H_ULTIMA_CABEZA) - 1;
                        } else if (head < ENANO_H_PRIMER_CABEZA) {
                            retHead = ENANO_H_ULTIMA_CABEZA - (ENANO_H_PRIMER_CABEZA - head) + 1;
                        }
                        break;

                    case Gnomo:
                        if (head > GNOMO_H_ULTIMA_CABEZA) {
                            retHead = GNOMO_H_PRIMER_CABEZA + (head - GNOMO_H_ULTIMA_CABEZA) - 1;
                        } else if (head < GNOMO_H_PRIMER_CABEZA) {
                            retHead = GNOMO_H_ULTIMA_CABEZA - (GNOMO_H_PRIMER_CABEZA - head) + 1;
                        }
                }
                break;

            case 1: // mujer
                switch (razaSelected) {
                    case Humano:
                        if(head > HUMANO_M_ULTIMA_CABEZA) {
                            retHead = HUMANO_M_PRIMER_CABEZA + (head - HUMANO_M_ULTIMA_CABEZA) - 1;
                        } else if(head < HUMANO_M_PRIMER_CABEZA) {
                            retHead =  HUMANO_M_ULTIMA_CABEZA - (HUMANO_M_PRIMER_CABEZA - head) + 1;
                        }
                        break;

                    case Elfo:
                        if (head > ELFO_M_ULTIMA_CABEZA) {
                            retHead = ELFO_M_PRIMER_CABEZA + (head - ELFO_M_ULTIMA_CABEZA) - 1;
                        } else if(head < ELFO_M_PRIMER_CABEZA) {
                            retHead = ELFO_M_ULTIMA_CABEZA - (ELFO_M_PRIMER_CABEZA - head) + 1;
                        }
                        break;

                    case Elfo_Drow:
                        if (head > DROW_M_ULTIMA_CABEZA) {
                            retHead = DROW_M_PRIMER_CABEZA + (head - DROW_M_ULTIMA_CABEZA) - 1;
                        } else if (head < DROW_M_PRIMER_CABEZA) {
                            retHead = DROW_M_ULTIMA_CABEZA - (DROW_M_PRIMER_CABEZA - head) + 1;
                        }
                        break;

                    case Enano:
                        if (head > ENANO_M_ULTIMA_CABEZA) {
                            retHead = ENANO_M_PRIMER_CABEZA + (head - ENANO_M_ULTIMA_CABEZA) - 1;
                        } else if (head < ENANO_M_PRIMER_CABEZA) {
                            retHead = ENANO_M_ULTIMA_CABEZA - (ENANO_M_PRIMER_CABEZA - head) + 1;
                        }
                        break;

                    case Gnomo:
                        if (head > GNOMO_M_ULTIMA_CABEZA) {
                            retHead = GNOMO_M_PRIMER_CABEZA + (head - GNOMO_M_ULTIMA_CABEZA) - 1;
                        } else if (head < GNOMO_M_PRIMER_CABEZA) {
                            retHead = GNOMO_M_ULTIMA_CABEZA - (GNOMO_M_PRIMER_CABEZA - head) + 1;
                        }
                }
        }

        return retHead;
    }

    private void buttonCreateCharacter() {
        final int userRaza = currentItemRaza.get() + 1;
        final int userSexo = currentItemGenero.get() + 1;
        final int userClase = currentItemClass.get() + 1;
        final int userHogar = currentItemHogar.get() + 1;

        if(!checkData()) return;

        new Thread(() -> {
            if (SocketConnection.get().connect()) {
                writeLoginNewChar(txtNombre.get(), txtPassword.get(), userRaza, userSexo, userClase, userHead, txtMail.get(), userHogar);
            }
        }).start();

        User.get().setUserName(txtNombre.get());
    }

    private void buttonThrowDices() {
        new Thread(() -> {
            if (SocketConnection.get().connect()) {
                writeThrowDices();
            }
        }).start();
    }

    private void buttonGoBack() {
        ImGUISystem.get().show(new FConnect());
        playMusic("6.ogg");
        this.close();
    }

    private void giveBodyAndHead(){
        E_Raza razaSelected = E_Raza.values()[currentItemRaza.get()];

        switch (currentItemGenero.get()) {
            case 0: // hombre
                switch (razaSelected){
                    case Humano:
                        this.userHead = HUMANO_H_PRIMER_CABEZA;
                        this.userBody = HUMANO_H_CUERPO_DESNUDO;
                        break;

                    case Elfo:
                        this.userHead = ELFO_H_PRIMER_CABEZA;
                        this.userBody = ELFO_H_CUERPO_DESNUDO;
                        break;

                    case Elfo_Drow:
                        this.userHead = DROW_H_PRIMER_CABEZA;
                        this.userBody = DROW_H_CUERPO_DESNUDO;
                        break;

                    case Enano:
                        this.userHead = ENANO_H_PRIMER_CABEZA;
                        this.userBody = ENANO_H_CUERPO_DESNUDO;
                        break;

                    case Gnomo:
                        this.userHead = GNOMO_H_PRIMER_CABEZA;
                        this.userBody = GNOMO_H_CUERPO_DESNUDO;
                        break;

                    default:
                        this.userHead = 0;
                        this.userBody = 0;

                }
                break;


            case 1: // mujer
                switch (razaSelected){
                    case Humano:
                        this.userHead = HUMANO_M_PRIMER_CABEZA;
                        this.userBody = HUMANO_M_CUERPO_DESNUDO;
                        break;

                    case Elfo:
                        this.userHead = ELFO_M_PRIMER_CABEZA;
                        this.userBody = ELFO_M_CUERPO_DESNUDO;
                        break;

                    case Elfo_Drow:
                        this.userHead = DROW_M_PRIMER_CABEZA;
                        this.userBody = DROW_M_CUERPO_DESNUDO;
                        break;

                    case Enano:
                        this.userHead = ENANO_M_PRIMER_CABEZA;
                        this.userBody = ENANO_M_CUERPO_DESNUDO;
                        break;

                    case Gnomo:
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
        if(txtNombre.get().isEmpty() || txtPassword.get().isEmpty() || txtMail.get().isEmpty()) {
            ImGUISystem.get().show(new FMessage("Por favor, rellene todos los campos."));
            return false;
        }

        if(txtNombre.get().endsWith(" ")) {
            ImGUISystem.get().show(new FMessage("Nombre invalido, se ha removido un espacio en blanco al final del nombre."));
            txtNombre.set(txtNombre.get().substring(0, txtNombre.get().length() - 1));
            return false;
        }

        if(!txtPassword.get().equals(txtConfirmPassword.get())) {
            ImGUISystem.get().show(new FMessage("Las contraseñas no son iguales. Recuerde que no se permite caracteres especiales."));
            return false;
        }

        if(!checkEmail(txtMail.get())) {
            ImGUISystem.get().show(new FMessage("Direccion de correo electronico invalido."));
            return false;
        }

        if (checkSpecialChar(txtPassword.get()) || txtPassword.get().contains(" ")) {
            ImGUISystem.get().show(new FMessage("Password invalido, no puede contener caracteres especiales ni espacios en blanco."));
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
}
