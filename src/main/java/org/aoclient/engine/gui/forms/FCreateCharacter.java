package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.ImInt;
import imgui.ImString;
import imgui.enums.ImGuiCol;
import imgui.enums.ImGuiCond;
import imgui.enums.ImGuiInputTextFlags;
import imgui.enums.ImGuiWindowFlags;
import org.aoclient.connection.SocketConnection;
import org.aoclient.engine.Window;
import org.aoclient.engine.game.models.E_Cities;
import org.aoclient.engine.game.models.E_Class;
import org.aoclient.engine.game.models.E_Raza;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.renderer.Surface;
import org.aoclient.engine.renderer.TextureOGL;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import static org.aoclient.connection.Protocol.writeThrowDices;
import static org.aoclient.engine.Sound.SND_DICE;
import static org.aoclient.engine.Sound.playSound;
import static org.aoclient.engine.renderer.Drawn.geometryBoxRenderGUI;
import static org.aoclient.engine.utils.GameData.music;
import static org.aoclient.engine.utils.GameData.options;

public final class FCreateCharacter extends Form{
    // Necesito hacer esto para dibujar despues el cuerpo y cabeza por encima de la interfaz
    private final TextureOGL background;

    // Text Boxes
    private final ImString txtNombre = new ImString();
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

    public FCreateCharacter(){
        this.formName = "frmCreateCharacter";
        this.background = Surface.get().createTexture("resources/gui/VentanaCrearPersonaje.jpg", true);
        this.loadComboBoxes();
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

        ImGui.setNextWindowSize(Window.get().getWidth() + 10, Window.get().getHeight() + 5, ImGuiCond.Once);
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
                    ImGui.inputText("", txtNombre, ImGuiInputTextFlags.CallbackResize | ImGuiInputTextFlags.CallbackCharFilter);
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
                ImGui.combo("", currentItemRaza, strRazas, strRazas.length);
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
                ImGui.combo("", currentItemGenero, strGenero, strGenero.length);
            ImGui.popID();
        ImGui.popItemWidth();

        ImGui.end();
    }

    private void buttonCreateCharacter() {
        if(txtNombre.get().contains(" ")) {
           ImGUISystem.get().checkAddOrChange("frmMessage", new FMessage("Nombre invalido, remueva los espacios en blanco."));
           return;
        }

        final int userRaza = currentItemRaza.get() + 1;
        final int userSexo = currentItemGenero.get() + 1;
        final int userClase = currentItemClass.get() + 1;
        final int userHogar = currentItemHogar.get() + 1;
        final String userMail = txtMail.get();


        if(!txtPassword.get().equals(txtConfirmPassword.get())) {
            ImGUISystem.get().checkAddOrChange("frmMessage", new FMessage("Las contraseñas no son iguales. Recuerde que no se permite caracteres especiales."));
            return;
        }

        /*

    Dim i As Integer
    Dim CharAscii As Byte

    UserName = txtNombre.Text

    If Right$(UserName, 1) = " " Then
        UserName = RTrim$(UserName)
        MsgBox "Nombre invalido, se han removido los espacios al final del nombre"
    End If

    UserRaza = lstRaza.ListIndex + 1
    UserSexo = lstGenero.ListIndex + 1
    UserClase = lstProfesion.ListIndex + 1

    For i = 1 To NUMATRIBUTES
        UserAtributos(i) = Val(lblAtributos(i).Caption)
    Next i

    UserHogar = lstHogar.ListIndex + 1

    If Not CheckData Then Exit Sub

#If SeguridadAlkon Then
    UserPassword = md5.GetMD5String(txtPasswd.Text)
    Call md5.MD5Reset
#Else
    UserPassword = txtPasswd.Text
#End If

    For i = 1 To Len(UserPassword)
        CharAscii = Asc(mid$(UserPassword, i, 1))
        If Not LegalCharacter(CharAscii) Then
            MsgBox ("Password inválido. El caractér " & Chr$(CharAscii) & " no está permitido.")
            Exit Sub
        End If
    Next i

    UserEmail = txtMail.Text

#If UsarWrench = 1 Then
    frmMain.Socket1.HostName = CurServerIp
    frmMain.Socket1.RemotePort = CurServerPort
#End If

    EstadoLogin = E_MODO.CrearNuevoPj

#If UsarWrench = 1 Then
    If Not frmMain.Socket1.Connected Then
#Else
    If frmMain.Winsock1.State <> sckConnected Then
#End If
        MsgBox "Error: Se ha perdido la conexion con el server."
        Unload Me

    Else
        Call Login
    End If

    bShowTutorial = True
         */
    }

    private void buttonThrowDices() {
        SocketConnection.get().connect(options.getIpServer(), options.getPortServer());
        writeThrowDices();
        playSound(SND_DICE);
    }

    private void buttonGoBack() {
        ImGUISystem.get().checkAddOrChange("frmConnect", new FConnect());
        music.stop();

        this.close();
    }
}
