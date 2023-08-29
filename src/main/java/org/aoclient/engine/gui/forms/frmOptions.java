package org.aoclient.engine.gui.forms;

import java.util.ArrayList;

public class frmOptions extends Form {
    private static frmOptions instance;

    private frmOptions() {

    }

    public static frmOptions get() {
        if (instance == null) {
            instance = new frmOptions();
        }

        return instance;
    }
    public void init() {
        this.visible = true;
        this.background.init("VentanaOpciones.jpg");

        this.loadPositionAttributes();
        this.buttonList = new ArrayList<>();
    }

    @Override
    public void render() {
        if (!visible) return;

        this.background.render();
    }

    @Override
    public void close() {
        super.close();
    }
}
