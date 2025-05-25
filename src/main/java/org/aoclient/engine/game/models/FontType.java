package org.aoclient.engine.game.models;

/**
 * Define los diferentes tipos de fuentes y estilos tipograficos utilizados para mostrar mensajes.
 * <p>
 * Cada tipo de fuente define:
 * <ul>
 * <li>Un color especifico mediante valores RGB (r, g, b)
 * <li>Si el texto debe mostrarse en <b>bold</b>
 * <li>Si el texto debe mostrarse en <i>italic</i>
 * </ul>
 */

public enum FontType {

    TALK(1f, 1f, 1f, false, false),
    FIGHT(1f, 0f, 0f, true, false),
    WARNING(0.12f, 0.2f, 0.87f, true, true),
    INFO(0.25f, 0.74f, 0.61f, false, false),
    INFO_BOLD(0.25f, 0.74f, 0.61f, true, false),
    EJECUCION(0.5f, 0.5f, 0.5f, true, false),
    PARTY(1f, 0.70f, 0.98f, false, false),
    POISON(0f, 1f, 0f, false, false),
    GUILD(1f, 1f, 1f, true, false),
    SERVER(0f, 0.72f, 0f, false, false),
    GUILDMSG(0.89f, 0.78f, 0.1f, false, false),
    CONSEJO(0.5f, 0.5f, 1f, true, false),
    CONSEJO_CAOS(1f, 0.23f, 0f, true, false),
    CONSEJO_VesA(0.78f, 1f, 0f, true, false),
    CONSEJO_CAOS_VesA(1f, 0.19f, 0f, true, false),
    CENTINELA(0f, 1f, 0f, true, false),
    GM_MSG(1f, 1f, 1f, false, true),
    GM(0.11f, 1f, 0.11f, true, false),
    CITIZEN(0f, 0f, 0.78f, true, false),
    CONSE(0.11f, 0.58f, 0.11f, true, false),
    DIOS(0.98f, 0.98f, 0.58f, true, false);

    public final float r, g, b;
    public final boolean bold, italic;

    FontType(float r, float g, float b, boolean bold, boolean italic) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.bold = bold;
        this.italic = italic;
    }

}
