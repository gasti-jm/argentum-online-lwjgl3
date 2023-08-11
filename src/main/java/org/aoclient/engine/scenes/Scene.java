package org.aoclient.engine.scenes;

import org.aoclient.engine.Window;
import org.aoclient.engine.renderer.Renderer;

public abstract class Scene {
    protected Renderer renderer;
    protected Camera camera;
    protected boolean visible = false;

    public void init() {
        visible = true;
        camera = new Camera();
    }

    public abstract void keyEvents();

    public abstract void render();
}
