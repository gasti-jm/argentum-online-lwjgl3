package org.aoclient.engine.scenes;

public class MainScene extends Scene {
    // Conectar


    @Override
    public void init(){
        super.init();

        canChangeTo = SceneNames.GAME_SCENE;


        //GameData.loadMap(58); // nix
    }

    @Override
    public void keyEvents() {

    }

    @Override
    public void render() {
        System.out.println("Estoy en MainScene");
    }

}
