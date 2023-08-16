package org.aoclient.engine.scenes;

import org.aoclient.engine.renderer.RGBColor;

public abstract class Scene {
    protected RGBColor background;
    protected Camera camera;
    protected boolean visible = false;
    protected SceneType canChangeTo;

    public void init() {
        this.visible = true;
        this.camera = new Camera();
        this.background = new RGBColor(0.0f, 0.0f, 0.0f);
    }
    public abstract void keyEvents();

    public abstract void render();

    public RGBColor getBackground() {
        return background;
    }

    public void setBackground(RGBColor background) {
        this.background = background;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public SceneType getChangeScene() {
        return canChangeTo;
    }
}
