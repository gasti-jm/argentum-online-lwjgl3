package org.aoclient.engine.utils.filedata;

public class IndexBodys {
    private short body[] = new short [4];
    private short headOffsetX;
    private short headOffsetY;

    public short getBody(int index) {
        return body[index];
    }

    public void setBody(int index, short body) {
        this.body[index] = body;
    }

    public short getHeadOffsetX() {
        return headOffsetX;
    }

    public void setHeadOffsetX(short headOffsetX) {
        this.headOffsetX = headOffsetX;
    }

    public short getHeadOffsetY() {
        return headOffsetY;
    }

    public void setHeadOffsetY(short headOffsetY) {
        this.headOffsetY = headOffsetY;
    }
}
