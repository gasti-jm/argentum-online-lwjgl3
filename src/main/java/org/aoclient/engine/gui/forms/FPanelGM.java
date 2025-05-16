package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import org.aoclient.engine.Window;
import org.aoclient.network.protocol.ProtocolCmdParse;

import static org.aoclient.engine.Sound.SND_CLICK;
import static org.aoclient.engine.Sound.playSound;
import static org.aoclient.engine.utils.GameData.options;

public final class FPanelGM extends Form {

    private final ImString myText = new ImString(64);
    private ProtocolCmdParse protocolCmdParse;

    @Override
    public void render() {
        ImGui.setNextWindowSize(355, 380, ImGuiCond.Always);
        ImGui.begin(this.getClass().getSimpleName(), ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize);

        ImGui.text("Panel GM:");
        ImGui.separator();

        protocolCmdParse = ProtocolCmdParse.getInstance();

        this.drawButtons();

        ImGui.end();
    }

    private void drawButtons() {

        if (ImGui.beginTabBar("GMTabBar")) {
            if (ImGui.beginTabItem("Message")) {
                ImGui.inputText("Argumentos", myText);
                ImGui.separator();
                if (ImGui.button("/HORA", 170, 20)) protocolCmdParse.parseUserCommand("/HORA");
                if (ImGui.button("/MOTDCAMBIA", 170, 20)) protocolCmdParse.parseUserCommand("/MOTDCAMBIA");
                if (ImGui.button("/TALKAS", 170, 20)) protocolCmdParse.parseUserCommand("/TALKAS");
                if (ImGui.button("/GMSG", 170, 20)) protocolCmdParse.parseUserCommand("/GMSG " + myText.get());
                if (ImGui.button("/RMSG", 170, 20)) protocolCmdParse.parseUserCommand("/RMSG " + myText.get());
                if (ImGui.button("/SMSG", 170, 20)) protocolCmdParse.parseUserCommand("/SMSG " + myText.get());
                if (ImGui.button("/REALMSG", 170, 20)) protocolCmdParse.parseUserCommand("/REALMSG " + myText.get());
                if (ImGui.button("/CAOSMSG", 170, 20)) protocolCmdParse.parseUserCommand("/CAOSMSG " + myText.get());
                if (ImGui.button("/CIUMSG", 170, 20)) protocolCmdParse.parseUserCommand("/CIUMSG " + myText.get());

                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Info")) {
                ImGui.inputText("Argumentos", myText);
                ImGui.separator();
                if (ImGui.button("/SHOW SOS", 170, 20)) protocolCmdParse.parseUserCommand("/SHOW SOS");
                if (ImGui.button("/BORRAR SOS", 170, 20)) protocolCmdParse.parseUserCommand("/BORRAR SOS");
                if (ImGui.button("/TRABAJANDO", 170, 20)) protocolCmdParse.parseUserCommand("/TRABAJANDO");
                if (ImGui.button("/OCULTANDO", 170, 20)) protocolCmdParse.parseUserCommand("/OCULTANDO");
                if (ImGui.button("/NENE", 170, 20)) protocolCmdParse.parseUserCommand("/NENE " + myText.get());
                if (ImGui.button("/ONLINEMAP", 170, 20)) protocolCmdParse.parseUserCommand("/ONLINEMAP");
                if (ImGui.button("/ONLINEREAL", 170, 20)) protocolCmdParse.parseUserCommand("/ONLINEREAL");
                if (ImGui.button("/ONLINECAOS", 170, 20)) protocolCmdParse.parseUserCommand("/ONLINECAOS");
                if (ImGui.button("/ONLINEGM", 170, 20)) protocolCmdParse.parseUserCommand("/ONLINEGM");

                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Player")) {
                ImGui.text("FALTA POR DESARROLLAR!!!");
                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Me")) {
                ImGui.inputText("Argumentos", myText);
                ImGui.separator();
                if (ImGui.button("/INVISIBLE", 170, 20)) protocolCmdParse.parseUserCommand("/INVISIBLE");
                if (ImGui.button("/IGNORADO", 170, 20)) protocolCmdParse.parseUserCommand("/IGNORADO");
                if (ImGui.button("/NAVE", 170, 20)) protocolCmdParse.parseUserCommand("/NAVE");
                if (ImGui.button("/SHOWNAME", 170, 20)) protocolCmdParse.parseUserCommand("/SHOWNAME");
                if (ImGui.button("/CHATCOLOR", 170, 20)) protocolCmdParse.parseUserCommand("/CHATCOLOR");
                if (ImGui.button("/SETDESC", 170, 20)) protocolCmdParse.parseUserCommand("/SETDESC " + myText.get());
                if (ImGui.button("/REM", 170, 20)) protocolCmdParse.parseUserCommand("/REM " + myText.get());

                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("World")) {
                ImGui.inputText("Argumentos", myText);
                ImGui.separator();
                if (ImGui.button("/LLUVIA", 170, 20)) protocolCmdParse.parseUserCommand("/LLUVIA");
                if (ImGui.button("/LIMPIAR", 170, 20)) protocolCmdParse.parseUserCommand("/LIMPIAR");
                if (ImGui.button("/CC", 170, 20)) protocolCmdParse.parseUserCommand("/CC");
                if (ImGui.button("/CT", 170, 20)) protocolCmdParse.parseUserCommand("/CT " + myText.get());
                if (ImGui.button("/DT", 170, 20)) protocolCmdParse.parseUserCommand("/DT");
                if (ImGui.button("/CI", 170, 20)) protocolCmdParse.parseUserCommand("/CI " + myText.get());
                if (ImGui.button("/DEST", 170, 20)) protocolCmdParse.parseUserCommand("/DEST");
                if (ImGui.button("/PISO", 170, 20)) protocolCmdParse.parseUserCommand("/PISO");
                if (ImGui.button("/MASSDEST", 170, 20)) protocolCmdParse.parseUserCommand("/MASSDEST");

                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Admin")) {
                ImGui.inputText("Argumentos", myText);
                ImGui.separator();
                if (ImGui.button("/IP2NICK", 170, 20)) protocolCmdParse.parseUserCommand("/IP2NICK " + myText.get());
                if (ImGui.button("/BANIP", 170, 20)) protocolCmdParse.parseUserCommand("/BANIP " + myText.get());
                if (ImGui.button("/UNBANIP", 170, 20)) protocolCmdParse.parseUserCommand("/UNBANIP " + myText.get());
                if (ImGui.button("/BANIPLIST", 170, 20)) protocolCmdParse.parseUserCommand("/BANIPLIST");
                if (ImGui.button("/BANIPRELOAD", 170, 20)) protocolCmdParse.parseUserCommand("/BANIPRELOAD");
                if (ImGui.button("/SHOWCMSG", 170, 20)) protocolCmdParse.parseUserCommand("/SHOWCMSG");
                if (ImGui.button("/MIEMBROSCLAN", 170, 20)) protocolCmdParse.parseUserCommand("/MIEMBROSCLAN " + myText.get());
                if (ImGui.button("/BANCLAN", 170, 20)) protocolCmdParse.parseUserCommand("/BANCLAN " + myText.get());

                ImGui.endTabItem();
            }

            ImGui.setCursorPos(1, 349);
            if (ImGui.button("Cerrar", 350, 20)) this.close();
            ImGui.endTabBar();
        }

    }

}
