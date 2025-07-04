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
                if (ImGui.button("/hora", 170, 20)) commandProcessor.process("/hora");
                if (ImGui.button("/motdcambia", 170, 20)) commandProcessor.process("/motdcambia");
                if (ImGui.button("/talkas", 170, 20)) commandProcessor.process("/talkas");
                if (ImGui.button("/gmsg", 170, 20)) commandProcessor.process("/gmsg " + myText.get());
                if (ImGui.button("/rmsg", 170, 20)) commandProcessor.process("/rmsg " + myText.get());
                if (ImGui.button("/smsg", 170, 20)) commandProcessor.process("/smsg " + myText.get());
                if (ImGui.button("/realmsg", 170, 20)) commandProcessor.process("/realmsg " + myText.get());
                if (ImGui.button("/caosmsg", 170, 20)) commandProcessor.process("/caosmsg " + myText.get());
                if (ImGui.button("/ciumsg", 170, 20)) commandProcessor.process("/ciumsg " + myText.get());

                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Info")) {
                ImGui.inputText("Argumentos", myText);
                ImGui.separator();
                if (ImGui.button("/show sos", 170, 20)) commandProcessor.process("/show sos");
                if (ImGui.button("/borrar sos", 170, 20)) commandProcessor.process("/borrar sos");
                if (ImGui.button("/trabajando", 170, 20)) commandProcessor.process("/trabajando");
                if (ImGui.button("/ocultando", 170, 20)) commandProcessor.process("/ocultando");
                if (ImGui.button("/nene", 170, 20)) commandProcessor.process("/nene " + myText.get());
                if (ImGui.button("/onlinemap", 170, 20)) commandProcessor.process("/onlinemap");
                if (ImGui.button("/onlinereal", 170, 20)) commandProcessor.process("/onlinereal");
                if (ImGui.button("/onlinecaos", 170, 20)) commandProcessor.process("/onlinecaos");
                if (ImGui.button("/onlinegm", 170, 20)) commandProcessor.process("/onlinegm");

                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Player")) {
                ImGui.text("FALTA POR DESARROLLAR!!!");
                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Me")) {
                ImGui.inputText("Argumentos", myText);
                ImGui.separator();
                if (ImGui.button("/invisible", 170, 20)) commandProcessor.process("/invisible");
                if (ImGui.button("/ignorado", 170, 20)) commandProcessor.process("/ignorado");
                if (ImGui.button("/nave", 170, 20)) commandProcessor.process("/nave");
                if (ImGui.button("/showname", 170, 20)) commandProcessor.process("/showname");
                if (ImGui.button("/chatcolor", 170, 20)) commandProcessor.process("/chatcolor");
                if (ImGui.button("/setdesc", 170, 20)) commandProcessor.process("/setdesc " + myText.get());
                if (ImGui.button("/rem", 170, 20)) commandProcessor.process("/rem " + myText.get());

                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("World")) {
                ImGui.inputText("Argumentos", myText);
                ImGui.separator();
                if (ImGui.button("/lluvia", 170, 20)) commandProcessor.process("/lluvia");
                if (ImGui.button("/limpiar", 170, 20)) commandProcessor.process("/limpiar");
                if (ImGui.button("/cc", 170, 20)) commandProcessor.process("/cc");
                if (ImGui.button("/ct", 170, 20)) commandProcessor.process("/ct " + myText.get());
                if (ImGui.button("/dt", 170, 20)) commandProcessor.process("/dt");
                if (ImGui.button("/co", 170, 20)) commandProcessor.process("/co " + myText.get());
                if (ImGui.button("/dest", 170, 20)) commandProcessor.process("/dest");
                if (ImGui.button("/piso", 170, 20)) commandProcessor.process("/piso");
                if (ImGui.button("/massdest", 170, 20)) commandProcessor.process("/massdest");

                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Admin")) {
                ImGui.inputText("Argumentos", myText);
                ImGui.separator();
                if (ImGui.button("/ip2nick", 170, 20)) commandProcessor.process("/ip2nick " + myText.get());
                if (ImGui.button("/banip", 170, 20)) commandProcessor.process("/banip " + myText.get());
                if (ImGui.button("/unbanip", 170, 20)) commandProcessor.process("/unbanip " + myText.get());
                if (ImGui.button("/baniplist", 170, 20)) commandProcessor.process("/baniplist");
                if (ImGui.button("/banipreload", 170, 20)) commandProcessor.process("/banipreload");
                if (ImGui.button("/showcmsg", 170, 20)) commandProcessor.process("/showcmsg");
                if (ImGui.button("/miembrosclan", 170, 20)) commandProcessor.process("/miembrosclan " + myText.get());
                if (ImGui.button("/banclan", 170, 20)) commandProcessor.process("/banclan " + myText.get());

                ImGui.endTabItem();
            }

            ImGui.setCursorPos(1, 349);
            if (ImGui.button("Cerrar", 350, 20)) this.close();
            ImGui.endTabBar();
        }

    }

}
