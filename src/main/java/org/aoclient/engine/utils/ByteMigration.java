package org.aoclient.engine.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class ByteMigration {

    /**
     * @desc: Permite migrar un Integer de mas cantidad de bytes a uno mas chico por que en
     *        VB6 las variables son de distinta cantidad de bytes
     */
    public static int bigToLittle_Int(int bigendian) {
        ByteBuffer buf = ByteBuffer.allocate(4);

        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putInt(bigendian);

        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getInt(0);
    }

    /**
     * @desc: Permite migrar un Float de mas cantidad de bytes a uno mas chico por que en
     *        VB6 las variables son de distinta cantidad de bytes
     */
    public static float bigToLittle_Float(float bigendian) {
        ByteBuffer buf = ByteBuffer.allocate(4);

        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putFloat(bigendian);

        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getFloat(0);
    }

    /**
     * @desc: Permite migrar un Float de mas cantidad de bytes a uno mas chico por que en
     *        VB6 las variables son de distinta cantidad de bytes
     */
    public static double bigToLittle_Double(double bigendian) {
        ByteBuffer buf = ByteBuffer.allocate(8);

        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putDouble(bigendian);

        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getDouble(0);
    }

    /**
     * @desc: Permite migrar un Short de mas cantidad de bytes a uno mas chico por que en
     *        VB6 las variables son de distinta cantidad de bytes
     */
    public static short bigToLittle_Short(short bigendian) {
        ByteBuffer buf = ByteBuffer.allocate(2);

        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putShort(bigendian);

        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getShort(0);
    }

    /**
     * @desc: Permite migrar un Byte de mas cantidad de bytes a uno mas chico por que en
     *        VB6 las variables son de distinta cantidad de bytes
     */
    public static byte bigToLittle_Byte(byte bigendian) {
        ByteBuffer buf = ByteBuffer.allocate(1);

        buf.order(ByteOrder.BIG_ENDIAN);
        buf.put(bigendian);

        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.get(0);
    }

}
