package org.aoclient.engine.utils.filedata;

import org.aoclient.engine.game.models.Position;

public class BodyData {
    private GrhInfo[] walk = new GrhInfo[4];
    private Position headOffset;

    public BodyData() {
        walk[0] = new GrhInfo();
        walk[1] = new GrhInfo();
        walk[2] = new GrhInfo();
        walk[3] = new GrhInfo();

        headOffset = new Position();
    }

    /**
     *
     * @desc: Sirve para asignar a un personaje su body ya inicializado.
     */
    public BodyData(BodyData other) {
        walk[0] = new GrhInfo(other.walk[0]);
        walk[1] = new GrhInfo(other.walk[1]);
        walk[2] = new GrhInfo(other.walk[2]);
        walk[3] = new GrhInfo(other.walk[3]);

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
