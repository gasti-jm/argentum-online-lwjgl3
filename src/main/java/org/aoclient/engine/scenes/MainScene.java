package org.aoclient.engine.scenes;

import org.aoclient.engine.Window;
import org.aoclient.engine.gui.ElementGUI;
import org.aoclient.engine.gui.elements.ImageGUI;
import org.aoclient.engine.listeners.KeyListener;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.engine.utils.GameData;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;

public final class MainScene extends Scene {
    // Conectar
    private ElementGUI loginInterface;
    private RGBColor ambientColor;

    @Override
    public void init(){
        super.init();
        canChangeTo = SceneNames.GAME_SCENE;

        // Interface
        loginInterface = new ImageGUI();
        loginInterface.init();
        loginInterface.loadTextures("login.png");

        loginInterface.setX(0);
        loginInterface.setX(0);

        ambientColor = new RGBColor(1.0f, 1.0f, 1.0f);


        GameData.loadMap(58); // banderbill

        camera.setHalfWindowTileWidth(((Window.getInstance().getWidth() / 32) / 2));
        camera.setHalfWindowTileHeight(((Window.getInstance().getHeight() / 32) / 2));
    }

    private void loadInterfaces() {

    }

    @Override
    public void keyEvents() {
        if(KeyListener.isKeyPressed(GLFW_KEY_ENTER)) {
            this.visible = false;
        }
    }

    @Override
    public void render() {
        if (!visible) return;

        loginInterface.render();
    }

}
