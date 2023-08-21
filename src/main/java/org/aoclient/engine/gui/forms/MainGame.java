package org.aoclient.engine.gui.forms;

import org.aoclient.engine.Engine;
import org.aoclient.engine.Window;
import org.aoclient.engine.game.Console;
import org.aoclient.engine.game.UserLogic;
import org.aoclient.engine.gui.elements.Button;
import org.aoclient.engine.gui.elements.Label;
import org.aoclient.engine.gui.elements.Shape;
import org.aoclient.engine.renderer.RGBColor;

import static org.aoclient.engine.utils.GameData.charList;

public class MainGame extends Form {
    private static MainGame instance;

    public Shape shpEnergia = new Shape(584, 453, 75, 12, new RGBColor(0.0f, 0.0f, 0.0f));
    public Shape shpMana = new Shape(584, 477, 75, 12, new RGBColor(0.0f, 0.0f, 0.0f));
    public Shape shpVida = new Shape(584, 498, 75, 12, new RGBColor(0.0f, 0.0f, 0.0f));
    public Shape shpHambre = new Shape(584, 521, 75, 12, new RGBColor(0.0f, 0.0f, 0.0f));
    public Shape shpSed = new Shape(584, 542, 75, 12, new RGBColor(0.0f, 0.0f, 0.0f));

    public Label lblEnergia = new Label("999/999", 584, 453, 75, 12, false, false);
    public Label lblMana = new Label("9999/9999", 584, 477, 75, 12, false, false);
    public Label lblVida = new Label("999/999", 584, 498, 75, 12, false, false);
    public Label lblHambre = new Label("999/999", 584, 521, 75, 12, false, false);
    public Label lblSed = new Label("999/999", 584, 542, 75, 12, false, false);

    private Label lblName;

    private Button buttonClose, buttonMinimizar;
    private Console console;

    private MainGame() {

    }

    public void init(){
        this.background.init("VentanaPrincipal.png");

        this.buttonClose = new Button(770,4, 17, 17);
        this.buttonClose.setAction(Engine::closeClient); // definimos su funcion pasando una lambda.

        this.buttonMinimizar = new Button(752, 4, 17, 17);
        this.buttonMinimizar.setAction(() -> Window.get().minimizar());

        this.lblName = new Label(charList[UserLogic.getInstance().getUserCharIndex()].getName().toUpperCase(),
                584, 24, true, false, new RGBColor(1.0f, 0.0f, 0.0f));

        this.console = Console.get();
    }

    public static MainGame get(){
        if(instance == null) {
            instance = new MainGame();
        }

        return instance;
    }

    @Override
    public void checkButtons(){
        buttonMinimizar.runAction();
        buttonClose.runAction();
    }

    @Override
    public void render() {
        background.render();
        console.drawConsole();
        renderUserStats();
    }

    private void renderUserStats() {
        shpEnergia.render();
        lblEnergia.render();

        shpMana.render();
        lblMana.render();

        shpVida.render();
        lblVida.render();

        shpHambre.render();
        lblHambre.render();

        shpSed.render();
        lblSed.render();

        lblName.render();
    }

    @Override
    public void close() {
        super.close();
        background.clear();
        buttonClose.clear();
        buttonMinimizar.clear();

        shpHambre.clear();
        shpMana.clear();
        shpVida.clear();
        shpSed.clear();
        shpEnergia.clear();

        lblHambre.clear();
        lblSed.clear();
        lblEnergia.clear();
        lblName.clear();
        lblMana.clear();
        lblVida.clear();
    }

}
