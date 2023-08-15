package org.aoclient.engine.utils.filedata;

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
