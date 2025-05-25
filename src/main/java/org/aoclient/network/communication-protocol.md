# Protocolo de Comunicacion Cliente-Servidor en Argentum Online

## Introduccion

Este documento describe el protocolo de comunicacion entre el cliente Java y el servidor VB6 para Argentum Online,
explicando su arquitectura, flujo de bytes y consideraciones tecnicas.

## 1. Fundamentos del Protocolo

### 1.1 Patron de Comunicacion

La comunicacion entre el cliente y el servidor sigue un patron especifico:

1. **El cliente envia paquetes minimos con intenciones:**
    - Cada paquete inicia con 1 byte que identifica el tipo de paquete (ID)
    - Solo incluye la informacion esencial necesaria para expresar la intencion del usuario
    - No incluye informacion de estado que es responsabilidad del servidor

2. **El servidor enriquece la informacion y responde:**
    - Procesa la intencion del cliente aplicando reglas y validaciones
    - Añade informacion contextual (quien realiza la accion, resultados, etc.)
    - Puede enviar multiples paquetes en respuesta a una unica accion del cliente
    - Determina quien debe recibir la informacion (el usuario, usuarios cercanos, etc.)

### 1.2 Fundamentos del Orden de Bytes (Little-Endian)

El protocolo utiliza el formato little-endian para la transmision de datos multi-byte, una decision que tiene raices en
la implementacion original del juego.

#### ¿Que es Little-Endian?

En el formato little-endian, el byte menos significativo (LSB) se almacena en la direccion de memoria mas baja, mientras
que el byte mas significativo (MSB) se almacena en la direccion mas alta.

Por ejemplo, el numero hexadecimal `0x12345678` se almacena en memoria como:

```
Direccion: 0x1000 0x1001 0x1002 0x1003 
Contenido:   78     56     34     12
```

#### Razones para el uso de Little-Endian en Argentum Online

- **Contexto historico**: Argentum Online fue desarrollado originalmente en 1999 usando Visual Basic 6.0 para Windows
  95/98, que operaban en procesadores x86 (little-endian).

- **Simplicidad de implementacion**: Usar el mismo formato que el sistema operativo y procesador subyacentes eliminaba
  la necesidad de conversiones.

- **Rendimiento**: En una epoca con recursos computacionales limitados, evitar conversiones innecesarias mejoraba el
  rendimiento.

- **Acceso directo a memoria**: VB6 permitia manipular memoria directamente, haciendo natural mantener el formato
  little-endian.

## 2. Flujo de Comunicacion

### 2.1 Inicio de la conexion

Cuando el cliente inicia una conexion, el proceso comienza en la clase `SocketConnection`:

1. **Se llama al metodo `connect()`**:
    - Verifica si ya hay un intento de conexion en curso
    - Comprueba si el socket no existe o esta cerrado
    - Crea un nuevo socket utilizando la IP y puerto del servidor
    - Inicializa los flujos de entrada (`inputStream`) y salida (`outputStream`)
    - Inicializa los buffers de entrada (`inputBuffer`) y salida (`outputBuffer`)

### 2.2 Envio de paquetes al servidor

Una vez establecida la conexion, el cliente puede enviar paquetes al servidor:

1. **Preparacion de los paquetes mediante `Protocol`**:
    - Se utilizan metodos `write` de la clase `Protocol` para añadir bytes al buffer de salida
    - Metodos como `writeLoginExistingChar()` o `writeTalk()` construyen paquetes segun el protocolo
    - Los bytes se almacenan en `outputBuffer`

2. **Transmision de bytes**:
    - Se llama al metodo `write()` de `SocketConnection`
    - Verifica si el socket esta listo para comunicacion
    - Lee los bytes acumulados en el buffer de salida
    - Envia los bytes al servidor a traves del flujo de salida

### 2.3 Recepcion de paquetes del servidor

El servidor responde y el cliente procesa los paquetes recibidos:

1. **Lectura de datos mediante `read()`**:
    - Verifica si el socket esta listo para leer
    - Comprueba si hay bytes disponibles en el flujo de entrada
    - Lee los bytes disponibles y los almacena en el buffer de entrada
    - Llama a `handleIncomingBytes()` para manejar los bytes entrantes

2. **Manejo de bytes entrantes**:
    - `handleIncomingBytes()` delega el manejo de los bytes a `PacketReceiver`
    - `PacketReceiver.handleIncomingBytes()` analiza el buffer de entrada
    - Identifica el tipo de paquete (definido en `ServerPacket`)
    - Busca un handler registrado para ese tipo de paquete
    - Delega el manejo al handler apropiado
    - Si quedan bytes en el buffer, continua manejando recursivamente

### 2.4 Ciclo de comunicacion

El proceso de comunicacion continua de manera ciclica mientras la conexion permanezca abierta:

1. El cliente envia paquetes al servidor utilizando los metodos `write` de `Protocol`
2. Los bytes se acumulan en el buffer de salida
3. El metodo `write()` de `SocketConnection` envia los bytes al servidor
4. El servidor procesa los paquetes y envia respuestas
5. El metodo `read()` de `SocketConnection` recibe los bytes y los almacena en `inputBuffer`
6. `handleIncomingBytes()` maneja los bytes entrantes y ejecuta las acciones correspondientes

### 2.5 Fin de la conexion

Cuando se necesita finalizar la comunicacion:

1. **Se llama al metodo `disconnect()`**:
    - Verifica si el socket esta preparado para comunicacion
    - Cierra los flujos de entrada y salida
    - Cierra el socket
    - Restablece el estado del juego del usuario

## 3. Estructura de los Paquetes

### 3.1 Formato general

Los paquetes siguen una estructura basica que consiste en:

- Un identificador de tipo de paquete (1 byte)
- Los datos especificos del paquete (longitud variable)

### 3.2 Ejemplos de transformacion de paquetes

#### TALK → CHAT_OVER_HEAD

**Cliente envia (TALK):**

- 1 byte: ID del paquete (3)
- N bytes: String del mensaje (longitud variable)

**Servidor responde (CHAT_OVER_HEAD):**

- 1 byte: ID del paquete (23)
- N bytes: String del mensaje (igual que el enviado)
- 2 bytes: CharIndex del personaje que habla
- 1 byte: Componente rojo del color (R)
- 1 byte: Componente verde del color (G)
- 1 byte: Componente azul del color (B)

## 4. Consideraciones para la Implementacion Java

### 4.1 Manejo de Little-Endian en Java

Para mantener la compatibilidad con el protocolo original, la implementacion Java debe:

- Serializar valores numericos multibyte en formato little-endian antes de enviarlos
- Deserializar los bytes recibidos interpretandolos como valores en formato little-endian

En la implementacion Java, esto se logra mediante:

```
// Ejemplo de uso de ByteBuffer con little-endian
ByteBuffer buffer = ByteBuffer.allocate(4);
buffer.order(ByteOrder.LITTLE_ENDIAN);
buffer.putInt(value);
```

### 4.2 Compatibilidad con el servidor VB6

Aunque los valores del enum de `ServerPacket` se especifican explicitamente con un ID, en el enum `ServerPacketID` de
`Protocol.bas` del servidor, los valores estan implicitos en el ordenamiento.

Para garantizar la compatibilidad con el servidor VB6 original:

- Los IDs de paquetes en `ClientPacket.java` y `ServerPacket.java` **deben coincidir exactamente** con los valores en el
  enum `ClientPacketID` y `ServerPacketID` del codigo VB6 del servidor
- Modificar estos IDs rompera la compatibilidad con el servidor
- Al implementar nuevos paquetes, se debe respetar el formato de bytes utilizando little-endian para valores multi-byte

### 4.3 Implementacion de metodos de lectura/escritura

La clase `Protocol` debe implementar metodos para manejar los diferentes tipos de datos:

```java
// Ejemplo conceptual de la clase Protocol
public class Protocol {
    // Escribe un entero en formato little-endian
    public void writeInteger(int value) {
        // Descompone el valor en bytes individuales (little-endian)
        outputBuffer.put((byte) (value & 0xFF)); // Byte menos significativo primero
        outputBuffer.put((byte) ((value >> 8) & 0xFF));
        outputBuffer.put((byte) ((value >> 16) & 0xFF));
        outputBuffer.put((byte) ((value >> 24) & 0xFF)); // Byte mas significativo al final
    }

    // Lee un entero en formato little-endian
    public int readInteger() {
        int value = 0;
        value |= (inputBuffer.get() & 0xFF);
        value |= ((inputBuffer.get() & 0xFF) << 8);
        value |= ((inputBuffer.get() & 0xFF) << 16);
        value |= ((inputBuffer.get() & 0xFF) << 24);
        return value;
    }
}
```

## 5. Beneficios del Diseño

Este modelo de comunicacion cliente-servidor ofrece numerosas ventajas:

- **Seguridad**: El cliente no puede tomar decisiones criticas que afecten al juego
- **Consistencia**: El servidor mantiene el estado unico y autoritativo del juego
- **Eficiencia**: Los paquetes del cliente son minimos, reduciendo el trafico de red
- **Autoridad centralizada**: Todas las reglas se aplican en un solo lugar
- **Anti-cheating**: Los jugadores no pueden modificar datos criticos como daño o posicion

## 6. Conclusion

El protocolo de comunicacion cliente-servidor de Argentum Online sigue un principio fundamental: **el cliente propone,
el servidor dispone**. El cliente nunca tiene autoridad final sobre el estado del juego, lo que brinda seguridad y
consistencia.

Esta arquitectura, caracteristica de los juegos online multijugador, proporciona un equilibrio entre rendimiento (
minimizando datos transmitidos) y seguridad (manteniendo la autoridad en el servidor).

La implementacion Java del cliente debe respetar las particularidades tecnicas del protocolo original, especialmente el
uso de little-endian, para garantizar la compatibilidad con el servidor VB6 existente.
