package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.Dialogs;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.PacketBuffer;

import static org.aoclient.engine.game.Dialogs.charDialogSet;
import static org.aoclient.engine.utils.GameData.charList;

/**
 * En este handler y en algunos otros, se utiliza un buffer temporal ({@code tempBuffer}) como "espacio seguro" para leer los
 * bytes antes de actualizar el buffer del servidor, funcionando como una "transaccion" que se confirma al finalizar.
 * <p>
 * Por lo tanto, se evita leer directamente del buffer del servidor para prevenir estados inconsistentes (paquetes concatenados o
 * recibidos parcialmente) durante errores. Es un patron comun en protocolos de red para garantizar integridad de datos y el
 * avance correcto del puntero de lectura.
 */

public class ChatOverHeadHandler implements PacketHandler {

    /**
     * Este valor representa el numero minimo de bytes necesarios para procesar correctamente el paquete:
     * <ul>
     * <li>1 byte: identificador del paquete
     * <li>n bytes: cadena de texto (variable pero necesita al menos 1) TODO No se referiere a la longitud de la cadena que son 2 bytes?
     * <li>2 bytes: indice del personaje
     * <li>3 bytes: componentes de color (r,g,b)
     * </ul>
     */
    private static final int MIN_REQUIRED_BYTES = 8;

    @Override
    public void handle(PacketBuffer buffer) {

        // Verifica que el buffer del servidor tenga el minimo de bytes requeridos para procesar el paquete
        if (buffer.checkBytes(MIN_REQUIRED_BYTES)) return;

        // Crea un buffer temporal para almacenar los bytes del buffer del servidor
        PacketBuffer tempBuffer = new PacketBuffer();

        // Copia el buffer del servidor en el buffer temporal
        tempBuffer.copy(buffer);

        /* Este byte identificador ya fue utilizado previamente para determinar que handler debe procesar el paquete (en la clase
         * PacketProcessor). Una vez que el paquete llega al handler correcto, este byte identificador ya no es necesario para el
         * procesamiento posterior, por lo que se lee para "consumirlo" y avanzar en el buffer, pero su valor no se usa. */
        tempBuffer.readByte(); // Descarta el ID "CHAT_OVER_HEAD" del paquete

        // Lee el contenido del buffer
        String message = tempBuffer.readCp1252String(); // El cliente añadio esto!
        short charIndex = tempBuffer.readInteger(); // El servidor desde el codigo de VB6 añadio esto!
        int r = tempBuffer.readByte(); // El servidor desde el codigo de VB6 añadio esto!
        int g = tempBuffer.readByte(); // El servidor desde el codigo de VB6 añadio esto!
        int b = tempBuffer.readByte(); // El servidor desde el codigo de VB6 añadio esto!

        // Realiza la accion correspondiente con los bytes leidos
        if (charList[charIndex].getName().length() <= 1) Dialogs.removeDialogsNPCArea();
        charDialogSet(charIndex, message, new RGBColor((float) r / 255, (float) g / 255, (float) b / 255));

        /* Despues de leer todos los bytes del buffer temporal, este queda con una longitud 0, por lo tanto se pasa este estado
         * al buffer del servidor para "eliminar" los bytes leidos dejandolo limpio para recibir el siguiente paquete. */
        buffer.copy(tempBuffer);

    }

}
