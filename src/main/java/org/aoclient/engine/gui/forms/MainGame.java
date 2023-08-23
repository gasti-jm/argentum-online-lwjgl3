package org.aoclient.engine.gui.forms;

import org.aoclient.engine.Engine;
import org.aoclient.engine.Window;
import org.aoclient.engine.game.Console;
import org.aoclient.engine.game.UserLogic;
import org.aoclient.engine.gui.elements.Label;
import org.aoclient.engine.gui.elements.Shape;
import org.aoclient.engine.renderer.RGBColor;

import java.util.ArrayList;

import static org.aoclient.engine.utils.GameData.charList;

public class MainGame extends Form {
    private static MainGame instance;

    public Shape shpEnergia = new Shape(584, 453, 75, 12, new RGBColor(0.0f, 0.0f, 0.0f));
    public Shape shpMana = new Shape(584, 477, 75, 12, new RGBColor(0.0f, 0.0f, 0.0f));
    public Shape shpVida = new Shape(584, 498, 75, 12, new RGBColor(0.0f, 0.0f, 0.0f));
    public Shape shpHambre = new Shape(584, 521, 75, 12, new RGBColor(0.0f, 0.0f, 0.0f));
    public Shape shpSed = new Shape(584, 542, 75, 12, new RGBColor(0.0f, 0.0f, 0.0f));

    public Label lblEnergia = new Label(584, 453, 75, 12);
    public Label lblMana = new Label(584, 477, 75, 12);
    public Label lblVida = new Label(584 , 498, 75, 12);
    public Label lblHambre = new Label(584, 521, 75, 12);
    public Label lblSed = new Label(584, 542, 75, 12);

    public Label lblLvl = new Label(615, 78, 134, 15);
    public Label lblPorcLvl = new Label(615, 90, 134, 15, new RGBColor(0.0f, 1.0f, 1.0f));
    public Label lblExp = new Label(615, 102, 134, 15);

    public Label gldLbl = new Label(724, 419, false, new RGBColor(1.0f, 1.0f, 0.0f));
    public Label lblDext = new Label(607, 412, true, new RGBColor(1.0f, 1.0f, 0.0f));
    public Label lblStrg = new Label(645, 412, true, new RGBColor(0.0f, 1.0f, 0.0f));

    public Label lblArmor = new Label(104, 579, false, new RGBColor(1.0f, 0.0f, 0.0f));
    public Label lblShielder = new Label(370, 579, false, new RGBColor(1.0f, 0.0f, 0.0f));
    public Label lblHelm = new Label(222, 579, false, new RGBColor(1.0f, 0.0f, 0.0f));
    public Label lblWeapon = new Label(488, 579, false, new RGBColor(1.0f, 0.0f, 0.0f));

    private Label lblName;
    private Console console;

    private MainGame() {

    }

    public static MainGame get(){
        if(instance == null) {
            instance = new MainGame();
        }

        return instance;
    }

    public void init(){
        this.visible = true;
        this.background.init("VentanaPrincipal.png");

        this.buttonList = new ArrayList<>();
        buttonList.add(addButton(770, 4, 17, 17, Engine::closeClient));
        buttonList.add(addButton(752, 4, 17, 17, () -> Window.get().minimizar()));

        this.lblName = new Label(charList[UserLogic.get().getUserCharIndex()].getName().toUpperCase(),
                584, 24, true, false, new RGBColor(1.0f, 0.0f, 0.0f));

        this.console = Console.get();
    }

    @Override
    public void render() {
        if(!visible) return;

        background.render();
        console.drawConsole();
        this.renderButtons();
        this.renderUserStats();
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

        lblLvl.render();
        lblPorcLvl.render();
        lblExp.render();
        gldLbl.render();
        lblDext.render();
        lblStrg.render();

        lblArmor.render();
        lblHelm.render();
        lblShielder.render();
        lblWeapon.render();
    }

    @Override
    public void close() {
        super.close();
        background.clear();
        buttonList.clear();

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

        lblLvl.clear();
        lblPorcLvl.clear();
        lblExp.clear();

        gldLbl.clear();
        lblDext.clear();
        lblStrg.clear();

        lblArmor.clear();
        lblHelm.clear();
        lblShielder.clear();
        lblWeapon.clear();
    }

}
