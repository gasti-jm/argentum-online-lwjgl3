package org.aoclient.engine.gui.forms;

public class Options extends Form {
    private static Options instance;

    private Options() {

    }

    public static Options get() {
        if (instance == null) {
            instance = new Options();
        }

        return instance;
    }
    public void init() {
        this.visible = true;
        this.background.init("VentanaOpciones.png");
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
