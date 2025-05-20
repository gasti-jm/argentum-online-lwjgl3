package org.aoclient.network;

import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FMessage;
import org.aoclient.network.protocol.PacketReceiver;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Implementa un buffer de paquetes para la comunicacion de red.
 * <p>
 * Esta clase proporciona funcionalidades para manejar la lectura y escritura de bytes siguiendo el protocolo de red del juego.
 * Esta diseñada para mantener compatibilidad con los tipos de datos y formatos utilizados en Visual Basic 6.
 * <p>
 * La clase gestiona un array de bytes interno que sirve como almacen temporal para los bytes que entran y salen a traves de la
 * conexion de red. Proporciona metodos para escribir y leer diferentes tipos de datos primitivos y cadenas con formatos
 * especificos.
 * <p>
 * Los metodos de la clase se dividen en tres categorias principales:
 * <ul>
 *   <li>Metodos de escritura ({@code write*}): Añaden bytes al buffer.
 *   <li>Metodos de lectura ({@code read*}): Leen y eliminan bytes del buffer.
 *   <li>Metodos de inspeccion ({@code peek*}): Leen bytes sin eliminarlos del buffer.
 * </ul>
 * <p>
 * La clase incluye soporte para gestionar diferentes codificaciones de texto (ASCII, UTF-8, UTF-16) y maneja la conversion entre
 * tipos de datos de Java y sus equivalentes en Visual Basic 6.
 */

public class PacketBuffer {

    /**
     * Tamaño en bytes para almacenar la longitud de una cadena utilizado para mantener compatibilidad con el protocolo original
     * de AO.
     */
    private static final int STRING_LENGTH_BYTES = 2;
    /** Tamaño en bytes del tipo Integer en VB6 utilizado para mantener compatibilidad con el protocolo original de AO. */
    private static final int VB6_INTEGER_BYTES = 2;
    /** Tamaño en bytes del tipo Long en VB6 utilizado para mantener compatibilidad con el protocolo original de AO. */
    private static final int VB6_LONG_BYTES = 4;
    /** Tamaño predeterminado del buffer en bytes. */
    private static final int DEFAULT_BUFFER_SIZE = 10240; // 10 KB Serial tamaño ideal?
    /**
     * Array de bytes que actua como un almacen temporal para los bytes que entran y salen del flujo de comunicacion.
     * <p>
     * Cuando en la documetacion de esta clase se dice "el buffer", "desde el buffer", etc., entonces hace referencia a este array
     * de bytes. Y cuando se dice "el buffer local", "en el buffer local", etc., se hace referencia al buffer local que se crea en
     * los metodos *read y *peek para almacenar los bytes de este buffer.
     */
    private byte[] buffer;
    /** Capacidad del buffer. */
    private int bufferCapacity = DEFAULT_BUFFER_SIZE;
    /** Longitud del buffer. */
    private int bufferLength;

    public PacketBuffer() {
        buffer = new byte[DEFAULT_BUFFER_SIZE];
    }

    /**
     * Copia los bytes del buffer de origen al buffer. Si el buffer de origen esta vacio, se eliminan todos los bytes del buffer.
     *
     * @param srcBuffer buffer de origen desde el cual se copiaran los bytes
     */
    public void copy(PacketBuffer srcBuffer) {
        // Si el buffer de origen (tempBuffer) esta vacio
        if (srcBuffer.getLength() == 0) {
            // Entonces el buffer tambien debe quedar en 0 para poder recibir el siguiente paquete
            remove(bufferLength);
            return;
        }

        // Ajusta la capacidad del buffer para que coincida con la del origen
        bufferCapacity = srcBuffer.getCapacity();
        buffer = new byte[bufferCapacity];

        // Crea un buffer local con la capacidad del buffer de origen (mejorando el rendimiento)
        byte[] buffer = new byte[srcBuffer.getLength()];
        // Lee los bytes del buffer de origen y los copia al buffer local
        srcBuffer.peekBlock(buffer, srcBuffer.getLength());
        // Reinicia la longitud del buffer
        bufferLength = 0;
        // Escribe los bytes del buffer local en el buffer
        writeBlock(buffer);
    }

    /**
     * Escribe una cantidad especifica de bytes desde el buffer de origen al buffer.
     *
     * @param srcBuffer buffer de origen que contiene los bytes a escribir en el buffer
     * @throws RuntimeException si no hay suficiente espacio disponible en el buffer
     */
    private void write(byte[] srcBuffer) {
        int bytesToWrite = srcBuffer.length;
        int availableSpace = bufferCapacity - bufferLength;
        if (availableSpace < bytesToWrite) throw new RuntimeException("Not enough space in the buffer!");
        // Lee los bytes del buffer de origen y los copia al buffer
        System.arraycopy(srcBuffer, 0, buffer, bufferLength, bytesToWrite); // Los bytes del buffer local creado en copyByffer() se copian al buffer!
        bufferLength += bytesToWrite;
    }

    /**
     * Lee una cantidad especifica de bytes desde el buffer al buffer de destino.
     *
     * @param destBuffer buffer de destino en el que se copiaran los bytes leidos desde el buffer
     * @return la cantidad de bytes leidos
     * @throws IllegalArgumentException si la cantidad de bytes a leer es mayor a la longitud del buffer
     */
    private int read(byte[] destBuffer) {
        int bytesToRead = destBuffer.length;
        if (bytesToRead > bufferLength)
            throw new IllegalArgumentException("Not enough bytes available. Requested: " + bytesToRead + ", Available: " + bufferLength);
        // Lee los bytes del buffer y los copia al buffer de destino para su procesamiento antes de ser "eliminados"
        System.arraycopy(buffer, 0, destBuffer, 0, bytesToRead);
        return bytesToRead;
    }

    /**
     * "Elimina" una cantidad especificada de bytes del buffer y reorganiza los bytes restantes.
     *
     * @param bytesToRemove cantidad de bytes a eliminar
     * @return la cantidad de bytes eliminados
     */
    private int remove(int bytesToRemove) {
        // Limita la cantidad de bytes a eliminar a la longitud del buffer
        int actualBytesRemoved = Math.min(bytesToRemove, bufferLength);
        // Si la cantidad de bytes a eliminar es menor a la longitud del buffer
        if (actualBytesRemoved < bufferLength) reorganizeBuffer(actualBytesRemoved);
        // Actualiza la longitud del buffer
        bufferLength -= actualBytesRemoved;
        return actualBytesRemoved;
    }

    /**
     * Mueve los bytes restantes al inicio del buffer.
     * <p>
     * En lugar de una eliminacion fisica (que seria imposible en un array de Java), lo que ocurre es una reorganizacion donde los
     * bytes que deben permanecer en el buffer sobreescriben aquellos que deben ser "eliminados".
     * <p>
     * Esta implementacion es eficiente porque evita crear nuevos arrays cada vez que se eliminan bytes del buffer, aprovechando
     * el espacio existente.
     *
     * @param startPosition posicion desde donde se empieza a mover los bytes
     */
    private void reorganizeBuffer(int startPosition) {
        // Calcula la cantidad de bytes restantes
        int remainingBytes = bufferLength - startPosition;
        // Sobreescribe los bytes "eliminados" por los bytes restantes
        System.arraycopy(buffer, startPosition, buffer, 0, remainingBytes);
    }

    /**
     * Escribe un valor entero representado como un byte en el buffer.
     * <p>
     * Nota: Solo se utiliza el byte menos significativo del valor int proporcionado.
     *
     * @param value valor entero a escribir, que se convertira a un {@code byte}
     */
    public void writeByte(int value) {
        write(new byte[]{(byte) value});
    }

    /**
     * <p>
     * Escribe un valor de tipo {@code short} en el buffer, convirtiendolo a su representacion binaria en formato de orden de
     * bytes {@code LITTLE_ENDIAN}.
     * <p>
     * Este metodo implementa la compatibilidad con el protocolo de red original del juego, usando el tipo de dato {@code short}
     * de Java (16 bits con signo) para representar al equivalente <b>Integer</b> de Visual Basic 6 que ocupa 2 bytes.
     *
     * @param value valor de tipo {@code short} a escribir en el buffer
     */
    public void writeInteger(short value) {
        write(ByteBuffer.allocate(VB6_INTEGER_BYTES).order(ByteOrder.LITTLE_ENDIAN).putShort(value).array());
    }

    /**
     * <p>
     * Escribe un valor de tipo {@code int} en el buffer, convirtiendolo a su representacion binaria en formato de orden de bytes
     * {@code LITTLE_ENDIAN}.
     * <p>
     * Este metodo implementa la compatibilidad con el protocolo de red original del juego, usando el tipo de dato {@code int} de
     * Java (32 bits con signo) para representar al equivalente <b>Long</b> de Visual Basic 6 que ocupa 4 bytes.
     *
     * @param value valor de tipo {@code int} a escribir en el buffer
     */
    public void writeLong(int value) {
        write(ByteBuffer.allocate(VB6_LONG_BYTES).order(ByteOrder.LITTLE_ENDIAN).putInt(value).array());
    }

    /**
     * <p>
     * Escribe un valor de tipo {@code float} en el buffer, convirtiendolo a su representacion binaria en formato de orden de
     * bytes {@code LITTLE_ENDIAN}.
     * <p>
     * Nota: En VB6, el tipo de dato {@code float} se conoce como <b>Single</b>.
     *
     * @param value valor de tipo {@code float} a escribir en el buffer
     */
    public void writeFloat(float value) {
        write(ByteBuffer.allocate(Float.BYTES).order(ByteOrder.LITTLE_ENDIAN).putFloat(value).array());
    }

    /**
     * <p>
     * Escribe un valor de tipo {@code double} en el buffer, convirtiendolo a su representacion binaria en formato de orden de
     * bytes {@code LITTLE_ENDIAN}.
     *
     * @param value valor de tipo {@code double} a escribir en el buffer
     */
    public void writeDouble(double value) {
        write(ByteBuffer.allocate(Double.BYTES).order(ByteOrder.LITTLE_ENDIAN).putDouble(value).array());
    }

    /**
     * <p>
     * Escribe un valor de tipo {@code boolean} en el buffer, convirtiendolo a su representacion binaria (1 para {@code true}, 0
     * para {@code false}).
     *
     * @param value valor de tipo {@code boolean} a escribir en el buffer
     */
    public void writeBoolean(boolean value) {
        write(new byte[]{(byte) (value ? 1 : 0)});
    }

    /**
     * Escribe una cadena de texto en formato ASCII en el buffer.
     *
     * @param string cadena de texto que sera codificada en ASCII y escrita en el buffer
     */
    public void writeASCIIStringFixed(String string) {
        write(string.getBytes(StandardCharsets.US_ASCII));
    }

    /**
     * Escribe una cadena de texto en formato Cp1252 en el buffer.
     *
     * @param string cadena de texto que sera codificada en Cp1252 y escrita en el buffer
     */
    public void writeCp1252String(String string) {
        byte[] stringBytes = string.getBytes(java.nio.charset.Charset.forName("Cp1252"));
        byte[] buffer = new byte[STRING_LENGTH_BYTES + string.length()];
        /* Como se esta escribiendo una cadena codificada en Cp1252, entonces los caracteres ocupan solo 1 byte, por lo tanto es
         * valido obtener la longitud de la cadena con string.length(). */
        ByteBuffer.wrap(buffer).put(0, (byte) string.length());
        System.arraycopy(stringBytes, 0, buffer, STRING_LENGTH_BYTES, string.length());
        write(buffer);
    }

    /**
     * Escribe una cadena de texto en formato UTF-8 en el buffer.
     * <p>
     * La longitud de la cadena en bytes se almacena al inicio del buffer, seguida de los bytes de la cadena. Este formato de
     * longitud prefijada ("length-prefixed strings"), es compatible con el protocolo de red del juego.
     * <p>
     * FIXME No maneja simbolos internacionales como el chino
     *
     * @param string cadena de texto que sera codificada en UTF-8 y escrita en el buffer
     */
    public void writeUTF8String(String string) {
        // Convierte la cadena en un arreglo de bytes usando la codificacion UTF-8
        byte[] stringBytes = string.getBytes(StandardCharsets.UTF_8);
        /* En UTF-8, ciertos caracteres especiales ocupan mas de un byte, por lo tanto no seria correcto usar string.length() ya
         * que solo devolveria la cantidad de caracteres sin considerar el espacio real que ocupan los caracteres especiales. Por
         * eso es necesario usar la longitud en bytes de la cadena, no en caracteres. */
        int bytes = stringBytes.length;
        // Crea un buffer local con la capacidad para almacenar la longitud y los bytes de la cadena
        byte[] buffer = new byte[STRING_LENGTH_BYTES + bytes];
        // Agrega la longitud de la cadena en los primeros 2 bytes del buffer
        ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).putShort((short) bytes);
        // Agrega los bytes de la cadena a partir de la posicion 2 del buffer, es decir, despues de haber agregado los dos bytes que representan la longitud de la cadena
        System.arraycopy(stringBytes, 0, buffer, STRING_LENGTH_BYTES, bytes);
        write(buffer);
    }

    /**
     * Escribe un bloque de bytes.
     * <p>
     * El termino "block" en este contexto se refiere a una unidad contigua de bytes que se maneja como una sola entidad,
     * siguiendo la terminologia comun en protocolos de comunicacion y programacion de redes.
     *
     * @param buffer array de bytes
     */
    public void writeBlock(byte[] buffer) {
        write(buffer);
    }

    /**
     * Lee una cantidad especifica de bytes.
     * <p>
     * Primero se copian los bytes del buffer al buffer local y luego se "eliminan" (marcandolos como leidos) para evitar que se
     * procesen de nuevo. Al "eliminar" los bytes ya procesados, el buffer mantiene solo los bytes pendientes por procesar, lo que
     * facilita la gestion del estado de la comunicacion.
     *
     * @return un arreglo de bytes que contiene los bytes leidos
     */
    public byte[] readBytes() {
        byte[] buffer = new byte[bufferLength];
        // En Java, los arreglos son objetos que se pasan por referencia, por lo tanto se copia el contenido del buffer al buffer local
        int bytesRead = read(buffer);
        remove(bytesRead); // "Elimina" los bytes leidos del buffer una vez ya copiados al buffer local
        return buffer;
    }

    /**
     * Lee un byte del buffer y lo devuelve como un entero sin signo.
     * <p>
     * Aplica una operacion bit a bit {@code & 0xFF} para asegurar que el valor sea tratado como un entero sin signo en el rango
     * de 0 a 255. Esto es necesario porque en Java los bytes son con signo (rango -128 a 127), pero en el protocolo de red suelen
     * tratarse como sin signo (0 a 255). Por eso mismo, las constantes (ID del paquete) de {@code ServerPacket}, estan enumeradas
     * de 0 a 255.
     *
     * @return el byte leido como un entero sin signo
     */
    public int readByte() {
        byte[] buffer = new byte[Byte.BYTES];
        // Copia el byte leido al buffer local
        int bytesRead = read(buffer);
        // "Elimina" el byte leido del buffer, avanzando efectivamente el puntero
        remove(bytesRead);
        return ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).get() & 0xFF;
    }

    /**
     * Lee un valor de tipo {@code int} del buffer.
     * <p>
     * Este metodo implementa la compatibilidad con el protocolo de red original del juego, usando el tipo de dato {@code short}
     * de Java para representar al equivalente <b>Integer</b> de Visual Basic 6 que ocupa 2 bytes. Por lo tanto, a pesar de
     * llamarse "readInteger", realmente lee un entero de 16 bits, no un int de Java (que seria de 32 bits).
     *
     * @return el valor de tipo int del buffer
     */
    public short readInteger() {
        byte[] buffer = new byte[VB6_INTEGER_BYTES];
        int bytesRead = read(buffer);
        remove(bytesRead);
        return ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }

    /**
     * Lee un valor de tipo {@code long} del buffer.
     * <p>
     * Este metodo implementa la compatibilidad con el protocolo de red original del juego, usando el tipo de dato {@code int} de
     * Java para representar al equivalente <b>Long</b> de Visual Basic 6 que ocupa 4 bytes. Por lo tanto, a pesar de llamarse
     * "readLong", realmente lee un entero de 32 bits, no un long de Java (que seria de 64 bits).
     *
     * @return el valor de tipo long del buffer
     */
    public int readLong() {
        byte[] buffer = new byte[VB6_LONG_BYTES];
        int bytesRead = read(buffer);
        remove(bytesRead);
        return ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    /**
     * Lee un valor de tipo {@code float} del buffer.
     *
     * @return el valor de tipo float del buffer
     */
    public float readFloat() {
        byte[] buffer = new byte[Float.BYTES];
        int bytesRead = read(buffer);
        remove(bytesRead);
        return ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }

    /**
     * Lee un valor de tipo {@code boolean} del buffer.
     *
     * @return el valor de tipo boolean del buffer
     */
    public boolean readBoolean() {
        byte[] buffer = new byte[Byte.BYTES];
        int bytesRead = read(buffer);
        remove(bytesRead);
        return buffer[0] == 1;
    }

    public String readUnicodeStringFixed(int length) {
        if (length <= 0) return "";
        if (bufferLength >= length * 2) {
            byte[] buf = new byte[length * 2];
            remove(read(buf));
            return new String(buf, StandardCharsets.UTF_16LE);
        } else throw new RuntimeException("Not enough data");
    }

    /**
     * Lee una cadena codificada en Cp1252 del buffer.
     * <p>
     * El metodo primero interpreta los primeros 2 bytes en formato little-endian como un valor short, el cual determina la
     * longitud de la cadena a leer. Despues, lee la cantidad especificada de bytes y los decodifica como texto en formato
     * Cp1252.
     *
     * @return la cadena leida en formato Cp1252, si la longitud de la cadena es 0, se devuelve una cadena vacia
     * @throws RuntimeException si no hay suficientes bytes en el buffer
     */
    public String readCp1252String() {
        // Verifica si hay suficientes bytes para leer la longitud de la cadena
        if (bufferLength <= 1) throw new RuntimeException("Not enough byte!");
        short stringLength = readStringLength();
        // Verifica si hay suficientes bytes para leer la cadena completa
        if (bufferLength < stringLength) throw new RuntimeException("Not enough byte!");
        // Si la longitud es cero, devuelve cadena vacia
        if (stringLength <= 0) return "";
        return readString(stringLength, java.nio.charset.Charset.forName("Cp1252"));
    }

    /**
     * Lee la longitud de una cadena desde un array de bytes.
     * <p>
     * El metodo asume que los bytes correspondientes a la longitud se encuentran al principio del buffer y se almacenan en
     * formato LITTLE_ENDIAN.
     *
     * @return La longitud de la cadena representada como un valor de tipo short
     */
    private short readStringLength() {
        byte[] buffer = new byte[STRING_LENGTH_BYTES];
        read(buffer);
        short length = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getShort();
        // Elimina los bytes de la longitud de la cadena que ya fueron leidos
        remove(STRING_LENGTH_BYTES);
        return length;
    }

    /**
     * Lee una cadena de texto desde el buffer utilizando la longitud especificada y el conjunto de caracteres proporcionado.
     *
     * @param stringLength longitud de la cadena en bytes
     * @param charset      charset utilizado para decodificar los bytes
     * @return la cadena decodificada
     */
    private String readString(short stringLength, Charset charset) {
        byte[] bytes = new byte[stringLength];
        read(bytes);
        remove(stringLength);
        return new String(bytes, charset);
    }

    public String readUnicodeString() {
        byte[] buffer = new byte[2];
        int length;
        if (bufferLength > 1) {
            remove(read(buffer));
            length = ByteBuffer.wrap(buffer).getShort();
            if (bufferLength >= length * 2 + 2) {
                remove(2);
                byte[] buf2 = new byte[length * 2];
                remove(read(buf2/*, length * 2*/));
                return new String(buf2, StandardCharsets.UTF_16LE);
            } else throw new RuntimeException("Not enough bytes!");
        }
        throw new RuntimeException("Not enough bytes!");
    }

    public long readBlock(byte[] block, long dataLength) {
        if (dataLength > 0) return remove(read(block));
        return 0;
    }

    /**
     * Examina el primer byte del buffer, tipicamente usado para identificar el ID del paquete, sin eliminarlo.
     * <p>
     * Este metodo permite inspeccionar el tipo de paquete recibido antes de procesarlo completamente, facilitando la toma de
     * decisiones sobre como manejar los datos entrantes. Al no eliminar el byte del buffer, permite operaciones posteriores de
     * lectura (read) que recuperaran el mismo valor.
     *
     * @return el primer byte del buffer, interpretado como un valor sin signo (0-255)
     */
    public byte peekByte() {
        byte[] buffer = new byte[Byte.BYTES];
        read(buffer);
        return buffer[0];
    }

    public short peekInteger() {
        byte[] buffer = new byte[VB6_INTEGER_BYTES];
        read(buffer);
        return ByteBuffer.wrap(buffer).getShort();
    }

    public int peekLong() {
        byte[] buffer = new byte[VB6_LONG_BYTES];
        read(buffer);
        return ByteBuffer.wrap(buffer).getInt();
    }

    public float peekFloat() {
        byte[] buffer = new byte[Float.BYTES];
        read(buffer);
        return ByteBuffer.wrap(buffer).getFloat();
    }

    public double peekDouble() {
        byte[] buffer = new byte[Double.BYTES];
        read(buffer);
        return ByteBuffer.wrap(buffer).getDouble();
    }

    public boolean peekBoolean() {
        byte[] buffer = new byte[Byte.BYTES];
        read(buffer);
        return buffer[0] == 1;
    }

    public String peekASCIIStringFixed(int length) {
        if (length <= 0) return "";
        if (bufferLength >= length) {
            byte[] buf = new byte[length];
            read(buf);
            return new String(buf, StandardCharsets.UTF_8);
        } else throw new RuntimeException("Not enough bytes!");
    }

    public String peekUnicodeStringFixed(int length) {
        if (length <= 0) return "";
        if (bufferLength >= length * 2) {
            byte[] buf = new byte[length * 2];
            read(buf);
            return new String(buf, StandardCharsets.UTF_16LE);
        } else throw new RuntimeException("Not enough bytes!");
    }

    public String peekASCIIString() {
        byte[] buf = new byte[2];
        int length;

        if (bufferLength > 1) {
            read(buf);
            length = ByteBuffer.wrap(buf).getShort();

            if (bufferLength >= length + 2) {
                byte[] buf2 = new byte[length + 2];
                read(buf2);

                if (length > 0) {
                    byte[] buf3 = new byte[length];
                    System.arraycopy(buf2, 2, buf3, 0, length);
                    return new String(buf3, StandardCharsets.UTF_8);
                }
            } else {
                throw new RuntimeException("Not enough bytes!");
            }
        }

        throw new RuntimeException("Not enough bytes!");
    }

    public String peekUnicodeString() {
        byte[] buf = new byte[2];
        int length;

        if (bufferLength > 1) {
            read(buf);
            length = ByteBuffer.wrap(buf).getShort();

            if (bufferLength >= length * 2 + 2) {
                byte[] buf2 = new byte[length * 2 + 2];
                read(buf2);
                byte[] buf3 = new byte[length * 2];
                System.arraycopy(buf2, 2, buf3, 0, length * 2);
                return new String(buf3, StandardCharsets.UTF_16LE);
            } else {
                throw new RuntimeException("Not enough bytes!");
            }
        }

        throw new RuntimeException("Not enough bytes!");
    }

    private void peekBlock(byte[] block, int dataLength) {
        if (dataLength > 0) read(block);
    }

    public int getLength() {
        return bufferLength;
    }

    public int getCapacity() {
        return bufferCapacity;
    }

    public void setCapacity(int value) {
        // Update capacity
        bufferCapacity = value;

        // All extra data is lost
        if (bufferLength > value) bufferLength = value;

        // Resize the queue
        byte[] newData = new byte[bufferCapacity];
        System.arraycopy(buffer, 0, newData, 0, bufferLength);
        buffer = newData;
    }

    /**
     * <p>
     * Revisa la cantidad de bytes que tiene que leer de un paquete en caso de que no coincida la cantidad se presentara un
     * error.
     */
    public boolean checkBytes(int bytes) {
        if (bufferLength < bytes) {
            disconnect();
            return true;
        }
        return false;
    }

    private void disconnect() {
        String msgErr = "Not enough bytes to read from the packet " + PacketReceiver.serverPacket;
        ImGUISystem.INSTANCE.show(new FMessage(msgErr));
        System.out.println(msgErr);
        SocketConnection.INSTANCE.disconnect();
        // Resetea el buffer para que termine la recursividad de lectura de paquetes
        buffer = new byte[DEFAULT_BUFFER_SIZE];
        bufferCapacity = DEFAULT_BUFFER_SIZE;
        bufferLength = 0;
    }

}