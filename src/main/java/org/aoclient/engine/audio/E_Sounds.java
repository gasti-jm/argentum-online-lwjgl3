package org.aoclient.engine.audio;

public enum E_Sounds {
    SND_CLICK("click.ogg"),
    SND_PASOS1("23.ogg"),
    SND_PASOS2("24.ogg"),
    SND_NAVEGANDO("50.ogg"),
    SND_OVER("click2.ogg"),
    SND_DICE("cupdice.ogg");

    private final String file;

    E_Sounds(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }
}
