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

    public final Shape shpEnergia;
    public final Shape shpMana;
    public final Shape shpVida;
    public final Shape shpHambre;
    public final Shape shpSed;

    public final Label lblEnergia;
    public final Label lblMana;
    public final Label lblVida;
    public final Label lblHambre;
    public final Label lblSed;

    private final Label lblName;


    private final Button buttonClose, buttonMinimizar;
    private final Console console;

    private MainGame() {
        super("VentanaPrincipal.png");
        this.buttonClose = new Button(770,4, 17, 17);
        this.buttonClose.setAction(Engine::closeClient); // definimos su funcion pasando una lambda.

        this.buttonMinimizar = new Button(752, 4, 17, 17);
        this.buttonMinimizar.setAction(() -> Window.get().minimizar());

        this.shpEnergia = new Shape(584, 453, 75, 12, new RGBColor(0.0f, 0.0f, 0.0f));
        this.shpMana = new Shape(584, 477, 75, 12, new RGBColor(0.0f, 0.0f, 0.0f));
        this.shpVida = new Shape(584, 498, 75, 12, new RGBColor(0.0f, 0.0f, 0.0f));
        this.shpHambre = new Shape(584, 521, 75, 12, new RGBColor(0.0f, 0.0f, 0.0f));
        this.shpSed = new Shape(584, 542, 75, 12, new RGBColor(0.0f, 0.0f, 0.0f));

        this.lblEnergia = new Label("999/999", 584, 453, 75, 12, false, false);
        this.lblMana = new Label("9999/9999", 584, 477, 75, 12, false, false);
        this.lblVida = new Label("999/999", 584, 498, 75, 12, false, false);
        this.lblHambre = new Label("999/999", 584, 521, 75, 12, false, false);
        this.lblSed = new Label("999/999", 584, 542, 75, 12, false, false);

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
    }


}
