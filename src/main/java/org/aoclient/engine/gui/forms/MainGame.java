package org.aoclient.engine.gui.forms;

import org.aoclient.engine.Engine;
import org.aoclient.engine.Window;
import org.aoclient.engine.gui.elements.Button;
import org.aoclient.engine.gui.elements.ImageGUI;

public class MainGame extends Form {
    private final Button buttonClose, buttonMinimizar;

    public MainGame() {
        super("VentanaPrincipal.png");

        this.buttonClose = new Button(770,4, 17, 17);
        this.buttonClose.setAction(Engine::closeClient); // definimos su funcion pasando una lambda.

        this.buttonMinimizar = new Button(752, 4, 17, 17);
        this.buttonMinimizar.setAction(() -> Window.get().minimizar());
    }

    @Override
    public void checkButtons(){
        buttonMinimizar.runAction();
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
        buttonMinimizar.clear();
    }
}
