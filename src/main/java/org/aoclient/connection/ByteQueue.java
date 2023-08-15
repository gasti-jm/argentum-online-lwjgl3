package org.aoclient.connection;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ByteQueue {
    private static final int NOT_ENOUGH_DATA = 10000;
    private static final int NOT_ENOUGH_SPACE = 10001;
    private static final int DATA_BUFFER = 10240;

    private byte[] data;
    private int queueCapacity;
    private int queueLength;

    public ByteQueue() {
        data = new byte[DATA_BUFFER];
        queueCapacity = DATA_BUFFER;
    }

    public void copyBuffer(ByteQueue source) {
        if (source.length() == 0) {
            // Clear the list and exit
            removeData(queueLength);
            return;
        }

        // Set capacity and resize array - make sure all data is lost
        queueCapacity = source.getCapacity();
        data = new byte[queueCapacity];

        // Read buffer
        byte[] buf = new byte[source.length()];
        source.peekBlock(buf, source.length());

        queueLength = 0;

        // Write buffer
        writeBlock(buf, source.length());
    }

    private int min(int val1, int val2) {
        return Math.min(val1, val2);
    }

    private int writeData(byte[] buf, int dataLength) {
        if (queueCapacity - queueLength - dataLength < 0) {
            throw new RuntimeException("Not enough space");
        }

        System.arraycopy(buf, 0, data, queueLength, dataLength);

        queueLength += dataLength;
        System.out.println(queueLength);
        return dataLength;
    }

    public int readData(byte[] buf, int dataLength) {
        // Check if we can read the number of bytes requested
        if (dataLength > queueLength) {
            throw new RuntimeException("Not enough data");
        }

        // Copy data to buffer
        System.arraycopy(data, 0, buf, 0, dataLength);
        return dataLength;
    }

    private int removeData(int dataLength) {
        int removedData = min(dataLength, queueLength);

        if (removedData != queueCapacity) {
            System.arraycopy(data, removedData, data, 0, queueLength - removedData);
        }

        queueLength -= removedData;
        return removedData;
    }

    public int writeByte(int value) {
        byte[] buf = { (byte) value };
        return writeData(buf, 1);
    }

    public int writeInteger(short value) {
        byte[] buf = new byte[2];
        ByteBuffer.wrap(buf).putShort(value);
        return writeData(buf, 2);
    }

    public int writeLong(int value) {
        byte[] buf = new byte[4];
        ByteBuffer.wrap(buf).putInt(value);
        return writeData(buf, 4);
    }

    public int writeSingle(float value) {
        byte[] buf = new byte[4];
        ByteBuffer.wrap(buf).putFloat(value);
        return writeData(buf, 4);
    }

    public long writeDouble(double value) {
        byte[] buf = new byte[8];
        ByteBuffer.wrap(buf).putDouble(value);
        return writeData(buf, 8);
    }

    public int writeBoolean(boolean value) {
        byte[] buf = new byte[1];
        buf[0] = (byte) (value ? 1 : 0);
        return writeData(buf, 1);
    }

    public int writeASCIIStringFixed(String value) {
        byte[] buf = value.getBytes();
        return writeData(buf, buf.length);
    }

    public int writeUnicodeStringFixed(String value) {
        byte[] buf = value.getBytes();
        return writeData(buf, buf.length);
    }

//    public int writeASCIIString(String value) {
//        byte[] valueBytes = value.getBytes();
//        byte[] buf = new byte[value.length() + 2];
//
//        buf[0] = (byte) value.length();
//        System.arraycopy(valueBytes, 0, buf, 2, value.length());
//
//        return writeData(buf, value.length() + 2);
//    }

    public int writeASCIIString(String value) {
        byte[] valueBytes = value.getBytes();
        byte[] buf = new byte[value.length() + 2];

        ByteBuffer.wrap(buf).put(0, (byte) value.length());
        //buf[0] = (byte) value.length();
        System.arraycopy(valueBytes, 0, buf, 2, value.length());
        return writeData(buf, value.length() + 2);
    }

    // OJO
    public int writeUnicodeString(String value) {
        byte[] valueBytes = value.getBytes();
        byte[] buf = new byte[value.length() + 2];

        buf[0] = (byte) value.length();
        System.arraycopy(valueBytes, 0, buf, 2, value.length());

        return writeData(buf, value.length() + 2);
    }

    public int writeBlock(byte[] value, int length) {
        // Prevent from copying memory outside the array
        if (length > value.length || length < 0) {
            length = value.length;
        }

        return writeData(value, length);
    }

    public byte readByte() {
        byte[] buf = new byte[1];
        removeData(readData(buf, 1));
        return buf[0];
    }

    public short readInteger() {
        byte[] buf = new byte[2];
        removeData(readData(buf, 2));
        return ByteBuffer.wrap(buf).getShort();
    }

    public int readLong() {
        byte[] buf = new byte[4];
        removeData(readData(buf, 4));
        return ByteBuffer.wrap(buf).getInt();
    }

    public float readSingle() {
        byte[] buf = new byte[4];
        removeData(readData(buf, 4));
        return ByteBuffer.wrap(buf).getFloat();
    }

    public double readDouble() {
        byte[] buf = new byte[8];
        removeData(readData(buf, 8));
        return ByteBuffer.wrap(buf).getDouble();
    }

    public boolean readBoolean() {
        byte[] buf = new byte[1];
        removeData(readData(buf, 1));
        return buf[0] == 1;
    }

    public String readASCIIStringFixed(int length) {
        if (length <= 0) {
            return "";
        }

        if (queueLength >= length) {
            byte[] buf = new byte[length];
            removeData(readData(buf, length));
            return new String(buf, StandardCharsets.UTF_8);
        } else {
            throw new RuntimeException("Not enough data");
        }
    }

    public String readUnicodeStringFixed(int length) {
        if (length <= 0) {
            return "";
        }

        if (queueLength >= length * 2) {
            byte[] buf = new byte[length * 2];
            removeData(readData(buf, length * 2));
            return new String(buf, StandardCharsets.UTF_16LE);
        } else {
            throw new RuntimeException("Not enough data");
        }
    }

    public String readASCIIString() {
        byte[] buf = new byte[2];
        int length;

        if (queueLength > 1) {
            removeData(readData(buf, 2));
            length = ByteBuffer.wrap(buf).getShort();

            if (queueLength >= length + 2) {
                removeData(2);

                if (length > 0) {
                    byte[] buf2 = new byte[length];
                    removeData(readData(buf2, length));
                    return new String(buf2, StandardCharsets.UTF_8);
                }
            } else {
                throw new RuntimeException("Not enough data");
            }
        }

        throw new RuntimeException("Not enough data");
    }

    public String readUnicodeString() {
        byte[] buf = new byte[2];
        int length;

        if (queueLength > 1) {
            removeData(readData(buf, 2));
            length = ByteBuffer.wrap(buf).getShort();

            if (queueLength >= length * 2 + 2) {
                removeData(2);
                byte[] buf2 = new byte[length * 2];
                removeData(readData(buf2, length * 2));
                return new String(buf2, StandardCharsets.UTF_16LE);
            } else {
                throw new RuntimeException("Not enough data");
            }
        }

        throw new RuntimeException("Not enough data");
    }

    public long readBlock(byte[] block, long dataLength) {
        if (dataLength > 0) {
            return removeData(readData(block, (int) dataLength));
        }
        return 0;
    }

    public byte peekByte() {
        byte[] buf = new byte[1];
        readData(buf, 1);
        return buf[0];
    }

    public short peekInteger() {
        byte[] buf = new byte[2];
        readData(buf, 2);
        return ByteBuffer.wrap(buf).getShort();
    }

    public int peekLong() {
        byte[] buf = new byte[4];
        readData(buf, 4);
        return ByteBuffer.wrap(buf).getInt();
    }

    public float peekSingle() {
        byte[] buf = new byte[4];
        readData(buf, 4);
        return ByteBuffer.wrap(buf).getFloat();
    }

    public double peekDouble() {
        byte[] buf = new byte[8];
        readData(buf, 8);
        return ByteBuffer.wrap(buf).getDouble();
    }

    public boolean peekBoolean() {
        byte[] buf = new byte[1];
        readData(buf, 1);
        return buf[0] == 1;
    }

    public String peekASCIIStringFixed(int length) {
        if (length <= 0) {
            return "";
        }

        if (queueLength >= length) {
            byte[] buf = new byte[length];
            readData(buf, length);
            return new String(buf, StandardCharsets.UTF_8);
        } else {
            throw new RuntimeException("Not enough data");
        }
    }

    public String peekUnicodeStringFixed(int length) {
        if (length <= 0) {
            return "";
        }

        if (queueLength >= length * 2) {
            byte[] buf = new byte[length * 2];
            readData(buf, length * 2);
            return new String(buf, StandardCharsets.UTF_16LE);
        } else {
            throw new RuntimeException("Not enough data");
        }
    }

    public String peekASCIIString() {
        byte[] buf = new byte[2];
        int length;

        if (queueLength > 1) {
            readData(buf, 2);
            length = ByteBuffer.wrap(buf).getShort();

            if (queueLength >= length + 2) {
                byte[] buf2 = new byte[length + 2];
                readData(buf2, length + 2);

                if (length > 0) {
                    byte[] buf3 = new byte[length];
                    System.arraycopy(buf2, 2, buf3, 0, length);
                    return new String(buf3, StandardCharsets.UTF_8);
                }
            } else {
                throw new RuntimeException("Not enough data");
            }
        }

        throw new RuntimeException("Not enough data");
    }

    public String peekUnicodeString() {
        byte[] buf = new byte[2];
        int length;

        if (queueLength > 1) {
            readData(buf, 2);
            length = ByteBuffer.wrap(buf).getShort();

            if (queueLength >= length * 2 + 2) {
                byte[] buf2 = new byte[length * 2 + 2];
                readData(buf2, length * 2 + 2);
                byte[] buf3 = new byte[length * 2];
                System.arraycopy(buf2, 2, buf3, 0, length * 2);
                return new String(buf3, StandardCharsets.UTF_16LE);
            } else {
                throw new RuntimeException("Not enough data");
            }
        }

        throw new RuntimeException("Not enough data");
    }

    private int peekBlock(byte[] block, int dataLength) {
        // Read the data
        if(dataLength > 0) {
            return readData(block, dataLength);
        }

        return 0;
    }

    public int length() {
        return queueLength;
    }

    public int getCapacity() {
        return queueCapacity;
    }

    public void setCapacity(int value) {
        // Update capacity
        queueCapacity = value;

        // All extra data is lost
        if (queueLength > value) {
            queueLength = value;
        }

        // Resize the queue
        byte[] newData = new byte[queueCapacity];
        System.arraycopy(data, 0, newData, 0, queueLength);
        data = newData;
    }

    public int getNotEnoughDataErrCode() {
        return NOT_ENOUGH_DATA;
    }

    public int getNotEnoughSpaceErrCode() {
        return NOT_ENOUGH_SPACE;
    }
}

