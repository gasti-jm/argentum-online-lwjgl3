package org.aoclient.engine.gui.forms;

import org.aoclient.engine.Engine;
import org.aoclient.engine.gui.elements.Button;
import org.aoclient.engine.gui.elements.ImageGUI;

public class MainGame extends Form {
    private final Button buttonClose;

    public MainGame(String fileName) {
        super(fileName);
        this.buttonClose = new Button(770,4, 17, 17);
        this.buttonClose.setAction(Engine::closeClient); // definimos su funcion pasando una lambda.
    }

    @Override
    public void checkButtons(){
        buttonClose.runAction();
    }

    @Override
    public void render() {
        background.render();
    }

    @Override
    public void close() {
        super.close();
        background.clear();
        buttonClose.clear();
    }
}
