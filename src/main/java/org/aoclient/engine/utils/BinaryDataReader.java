package org.aoclient.engine.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * <p>
 * Proporciona una interfaz simplificada para leer diferentes tipos de datos primitivos y estructurados a partir de un arreglo de
 * bytes. Esta clase es fundamental para la carga de recursos como graficos, mapas, sonidos y configuraciones que se almacenan en
 * formato binario.
 * <p>
 * La clase permite especificar el orden de bytes durante la inicializacion, lo que facilita la lectura correcta de datos binarios
 * generados en diferentes plataformas o por diferentes herramientas.
 * <p>
 * Funcionalidades principales:
 * <ul>
 * <li>Lectura de tipos primitivos
 * <li>Lectura de cadenas de texto con longitud fija o variable
 * <li>Soporte para diferentes ordenes de bytes (big-endian, little-endian)
 * <li>Capacidad para saltar bytes o posicionarse en puntos especificos del buffer
 * <li>Verificacion de disponibilidad de datos restantes
 * </ul>
 * <p>
 * Esta clase es utilizada extensivamente en el proceso de carga de archivos de inicializacion, como {@code inits.ao} y
 * otros recursos que definen la apariencia y comportamiento del mundo de Argentum Online.
 * <p>
 * Ejemplo de uso tipico: inicializar con un arreglo de bytes proveniente de un archivo comprimido y luego leer secuencialmente
 * los datos segun la estructura esperada del archivo.
 */

public class BinaryDataReader {

    private ByteBuffer buffer;

    public BinaryDataReader() {

    }

    public void init(byte[] data) {
        buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN); // Especifica el orden de los bytes (BIG_ENDIAN o LITTLE_ENDIAN)
    }

    public void init(byte[] data, ByteOrder order) {
        buffer = ByteBuffer.wrap(data);
        buffer.order(order); // Especifica el orden de los bytes (BIG_ENDIAN o LITTLE_ENDIAN)
    }

    // Lee un entero (4 bytes)
    public int readInt() {
        /* Lee los siguientes 4 bytes de la posicion actual del buffer, componiendolos en un valor int segun el orden de bytes
         * actual y luego incrementa la posicion en 4. */
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
