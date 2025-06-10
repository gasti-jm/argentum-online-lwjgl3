package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import org.aoclient.network.protocol.command.CommandProcessor;

public final class FPanelGM extends Form {

    private final ImString myText = new ImString(64);
    private final CommandProcessor commandProcessor = CommandProcessor.INSTANCE;

    @Override
    public void render() {
        ImGui.setNextWindowSize(355, 380, ImGuiCond.Always);
        ImGui.begin(this.getClass().getSimpleName(), ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize);

        ImGui.text("Panel GM:");
        ImGui.separator();

        this.drawButtons();

        ImGui.end();
    }

    private void drawButtons() {

        if (ImGui.beginTabBar("GMTabBar")) {
            if (ImGui.beginTabItem("Message")) {
                ImGui.inputText("Argumentos", myText);
                ImGui.separator();
                if (ImGui.button("/HORA", 170, 20)) commandProcessor.process("/HORA");
                if (ImGui.button("/MOTDCAMBIA", 170, 20)) commandProcessor.process("/MOTDCAMBIA");
                if (ImGui.button("/TALKAS", 170, 20)) commandProcessor.process("/TALKAS");
                if (ImGui.button("/GMSG", 170, 20)) commandProcessor.process("/GMSG " + myText.get());
                if (ImGui.button("/RMSG", 170, 20)) commandProcessor.process("/RMSG " + myText.get());
                if (ImGui.button("/SMSG", 170, 20)) commandProcessor.process("/SMSG " + myText.get());
                if (ImGui.button("/REALMSG", 170, 20)) commandProcessor.process("/REALMSG " + myText.get());
                if (ImGui.button("/CAOSMSG", 170, 20)) commandProcessor.process("/CAOSMSG " + myText.get());
                if (ImGui.button("/CIUMSG", 170, 20)) commandProcessor.process("/CIUMSG " + myText.get());

                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Info")) {
                ImGui.inputText("Argumentos", myText);
                ImGui.separator();
                if (ImGui.button("/SHOW SOS", 170, 20)) commandProcessor.process("/SHOW SOS");
                if (ImGui.button("/BORRAR SOS", 170, 20)) commandProcessor.process("/BORRAR SOS");
                if (ImGui.button("/TRABAJANDO", 170, 20)) commandProcessor.process("/TRABAJANDO");
                if (ImGui.button("/OCULTANDO", 170, 20)) commandProcessor.process("/OCULTANDO");
                if (ImGui.button("/NENE", 170, 20)) commandProcessor.process("/NENE " + myText.get());
                if (ImGui.button("/ONLINEMAP", 170, 20)) commandProcessor.process("/ONLINEMAP");
                if (ImGui.button("/ONLINEREAL", 170, 20)) commandProcessor.process("/ONLINEREAL");
                if (ImGui.button("/ONLINECAOS", 170, 20)) commandProcessor.process("/ONLINECAOS");
                if (ImGui.button("/ONLINEGM", 170, 20)) commandProcessor.process("/ONLINEGM");

                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Player")) {
                ImGui.text("FALTA POR DESARROLLAR!!!");
                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Me")) {
                ImGui.inputText("Argumentos", myText);
                ImGui.separator();
                if (ImGui.button("/INVISIBLE", 170, 20)) commandProcessor.process("/INVISIBLE");
                if (ImGui.button("/IGNORADO", 170, 20)) commandProcessor.process("/IGNORADO");
                if (ImGui.button("/NAVE", 170, 20)) commandProcessor.process("/NAVE");
                if (ImGui.button("/SHOWNAME", 170, 20)) commandProcessor.process("/SHOWNAME");
                if (ImGui.button("/CHATCOLOR", 170, 20)) commandProcessor.process("/CHATCOLOR");
                if (ImGui.button("/SETDESC", 170, 20)) commandProcessor.process("/SETDESC " + myText.get());
                if (ImGui.button("/REM", 170, 20)) commandProcessor.process("/REM " + myText.get());

                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("World")) {
                ImGui.inputText("Argumentos", myText);
                ImGui.separator();
                if (ImGui.button("/LLUVIA", 170, 20)) commandProcessor.process("/LLUVIA");
                if (ImGui.button("/LIMPIAR", 170, 20)) commandProcessor.process("/LIMPIAR");
                if (ImGui.button("/CC", 170, 20)) commandProcessor.process("/CC");
                if (ImGui.button("/CT", 170, 20)) commandProcessor.process("/CT " + myText.get());
                if (ImGui.button("/DT", 170, 20)) commandProcessor.process("/DT");
                if (ImGui.button("/CI", 170, 20)) commandProcessor.process("/CI " + myText.get());
                if (ImGui.button("/DEST", 170, 20)) commandProcessor.process("/DEST");
                if (ImGui.button("/PISO", 170, 20)) commandProcessor.process("/PISO");
                if (ImGui.button("/MASSDEST", 170, 20)) commandProcessor.process("/MASSDEST");

                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Admin")) {
                ImGui.inputText("Argumentos", myText);
                ImGui.separator();
                if (ImGui.button("/IP2NICK", 170, 20)) commandProcessor.process("/IP2NICK " + myText.get());
                if (ImGui.button("/BANIP", 170, 20)) commandProcessor.process("/BANIP " + myText.get());
                if (ImGui.button("/UNBANIP", 170, 20)) commandProcessor.process("/UNBANIP " + myText.get());
                if (ImGui.button("/BANIPLIST", 170, 20)) commandProcessor.process("/BANIPLIST");
                if (ImGui.button("/BANIPRELOAD", 170, 20)) commandProcessor.process("/BANIPRELOAD");
                if (ImGui.button("/SHOWCMSG", 170, 20)) commandProcessor.process("/SHOWCMSG");
                if (ImGui.button("/MIEMBROSCLAN", 170, 20)) commandProcessor.process("/MIEMBROSCLAN " + myText.get());
                if (ImGui.button("/BANCLAN", 170, 20)) commandProcessor.process("/BANCLAN " + myText.get());

                ImGui.endTabItem();
            }

            ImGui.setCursorPos(1, 349);
            if (ImGui.button("Cerrar", 350, 20)) this.close();
            ImGui.endTabBar();
        }

    }

}
