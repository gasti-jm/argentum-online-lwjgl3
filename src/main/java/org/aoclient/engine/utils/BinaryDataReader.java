package org.aoclient.engine.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BinaryDataReader {

    private ByteBuffer buffer;

    public BinaryDataReader() {

    }

    public void init(byte[] data) {
        this.buffer = ByteBuffer.wrap(data);
        this.buffer.order(ByteOrder.LITTLE_ENDIAN); // Especifica el orden de los bytes (BIG_ENDIAN o LITTLE_ENDIAN)
    }

    public void init(byte[] data, ByteOrder order) {
        this.buffer = ByteBuffer.wrap(data);
        this.buffer.order(order); // Especifica el orden de los bytes (BIG_ENDIAN o LITTLE_ENDIAN)
    }

    // Lee un entero (4 bytes)
    public int readInt() {
        return buffer.getInt();
    }

    // Lee un short (2 bytes)
    public short readShort() {
        return buffer.getShort();
    }

    // Lee un long (8 bytes)
    public long readLong() {
        return buffer.getLong();
    }

    // Lee un float (4 bytes)
    public float readFloat() {
        return buffer.getFloat();
    }

    // Lee un double (8 bytes)
    public double readDouble() {
        return buffer.getDouble();
    }

    // Lee un byte (1 byte)
    public byte readByte() {
        return buffer.get();
    }

    // Lee un boolean (1 byte, 0 = false, cualquier otro valor = true)
    public boolean readBoolean() {
        return buffer.get() != 0;
    }

    // Lee una cadena de texto de longitud fija
    public String readString(int length) {
        byte[] stringBytes = new byte[length];
        buffer.get(stringBytes);
        return new String(stringBytes);
    }

    // Salta una cantidad de bytes
    public void skipBytes(int count) {
        buffer.position(buffer.position() + count);
    }

    // Verifica si hay más datos para leer
    public boolean hasRemaining() {
        return buffer.hasRemaining();
    }

}
