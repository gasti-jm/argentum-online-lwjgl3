package org.aoclient.engine.utils.inits;

import org.aoclient.engine.game.models.Position;

/**
 * Simula el "Type" o la estructura de BodyData.
 */

public final class BodyData {

    private GrhInfo[] walk = new GrhInfo[5];
    private Position headOffset;

    public BodyData() {
        walk[1] = new GrhInfo();
        walk[2] = new GrhInfo();
        walk[3] = new GrhInfo();
        walk[4] = new GrhInfo();

        headOffset = new Position();
    }

    /**
     * @desc: Sirve para asignar a un personaje su body ya inicializado.
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
