# Flujo de Comunicacion Cliente-Servidor

## 1. Inicio de la conexion

Cuando el cliente inicia una conexion, el proceso comienza en la clase `SocketConnection`:

1. **Se llama al metodo `connect()`**:
    - Verifica si ya hay un intento de conexion en curso.
    - Comprueba si el socket no existe o esta cerrado.
    - Crea un nuevo socket utilizando la IP y puerto del servidor.
    - Inicializa los flujos de entrada (`inputStream`) y salida (`outputStream`).
    - Inicializa los buffers de entrada (`inputBuffer`) y salida (`outputBuffer`).

## 2. Envio de paquetes al servidor

Una vez establecida la conexion, el cliente puede enviar paquetes al servidor:

1. **Preparacion de los paquetes mediante `Protocol`**:
    - Se utilizan metodos `write` de la clase `Protocol` para añadir bytes al buffer de salida.
    - Metodos como `writeLoginExistingChar()` o `writeTalk()` construyen paquetes segun el protocolo.
    - Los bytes se almacenan en `outputBuffer`.

2. **Transmision de bytes**:
    - Se llama al metodo `write()` de `SocketConnection`.
    - Verifica si el socket esta listo para comunicacion.
    - Lee los bytes acumulados en el buffer de salida.
    - Envia los bytes al servidor a traves del flujo de salida.

## 3. Recepcion de paquetes del servidor

El servidor responde y el cliente procesa los paquetes recibidos:

1. **Lectura de datos mediante `read()`**:
    - Verifica si el socket esta listo para leer.
    - Comprueba si hay bytes disponibles en el flujo de entrada.
    - Lee los bytes disponibles y los almacena en el buffer de entrada.
    - Llama a `handleIncomingBytes()` para manejar los bytes entrantes.

2. **Manejo de bytes entrantes**:
    - `handleIncomingBytes()` delega el manejo de los bytes a `PacketReceiver`.
    - `PacketReceiver.handleIncomingBytes()` analiza el buffer de entrada.
    - Identifica el tipo de paquete (definido en `ServerPacket`).
    - Busca un handler registrado para ese tipo de paquete.
    - Delega el manejo al handler apropiado.
    - Si quedan bytes en el buffer, continua manejando recursivamente.

## 4. Ciclo de comunicacion

El proceso de comunicacion continua de manera ciclica mientras la conexion permanezca abierta:

1. El cliente envia paquetes al servidor utilizando los metodos `write` de `Protocol`.
2. Los bytes se acumulan en el buffer de salida.
3. El metodo `write()` de `SocketConnection` envia los bytes al servidor.
4. El servidor procesa los paquetes y envia respuestas.
5. El metodo `read()` de `SocketConnection` recibe los bytes y los almacena en `inputBuffer`.
6. `handleIncomingBytes()` maneja los bytes entrantes y ejecuta las acciones correspondientes.

## 5. Fin de la conexion

Cuando se necesita finalizar la comunicacion:

1. **Se llama al metodo `disconnect()`**:
    - Verifica si el socket esta preparado para comunicacion.
    - Cierra los flujos de entrada y salida.
    - Cierra el socket.
    - Restablece el estado del juego del usuario.

Este flujo garantiza una comunicacion bidireccional organizada entre el cliente y el servidor, con un manejo modular de
los diferentes tipos de paquetes segun el protocolo establecido para el juego Argentum Online.