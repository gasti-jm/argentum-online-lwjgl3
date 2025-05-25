package org.aoclient.engine.utils.inits;

import org.aoclient.engine.game.models.Position;

/**
 * Simula el "Type" o la estructura de {@code BodyData}, representando los datos graficos del cuerpo de los personajes.
 * <p>
 * Esta clase almacena la informacion grafica necesaria para representar los cuerpos de los personajes segun las diferentes
 * orientaciones durante su desplazamiento por el mapa. Mantiene un arreglo de objetos GrhInfo para las animaciones de caminar
 * donde cada indice del arreglo (1-4) representa una direccion diferente (Norte, Este, Sur, Oeste).
 *
 * @see GrhInfo
 * @see IndexBodys
 * @see Character
 */

public final class BodyData {

    private final GrhInfo[] walk = new GrhInfo[5];
    private final Position headOffset;

    public BodyData() {
        walk[1] = new GrhInfo();
        walk[2] = new GrhInfo();
        walk[3] = new GrhInfo();
        walk[4] = new GrhInfo();

        headOffset = new Position();
    }

    /**
     *  Sirve para asignar a un personaje su body ya inicializado.
     */
    public BodyData(BodyData other) {
        walk[1] = new GrhInfo(other.walk[1]);
        walk[2] = new GrhInfo(other.walk[2]);
        walk[3] = new GrhInfo(other.walk[3]);
        walk[4] = new GrhInfo(other.walk[4]);

        headOffset = other.headOffset;
    }

    public GrhInfo getWalk(int index) {
        return walk[index];
    }

    public void setWalk(int index, GrhInfo walk) {
        this.walk[index] = walk;
    }

    public Position getHeadOffset() {
        return headOffset;
    }

}
