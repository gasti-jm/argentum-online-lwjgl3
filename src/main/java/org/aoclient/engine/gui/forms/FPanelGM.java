package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import org.aoclient.engine.game.User;
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
                if (ImGui.button(CHANGE_MOTD.getCommand(), 170, 20)) commandExecutor.execute(CHANGE_MOTD.getCommand());
                if (ImGui.button(TALK_AS_NPC.getCommand(), 170, 20)) commandExecutor.execute(TALK_AS_NPC.getCommand());
                if (ImGui.button(GM_MSG.getCommand(), 170, 20)) commandExecutor.execute(GM_MSG.getCommand() + " " + myText.get());
                if (ImGui.button(ADMIN_SERVER.getCommand(), 170, 20)) commandExecutor.execute(ADMIN_SERVER.getCommand() + " " + myText.get());
                if (ImGui.button(SYSTEM_MESSAGE.getCommand(), 170, 20)) commandExecutor.execute(SYSTEM_MESSAGE.getCommand() + " " + myText.get());
                if (ImGui.button(ROYAL_ARMY_MSG.getCommand(), 170, 20)) commandExecutor.execute(ROYAL_ARMY_MSG.getCommand() + " " + myText.get());
                if (ImGui.button(CHAOS_MSG.getCommand(), 170, 20)) commandExecutor.execute(CHAOS_MSG.getCommand() + " " + myText.get());
                if (ImGui.button(CITIZEN_MSG.getCommand(), 170, 20)) commandExecutor.execute(CITIZEN_MSG.getCommand() + " " + myText.get());

                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Info")) {
                ImGui.inputText("Arguments", myText);
                ImGui.separator();
                if (ImGui.button(SHOW.getCommand() + " sos", 170, 20)) commandExecutor.execute(SHOW.getCommand() + " sos");
                if (ImGui.button(CLEAN_SOS.getCommand() + " sos", 170, 20)) commandExecutor.execute(CLEAN_SOS.getCommand() + " sos");
                if (ImGui.button(SHOW_WORKERS.getCommand(), 170, 20)) commandExecutor.execute(SHOW_WORKERS.getCommand());
                if (ImGui.button(SHOW_HIDDEN.getCommand(), 170, 20)) commandExecutor.execute(SHOW_HIDDEN.getCommand());
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
                if (ImGui.button(SHOW_IGNORED.getCommand(), 170, 20)) commandExecutor.execute(SHOW_IGNORED.getCommand());
                if (ImGui.button(NAVIGATION.getCommand(), 170, 20)) commandExecutor.execute(NAVIGATION.getCommand());
                if (ImGui.button(SHOW_NAME.getCommand(), 170, 20)) commandExecutor.execute(SHOW_NAME.getCommand());
                if (ImGui.button(CHAT_COLOR.getCommand(), 170, 20)) commandExecutor.execute(CHAT_COLOR.getCommand());
                if (ImGui.button(SET_DESC.getCommand(), 170, 20)) commandExecutor.execute(SET_DESC.getCommand() + " " + myText.get());
                if (ImGui.button(COMMENT_SERVER_LOG.getCommand(), 170, 20)) commandExecutor.execute(COMMENT_SERVER_LOG.getCommand() + " " + myText.get());

                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("World")) {
                ImGui.inputText("Arguments", myText);
                ImGui.separator();
                if (ImGui.button(RAIN.getCommand(), 170, 20)) commandExecutor.execute(RAIN.getCommand());
                if (ImGui.button(CLEAN_WORLD.getCommand(), 170, 20)) commandExecutor.execute(CLEAN_WORLD.getCommand());
                if (ImGui.button(MOB_PANEL.getCommand(), 170, 20)) commandExecutor.execute(MOB_PANEL.getCommand());
                if (ImGui.button(CREATE_TELEPORT.getCommand(), 170, 20)) commandExecutor.execute(CREATE_TELEPORT.getCommand() + " " + myText.get());
                if (ImGui.button(REMOVE_TELEPORT.getCommand(), 170, 20)) commandExecutor.execute(REMOVE_TELEPORT.getCommand());
                if (ImGui.button(CREATE_OBJ.getCommand(), 170, 20)) commandExecutor.execute(CREATE_OBJ.getCommand() + " " + myText.get());
                if (ImGui.button(REMOVE_ITEM.getCommand(), 170, 20)) commandExecutor.execute(REMOVE_ITEM.getCommand());
                if (ImGui.button(SHOW_OBJ_MAP.getCommand(), 170, 20)) commandExecutor.execute(SHOW_OBJ_MAP.getCommand());
                if (ImGui.button(REMOVE_ITEM_AREA.getCommand(), 170, 20)) commandExecutor.execute(REMOVE_ITEM_AREA.getCommand());

                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Admin")) {
                ImGui.inputText("Arguments", myText);
                ImGui.separator();
                if (ImGui.button(IP_TO_NICK.getCommand(), 170, 20)) commandExecutor.execute(IP_TO_NICK.getCommand() + " " + myText.get());
                if (ImGui.button(BAN_IP.getCommand(), 170, 20)) commandExecutor.execute(BAN_IP.getCommand() + " " + myText.get());
                if (ImGui.button(UNBAN_IP.getCommand(), 170, 20)) commandExecutor.execute(UNBAN_IP.getCommand() + " " + myText.get());
                if (ImGui.button(BAN_IP_LIST.getCommand(), 170, 20)) commandExecutor.execute(BAN_IP_LIST.getCommand());
                if (ImGui.button(BAN_IP_RELOAD.getCommand(), 170, 20)) commandExecutor.execute(BAN_IP_RELOAD.getCommand());
                if (ImGui.button(GUILD_MESSAGES.getCommand(), 170, 20)) commandExecutor.execute(GUILD_MESSAGES.getCommand());
                if (ImGui.button(GUILD_MEMBERS.getCommand(), 170, 20)) commandExecutor.execute(GUILD_MEMBERS.getCommand() + " " + myText.get());
                if (ImGui.button(GUILD_BAN.getCommand(), 170, 20)) commandExecutor.execute(GUILD_BAN.getCommand() + " " + myText.get());

                ImGui.endTabItem();
            }

            ImGui.setCursorPos(1, 349);
            if (ImGui.button("Close", 350, 20)) {
                User.INSTANCE.setUserBussy(false);
                this.close();
            }
            ImGui.endTabBar();
        }

    }

}
