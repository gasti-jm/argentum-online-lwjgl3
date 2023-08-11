package org.aoclient.engine.renderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;

public class Renderer {
    private int width, height;
    private int x, y;

    public Renderer(int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public void initialize() {
        glEnable(GL_TEXTURE_2D);
        glClearColor(1.0f, 0.0f, 0.0f, 1.0f);

        glViewport(this.x, this.y, this.width, this.height);
        glOrtho(0, this.width, this.height, 0, 1, -1);

        glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
        glEnable(GL_ALPHA);

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        glDisable(GL_DEPTH_TEST);
    }



}
