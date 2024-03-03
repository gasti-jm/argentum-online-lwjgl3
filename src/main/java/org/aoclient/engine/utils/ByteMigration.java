package org.aoclient.engine.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Esta clase contiene metodos para migrar ciertos tipos de datos de altas cantidades de bytes a bajas. Para que la
 * lectura de estos mismos sean compatibles con la de Visual Basic 6.0
 */
public final class ByteMigration {

    public static int bigToLittle_Int(int bigendian) {
        ByteBuffer buf = ByteBuffer.allocate(4);

        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putInt(bigendian);

        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getInt(0);
    }

    public static float bigToLittle_Float(float bigendian) {
        ByteBuffer buf = ByteBuffer.allocate(4);

        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putFloat(bigendian);

        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getFloat(0);
    }

    public static double bigToLittle_Double(double bigendian) {
        ByteBuffer buf = ByteBuffer.allocate(8);

        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putDouble(bigendian);

        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getDouble(0);
    }

    public static short bigToLittle_Short(short bigendian) {
        ByteBuffer buf = ByteBuffer.allocate(2);

        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putShort(bigendian);

        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getShort(0);
    }

    public static byte bigToLittle_Byte(byte bigendian) {
        ByteBuffer buf = ByteBuffer.allocate(1);

        buf.order(ByteOrder.BIG_ENDIAN);
        buf.put(bigendian);

        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.get(0);
    }

}
