package org.aoclient.engine.game;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * La clase Options representa las opciones de configuración del juego.
 * Puede utilizarse para controlar la configuración relacionada con la música,
 * el sonido, la visualización del nombre, el nombre del jugador en el juego, etc.
 */
public class Options {
    private boolean Music;
    private boolean Sound;
    private boolean ShowName;
    private String Name;

    /**
     * Crea una instancia de Options con valores predeterminados.
     * La música, el sonido y la visualización del nombre están habilitados por defecto,
     * y el nombre del jugador se inicializa como una cadena vacía.
     */
    public Options() {
        Music = true;
        Sound = true;
        ShowName = true;
        Name = "";
    }

    /**
     * Obtiene el nombre del jugador configurado.
     *
     * @return El nombre del jugador.
     */
    public String getName() {
        return Name;
    }

    /**
     * Establece el nombre del jugador.
     *
     * @param name El nuevo nombre del jugador.
     */
    public void setName(String name) {
        Name = name;
    }

    /**
     * Verifica si la música está habilitada.
     *
     * @return `true` si la música está habilitada, de lo contrario `false`.
     */
    public boolean isMusic() {
        return Music;
    }

    /**
     * Establece el estado de habilitación de la música.
     *
     * @param music `true` para habilitar la música, `false` para deshabilitarla.
     */
    public void setMusic(boolean music) {
        Music = music;
    }

    /**
     * Verifica si el sonido está habilitado.
     *
     * @return `true` si el sonido está habilitado, de lo contrario `false`.
     */
    public boolean isSound() {
        return Sound;
    }

    /**
     * Establece el estado de habilitación del sonido.
     *
     * @param sound `true` para habilitar el sonido, `false` para deshabilitarlo.
     */
    public void setSound(boolean sound) {
        Sound = sound;
    }

    /**
     * Verifica si la visualización del nombre está habilitada.
     *
     * @return `true` si la visualización del nombre está habilitada, de lo contrario `false`.
     */
    public boolean isShowName() {
        return ShowName;
    }

    /**
     * Establece el estado de habilitación de la visualización del nombre.
     *
     * @param showName `true` para habilitar la visualización del nombre, `false` para deshabilitarla.
     */
    public void setShowName(boolean showName) {
        ShowName = showName;
    }

    /**
     * Carga las opciones de configuración desde un archivo de inicialización.
     */
    public void LoadOptions() {
        try (BufferedReader reader = new BufferedReader(new FileReader("resources/inits/options.ini"))) {
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
    public void SaveOptions() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resources/inits/options.ini"))) {
            writer.write("Music = " + Music);
            writer.newLine();
            writer.write("Sound = " + Sound);
            writer.newLine();
            writer.write("ShowName = " + ShowName);
            writer.newLine();
            writer.write("Name = " + Name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualiza una opción de configuración con su nuevo valor.
     *
     * @param option El nombre de la opción a actualizar.
     * @param value El nuevo valor de la opción.
     */
    private void updateOption(String option, String value) {
        switch (option) {
            case "Music":
                Music = Boolean.parseBoolean(value);
                break;
            case "Sound":
                Sound = Boolean.parseBoolean(value);
                break;
            case "ShowName":
                ShowName = Boolean.parseBoolean(value);
                break;
            case "Name":
                Name = value;
                break;
        }
    }
}
