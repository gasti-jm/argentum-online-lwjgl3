package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.Dialogs;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.PacketBuffer;

import static org.aoclient.engine.game.Dialogs.charDialogSet;
import static org.aoclient.engine.utils.GameData.charList;

public class ChatOverHeadHandler implements PacketHandler {

    @Override
    public void handle(PacketBuffer srcBuffer) {
        /* El valor 8 representa el numero minimo de bytes que deben estar disponibles en el buffer para poder procesar
         * correctamente este paquete. Estos 8 bytes corresponden un byte para identificar el paquete, bytes para la cadena de
         * texto del chat (variable, pero necesita al menos algo), dos bytes para el indice del personaje, un byte para el
         * componente rojo del color (r), un byte para el componente verde del color (g) y un byte para el componente azul del
         * color (b). Al verificar que haya al menos 8 bytes disponibles, el manjeador se asegura de que puede leer el paquete
         * para mostrar correctamente el mensaje sobre la cabeza del personaje. */
        if (srcBuffer.checkBytes(8)) return; // TODO Reemplazar el valor magico 8

        // Crea un buffer temporal
        PacketBuffer buffer = new PacketBuffer();

        // Copia el buffer de origen al buffer temporal
        buffer.copy(srcBuffer);

        // Lee los bytes del buffer de origen usando el buffer temporal

        /* Este byte identificador ya fue utilizado previamente para determinar que handler debe procesar el paquete (en la clase
         * PacketReceiver). Una vez que el paquete llega al handler correcto, este byte identificador ya no es necesario para el
         * procesamiento posterior, por lo que se lee para "consumirlo" y avanzar en el buffer, pero su valor no se usa. */
        buffer.readByte(); // Descarta el ID del paquete (ya sabemos que es CHAT_OVER_HEAD)

        // Lee el contenido del paquete
        String chat = buffer.readUTF8String();
        short charIndex = buffer.readInteger(); // El servidor añadio esto!
        int r = buffer.readByte(); // El servidor añadio esto!
        int g = buffer.readByte(); // El servidor añadio esto!
        int b = buffer.readByte(); // El servidor añadio esto!

        if (charList[charIndex].getName().length() <= 1) Dialogs.removeDialogsNPCArea(); // TODO es un NPC?

        charDialogSet(charIndex, chat, new RGBColor((float) r / 255, (float) g / 255, (float) b / 255));

        // Copia el buffer temporal de vuelta al original
        srcBuffer.copy(buffer); // TODO Pero no se supone que el buffer temporal ya esta vacio ya que se leyeron todos sus bytes?

        /* Este patron (de usar un buffer temporal para lo bytes entrantes) permite manipular los bytes entrantes sin alterar el
         * buffer de origen hasta que se completan todas las operaciones necesarias, proporcionando una forma segura de procesar
         * los paquetes de red. */

    }

}
