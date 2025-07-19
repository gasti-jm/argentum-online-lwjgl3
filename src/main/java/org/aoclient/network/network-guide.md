# Guía de Comunicación Cliente-Servidor

## Índice

1. [¿Qué es la comunicación cliente-servidor?](#qué-es-la-comunicación-cliente-servidor)
2. [Conceptos básicos](#conceptos-básicos-que-necesitas-entender)
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

Imagina que estás en un restaurante. Tú eres el **cliente** y el cocinero es el **servidor**. Cuando quieres comer algo:

1. **Tú (cliente)** le dices al mesero "Quiero una pizza"
2. **El cocinero (servidor)** recibe tu pedido, prepara la pizza y te la envía de vuelta
3. **Tú (cliente)** recibes la pizza y puedes comerla

En nuestro juego funciona igual:

- **El cliente Java** es como tú en el restaurante (el programa que ejecutas en tu computadora)
- **El servidor VB6** es como el cocinero (la computadora que maneja el juego para todos)
- **Los mensajes** son como los pedidos que van y vienen

## Conceptos básicos que necesitas entender

### ¿Qué es un "paquete"?

Un paquete es como un sobre que contiene información. Cada paquete sigue un formato estricto que tanto el cliente como
el servidor deben conocer perfectamente para poder comunicarse correctamente. Los paquetes no se envían como texto
legible sino como secuencias de bytes En nuestro caso:

- **Sobre (ID del paquete)**: Dice qué tipo de mensaje es (hablar, caminar, atacar, etc.)
- **Contenido**: Los datos específicos (el mensaje que escribiste, hacia dónde quieres caminar, etc.)

### ¿Qué es "Little-Endian"?

Es simplemente el orden en que se guardan los números. En el formato little-endian, el byte menos significativo (LSB) se
almacena en la direccion de memoria mas baja, mientras que el byte mas significativo (MSB) se almacena en la direccion
mas alta.

Por ejemplo, el numero hexadecimal `0x12345678` se almacena en memoria como:

```
Direccion: 0x1000 0x1001 0x1002 0x1003 
Contenido:   78     56     34     12
```

No te preocupes por entender esto completamente, solo recuerda que nuestro sistema guarda los números "al revés" para
que funcione con el servidor original.

### ¿Qué es un "buffer"?

Un buffer es como una caja temporal (una región de memoria) donde guardas cosas antes de enviarlas o después de
recibirlas que actúa como almacén intermedio de datos entre dos procesos que operan a diferentes velocidades. En
comunicación de red, los buffers son esenciales porque la velocidad de generación de datos (escribir un mensaje) y la
velocidad de transmisión (enviar por internet) raramente coinciden. Técnicamente, es un array de bytes con punteros que
indican la posición actual de lectura y escritura. En nuestro sistema tenemos dos buffers principales: el `outputBuffer`
que acumula todos los paquetes que queremos enviar al servidor antes de transmitirlos en una sola operación de red
(reduciendo overhead), y el `inputBuffer` que almacena los bytes recibidos del servidor hasta que tengamos un paquete
completo para procesar. El buffer maneja automáticamente la capacidad, redimensionándose cuando es necesario, y gestiona
la fragmentación de paquetes - por ejemplo, si un paquete de 50 bytes llega en dos fragmentos de 30 y 20 bytes, el
buffer los ensambla correctamente. Además, implementa validaciones como `checkBytes()` para verificar que hay
suficientes datos antes de intentar leer, evitando errores de lectura incompleta que podrían corromper el protocolo de
comunicación.

## Arquitectura del sistema

La arquitectura es de dos capas con separación física: el cliente Java maneja interfaz y renderizado, el servidor VB6
centraliza lógica del juego y datos. Se comunican mediante protocolo TCP personalizado. El cliente actúa como "terminal
inteligente" que envía comandos y recibe actualizaciones, mientras el servidor valida todas las acciones y mantiene el
estado autoritativo del mundo. Cada lado tiene componentes especializados (buffers, processors, handlers en cliente;
colas de bytes, módulos de protocolo en servidor) que garantizan escalabilidad, seguridad y mantenibilidad.

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

- **Tu computadora** ejecuta el cliente Java (el juego que ves)
- **El servidor** es una computadora remota que coordina el juego para todos
- **Internet** es el cable invisible que los conecta
- **Cada lado tiene herramientas específicas** para manejar la comunicación

## Los componentes del cliente Java

### 1. PacketBuffer - La caja de herramientas

Es como una caja mágica que puede guardar y leer información en el formato que entiende el servidor.

**¿Para qué sirve?**

- **Guardar información** antes de enviarla
- **Leer información** que llegó del servidor
- **Convertir números y texto** al formato correcto

**Métodos más importantes:**

- `writeByte()` - Escribe un número pequeño (0-255)
- `writeInteger()` - Escribe un número mediano
- `writeCp1252String()` - Escribe texto
- `readByte()` - Lee un número pequeño
- `readInteger()` - Lee un número mediano
- `readCp1252String()` - Lee texto

### 2. Protocol - El traductor

Un protocolo es un **conjunto de reglas y especificaciones técnicas** que define cómo dos sistemas se comunican, similar
a un idioma común con gramática estricta. En redes, especifica el formato exacto de los mensajes, el orden de
intercambio, y cómo manejar errores.

En nuestro sistema usamos un **protocolo binario de aplicación** construido sobre TCP. Define que cada mensaje inicia
con 1 byte identificador del tipo de paquete, seguido de datos estructurados según ese tipo. Por ejemplo, un mensaje de
chat tiene estructura: ID(1 byte) + longitud_texto(2 bytes) + texto(N bytes). Es como un traductor que sabe cómo crear
mensajes que el servidor entiende.

**¿Para qué sirve?**

- **Crear paquetes** para diferentes acciones (hablar, caminar, atacar)
- **Tener dos buffers principales:**
    - `outputBuffer` - Para mensajes que vas a enviar
    - `inputBuffer` - Para mensajes que recibiste

Cuando quieres hablar, `Protocol` sabe exactamente cómo crear el paquete correcto.

### 3. ClientPacket y ServerPacket - Los números de identificación

Son como números de teléfono. Cada tipo de mensaje tiene su número único.

**ClientPacket** - Mensajes que envías al servidor:

- `TALK(3)` - Quiero hablar
- `WALK(6)` - Quiero caminar
- `ATTACK(8)` - Quiero atacar

**ServerPacket** - Mensajes que el servidor te envía:

- `LOGGED(0)` - Te conectaste exitosamente
- `CHAT_OVER_HEAD(23)` - Alguien habló
- `UPDATE_HP(17)` - Tu vida cambió

### 4. PacketProcessor - El organizador

Es como un organizador que recibe mensajes del servidor y decide qué hacer con cada uno.

**¿Cómo funciona?**

1. **Recibe un mensaje** del servidor
2. **Lee el número de identificación** (ID del paquete)
3. **Busca quién sabe manejar** ese tipo de mensaje
4. **Le da el mensaje** a la persona correcta (handler)

### 5. PacketHandler - Los especialistas

**¿Qué son?** Son como especialistas que saben manejar un tipo específico de mensaje.

**Ejemplo:** `ChatOverHeadHandler` es el especialista en manejar mensajes de chat. Cuando llega un mensaje de chat, él
sabe exactamente qué hacer: leer el texto, leer quién lo dijo, leer el color, y mostrarlo en pantalla.

### 6. Connection - El cartero

**¿Qué hace?** Es como un cartero que se encarga de enviar y recibir mensajes entre tu computadora y el servidor.

**Funciones principales:**

- `connect()` - Conectarse al servidor
- `disconnect()` - Desconectarse del servidor
- `write()` - Enviar mensajes al servidor
- `read()` - Recibir mensajes del servidor

## Los componentes del servidor VB6

### 1. clsByteQueue.cls - La caja del servidor

Similar al `PacketBuffer` del cliente, pero programada en VB6. Maneja la información que el servidor recibe y envía.

### 2. Protocol.bas - El centro de control

Es el cerebro del servidor que:

- **Recibe todos los mensajes** de todos los jugadores
- **Decide qué hacer** con cada mensaje
- **Envía respuestas** a los jugadores correspondientes

### 3. TCP.bas y wsksock.bas - La infraestructura de red

Son las herramientas básicas que permiten al servidor comunicarse por internet. Es como el sistema telefónico que
permite las llamadas.

## Cómo funciona la comunicación paso a paso

### Paso 1: Establecer la conexión

Es como llamar por teléfono y esperar a que contesten.

**¿Qué pasa exactamente?**

1. **Tu cliente** intenta conectarse al servidor (como marcar un número)
2. **El servidor** acepta la conexión (como contestar el teléfono)
3. **Se establece un canal** de comunicación entre ambos
4. **Ambos se preparan** para enviar y recibir mensajes

**¿Dónde sucede?** En la clase `Connection` cuando se establece la conexion usando un Socket de Java:

```java
Socket socket = new Socket(options.getIpServer(), Integer.parseInt(options.getPortServer()));
```

### Paso 2: Preparar un mensaje para enviar

Es como escribir una carta antes de enviarla por correo.

**¿Qué pasa exactamente?**

1. **Decides qué quieres hacer** (por ejemplo, hablar)
2. **`Protocol` crea el paquete correcto** usando el ID correspondiente
3. **Se agregan los datos específicos** (tu mensaje)
4. **Todo se guarda** en el `outputBuffer` (como poner la carta en el buzón)

Esto sucede en la clase `Protocol` con métodos como `talk("mi mensaje")`

### Paso 3: Enviar el mensaje

Es como que el cartero recoja la carta de tu buzón y la lleve al destinatario.

**¿Qué pasa exactamente?**

1. **`Connection` revisa** si hay mensajes en el `outputBuffer`
2. **Convierte los datos** al formato correcto para enviar por internet
3. **Envía todo** al servidor a través de la conexión
4. **Limpia el buffer** para el próximo mensaje

**¿Dónde sucede?** En la clase `Connection` cuando llamas a `write()`

### Paso 4: El servidor procesa el mensaje

Es como que el destinatario abra y lea tu carta.

**¿Qué pasa exactamente?**

1. **El servidor recibe** tu mensaje
2. **Lee el ID del paquete** para saber qué tipo de mensaje es
3. **Aplica las reglas del juego** (¿puedes hablar? ¿estás vivo? etc.)
4. **Decide qué responder** y a quién enviarle la respuesta

**¿Dónde sucede?** En `Protocol.bas` del servidor VB6

### Paso 5: Recibir la respuesta

Es como recibir la respuesta a tu carta.

**¿Qué pasa exactamente?**

1. **Connection detecta** que llegaron datos del servidor
2. **Lee los bytes** y los guarda en el `inputBuffer`
3. **PacketProcessor examina** qué tipo de mensaje llegó
4. **Busca el handler correcto** para procesar ese mensaje

**¿Dónde sucede?** En las clases `Connection` y `PacketProcessor`

### Paso 6: Procesar la respuesta

Es como leer y entender la respuesta que recibiste.

**¿Qué pasa exactamente?**

1. **El handler correspondiente** recibe el mensaje
2. **Lee todos los datos** del mensaje (texto, colores, jugador, etc.)
3. **Actualiza la pantalla** o realiza la acción correspondiente
4. **Limpia el buffer** para el próximo mensaje

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

- **3**: Es el número que identifica un mensaje de chat
- **10**: Le dice al servidor cuántas letras tiene tu mensaje
- **"Hola mundo"**: Tu mensaje exacto

### 3. `Connection` envía el paquete al servidor

**¿Qué pasa?**

- Los 13 bytes totales (1+2+10) viajan por internet al servidor
- Es como enviar "3 10 Hola mundo" en un lenguaje que las computadoras entienden

### 4. El servidor procesa tu mensaje

**¿Qué verifica el servidor?**

- ¿Estás conectado al juego?
- ¿Tu personaje está vivo?
- ¿No estás silenciado?
- ¿El mensaje no es muy largo?

**¿Qué hace si todo está bien?**

- Añade información extra: quién eres, tu posición, el color de tu texto
- **Determina qué jugadores están cerca** de tu posición
- Prepara un paquete `CHAT_OVER_HEAD` idéntico
- **Envía el paquete a múltiples clientes:** a ti y a todos los jugadores cercanos simultáneamente

#### 4.1 El servidor distribuye el mensaje (Broadcasting)

**¿Qué significa distribuir?** El servidor no solo te responde a ti, sino que envía tu mensaje a todos los jugadores que
deben verlo.

**¿Cómo determina quién debe verlo?**

- **Área**: Calcula qué jugadores están en tu mismo mapa y dentro del rango de visión

**¿Por qué es importante?** Esto permite que cuando hablas, no solo aparezca en tu pantalla, sino que todos los
jugadores cercanos vean tu mensaje flotando sobre tu cabeza, creando la experiencia social del MMORPG.

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

- **23**: Número que identifica un mensaje de chat sobre la cabeza
- **CharIndex**: Número que identifica a tu personaje en el juego
- **R, G, B**: Números que definen el color del texto (rojo, verde, azul)

### 6. Tu cliente recibe y procesa la respuesta

**¿Qué pasa en tu computadora?**

1. **Connection detecta** que llegaron datos
2. **PacketProcessor lee** el ID 23 y sabe que es un `CHAT_OVER_HEAD`
3. **ChatOverHeadHandler se activa** para manejar este mensaje
4. **El handler lee** toda la información: mensaje, quién lo dijo, color
5. **Actualiza la pantalla** mostrando "Hola mundo" sobre la cabeza de tu personaje

### 7. El resultado final

**¿Qué ves en pantalla?**

- Tu mensaje "Hola mundo" aparece flotando sobre la cabeza de tu personaje
- El texto tiene el color correspondiente a tu estado (ciudadano, criminal, etc.)
- Otros jugadores cercanos también ven tu mensaje

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

- PacketProcessor no puede identificar el ID del paquete
- O no hay suficientes bytes para leer los datos completos

**¿Qué hace el cliente?**

- Ignora el paquete problemático
- Registra el error en el log
- Continúa procesando los siguientes paquetes

### Problema 3: El servidor rechaza tu acción

**¿Qué pasa?**

- El servidor te envía un paquete de error
- ErrorMessageHandler recibe el mensaje

**¿Qué hace el cliente?**

- Muestra el mensaje de error en pantalla
- Puede desconectarte si el error es grave
- Te permite intentar la acción nuevamente

## Puntos importantes a recordar

### 1. El servidor siempre tiene la razón

- **Tu cliente propone** acciones ("quiero caminar aquí")
- **El servidor decide** si las permite o no
- **Tu cliente acata** las decisiones del servidor

### 2. Los paquetes tienen un orden específico

- **Los IDs de paquetes** deben coincidir exactamente entre cliente y servidor
- **El formato de bytes** debe ser idéntico en ambos lados
- **No puedes cambiar** estos números sin romper la comunicación

### 3. La comunicación es asíncrona

- **Puedes enviar** varios mensajes seguidos
- **Las respuestas pueden llegar** en diferente orden
- **Cada mensaje** se procesa independientemente

### 4. Siempre hay validación

- **Verificación de bytes disponibles** antes de leer
- **Validación de IDs de paquetes** antes de procesar
- **Manejo de errores** en cada paso crítico

### 5. El protocolo es binario y específico

- **Todo se envía como bytes**, no como texto legible
- **El formato debe ser exacto** (little-endian, longitudes correctas)
- **Cada tipo de dato** ocupa un espacio específico

### 6. La compatibilidad es crucial

- **El cliente Java** debe comunicarse perfectamente con el servidor VB6 original
- **Los cambios en el protocolo** pueden romper la compatibilidad
- **Siempre respetar** el formato original al implementar nuevas funciones

Este sistema de comunicación, aunque complejo internamente, está diseñado para ser robusto, seguro y eficiente,
permitiendo que cientos de jugadores puedan interactuar simultáneamente en el mundo del juego de manera fluida y
confiable.
