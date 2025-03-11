package org.aoclient.scripts;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class Compressor {

    // Comprime una carpeta en un archivo .AO
    public static void compressFolder(String folderPath, String outputFilePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(outputFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            Path sourcePath = Paths.get(folderPath);
            Files.walk(sourcePath).filter(Files::isRegularFile).forEach(file -> {
                try {
                    ZipEntry zipEntry = new ZipEntry(sourcePath.relativize(file).toString());
                    zos.putNextEntry(zipEntry);

                    Files.copy(file, zos);
                    zos.closeEntry();
                } catch (IOException e) {
                    System.err.println("Error al comprimir archivo: " + file + " - " + e.getMessage());
                }
            });
        }
    }

    // Leemos un archivo específico del archivo .AO
    public static byte[] readResource(String compressedFilePath, String resourceBaseName) throws IOException {
        try (ZipFile zipFile = new ZipFile(compressedFilePath)) {
            ZipEntry matchedEntry = null;

            // Itera sobre todas las entradas del ZIP para encontrar una coincidencia
            for (ZipEntry entry : zipFile.stream().collect(Collectors.toList())) {
                String fileName = Paths.get(entry.getName()).getFileName().toString();
                String baseName = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName;

                if (baseName.equals(resourceBaseName)) {
                    matchedEntry = entry;
                    break;
                }
            }

            if (matchedEntry == null) throw new FileNotFoundException("No se encontró un archivo que coincida con: " + resourceBaseName);

            // Lee los datos del recurso encontrado
            try (InputStream is = zipFile.getInputStream(matchedEntry)) {
                return is.readAllBytes();
            }
        }
    }

    public static void main(String[] args) {
        String folderToCompress = "resources/sounds-descompressed";
        String compressedFile = "resources/sounds.ao";
        String resourceToRead = "9";

        try {
            // Comprime la carpeta
            compressFolder(folderToCompress, compressedFile);
            System.out.println("Carpeta comprimida en: " + compressedFile);

            // Lee un recurso específico del archivo comprimido
            byte[] resourceData = readResource(compressedFile, resourceToRead);
            System.out.println("Recurso leído: " + resourceToRead + " (" + resourceData.length + " bytes)");

            // Opcional: guarda el recurso leído para verificar
            Files.write(Paths.get("output_" + Paths.get(resourceToRead).getFileName() + ".ogg"), resourceData);

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

}
