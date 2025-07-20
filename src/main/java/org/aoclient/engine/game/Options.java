package org.aoclient.engine.game;

import org.tinylog.Logger;

import java.io.*;

/**
 * Sistema de configuracion del juego que permite cargar y guardar opciones en {@code options.ini}.
 */

public enum Options {

    INSTANCE; // Implementacion del patron Singleton de Joshua Bloch (considerada la mejor)

    private static final String OPTIONS_FILE_PATH = "resources/options.ini";

    private boolean music = true;
    private boolean sound = true;
    private boolean showName = true;
    private boolean fullscreen = true;
    private boolean vsync = true;
    private boolean cursorGraphic = true;
    private String nick = "";
    private String ipServer = "127.0.0.1";
    private String portServer = "7666";
    private String language = "es";

    /**
     * Carga las opciones.
     * <p>
     * Si el archivo existe y puede leerse, se recorren sus lineas para extraer las opciones y sus valores, que luego son cargados
     * en las propiedades correspondientes. En caso de que el archivo no exista o ocurra un error al leerlo, se genera una nueva
     * configuracion con valores predeterminados.
     */
    public void load() {
        try (BufferedReader reader = new BufferedReader(new FileReader(OPTIONS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String option = parts[0].trim();
                    String value = parts[1].trim();
                    load(option, value);
                }
            }
        } catch (IOException e) {
            Logger.error("The " + OPTIONS_FILE_PATH + " file was not found or could not be read, a new one was created with the default configuration.");
            save();
        }
    }

    /**
     * Guarda las opciones.
     */
    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OPTIONS_FILE_PATH))) {
            write(writer, "Music", music);
            write(writer, "Sound", sound);
            write(writer, "ShowName", showName);
            write(writer, "Name", nick);
            write(writer, "IP", ipServer);
            write(writer, "PORT", portServer);
            write(writer, "Fullscreen", fullscreen);
            write(writer, "VSYNC", vsync);
            write(writer, "CursorGraphic", cursorGraphic);
            write(writer, "Language", language);
        } catch (IOException e) {
            Logger.error("Could not write to options.ini file!");
        }
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public boolean isMusic() {
        return music;
    }

    public void setMusic(boolean music) {
        this.music = music;
    }

    public boolean isSound() {
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    public boolean isShowName() {
        return showName;
    }

    public void setShowName(boolean showName) {
        this.showName = showName;
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

    public boolean isCursorGraphic() {
        return cursorGraphic;
    }



    public void setCursorGraphic(boolean cursorGraphic) {
        this.cursorGraphic = cursorGraphic;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Escribe una opcion con su valor asociado en un objeto {@code BufferedWriter}.
     *
     * @param writer objeto {@code BufferedWriter} que sera utilizado para escribir la opcion
     * @param option nombre de la opcion
     * @param value  valor de la opcion
     */
    private void write(BufferedWriter writer, String option, Object value) throws IOException {
        writer.write(option + " = " + value);
        writer.newLine();
    }

    /**
     * Carga una opcion.
     *
     * @param option nombre de la opcion
     * @param value  valor de la opcion
     */
    private void load(String option, String value) {
        switch (option) {
            case "Music" -> music = Boolean.parseBoolean(value);
            case "Sound" -> sound = Boolean.parseBoolean(value);
            case "ShowName" -> showName = Boolean.parseBoolean(value);
            case "Name" -> nick = value;
            case "IP" -> ipServer = value;
            case "PORT" -> portServer = value;
            case "Fullscreen" -> fullscreen = Boolean.parseBoolean(value);
            case "VSYNC" -> vsync = Boolean.parseBoolean(value);
            case "CursorGraphic" -> cursorGraphic = Boolean.parseBoolean(value);
            case "Language" -> language = value;
            default -> Logger.warn("Unknown option ignored: {}", option);
        }
    }

}
