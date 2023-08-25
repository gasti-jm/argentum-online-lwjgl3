package org.aoclient.engine.game;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Options {
    private boolean Music;
    private boolean Sound;
    private boolean ShowName;

    public Options() {
        Music = true;
        Sound = true;
        ShowName = true;
    }

    public boolean isMusic() {
        return Music;
    }

    public void setMusic(boolean music) {
        Music = music;
    }

    public boolean isSound() {
        return Sound;
    }

    public void setSound(boolean sound) {
        Sound = sound;
    }

    public boolean isShowName() {
        return ShowName;
    }

    public void setShowName(boolean showName) {
        ShowName = showName;
    }

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

    public void SaveOptions() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resources/inits/options.ini"))) {
            writer.write("Music = " + Music);
            writer.newLine();
            writer.write("Sound = " + Sound);
            writer.newLine();
            writer.write("ShowName = " + ShowName);
            // Agrega más líneas aquí para otras opciones si es necesario
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
            // Puedes añadir más opciones aquí
        }
    }
}
