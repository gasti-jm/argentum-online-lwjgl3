package org.aoclient.engine.renderer;

import java.nio.ByteBuffer;

public class TextureData {
    private final ByteBuffer buffer;
    private final int width;
    private final int height;

    public TextureData(ByteBuffer buffer, int width, int height) {
        this.buffer = buffer;
        this.width = width;
        this.height = height;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
