
<div align='center'>
    <br/>
    <p align='right'><a href="README_ES.md">🇪🇸 Leer en Español</a></p>
    <img src="https://github.com/user-attachments/assets/3f6d8658-ea84-4ff3-b3d2-cb2fb1eeabd2" width="500px"> <br/> <br/>
    <a target="_blank"><img src="https://img.shields.io/badge/Built%20in-Java_17-43ca1f.svg?style=flat-square"></img></a>
    <a target="_blank"><img src="https://img.shields.io/badge/Graphics-LWJGL3_+_OpenGL_3.3-2196F3.svg?style=flat-square"></img></a>
    <a target="_blank"><img src="https://img.shields.io/badge/GUI-Dear_ImGui-FF6F00.svg?style=flat-square"></img></a>
    <a target="_blank"><img src="https://img.shields.io/badge/License-GNU%20General%20Public%20License%20-e98227.svg?style=flat-square"></img></a>
</div>
<br/>

# Argentum Online Java — LWJGL3 Client

<a target="_blank" href="https://es.wikipedia.org/wiki/Argentum_Online">Argentum Online</a> is a MMORPG Video Game of the year 1999 created in Visual Basic 6.0 by Pablo Marquez (Morgolock), this game is open source.

This is the **client** translated to Java with LWJGL3. The connection to the server is implemented with Java sockets and is **fully compatible** with the <a target="_blank" href="https://www.gs-zone.org/temas/cliente-servidor-v0-13-0-completos-y-funcionales.36521/">Argentum Online Alkon v13.0 VB6 server</a>. The Java server is also being developed in a [separate module](../server/).

> [!TIP]
> Para un análisis técnico profundo del motor, red y renderizado, consulta la [Documentación de Arquitectura del Cliente](../docs/client-architecture.md).


## 🎯 Discord

For those who want to collaborate with the project, they are more than welcome! You can communicate with us on our <a href="https://discord.gg/RtsGRqJVt9">Discord.</a>

---

## ✨ Features

- 🎮 **2D Rendering Engine** with OpenGL 3.3 (batch renderer + GLSL shaders)
- 🖥️ **Dear ImGui** GUI system with 20+ game forms (login, inventory, bank, commerce, skills, stats, mini-map, GM panel...)
- 🔌 **Complete protocol** — 129 outgoing packets + 104 incoming packets + 105 handlers
- 🔊 **OpenAL Audio** — Music (OGG) and sound effects
- ⌨️ **Configurable key bindings** — 30+ remappable actions
- 🌍 **Internationalization** — Spanish and English supported
- 🏗️ **Multi-platform** — Windows, Linux, macOS (x64 & arm64)
- 📦 **Automated releases** — JAR, portable ZIP, and Windows EXE via GitHub Actions
- 🎭 **Full asset loading** — Bodies, heads, helmets, weapons, shields, FX, maps from legacy binary format

---

## 🏗️ Architecture

```
org.aoclient
├── Main.java                  ← Entry point (OpenGL / Vulkan selection)
├── engine/
│   ├── EngineGL.java          ← OpenGL game loop (init → loop → close)
│   ├── audio/                 ← OpenAL music and SFX
│   ├── game/                  ← Game logic (User, Console, Weather, Inventory)
│   ├── gui/                   ← Dear ImGui integration (20 forms)
│   ├── renderer/              ← 2D batch renderer + shaders + textures
│   ├── scenes/                ← IntroScene → MainScene → GameScene
│   ├── listeners/             ← Keyboard + Mouse handlers
│   └── window/                ← GLFW window management
├── network/
│   ├── Connection.java        ← TCP socket connection
│   ├── PacketBuffer.java      ← Little-endian binary buffer
│   └── protocol/
│       ├── Protocol.java      ← 130+ packet serialization methods
│       ├── ClientPacket.java   ← 129 client→server packet IDs
│       ├── ServerPacket.java   ← 104 server→client packet IDs
│       ├── PacketProcessor.java ← Incoming packet dispatch
│       ├── handlers/           ← 105 packet handler implementations
│       └── command/            ← Chat command system (user + GM)
└── scripts/
    └── Compressor.java        ← .ao compressed file reader
```

### Tech Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| Language | Java | 17 |
| Build | Gradle + Shadow JAR | 8.1.1 |
| Graphics | LWJGL3 (OpenGL 3.3) | 3.3.3 |
| Math | JOML | 1.10.5 |
| GUI | Dear ImGui | 1.86.11 |
| Logging | TinyLog | 2.7.0 |
| Audio | OpenAL (via LWJGL) | — |
| Window | GLFW (via LWJGL) | — |

---

## 🚀 Requirements

- <a href="https://www.oracle.com/java/technologies/downloads/#java17" target="_blank">Java Development Kit (JDK) 17</a> or higher
- <a href="https://www.jetbrains.com/idea/download/" target="_blank">IntelliJ IDEA</a> (recommended), <a href="https://netbeans.apache.org/" target="_blank">NetBeans</a>, <a href="https://www.eclipse.org/downloads/" target="_blank">Eclipse</a> or any Java IDE
- Gradle (automatically managed by the IDE)

## 📦 How to compile and run

1. Clone the repository:
```bash
git clone https://github.com/gasti-jm/argentum-online-lwjgl3.git
```

2. Open the project:
   - In IntelliJ IDEA: Go to `File > Open` and select the project folder
   - The IDE will automatically download all dependencies through Gradle

3. Build the project:
```bash
./gradlew build
```

4. Run the project:
   - Locate the main class `org.aoclient.Main`
   - Right click and select 'Run' or press Shift+F10
   - Alternatively: `./gradlew run`

**IMPORTANT:** MacOS users should start their application passing `-XstartOnFirstThread` as a VM option.

---

## ⚙️ Configuration

### `options.ini` — Client options
```ini
Music = true
Sound = true
IP = 127.0.0.1
PORT = 7666
Fullscreen = false
VSYNC = true
Language = es
```

### `keys.properties` — Key bindings (GLFW keycodes)
All actions are remappable. Default: WASD movement, Enter chat, Space use, Ctrl attack, Q pickup, E equip.

---

## 📸 Screenshots

![Screenshot 2023-08-21 044542](https://github.com/gasti-jm/argentum-online-lwjgl3/assets/82490615/8f4c7864-feee-4ac6-b957-651a6b03a627)

![VirtualBox_Linux_09_03_2024_04_13_54](https://github.com/gasti-jm/argentum-online-lwjgl3/assets/82490615/8f54e716-3824-48ac-92ca-f22bfcfe74d5)

<img width="1360" height="768" alt="2R3ZxNl" src="https://github.com/user-attachments/assets/56cb53b9-2070-40b4-ae85-f044209e020c" />

---

## 📋 Project Status

### ✅ Implemented
- Complete game protocol (compatible with VB6 server v13.0)
- Full 2D rendering engine with batch rendering
- 20+ Dear ImGui game forms
- OpenAL audio system (music + SFX)
- Asset loading from compressed `.ao` files
- Full scene system (Intro → Login → Game)
- Chat command system (user + GM)
- Configurable key bindings
- Weather effects (rain)
- Mini-map
- Tutorial system
- CI/CD with automated releases

### 🔧 In Progress / Planned
- Vulkan graphics backend (`EngineVulkan.java` placeholder)
- NIO networking (current: blocking sockets)
- Unit tests

---

## 📁 Game Resources

| File | Content | Size |
|------|---------|------|
| `graphics.ao` | All game sprites | ~25 MB |
| `gui.ao` | UI textures | ~18 MB |
| `inits.ao` | Data indices (GRH, heads, bodies...) | ~133 KB |
| `maps.ao` | All game maps | ~508 KB |
| `music/` | Background music (OGG) | — |
| `sounds/` | Sound effects | — |
| `shaders/` | GLSL vertex + fragment shaders | — |

---

## Development Notes

- The project uses Gradle for dependency management
- Native libraries are automatically downloaded based on your operating system
- Supports Windows, Linux and MacOS (x64 & arm64)
- Make sure your graphics drivers are up to date for optimal OpenGL performance

---

<h1>Thanks to:</h1>
<br/>

<div align='center'>

<p>
  <b>Pablo Marquez (Morgolock) creator of Argentum Online</b> <br/>
  <a target="_blank" href="https://www.gs-zone.org/temas/argentum-online-en-java-opengl.92672/#post-785702"><b>Lord Fers</b> - GS-Zone user who released a base client offline in LWJGL2</a> <br/>
  And the GS-Zone people <br/>
  <a target="_blank" href="https://www.gs-zone.org/"><img src="https://user-images.githubusercontent.com/82490615/187148671-1d7f92b9-7ea1-45f2-b6f1-f53b07454d93.png"></img></a>
</p>

<h1>How to Contribute:</h1>
<br/>

1. Fork the Repository: Click on "Fork" in the top right corner of the page to create your own copy.
2. Clone Your Repository: Clone your fork to your computer using git clone https://github.com/YOUR_USERNAME/argentum-online-lwjgl3.git.
3. Create a Branch: Create a branch with git checkout -b branch-name for your changes.
4. Make Changes: Make your improvements or fixes and commit them.
5. Submit a Pull Request: From your fork, create a pull request for us to review your changes.

</div>
