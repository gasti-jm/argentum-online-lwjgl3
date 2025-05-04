# Flujo de Comunicacion Cliente-Servidor

## 1. Inicio de la conexion

Cuando el cliente inicia una conexion, el proceso comienza en la clase `SocketConnection`:

1. **Se llama al metodo `connect()`**:
    - Verifica si ya hay un intento de conexion en curso.
    - Comprueba si el socket no existe o esta cerrado.
    - Crea un nuevo socket utilizando la IP y puerto configurados.
    - Inicializa los flujos de entrada (`inputStream`) y salida (`outputStream`).
    - Limpia los buffers de entrada (`incomingData`) y salida (`outgoingData`).

## 2. Envio de datos al servidor

Una vez establecida la conexion, el cliente puede enviar datos al servidor:

1. **Preparacion de los datos mediante `Protocol`**:
    - Se utilizan metodos `writeX` de la clase `Protocol` para añadir comandos al buffer de salida.
    - Metodos como `writeLoginExistingChar()` o `writeThrowDices()` construyen paquetes segun el protocolo.
    - Los datos se almacenan en el buffer `outgoingData` (un objeto `ByteQueue`).

2. **Transmision de datos**:
    - Se llama al metodo `write()` de `SocketConnection`.
    - Verifica si el socket esta listo para comunicacion.
    - Lee los bytes acumulados en el buffer `outgoingData`.
    - Envia los bytes al servidor a traves del `outputStream`.

## 3. Recepcion de datos del servidor

El servidor responde y el cliente procesa los datos recibidos:

1. **Lectura de datos mediante `read()`**:
    - Verifica si el socket esta listo para leer.
    - Comprueba si hay bytes disponibles en el flujo de entrada.
    - Lee los bytes disponibles y los almacena en el buffer `incomingData`.
    - Llama a `handleIncomingData()` para procesar los datos recibidos.

2. **Procesamiento de datos entrantes**:
    - `handleIncomingData()` delega el procesamiento al `PacketReceiver`.
    - `PacketReceiver.processIncomingData()` analiza el buffer de entrada.
    - Identifica el tipo de paquete (definido en `ServerPacket`).
    - Busca un manejador registrado para ese tipo de paquete.
    - Delega el procesamiento al manejador apropiado.
    - Si quedan datos en el buffer, continua procesando recursivamente.

## 4. Ciclo de comunicacion

El proceso de comunicacion continua de manera ciclica mientras la conexion permanezca abierta:

1. El cliente envia comandos al servidor utilizando los metodos `writeX` de `Protocol`.
2. Los datos se acumulan en el buffer `outgoingData`.
3. El metodo `write()` de `SocketConnection` envia los datos al servidor.
4. El servidor procesa los comandos y envia respuestas.
5. El metodo `read()` de `SocketConnection` recibe los datos y los almacena en `incomingData`.
6. `handleIncomingData()` procesa los paquetes recibidos y ejecuta las acciones correspondientes.

## 5. Fin de la conexion

Cuando se necesita finalizar la comunicacion:

1. **Se llama al metodo `disconnect()`**:
    - Verifica si el socket esta preparado para comunicacion.
    - Cierra los flujos de entrada y salida.
    - Cierra el socket.
    - Restablece el estado del juego del usuario.

Este flujo garantiza una comunicacion bidireccional organizada entre el cliente y el servidor, con un manejo modular de los diferentes tipos de paquetes y comandos segun el protocolo establecido para el juego Argentum Online.