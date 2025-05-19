package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.Console;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.PacketBuffer;
import org.aoclient.network.protocol.Protocol;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class PongHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer data) {
        data.readByte();

        Console.INSTANCE.addMsgToConsole("El ping es " + (int) (glfwGetTime() - Protocol.pingTime) + " ms.", false, false, new RGBColor(1f, 0f, 0f));
        Protocol.pingTime = 0;
    }

}
