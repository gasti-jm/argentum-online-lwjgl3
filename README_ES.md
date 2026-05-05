
<div align='center'>
    <br/>
    <p align='right'><a href="README.md">🇬🇧 Read in English</a></p>
    <img src="https://github.com/user-attachments/assets/3f6d8658-ea84-4ff3-b3d2-cb2fb1eeabd2" width="500px"> <br/> <br/>
    <a target="_blank"><img src="https://img.shields.io/badge/Built%20in-Java_17-43ca1f.svg?style=flat-square"></img></a>
    <a target="_blank"><img src="https://img.shields.io/badge/Gráficos-LWJGL3_+_OpenGL_3.3-2196F3.svg?style=flat-square"></img></a>
    <a target="_blank"><img src="https://img.shields.io/badge/GUI-Dear_ImGui-FF6F00.svg?style=flat-square"></img></a>
    <a target="_blank"><img src="https://img.shields.io/badge/License-GNU%20General%20Public%20License%20-e98227.svg?style=flat-square"></img></a>
</div>
<br/>

# Argentum Online Java — Cliente LWJGL3

<a target="_blank" href="https://es.wikipedia.org/wiki/Argentum_Online">Argentum Online</a> es un videojuego MMORPG del año 1999 creado en Visual Basic 6.0 por Pablo Márquez (Morgolock), este juego es de código abierto.

Este es el **cliente** traducido a Java con LWJGL3. La conexión al servidor está implementada con sockets Java y es **totalmente compatible** con el <a target="_blank" href="https://www.gs-zone.org/temas/cliente-servidor-v0-13-0-completos-y-funcionales.36521/">servidor Argentum Online Alkon v13.0 de Visual Basic 6.0</a>. El servidor en Java también se está desarrollando en un [módulo separado](../server/).

> [!TIP]
> Para un análisis técnico profundo del motor, red y renderizado, consulta la [Documentación de Arquitectura del Cliente](../docs/client-architecture.md).


## 🎯 Discord

Para aquellos que quieran colaborar con el proyecto, ¡son más que bienvenidos! Pueden comunicarse con nosotros en nuestro <a href="https://discord.gg/RtsGRqJVt9">Discord.</a>

---

## ✨ Características

- 🎮 **Motor de renderizado 2D** con OpenGL 3.3 (batch renderer + shaders GLSL)
- 🖥️ **Dear ImGui** — Sistema de GUI con 20+ formularios de juego (login, inventario, banco, comercio, habilidades, estadísticas, mini-mapa, panel GM...)
- 🔌 **Protocolo completo** — 129 paquetes de salida + 104 paquetes de entrada + 105 handlers
- 🔊 **Audio OpenAL** — Música (OGG) y efectos de sonido
- ⌨️ **Teclas configurables** — 30+ acciones remapeables
- 🌍 **Internacionalización** — Español e inglés soportados
- 🏗️ **Multiplataforma** — Windows, Linux, macOS (x64 y arm64)
- 📦 **Releases automáticas** — JAR, ZIP portable y EXE de Windows vía GitHub Actions
- 🎭 **Carga completa de assets** — Cuerpos, cabezas, cascos, armas, escudos, FX y mapas desde formato binario legacy

---

## 🏗️ Arquitectura

```
org.aoclient
├── Main.java                  ← Punto de entrada (selección OpenGL / Vulkan)
├── engine/
│   ├── EngineGL.java          ← Game loop OpenGL (init → loop → close)
│   ├── audio/                 ← Música y SFX con OpenAL
│   ├── game/                  ← Lógica de juego (User, Console, Weather, Inventory)
│   ├── gui/                   ← Integración Dear ImGui (20 formularios)
│   ├── renderer/              ← Batch renderer 2D + shaders + texturas
│   ├── scenes/                ← IntroScene → MainScene → GameScene
│   ├── listeners/             ← Handlers de teclado y ratón
│   └── window/                ← Gestión de ventana GLFW
├── network/
│   ├── Connection.java        ← Conexión TCP por socket
│   ├── PacketBuffer.java      ← Buffer binario Little-Endian
│   └── protocol/
│       ├── Protocol.java      ← 130+ métodos de serialización de paquetes
│       ├── ClientPacket.java   ← 129 IDs de paquetes cliente→servidor
│       ├── ServerPacket.java   ← 104 IDs de paquetes servidor→cliente
│       ├── PacketProcessor.java ← Dispatch de paquetes entrantes
│       ├── handlers/           ← 105 implementaciones de handlers
│       └── command/            ← Sistema de comandos de chat (usuario + GM)
└── scripts/
    └── Compressor.java        ← Lector de archivos comprimidos .ao
```

### Stack Tecnológico

| Componente | Tecnología | Versión |
|-----------|-----------|---------|
| Lenguaje | Java | 17 |
| Build | Gradle + Shadow JAR | 8.1.1 |
| Gráficos | LWJGL3 (OpenGL 3.3) | 3.3.3 |
| Matemáticas | JOML | 1.10.5 |
| GUI | Dear ImGui | 1.86.11 |
| Logging | TinyLog | 2.7.0 |
| Audio | OpenAL (vía LWJGL) | — |
| Ventana | GLFW (vía LWJGL) | — |

---

## 🚀 Requisitos

- <a href="https://www.oracle.com/java/technologies/downloads/#java17" target="_blank">Kit de Desarrollo de Java (JDK) 17</a> o superior
- <a href="https://www.jetbrains.com/idea/download/" target="_blank">IntelliJ IDEA</a> (recomendado), <a href="https://netbeans.apache.org/" target="_blank">NetBeans</a>, <a href="https://www.eclipse.org/downloads/" target="_blank">Eclipse</a> o cualquier IDE de Java
- Gradle (gestionado automáticamente por el IDE)

## 📦 Cómo compilar y ejecutar

1. Clonar el repositorio:
```bash
git clone https://github.com/gasti-jm/argentum-online-lwjgl3.git
```

2. Abrir el proyecto:
   - En IntelliJ IDEA: Ve a `File > Open` y selecciona la carpeta del proyecto
   - El IDE descargará automáticamente todas las dependencias a través de Gradle

3. Compilar el proyecto:
```bash
./gradlew build
```

4. Ejecutar el proyecto:
   - Localiza la clase principal `org.aoclient.Main`
   - Haz clic derecho y selecciona 'Run' o presiona Shift+F10
   - Alternativamente: `./gradlew run`

**IMPORTANTE:** Los usuarios de macOS deben iniciar la aplicación pasando `-XstartOnFirstThread` como opción de VM.

---

## ⚙️ Configuración

### `options.ini` — Opciones del cliente
```ini
Music = true
Sound = true
IP = 127.0.0.1
PORT = 7666
Fullscreen = false
VSYNC = true
Language = es
```

### `keys.properties` — Atajos de teclado (keycodes GLFW)
Todas las acciones son remapeables. Por defecto: WASD movimiento, Enter chat, Espacio usar, Ctrl atacar, Q recoger, E equipar.

---

## 📸 Capturas de pantalla

![Screenshot 2023-08-21 044542](https://github.com/gasti-jm/argentum-online-lwjgl3/assets/82490615/8f4c7864-feee-4ac6-b957-651a6b03a627)

![VirtualBox_Linux_09_03_2024_04_13_54](https://github.com/gasti-jm/argentum-online-lwjgl3/assets/82490615/8f54e716-3824-48ac-92ca-f22bfcfe74d5)

<img width="1360" height="768" alt="2R3ZxNl" src="https://github.com/user-attachments/assets/56cb53b9-2070-40b4-ae85-f044209e020c" />

---

## 📋 Estado del Proyecto

### ✅ Implementado
- Protocolo del juego completo (compatible con servidor VB6 v13.0)
- Motor de renderizado 2D con batch rendering
- 20+ formularios de juego con Dear ImGui
- Sistema de audio OpenAL (música + efectos)
- Carga de assets desde archivos comprimidos `.ao`
- Sistema de escenas (Intro → Login → Juego)
- Sistema de comandos de chat (usuario + GM)
- Atajos de teclado configurables
- Efectos de clima (lluvia)
- Mini-mapa
- Sistema de tutorial
- CI/CD con releases automatizadas

### 🔧 En progreso / Planificado
- Backend gráfico Vulkan (placeholder `EngineVulkan.java` existente)
- Networking con NIO (actualmente: sockets bloqueantes)
- Tests unitarios

---

## 📁 Recursos del Juego

| Archivo | Contenido | Tamaño |
|---------|-----------|--------|
| `graphics.ao` | Todos los sprites del juego | ~25 MB |
| `gui.ao` | Texturas de interfaz | ~18 MB |
| `inits.ao` | Índices de datos (GRH, cabezas, cuerpos...) | ~133 KB |
| `maps.ao` | Todos los mapas del juego | ~508 KB |
| `music/` | Música de fondo (OGG) | — |
| `sounds/` | Efectos de sonido | — |
| `shaders/` | Shaders GLSL (vertex + fragment) | — |

---

## Notas de Desarrollo

- El proyecto utiliza Gradle para la gestión de dependencias
- Las librerías nativas se descargan automáticamente según tu sistema operativo
- Compatible con Windows, Linux y macOS (x64 y arm64)
- Asegúrate de tener los drivers gráficos actualizados para un rendimiento óptimo de OpenGL

---

<h1>Agradecimientos:</h1>
<br/>

<div align='center'>

<p>
  <b>Pablo Márquez (Morgolock) creador de Argentum Online</b> <br/>
  <a target="_blank" href="https://www.gs-zone.org/temas/argentum-online-en-java-opengl.92672/#post-785702"><b>Lord Fers</b> - Usuario de GS-Zone que liberó un cliente base offline en LWJGL2</a> <br/>
  Y a la gente de GS-Zone <br/>
  <a target="_blank" href="https://www.gs-zone.org/"><img src="https://user-images.githubusercontent.com/82490615/187148671-1d7f92b9-7ea1-45f2-b6f1-f53b07454d93.png"></img></a>
</p>

<h1>Cómo Contribuir:</h1>
<br/>

1. Haz un Fork del Repositorio: Haz clic en "Fork" en la esquina superior derecha de la página para crear tu propia copia.
2. Clona tu Repositorio: Clona tu fork a tu computadora usando git clone https://github.com/TU_USUARIO/argentum-online-lwjgl3.git.
3. Crea una Rama: Crea una rama con git checkout -b nombre-rama para tus cambios.
4. Realiza Cambios: Haz tus mejoras o correcciones y confírmalas.
5. Envía un Pull Request: Desde tu fork, crea un pull request para que revisemos tus cambios.

</div>
