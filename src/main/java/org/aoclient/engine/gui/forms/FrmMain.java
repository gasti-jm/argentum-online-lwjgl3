package org.aoclient.engine.gui.forms;

import org.aoclient.engine.Engine;
import org.aoclient.engine.Window;
import org.aoclient.engine.game.Console;
import org.aoclient.engine.game.User;
import org.aoclient.engine.gui.elements.Button;
import org.aoclient.engine.gui.elements.Label;
import org.aoclient.engine.gui.elements.Shape;
import org.aoclient.engine.renderer.RGBColor;

import java.util.ArrayList;

import static org.aoclient.engine.Engine.forms;
import static org.aoclient.engine.utils.GameData.charList;

/**
 * Este seria la clase que define nuestro formulario "frmMain".
 * Contiene elementos de GUI.
 */
public class FrmMain extends Form {
    private static FrmMain instance;

    public Shape shpEnergia = new Shape(584, 453, 75, 12, new RGBColor(0.0f, 0.0f, 0.0f));
    public Shape shpMana = new Shape(584, 477, 75, 12, new RGBColor(0.0f, 0.0f, 0.0f));
    public Shape shpVida = new Shape(584, 498, 75, 12, new RGBColor(0.0f, 0.0f, 0.0f));
    public Shape shpHambre = new Shape(584, 521, 75, 12, new RGBColor(0.0f, 0.0f, 0.0f));
    public Shape shpSed = new Shape(584, 542, 75, 12, new RGBColor(0.0f, 0.0f, 0.0f));

    public Label lblEnergia = new Label(584, 453, 75, 12, true);
    public Label lblMana = new Label(584, 477, 75, 12, true);
    public Label lblVida = new Label(584 , 498, 75, 12, true);
    public Label lblHambre = new Label(584, 521, 75, 12, true);
    public Label lblSed = new Label(584, 542, 75, 12, true);

    public Label lblLvl = new Label(615, 78, 134, 15);
    public Label lblPorcLvl = new Label(615, 90, 134, 15, new RGBColor(0.0f, 1.0f, 1.0f));
    public Label lblExp = new Label(615, 102, 134, 15);

    public Label gldLbl = new Label(724, 419, false, new RGBColor(1.0f, 1.0f, 0.0f));
    public Label lblDext = new Label(607, 412, true, new RGBColor(1.0f, 1.0f, 0.0f));
    public Label lblStrg = new Label(645, 412, true, new RGBColor(0.0f, 1.0f, 0.0f));

    public Label lblArmor = new Label(104, 579, false, true, new RGBColor(1.0f, 0.0f, 0.0f));
    public Label lblShielder = new Label(370, 579, false, true, new RGBColor(1.0f, 0.0f, 0.0f));
    public Label lblHelm = new Label(222, 579, false, true, new RGBColor(1.0f, 0.0f, 0.0f));
    public Label lblWeapon = new Label(488, 579, false, true, new RGBColor(1.0f, 0.0f, 0.0f));

    public Label lblCoords = new Label(630,571, true, true, new RGBColor(1.0f, 1.0f, 0.0f));

    private Label lblName;
    private Console console;

    /**
     * @desc: Constructor privado por singleton.
     */
    private FrmMain() {

    }

    /**
     *
     * @return Mismo objeto (Patron de diseño Singleton)
     */
    public static FrmMain get(){
        if(instance == null) {
            instance = new FrmMain();
        }

        return instance;
    }

    /**
     * Como tenemos un singleton, necesitamos de una funcion init para incializar nuestros atributos de la clase.
     */
    public void init(){
        this.visible = true;
        this.background.init("VentanaPrincipal.png");

        this.buttonList = new ArrayList<>();
        buttonList.add(new Button(770, 4, 17, 17, Engine::closeClient));
        buttonList.add(new Button(752, 4, 17, 17, () -> Window.get().minimizar()));


        //Boton Mapa
        buttonList.add(new Button(682, 445, 93, 20, () -> {
            // nothing to do...
        }, "", "BotonMapaRollover.jpg", "BotonMapaClick.jpg"));

        //Boton Grupo
        buttonList.add(new Button(681, 466, 94, 21, () -> {
            // nothing to do...
        }, "", "BotonGrupoRollover.jpg", "BotonGrupoClick.jpg"));

        //Boton Opciones
        buttonList.add(new Button(681, 485, 92, 22, () -> {
            FrmOptions.get().init();
            forms.add(FrmOptions.get());
        }, "", "BotonOpcionesRollover.jpg", "BotonOpcionesClick.jpg"));

        //Boton Estadisticas
        buttonList.add(new Button(681, 507, 95, 24, () -> {
            //asd
        }, "", "BotonEstadisticasRollover.jpg", "BotonEstadisticasClick.jpg"));

        //Boton Clanes
        buttonList.add(new Button(683, 532, 92, 26, () -> {
            //asd
        }, "", "BotonClanesRollover.jpg", "BotonClanesClick.jpg"));


        this.lblName = new Label(charList[User.get().getUserCharIndex()].getName().toUpperCase(),
                584, 24, true, false, new RGBColor(1.0f, 0.0f, 0.0f));

        this.console = Console.get();
    }

    /**
     * @desc Renderiza el contenido de nuestro formulario.
     */
    @Override
    public void render() {
        if(!visible) return;

        background.render();
        console.drawConsole();
        lblCoords.render();

        this.renderButtons();
        this.renderUserStats();
    }

    /**
     * @desc Renderiza todos los componentes agregados al frmMain.
     *
     * @pd Habria que crear una lista o una coleccion de "ElementGUI" y hacer esto mas sencillo.
     */
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

    /**
     * @desc Cierra y elimina nuestro formulario.
     */
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
