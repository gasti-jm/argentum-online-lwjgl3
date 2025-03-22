package org.aoclient.engine.utils.inits;

/**
 * Simula el "Type" o la estructura de {@code FxData}, gestionando la informacion de efectos especiales.
 * <p>
 * Esta clase almacena los datos necesarios para representar y posicionar efectos visuales (FX) que se aplican a objetos o
 * personajes.
 * <p>
 * Esta clase es utilizada por el sistema de renderizado para mostrar efectos visuales como hechizos, impactos, auras y otros
 * elementos animados que se superponen a los personajes u objetos del mundo.
 *
 * @see GrhInfo
 * @see Character
 * @see org.aoclient.engine.utils.GameData
 */

public final class FxData {

    private short Animacion;
    private short OffsetX;
    private short OffsetY;

    public short getAnimacion() {
        return Animacion;
    }

    public void setAnimacion(short animacion) {
        Animacion = animacion;
    }

    public short getOffsetX() {
        return OffsetX;
    }

    public void setOffsetX(short offsetX) {
        OffsetX = offsetX;
    }

    public short getOffsetY() {
        return OffsetY;
    }

    public void setOffsetY(short offsetY) {
        OffsetY = offsetY;
    }

}
