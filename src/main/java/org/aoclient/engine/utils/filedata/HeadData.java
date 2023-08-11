package org.aoclient.engine.utils.filedata;

public class HeadData {
    private GrhInfo[] head = new GrhInfo[4];

    public HeadData() {
        head[0] = new GrhInfo();
        head[1] = new GrhInfo();
        head[2] = new GrhInfo();
        head[3] = new GrhInfo();
    }

    public HeadData(HeadData other) {
        head[0] = new GrhInfo(other.head[0]);
        head[1] = new GrhInfo(other.head[1]);
        head[2] = new GrhInfo(other.head[2]);
        head[3] = new GrhInfo(other.head[3]);
    }

    public GrhInfo getHead(int index) {
        return head[index];
    }

    public void setHead(int index, GrhInfo head) {
        this.head[index] = head;
    }
}
