<div align='center'>
    <br/>
    <img src="https://github.com/gasti-jm/argentum-online-lwjgl3/assets/82490615/0ad2c3a1-c512-464c-a0a8-2cfc9f06c0f8" width= "300px"/> <br/> <br/>
    <a target="_blank"><img src="https://img.shields.io/badge/Built%20in-Java_17-43ca1f.svg?style=flat-square"></img></a>
    <a target="_blank"><img src="https://img.shields.io/badge/Made%20in-IntelliJ%20Community-be27e9.svg?style=flat-square"></img></a>
    <a target="_blank"><img src="https://img.shields.io/badge/License-GNU%20General%20Public%20License%20-e98227.svg?style=flat-square"></img></a>
</div>
<br/>

<h1>Argentum Online Java LWJGL3</h1>

<p>
  <a target="_blank" href="https://es.wikipedia.org/wiki/Argentum_Online">Argentum Online</a> es un videojuego MMORPG del año 1999 creado en Visual Basic 6.0 por Pablo Marquez (Morgolock), este juego es de código abierto. <br/> <br/>
  Lo he traducido a Java con LWJGL3, este sería el cliente base, la conexión al servidor se está implementando con sockets Java. El servidor en Java aún no ha sido desarrollado, pero puedes usar el <a target="_blank" href="https://www.gs-zone.org/temas/cliente-servidor-v0-13-0-completos-y-funcionales.36521/">servidor Argentum Online Alkon v13.0 de Visual Basic 6.0</a>.
</p>

# Discord
Para aquellos que quieran colaborar con el proyecto, ¡son más que bienvenidos! Pueden comunicarse con nosotros en nuestro <a href="https://discord.gg/RtsGRqJVt9">Discord.</a>

# Requisitos

- <a href="https://www.oracle.com/java/technologies/downloads/#java17" target="_blank">Kit de Desarrollo de Java (JDK) 17</a> o superior
- <a href="https://www.jetbrains.com/idea/download/" target="_blank">IntelliJ IDEA</a> (recomendado), <a href="https://netbeans.apache.org/" target="_blank">NetBeans</a>, <a href="https://www.eclipse.org/downloads/" target="_blank">Eclipse</a> o cualquier IDE de Java
- Gradle (gestionado automáticamente por el IDE)

# Dependencias

El proyecto utiliza las siguientes dependencias principales (gestionadas por Gradle):
- LWJGL 3.3.3
- JOML 1.10.5
- Dear ImGui 1.86.11
- TinyLog 2.7.0

# Cómo compilar y ejecutar

1. Clonar el repositorio:
```bash
git clone https://github.com/gasti-jm/argentum-online-lwjgl3.git
```

2. Abrir el proyecto:
   - En IntelliJ IDEA: Ve a `File > Open` y selecciona la carpeta del proyecto
   - El IDE descargará automáticamente todas las dependencias a través de Gradle

3. Compilar el proyecto:
   - Usando el IDE: Haz clic en el botón 'Build Project' o presiona Ctrl+F9
   - Usando Gradle directamente: `./gradlew build`

4. Ejecutar el proyecto:
   - Localiza la clase principal `org.aoclient.engine.Main`
   - Haz clic derecho y selecciona 'Run' o presiona Shift+F10

# Notas de Desarrollo

- El proyecto utiliza Gradle para la gestión de dependencias
- Las librerías nativas se descargan automáticamente según tu sistema operativo
- Compatible con Windows, Linux y MacOS (x64 & arm64)
- Asegúrate de tener los drivers gráficos actualizados para un rendimiento óptimo de OpenGL

# Imágenes
![Screenshot 2023-08-21 044542](https://github.com/gasti-jm/argentum-online-lwjgl3/assets/82490615/8f4c7864-feee-4ac6-b957-651a6b03a627)
#
![VirtualBox_Linux_09_03_2024_04_13_54](https://github.com/gasti-jm/argentum-online-lwjgl3/assets/82490615/8f54e716-3824-48ac-92ca-f22bfcfe74d5)

<h1>Agradecimientos:</h1>
<br/>

<div align='center'>

<p>
  <b>Pablo Marquez (Morgolock) creador de Argentum Online</b> <br/>
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
