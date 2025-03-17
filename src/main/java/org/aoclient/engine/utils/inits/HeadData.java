package org.aoclient.engine.utils.inits;

/**
 * Simula el "Type" o la estructura de {@code HeadData}, representando los datos de las cabezas de los personajes.
 * <p>
 * Esta clase almacena la informacion grafica necesaria para representar las cabezas de los personajes segun las diferentes
 * orientaciones posibles.
 * <p>
 * Es utilizada tanto para las cabezas normales de los personajes como para los cascos (helmets), permitiendo representar
 * graficamente estos elementos segun la direccion en que se mueve o mira el personaje.
 *
 * @see org.aoclient.engine.utils.inits.GrhInfo
 * @see org.aoclient.engine.utils.inits.IndexHeads
 * @see org.aoclient.engine.game.models.Character
 */

public final class HeadData {

    private GrhInfo[] head = new GrhInfo[5];

    public HeadData() {
        head[1] = new GrhInfo();
        head[2] = new GrhInfo();
        head[3] = new GrhInfo();
        head[4] = new GrhInfo();
    }

    public HeadData(HeadData other) {
        head[1] = new GrhInfo(other.head[1]);
        head[2] = new GrhInfo(other.head[2]);
        head[3] = new GrhInfo(other.head[3]);
        head[4] = new GrhInfo(other.head[4]);
    }

    public GrhInfo getHead(int index) {
        return head[index];
    }

    public void setHead(int index, GrhInfo head) {
        this.head[index] = head;
    }

}
