package org.aoclient.engine.gui.forms;

import org.aoclient.engine.Engine;
import org.aoclient.engine.gui.elements.Button;
import org.aoclient.engine.gui.elements.ImageGUI;

public class MainGame extends Form {
    private final ImageGUI main;
    private final Button buttonClose;

    public MainGame() {
        main = new ImageGUI("VentanaPrincipal.png");
        buttonClose = new Button(770,4, 17, 17);
        buttonClose.setAction(Engine::closeClient); // definimos su funcion pasando una lambda.
    }

    @Override
    public void checkButtons(){
        buttonClose.runAction();
    }

    @Override
    public void render() {
        main.render();
    }

    @Override
    public void close() {
        super.close();
        main.clear();
        buttonClose.clear();
    }
}
