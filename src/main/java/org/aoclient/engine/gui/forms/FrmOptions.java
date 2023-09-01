package org.aoclient.engine.gui.forms;

import org.aoclient.engine.gui.elements.Button;

import java.util.ArrayList;

public class FrmOptions extends Form {
    private static FrmOptions instance;

    private FrmOptions() {

    }

    public static FrmOptions get() {
        if (instance == null) {
            instance = new FrmOptions();
        }

        return instance;
    }
    public void init() {
        this.visible = true;
        this.background.init("VentanaOpciones.jpg");
        this.loadPositionAttributes();

        this.buttonList = new ArrayList<>();

        buttonList.add(new Button(this.background.getX() + 96, this.background.getY() + 440, 134, 19, this::close,
                "BotonSalirOpciones.jpg", "BotonSalirRolloverOpciones.jpg", "BotonSalirClickOpciones.jpg"));


    }

    @Override
    public void render() {
        if (!visible) return;

        this.background.render();
        this.renderButtons();
    }

    @Override
    public void close() {
        super.close();
        background.clear();
    }
}
