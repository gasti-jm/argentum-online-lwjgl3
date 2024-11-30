package org.aoclient.engine.game.models;

public enum E_FontType {
    FONTTYPE_TALK(1f, 1f, 1f, false, false),
    FONTTYPE_FIGHT(1f, 0f, 0f, true, false),
    FONTTYPE_WARNING(0.12f, 0.2f, 0.87f, true, true),
    FONTTYPE_INFO(0.25f, 0.74f, 0.61f, false, false),
    FONTTYPE_INFOBOLD(0.25f, 0.74f, 0.61f, true, false),
    FONTTYPE_EJECUCION(0.5f, 0.5f, 0.5f, true, false),
    FONTTYPE_PARTY(1f, 0.70f, 0.98f, false, false),
    FONTTYPE_VENENO(0f, 1f, 0f, false, false),
    FONTTYPE_GUILD(1f, 1f, 1f, true, false),
    FONTTYPE_SERVER(0f, 0.72f, 0f, false, false),
    FONTTYPE_GUILDMSG(0.89f, 0.78f, 0.1f, false, false),
    FONTTYPE_CONSEJO(0.5f, 0.5f, 1f, true, false),
    FONTTYPE_CONSEJOCAOS(1f, 0.23f, 0f, true, false),
    FONTTYPE_CONSEJOVesA(0.78f, 1f, 0f, true, false),
    FONTTYPE_CONSEJOCAOSVesA(1f, 0.19f, 0f, true, false),
    FONTTYPE_CENTINELA(0f, 1f, 0f, true, false),
    FONTTYPE_GMMSG(1f, 1f, 1f, false, true),
    FONTTYPE_GM(0.11f, 1f, 0.11f, true, false),
    FONTTYPE_CITIZEN(0f, 0f, 0.78f, true, false),
    FONTTYPE_CONSE(0.11f, 0.58f, 0.11f, true, false),
    FONTTYPE_DIOS(0.98f, 0.98f,0.58f, true, false);

    public final float r, g, b;
    public final boolean bold, italic;

    E_FontType(float r, float g, float b, boolean bold, boolean italic) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.bold = bold;
        this.italic = italic;
    }
}
