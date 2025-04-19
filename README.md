
<div align='center'>
    <br/>
    <p align='right'><a href="README_ES.md">🇪🇸 Leer en Español</a></p>
    <img src="https://github.com/gasti-jm/argentum-online-lwjgl3/assets/82490615/0ad2c3a1-c512-464c-a0a8-2cfc9f06c0f8" width= "300px"/> <br/> <br/>
    <a target="_blank"><img src="https://img.shields.io/badge/Built%20in-Java_17-43ca1f.svg?style=flat-square"></img></a>
    <a target="_blank"><img src="https://img.shields.io/badge/Made%20in-IntelliJ%20Community-be27e9.svg?style=flat-square"></img></a>
    <a target="_blank"><img src="https://img.shields.io/badge/License-GNU%20General%20Public%20License%20-e98227.svg?style=flat-square"></img></a>
</div>
<br/>

<h1>Argentum Online Java LWJGL3</h1>

<p>
  <a target="_blank" href="https://es.wikipedia.org/wiki/Argentum_Online">Argentum Online</a> is a MMORPG Video Game of the year 1999 created in Visual Basic 6.0 by Pablo Marquez (Morgolock), this game is open source. <br/> <br/>
  I have translated it to Java with LWJGL3, this game would be the base client, the connection to the server is being implemented with Java sockets. The java server has not been developed yet, but you can use the <a target="_blank" href="https://www.gs-zone.org/temas/cliente-servidor-v0-13-0-completos-y-funcionales.36521/">Argentum Online Alkon v13.0 server from Visual Basic 6.0</a>.
</p>

# Discord
For those who want to collaborate with the project, they are more than welcome! You can communicate with us on our <a href="https://discord.gg/RtsGRqJVt9">Discord.</a>

# Requirements

- <a href="https://www.oracle.com/java/technologies/downloads/#java17" target="_blank">Java Development Kit (JDK) 17</a> or higher
- <a href="https://www.jetbrains.com/idea/download/" target="_blank">IntelliJ IDEA</a> (recommended), <a href="https://netbeans.apache.org/" target="_blank">NetBeans</a>, <a href="https://www.eclipse.org/downloads/" target="_blank">Eclipse</a> or any Java IDE
- Gradle (automatically managed by the IDE)

# Dependencies

The project uses the following main dependencies (managed by Gradle):
- LWJGL 3.3.4
- JOML 1.10.5
- Dear ImGui 1.86.11
- TinyLog 2.7.0

# How to compile and run

1. Clone the repository:
```bash
git clone https://github.com/gasti-jm/argentum-online-lwjgl3.git
```

2. Open the project:
   - In IntelliJ IDEA: Go to `File > Open` and select the project folder
   - The IDE will automatically download all dependencies through Gradle

3. Build the project:
   - Using IDE: Click on the 'Build Project' button or press Ctrl+F9
   - Using Gradle directly: `./gradlew build`

4. Run the project:
   - Locate the main class `org.aoclient.engine.Main`
   - Right click and select 'Run' or press Shift+F10

# Development Notes

- The project uses Gradle for dependency management
- Native libraries are automatically downloaded based on your operating system
- Supports Windows, Linux and MacOS (x64 & arm64)
- Make sure your graphics drivers are up to date for optimal OpenGL performance


# Image
![Screenshot 2023-08-21 044542](https://github.com/gasti-jm/argentum-online-lwjgl3/assets/82490615/8f4c7864-feee-4ac6-b957-651a6b03a627)
#
![VirtualBox_Linux_09_03_2024_04_13_54](https://github.com/gasti-jm/argentum-online-lwjgl3/assets/82490615/8f54e716-3824-48ac-92ca-f22bfcfe74d5)

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
