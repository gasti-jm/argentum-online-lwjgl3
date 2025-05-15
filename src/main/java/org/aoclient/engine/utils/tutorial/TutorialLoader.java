package org.aoclient.engine.utils.tutorial;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TutorialLoader {
    public static TutorialData loadFromFile(File file) throws IOException {
        List<TutorialPage> pages = new ArrayList<>();
        Map<String, Map<String, String>> ini = parseIni(file);
        int numPages = Integer.parseInt(ini.getOrDefault("INIT", Collections.emptyMap()).getOrDefault("NumPags", "0"));
        for (int i = 1; i <= numPages; i++) {
            String section = "PAG" + i;
            Map<String, String> pageData = ini.get(section);
            if (pageData == null) continue;
            String title = pageData.getOrDefault("Title", "");
            int numLines = Integer.parseInt(pageData.getOrDefault("NumLines", "0"));
            StringBuilder text = new StringBuilder();
            for (int l = 1; l <= numLines; l++) {
                String line = pageData.getOrDefault("Line" + l, "");
                text.append(line);
                if (l != numLines) text.append("\n");
            }
            pages.add(new TutorialPage(title, text.toString()));
        }
        return new TutorialData(pages);
    }

    private static Map<String, Map<String, String>> parseIni(File file) throws IOException {
        Map<String, Map<String, String>> ini = new LinkedHashMap<>();
        Map<String, String> currentSection = null;
        String currentSectionName = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith(";")) continue;
                if (line.startsWith("[") && line.endsWith("]")) {
                    currentSectionName = line.substring(1, line.length() - 1);
                    currentSection = new LinkedHashMap<>();
                    ini.put(currentSectionName, currentSection);
                } else if (currentSection != null && line.contains("=")) {
                    int idx = line.indexOf('=');
                    String key = line.substring(0, idx).trim();
                    String value = line.substring(idx + 1).trim();
                    currentSection.put(key, value);
                }
            }
        }
        return ini;
    }
}
