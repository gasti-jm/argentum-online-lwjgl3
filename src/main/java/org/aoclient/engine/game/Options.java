package org.aoclient.engine.game;

import java.io.*;

/**
 * La clase Options representa las opciones de configuración del juego. Puede utilizarse para controlar la configuración
 * relacionada con la música, el sonido, la visualización del nombre, el nombre del jugador en el juego, etc.
 */

public class Options {

    private boolean music;
    private boolean sound;
    private boolean showName;
    private boolean fullscreen;
    private boolean vsync;
    private String nickName;
    private String ipServer;
    private String portServer;

    /**
     * Crea una instancia de Options con valores predeterminados. La música, el sonido y la visualización del nombre están
     * habilitados por defecto, y el nombre del jugador se inicializa como una cadena vacía.
     */
    public Options() {
        this.fullscreen = true;
        this.vsync = true;
        this.music = true;
        this.sound = true;
        this.showName = true;
        this.nickName = "";
        this.ipServer = "";
        this.portServer = "";
    }

    /**
     * Carga las opciones de configuración desde un archivo de inicialización.
     */
    public void loadOptions() {
        try (BufferedReader reader = new BufferedReader(new FileReader("resources/options.ini"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String option = parts[0].trim();
                    String value = parts[1].trim();
                    updateOption(option, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Guarda las opciones de configuración en un archivo de inicialización.
     */
    public void saveOptions() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resources/options.ini"))) {
            writer.write("Music = " + music);
            writer.newLine();
            writer.write("Sound = " + sound);
            writer.newLine();
            writer.write("ShowName = " + showName);
            writer.newLine();
            writer.write("Name = " + nickName);
            writer.newLine();
            writer.write("IP = " + ipServer);
            writer.newLine();
            writer.write("PORT = " + portServer);
            writer.newLine();
            writer.write("Fullscreen = " + fullscreen);
            writer.newLine();
            writer.write("VSYNC = " + vsync);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualiza una opción de configuración con su nuevo valor.
     *
     * @param option El nombre de la opción a actualizar.
     * @param value  El nuevo valor de la opción.
     */
    private void updateOption(String option, String value) {
        switch (option) {
            case "Music":
                music = Boolean.parseBoolean(value);
                break;
            case "Sound":
                sound = Boolean.parseBoolean(value);
                break;
            case "ShowName":
                showName = Boolean.parseBoolean(value);
                break;
            case "Name":
                nickName = value;
                break;
            case "IP":
                ipServer = value;
                break;
            case "PORT":
                portServer = value;
                break;
            case "Fullscreen":
                fullscreen = Boolean.parseBoolean(value);
                break;
            case "VSYNC":
                vsync = Boolean.parseBoolean(value);
                break;
        }
    }

    /**
     * Obtiene el nombre del jugador configurado.
     *
     * @return El nombre del jugador.
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Establece el nombre del jugador.
     *
     * @param nickName El nuevo nombre del jugador.
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * Verifica si la música está habilitada.
     *
     * @return `true` si la música está habilitada, de lo contrario `false`.
     */
    public boolean isMusic() {
        return music;
    }

    /**
     * Establece el estado de habilitación de la música.
     *
     * @param music `true` para habilitar la música, `false` para deshabilitarla.
     */
    public void setMusic(boolean music) {
        this.music = music;
    }

    /**
     * Verifica si el sonido está habilitado.
     *
     * @return `true` si el sonido está habilitado, de lo contrario `false`.
     */
    public boolean isSound() {
        return sound;
    }

    /**
     * Establece el estado de habilitación del sonido.
     *
     * @param sound `true` para habilitar el sonido, `false` para deshabilitarlo.
     */
    public void setSound(boolean sound) {
        this.sound = sound;
    }

    /**
     * Verifica si la visualización del nombre está habilitada.
     *
     * @return `true` si la visualización del nombre está habilitada, de lo contrario `false`.
     */
    public boolean isShowName() {
        return showName;
    }

    /**
     * Establece el estado de habilitación de la visualización del nombre.
     *
     * @param showName `true` para habilitar la visualización del nombre, `false` para deshabilitarla.
     */
    public void setShowName(boolean showName) {
        showName = showName;
    }

    public String getIpServer() {
        return ipServer;
    }

    public void setIpServer(String ipServer) {
        this.ipServer = ipServer;
    }

    public String getPortServer() {
        return portServer;
    }

    public void setPortServer(String portServer) {
        this.portServer = portServer;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public boolean isVsync() {
        return vsync;
    }

    public void setVsync(boolean vsync) {
        this.vsync = vsync;
    }

}
