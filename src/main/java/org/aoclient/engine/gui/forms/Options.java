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
        this.background.init("VentanaOpciones.jpg");
        this.visible = true;
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
