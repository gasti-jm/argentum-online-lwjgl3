package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.console.Console;
import org.aoclient.engine.game.console.FontStyle;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.PacketBuffer;
import org.aoclient.network.protocol.Protocol;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class PingHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer buffer) {
        buffer.readByte();
        Console.INSTANCE.addMsgToConsole("The ping is " + String.format("%.2f", glfwGetTime() - Protocol.pingTime) + " ms", FontStyle.REGULAR, new RGBColor(1f, 0f, 0f));
        Protocol.pingTime = 0;
    }

}
