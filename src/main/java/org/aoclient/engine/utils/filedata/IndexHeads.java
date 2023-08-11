package org.aoclient.engine.utils.filedata;

public class IndexHeads {
    private short[] head = new short[4];

    public short getHead(int index) {
        return head[index];
    }

    public void setHead(int index, short head) {
        this.head[index] = head;
    }
}
