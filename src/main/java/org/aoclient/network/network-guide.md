# Guía de Comunicación Cliente-Servidor

## Índice

1. [¿Qué es la comunicación cliente-servidor?](#qué-es-la-comunicación-cliente-servidor)
2. [Conceptos básicos](#conceptos-básicos)
3. [Arquitectura del sistema](#arquitectura-del-sistema)
4. [Los componentes del cliente Java](#los-componentes-del-cliente-java)
5. [Los componentes del servidor VB6](#los-componentes-del-servidor-vb6)
6. [Cómo funciona la comunicación paso a paso](#cómo-funciona-la-comunicación-paso-a-paso)
7. [Ejemplo práctico: Enviar un mensaje de chat](#ejemplo-práctico-enviar-un-mensaje-de-chat)
8. [¿Qué pasa cuando algo sale mal?](#qué-pasa-cuando-algo-sale-mal)
9. [Puntos importantes a recordar](#puntos-importantes-a-recordar)

## ¿Qué es la comunicación cliente-servidor?

Un sistema cliente-servidor en un MMORPG es una arquitectura de red distribuida donde múltiples programas cliente
(ejecutándose en las computadoras de los jugadores) se conectan a un programa servidor centralizado que mantiene el
estado autoritativo del mundo virtual.

**_A lo largo de esta guia vamos a utilizar la analogia de una red logística de distribución de mercaderia con camiones 
para transportar la mercaderia, almacenes locales y un sistema de ruta._**

Cuando quieres enviar mercancía:

1. El almacén local (cliente) prepara un paquete como por ejemplo "Quiero hablar en el juego"
2. La oficina central (servidor) recibe el paquete, lo procesa y distribuye
3. El cliente recibe el paquete de repuesta y refleja el resultado en pantalla

En nuestro juego funciona igual:

- El cliente Java es como tu almacén local (el programa que ejecutas en tu computadora)
- El servidor VB6 es como el centro de distribución central (la computadora que maneja el juego para todos)
- Los paquetes son como las mercaderias que se transportan por la red logística

## Conceptos básicos

### ¿Qué es un "paquete"?

Un paquete es literalmente eso: un paquete de datos que contiene información. Cada paquete sigue un formato estricto de
etiquetado que tanto tu almacén como el centro de distribución deben conocer perfectamente para poder procesar la
mercancía correctamente. Los paquetes no se envían como texto legible sino como secuencias de bytes. En nuestro caso:

- **ID del paquete:** Dice qué tipo de mercancía es (hablar, caminar, atacar, etc.)
- **Contenido:** Los datos específicos (el mensaje que escribiste, hacia dónde quieres caminar, etc.)

### ¿Qué es "Little-Endian"?

Es simplemente el orden en que se guardan los bytes en el paquete. En el formato little-endian, el byte menos
significativo (LSB) se almacena en la posición más baja, mientras que el byte más significativo (MSB) se almacena en la
posición más alta.

Por ejemplo, el numero hexadecimal `0x12345678` se empaqueta como:

```
Direccion: 0x1000 0x1001 0x1002 0x1003 
Contenido:   78     56     34     12
```

No te preocupes por entender esto completamente, solo recuerda que nuestro sistema empaqueta los bytes "al revés" para
que funcione con el centro de distribución original (el servidor).

### ¿Qué es un "buffer"?

Un buffer es como una zona de carga/descarga en tu almacén donde acumulas paquetes antes de enviarlos o después de
recibirlos. Actúa como almacén intermedio de mercancía entre dos procesos que operan a diferentes velocidades. Los
buffers son esenciales porque la velocidad de preparar paquetes y la velocidad de transporte raramente coinciden.

Técnicamente, es un array de bytes con punteros que indican la posición actual de escritura y lectura. En nuestra
analogia del sistema de distribuccion de mercaderia tenemos dos zonas principales: la **zona de salida**
(`outputBuffer`) que acumula todos los paquetes que queremos enviar al centro de distribuccion antes de cargarlos en el
camión (reduciendo viajes), y la **zona de llegada** (`inputBuffer`) que almacena los paquetes recibidos del centro
hasta que tengamos un paquete completo para procesar.

El buffer maneja automáticamente la capacidad, expandiendo la zona cuando es necesario, y gestiona la fragmentación de
envíos - por ejemplo, si un paquete de 50 kg llega en dos entregas de 30 y 20 kg, el buffer los ensambla correctamente.
Además, implementa validaciones como `checkBytes()` para verificar que hay suficiente mercancía antes de intentar
descargar, evitando errores de procesamiento incompleto que podrían corromper el sistema logístico.

## Arquitectura del sistema

La arquitectura es de dos capas con separación física: el cliente Java maneja interfaz y renderizado, el servidor VB6
centraliza lógica del juego y datos. El cliente envía paquetes y recibe distribuciones, mientras el servidor valida
todas las solicitudes y mantiene el estado autoritativo del mundo. Cada lado tiene componentes especializados (buffers,
processors, handlers en cliente; colas de bytes, módulos de protocolo en servidor) que garantizan escalabilidad,
seguridad y mantenibilidad.

```
      Tu Computadora                    Servidor del Juego
    ┌─────────────────┐                ┌─────────────────┐
    │   Cliente Java  │ <──Internet──> │  Servidor VB6   │
    │                 │                │                 │
    │ • PacketBuffer  │                │ • clsByteQueue  │
    │ • Protocol      │                │ • Protocol.bas  │
    │ • Connection    │                │ • TCP.bas       │
    │ • PacketProcess │                │                 │
    └─────────────────┘                └─────────────────┘
```

**Explicación simple:**

- Tu computadora ejecuta el cliente Java (el juego que ves)
- El servidor es una computadora remota que coordina el juego para todos
- Internet es el cable invisible que los conecta
- Cada lado tiene herramientas específicas para manejar la comunicación

## Los componentes del cliente Java

### 1. `PacketBuffer.java` - La zona de carga/descarga

Es como una zona especializada de tu almacén que puede empaquetar y desempaquetar mercancía en el formato que entiende
el centro de distribución (el servidor).

**¿Para qué sirve?**

- Guardar información antes de enviarla
- Leer información que llegó del servidor
- Convertir números y texto al formato correcto

**Métodos más importantes:**

- `writeBlock()`
- `writeByte()`
- `writeInteger()`
- `writeCp1252String()`
- `readBytes()`
- `readByte()`
- `readInteger()`
- `readCp1252String()`
- `checkBytes()`

### 2. `Protocol.java` - El traductor

Un protocolo es un **conjunto de reglas y especificaciones técnicas** que define cómo dos sistemas se comunican, similar
a un manual de empaquetado y etiquetado con estándares estrictos. En redes, especifica el formato exacto de los
paquetes, el orden de transporte, y cómo manejar paquetes perdidos.

En nuestro sistema usamos un **protocolo binario de aplicación** construido sobre TCP. Define que cada paquete inicia
con 1 byte identificador del tipo de paquete, seguido de datos estructurados según ese tipo. Por ejemplo, un paquete de
chat tiene estructura: ID(1 byte) + longitud_texto(2 bytes) + texto(N bytes). Es como un manual que sabe cómo crear
paquetes que el centro de distribución entiende.

**¿Para qué sirve?**

- Crear paquetes para diferentes acciones (hablar, caminar, atacar)
- Gestionar dos buffers principales:
    - `outputBuffer` - Para enviar paquetes
    - `inputBuffer` - Para recibir paquetes

Cuando quieres hablar, `Protocol` sabe exactamente cómo crear el paquete correcto.

### 3. `ClientPacket.java` y `ServerPacket.java` - Los indentificadores de mercancía

Son como etiquetas de producto en logística. Cada tipo de paquete tiene su código único para que el centro de distribución
sepa cómo clasificarlo y procesarlo.

**ClientPacket.java** - Paquetes que envías al servidor:

- `TALK(3)` - Paquete de chat (quiero hablar)
- `WALK(6)` - Paquete de movimiento (quiero caminar)
- `ATTACK(8)` - Paquete de ataque (quiero atacar)

**ServerPacket.java** - Paquetes que el servidor te envía:

- `CHAT_OVER_HEAD(23)` - Distribución de chat
- `LOGGED(0)` - Confirmación de conexión
- `UPDATE_HP(17)` - Actualización de vida

### 4. `PacketProcessor.java` - El clasificador automático

Es como un sistema automatizado que recibe paquetes del centro de distribución y decide qué hacer con cada uno según su
ID.

**¿Cómo funciona?**

1. Recibe un paquete del servidor
2. Lee el identificador del paquete
3. Busca quién sabe manejar ese paquete
4. Deriva el paquete al especialista correcto (handler)

### 5. `PacketHandler.java` - Los especialistas

Son como especialistas que saben manejar un tipo específico de paquete.

**Ejemplo:** `ChatOverHeadHandler` es el especialista en manejar paquetes de chat. Cuando llega un paquete de chat, él
sabe exactamente qué hacer: extraer el texto, identificar quién lo envió, leer el color, y mostrarlo en pantalla.

### 6. `Connection.java` - La ruta de transporte

Es como la infraestructura de transporte que se encarga de enviar y recibir paquetes entre tu almacén y el centro de 
distribución.

**Funciones principales:**

- `connect()` - Conectarse al servidor
- `disconnect()` - Desconectarse del servidor
- `write()` - Enviar paquetes al servidor
- `read()` - Recibir paquetes del servidor

## Los componentes del servidor VB6

### 1. `clsByteQueue.cls` - El centro de sorting del servidor

Similar al `PacketBuffer` del cliente, pero programada en VB6. Maneja la información que el servidor recibe y envía.

### 2. `Protocol.bas` - El centro de control logístico

Es el cerebro del servidor que:

- Recibe todos los paquetes de todos los clientes
- Decide qué hacer con cada envío
- Organiza las distribuciones a los clientes correspondientes

### 3. `TCP.bas` y `wsksock.bas` - La infraestructura de transporte

Son las herramientas básicas que permiten al servidor comunicarse por internet. Es como la flota de camiones y el
sistema de carreteras que permite las entregas.

## Cómo funciona la comunicación paso a paso

### Paso 1: Establecer la conexión

Es como establecer una ruta de transporte entre tu almacén y el centro de distribución.

**¿Qué pasa exactamente?**

1. El cliente intenta conectarse al servidor
2. El servidor acepta la conexión
3. Se establece un canal de comunicación entre ambos
4. Ambos se preparan para enviar y recibir paquetes

**¿Dónde sucede?** En la clase `Connection` cuando se establece la conexion usando un Socket de Java:

```java
Socket socket = new Socket(options.getIpServer(), Integer.parseInt(options.getPortServer()));
```

### Paso 2: Preparar un paquete para enviar

Es como preparar mercancía en tu zona de carga antes de enviarla.

**¿Qué pasa exactamente?**

1. Decides qué quieres enviar (por ejemplo, un mensaje de chat)
2. `Protocol` prepara el paquete usando el ID correspondiente
3. Se agregan los datos específicos (tu mensaje)
4. Todo se guarda en el `outputBuffer`

Esto sucede en la clase `Protocol` usando el metodo `talk("mi mensaje")`

### Paso 3: Enviar el paquete

Es como cargar el camión con los paquetes preparados y enviarlo al centro de distribución.

**¿Qué pasa exactamente?**

1. `Connection` revisa si hay paquetes en el `outputBuffer`
2. Convierte los datos al formato correcto para enviar por internet
3. Envía todo al servidor a través de la conexión
4. Limpia el buffer para el próximo paquete

**¿Dónde sucede?** En la clase `Connection` cuando llamas a `write()`

### Paso 4: El servidor procesa el paquete

Es como que el centro de distribución recibe y procesa tu camión de mercancía.

**¿Qué pasa exactamente?**

1. El servidor recibe tu paquete
2. Lee el ID del paquete para saber qué tipo de paquete es
3. Aplica las reglas del juego (¿puedes hablar? ¿estás vivo? etc.)
4. Decide qué responder y a quién enviarle la respuesta

**¿Dónde sucede?** En `Protocol.bas` del servidor VB6

### Paso 5: Recibir el paquete

Es como recibir un camión con paquetes del centro de distribución.

**¿Qué pasa exactamente?**

1. `Connection` detecta que llegaron datos del servidor
2. Lee los bytes y los guarda en el `inputBuffer`
3. `PacketProcessor` examina qué tipo de paquete llegó
4. Busca el handler correcto para procesar ese paquete

**¿Dónde sucede?** En las clases `Connection` y `PacketProcessor`

### Paso 6: Procesar el paquete

Es como que cada departamento procese los paquetes que le corresponden.

**¿Qué pasa exactamente?**

1. El handler correspondiente recibe el paquete
2. Lee todos los datos del paquete (texto, colores, jugador, etc.)
3. Ejecuta la acción correspondiente
4. Limpia el buffer para el próximo paquete

**¿Dónde sucede?** En los handlers específicos como `ChatOverHeadHandler`

## Ejemplo práctico: Enviar un mensaje de chat

Vamos a seguir el viaje completo de un mensaje desde que escribes "Hola mundo" hasta que aparece en pantalla.

### 1. Escribes "Hola mundo" y presionas Enter

**¿Qué pasa internamente?**

- La interfaz detecta que quieres hablar
- Llama a `Protocol.talk("Hola mundo")`

### 2. Protocol prepara el paquete `TALK`

**¿Qué contiene el paquete?**

```
┌─────────────┬─────────────┬─────────────────┐
│     3       │    10       │ "Hola mundo"    │
│ (ID TALK)   │ (longitud)  │ (tu mensaje)    │
│  1 byte     │  2 bytes    │  10 bytes       │
└─────────────┴─────────────┴─────────────────┘
```

**Explicación de cada parte:**

- **3**: Es el número que identifica un mensaje de chat definido en `ClientPacket` como la constante `TALK`
- **10**: Le dice al servidor cuántas letras tiene tu mensaje
- **"Hola mundo"**: Tu mensaje exacto

### 3. `Connection.java` envía el paquete al servidor

**¿Qué pasa?**

- Los 13 bytes totales (1+2+10) viajan por internet al servidor
- Es como enviar "3 10 Hola mundo" en un lenguaje que las computadoras entienden

### 4. El servidor procesa tu paquete

**¿Qué verifica el servidor?**

- ¿Estás conectado al juego?
- ¿Tu personaje está vivo?
- ¿No estás silenciado?
- ¿El mensaje no es muy largo?

**¿Qué hace si todo está bien?**

- Añade información extra: quién eres, tu posición, el color de tu texto
- Determina qué jugadores están cerca de tu posición
- Prepara un paquete `CHAT_OVER_HEAD` definido en `Protocol.bas` dentro del enum `ServerPacketID`
- Envía el paquete a múltiples clientes: a ti y a todos los jugadores cercanos simultáneamente

#### 4.1 El servidor distribuye el paquete (broadcasting)

El servidor no solo te responde a ti, sino que envía tu paquete a todos los jugadores que deben verlo. Esto se determina
calculando los jugadores que estan en tu mimso mapa y dentro del rango de vision. Por lo tanto, cuando hablas, no solo
aparezca en tu pantalla, sino que todos los jugadores cercanos vean tu mensaje flotando  sobre tu cabeza.

### 5. El servidor envía el paquete CHAT_OVER_HEAD

**¿Qué contiene este nuevo paquete?**

```
┌─────────────┬─────────────┬─────────────┬─────┬─────┬─────┐
│     23      │    10       │ "Hola mundo"│ 150 │ 255 │ 255 │
│(ID CHAT_OH) │ (longitud)  │(tu mensaje) │ (R) │ (G) │ (B) │
│  1 byte     │  2 bytes    │  10 bytes   │1 by │1 by │1 by │
└─────────────┴─────────────┴─────────────┴─────┴─────┴─────┘
│                                         │
│                    + CharIndex (2 bytes)│
└─────────────────────────────────────────┘
```

**Explicación de las partes nuevas:**

- **23**: Número que identifica un mensaje sobre la cabeza que pertenece a la constante `CHAT_OVER_HEAD` de `ServerPacketID`
- **CharIndex**: Número que identifica a tu personaje en el juego
- **R, G, B**: Números que definen el color del texto (rojo, verde, azul)

### 6. Tu cliente recibe y procesa la respuesta

**¿Qué pasa en tu computadora?**

1. `Connection` detecta que llegaron datos
2. `PacketProcessor` lee el ID 23 y sabe que es un `CHAT_OVER_HEAD`
3. `ChatOverHeadHandler` se activa para manejar este paquete
4. El handler lee toda la información: mensaje, quién lo dijo, color
5. Actualiza la pantalla mostrando "Hola mundo" sobre la cabeza de tu personaje

## ¿Qué pasa cuando algo sale mal?

### Problema 1: Se pierde la conexión

**¿Qué detecta el sistema?**

- Connection no puede enviar o recibir datos
- Se detecta un error de comunicación

**¿Qué hace el cliente?**

- Llama automáticamente a `disconnect()`
- Limpia todos los buffers
- Te muestra un mensaje de error
- Te regresa al menú principal

### Problema 2: Llega un paquete corrupto

**¿Qué detecta el sistema?**

- `PacketProcessor` no puede identificar el ID del paquete
- O no hay suficientes bytes para leer los datos completos

**¿Qué hace el cliente?**

- Ignora el paquete problemático
- Registra el error en el log
- Continúa procesando los siguientes paquetes

### Problema 3: El servidor rechaza tu acción

**¿Qué pasa?**

- El servidor te envía un paquete de error

**¿Qué hace el cliente?**

- Muestra el mensaje de error en pantalla
- Puede desconectarte si el error es grave
- Te permite intentar la acción nuevamente

## Puntos importantes a recordar

### 1. El servidor siempre tiene autoridad

- Tu cliente propone acciones ("quiero caminar aquí")
- El servidor decide si las permite o no
- Tu cliente acata las decisiones del servidor

### 2. Los paquetes tienen un orden específico

- Los IDs de paquetes deben coincidir exactamente entre cliente y servidor
- El formato de bytes debe ser idéntico en ambos lados
- No puedes cambiar estos números sin romper la comunicación

### 3. La comunicación es asíncrona

- Puedes enviar varios paquetes seguidos
- Las respuestas pueden llegar en diferente orden
- Cada paquete se procesa independientemente

### 4. Siempre hay validación

- Verificación de bytes disponibles antes de leer
- Validación de IDs de paquetes antes de procesar
- Manejo de errores en cada paso crítico

### 5. El protocolo es binario y específico

- Todo se envía como bytes, no como texto legible
- El formato debe ser exacto (little-endian, longitudes correctas)
- Cada tipo de dato ocupa un espacio específico

### 6. La compatibilidad es crucial

- El cliente Java debe comunicarse perfectamente con el servidor VB6 original
- Los cambios en el protocolo pueden romper la compatibilidad
- Siempre respetar el formato original al implementar nuevas funciones

Este sistema de comunicación, aunque complejo internamente, está diseñado para ser robusto, seguro y eficiente,
permitiendo que cientos de clientes puedan interactuar simultáneamente en el mundo del juego de manera fluida y
confiable.
