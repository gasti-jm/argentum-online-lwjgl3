package org.aoclient.engine.gui;


import org.aoclient.engine.listeners.MouseListener;
import org.aoclient.engine.renderer.Surface;
import org.aoclient.engine.renderer.TextureOGL;

import java.util.ArrayList;
import java.util.List;

public abstract class ElementGUI {
    protected float alphaTexture = 1.0f;

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected List<TextureOGL> texture;

    public ElementGUI() {
        this.texture = new ArrayList<>();
    }

    public ElementGUI(int x, int y, int width, int height) {
        this.texture = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void render();

    public void clear() {
        this.texture.clear();
    }

    public void loadTextures(String... files) {
        for (int i = 0; i < files.length; i++) {
            texture.add(Surface.get().createTexture(files[i]));
        }
    }

    public boolean isInside(){
        return MouseListener.getX() >= this.x && MouseListener.getX() <= this.x + this.width &&
                MouseListener.getY() >= this.y && MouseListener.getY() <= this.y + this.height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getAlphaTexture() {
        return alphaTexture;
    }

    public void setAlphaTexture(float alphaTexture) {
        this.alphaTexture = alphaTexture;
    }
}
