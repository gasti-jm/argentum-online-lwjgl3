package org.aoclient.engine.utils.structs;

/**
 * Simula el "Type" o la estructura de IndexHeads.
 */
public final class IndexHeads {
    private short[] head = new short[5];

    public short getHead(int index) {
        return head[index];
    }

    public void setHead(int index, short head) {
        this.head[index] = head;
    }
}
