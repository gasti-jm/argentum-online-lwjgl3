package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import org.aoclient.network.protocol.command.execution.CommandExecutor;

import static org.aoclient.network.protocol.command.metadata.GameCommand.*;

public final class FPanelGM extends Form {

    private final ImString myText = new ImString(64);
    private final CommandExecutor commandExecutor = CommandExecutor.INSTANCE;

    @Override
    public void render() {
        ImGui.setNextWindowSize(355, 380, ImGuiCond.Always);
        ImGui.begin(this.getClass().getSimpleName(), ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize);

        ImGui.text("GM Panel:");
        ImGui.separator();

        this.drawButtons();

        ImGui.end();
    }

    private void drawButtons() {

        if (ImGui.beginTabBar("GMTabBar")) {
            if (ImGui.beginTabItem("Message")) {
                ImGui.inputText("Arguments", myText);
                ImGui.separator();
                if (ImGui.button(TIME.getCommand(), 170, 20)) commandExecutor.execute(TIME.getCommand());
                if (ImGui.button(MOTD_CHANGE.getCommand(), 170, 20)) commandExecutor.execute(MOTD_CHANGE.getCommand());
                if (ImGui.button(TALKAS.getCommand(), 170, 20)) commandExecutor.execute(TALKAS.getCommand());
                if (ImGui.button(GMSG.getCommand(), 170, 20)) commandExecutor.execute(GMSG.getCommand() + " " + myText.get());
                if (ImGui.button(RMSG.getCommand(), 170, 20)) commandExecutor.execute(RMSG.getCommand() + " " + myText.get());
                if (ImGui.button(SMSG.getCommand(), 170, 20)) commandExecutor.execute(SMSG.getCommand() + " " + myText.get());
                if (ImGui.button(REALMSG.getCommand(), 170, 20)) commandExecutor.execute(REALMSG.getCommand() + " " + myText.get());
                if (ImGui.button(CAOSMSG.getCommand(), 170, 20)) commandExecutor.execute(CAOSMSG.getCommand() + " " + myText.get());
                if (ImGui.button(CIUMSG.getCommand(), 170, 20)) commandExecutor.execute(CIUMSG.getCommand() + " " + myText.get());

                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Info")) {
                ImGui.inputText("Arguments", myText);
                ImGui.separator();
                if (ImGui.button(SHOW.getCommand() + " sos", 170, 20)) commandExecutor.execute(SHOW.getCommand() + " sos");
                if (ImGui.button(CLEAR.getCommand() + " sos", 170, 20)) commandExecutor.execute(CLEAR.getCommand() + " sos");
                if (ImGui.button(SEE_WORKERS.getCommand(), 170, 20)) commandExecutor.execute(SEE_WORKERS.getCommand());
                if (ImGui.button(SHOW_HIDDEN_PLAYERS.getCommand(), 170, 20)) commandExecutor.execute(SHOW_HIDDEN_PLAYERS.getCommand());
                if (ImGui.button(SHOW_MOBS.getCommand(), 170, 20)) commandExecutor.execute(SHOW_MOBS.getCommand() + " " + myText.get());
                if (ImGui.button(ONLINE_MAP.getCommand(), 170, 20)) commandExecutor.execute(ONLINE_MAP.getCommand());
                if (ImGui.button(ONLINE_ROYAL.getCommand(), 170, 20)) commandExecutor.execute(ONLINE_ROYAL.getCommand());
                if (ImGui.button(ONLINE_CHAOS.getCommand(), 170, 20)) commandExecutor.execute(ONLINE_CHAOS.getCommand());
                if (ImGui.button(ONLINE_GM.getCommand(), 170, 20)) commandExecutor.execute(ONLINE_GM.getCommand());

                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Player")) {
                ImGui.text("FALTA POR DESARROLLAR!!!");
                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Me")) {
                ImGui.inputText("Arguments", myText);
                ImGui.separator();
                if (ImGui.button(INVISIBILITY.getCommand(), 170, 20)) commandExecutor.execute(INVISIBILITY.getCommand());
                if (ImGui.button(SHOWIGNORED.getCommand(), 170, 20)) commandExecutor.execute(SHOWIGNORED.getCommand());
                if (ImGui.button(NAVE.getCommand(), 170, 20)) commandExecutor.execute(NAVE.getCommand());
                if (ImGui.button(SHOW_NAME.getCommand(), 170, 20)) commandExecutor.execute(SHOW_NAME.getCommand());
                if (ImGui.button(CHAT_COLOR.getCommand(), 170, 20)) commandExecutor.execute(CHAT_COLOR.getCommand());
                if (ImGui.button(SETDESC.getCommand(), 170, 20)) commandExecutor.execute(SETDESC.getCommand() + " " + myText.get());
                if (ImGui.button(REM.getCommand(), 170, 20)) commandExecutor.execute(REM.getCommand() + " " + myText.get());

                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("World")) {
                ImGui.inputText("Arguments", myText);
                ImGui.separator();
                if (ImGui.button(RAIN.getCommand(), 170, 20)) commandExecutor.execute(RAIN.getCommand());
                if (ImGui.button(CLEAN.getCommand(), 170, 20)) commandExecutor.execute(CLEAN.getCommand());
                if (ImGui.button(CC.getCommand(), 170, 20)) commandExecutor.execute(CC.getCommand());
                if (ImGui.button(CREATE_TELEPORT.getCommand(), 170, 20)) commandExecutor.execute(CREATE_TELEPORT.getCommand() + " " + myText.get());
                if (ImGui.button(DESTROYTELEPORT.getCommand(), 170, 20)) commandExecutor.execute(DESTROYTELEPORT.getCommand());
                if (ImGui.button(CREATE_OBJ.getCommand(), 170, 20)) commandExecutor.execute(CREATE_OBJ.getCommand() + " " + myText.get());
                if (ImGui.button(DESTROYITEMS.getCommand(), 170, 20)) commandExecutor.execute(DESTROYITEMS.getCommand());
                if (ImGui.button(SHOW_OBJ_MAP.getCommand(), 170, 20)) commandExecutor.execute(SHOW_OBJ_MAP.getCommand());
                if (ImGui.button(REMOVE_ITEM_AREA.getCommand(), 170, 20)) commandExecutor.execute(REMOVE_ITEM_AREA.getCommand());

                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Admin")) {
                ImGui.inputText("Arguments", myText);
                ImGui.separator();
                if (ImGui.button(IP2NICK.getCommand(), 170, 20)) commandExecutor.execute(IP2NICK.getCommand() + " " + myText.get());
                if (ImGui.button(BAN_IP.getCommand(), 170, 20)) commandExecutor.execute(BAN_IP.getCommand() + " " + myText.get());
                if (ImGui.button(UNBANIP.getCommand(), 170, 20)) commandExecutor.execute(UNBANIP.getCommand() + " " + myText.get());
                if (ImGui.button(BANIPLIST.getCommand(), 170, 20)) commandExecutor.execute(BANIPLIST.getCommand());
                if (ImGui.button(BANIPRELOAD.getCommand(), 170, 20)) commandExecutor.execute(BANIPRELOAD.getCommand());
                if (ImGui.button(GUILD_MSG_HISTORY.getCommand(), 170, 20)) commandExecutor.execute(GUILD_MSG_HISTORY.getCommand());
                if (ImGui.button(GUILD_MEMBER_LIST.getCommand(), 170, 20)) commandExecutor.execute(GUILD_MEMBER_LIST.getCommand() + " " + myText.get());
                if (ImGui.button(GUILD_BAN.getCommand(), 170, 20)) commandExecutor.execute(GUILD_BAN.getCommand() + " " + myText.get());

                ImGui.endTabItem();
            }

            ImGui.setCursorPos(1, 349);
            if (ImGui.button("Close", 350, 20)) this.close();
            ImGui.endTabBar();
        }

    }

}
