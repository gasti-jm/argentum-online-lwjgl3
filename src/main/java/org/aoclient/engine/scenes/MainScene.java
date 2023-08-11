package org.aoclient.engine.scenes;

import org.aoclient.engine.Window;
import org.aoclient.engine.renderer.Renderer;
import org.aoclient.engine.utils.GameData;

public class MainScene extends Scene {
    // Intro?
    // Conectar? ....


    @Override
    public void init(){
        GameData.loadMap(58); // nix
    }

    @Override
    public void keyEvents() {

    }

    @Override
    public void render() {
        this.renderer = new Renderer(Window.getInstance().getWidth(), Window.getInstance().getHeight(), 0, 0);
        this.renderer.initialize();
    }





}
