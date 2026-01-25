package org.aoclient.scripts;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * <p>
 * Se han implementado las mejoras solicitadas para optimizar el consumo de memoria, la latencia de carga y la eficiencia del motor gráfico:
 * <ol>
 *   <li><b>Nuevo Formato Packed (.ao):</b> Sustitución del formato ZIP por un formato binario personalizado con cabecera e índice, permitiendo acceso aleatorio.</li>
 *   <li><b>Mapeo de Memoria (MMap):</b> Uso de FileChannel.map() para que el SO gestione la carga, evitando el consumo masivo de RAM.</li>
 *   <li><b>Integración de STB Image:</b> Reemplazo de ImageIO por STB Image para una decodificación más rápida y eficiente.</li>
 *   <li><b>Recursos Externos:</b> Priorización de la carga desde la carpeta resources/ externa para facilitar parches y modding.</li>
 *   <li><b>Optimización de Lectura Binaria:</b> Uso directo de ByteBuffer en BinaryDataReader y GameData.</li>
 *   <li><b>Herramienta de Conversión:</b> Inclusión de utilidad para migrar de ZIP al nuevo formato optimizado.</li>
 * </ol>
 * <p>
 * Esta clase proporciona funcionalidades para gestionar los archivos empaquetados .ao utilizando mapeo de memoria
 * y acceso aleatorio para optimizar el rendimiento del juego.
 */
public class Compressor {
    private static final int MAGIC = 0x414F504B; // 'AOPK'

    private static class ResourceEntry {
        final long offset;
        final long size;

        ResourceEntry(long offset, long size) {
            this.offset = offset;
            this.size = size;
        }
    }

    private static final Map<String, Map<String, ResourceEntry>> fileIndexes = new HashMap<>();
    private static final Map<String, MappedByteBuffer> mappedFiles = new HashMap<>();

    public static int compressResource(String resourceName, String aoName) {
        if (resourceName == null || resourceName.isEmpty() || aoName == null || aoName.isEmpty()) {
            System.err.println("resourceName or aoName cannot be null or empty");
            return -1;
        }

        Path path = Paths.get(resourceName);
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            System.err.println("'" + resourceName + "' is not a valid directory");
            return -1;
        }

        try (RandomAccessFile raf = new RandomAccessFile(aoName, "rw")) {
            raf.setLength(0);
            raf.writeInt(MAGIC);
            
            List<Path> files;
            try (Stream<Path> stream = Files.walk(path)) {
                files = stream.filter(Files::isRegularFile).collect(Collectors.toList());
            }

            raf.writeInt(files.size());
            long indexPosition = raf.getFilePointer();
            
            int indexSize = 0;
            for (Path file : files) {
                String name = getBaseName(file.getFileName().toString()).toLowerCase();
                byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);
                indexSize += 4 + nameBytes.length + 8 + 8;
            }
            
            long dataStartOffset = indexPosition + indexSize;
            raf.seek(dataStartOffset);

            Map<String, ResourceEntry> entries = new LinkedHashMap<>();
            for (Path file : files) {
                long offset = raf.getFilePointer();
                byte[] data = Files.readAllBytes(file);
                raf.write(data);
                long size = data.length;
                String name = getBaseName(file.getFileName().toString()).toLowerCase();
                entries.put(name, new ResourceEntry(offset, size));
            }

            raf.seek(indexPosition);
            for (Map.Entry<String, ResourceEntry> entry : entries.entrySet()) {
                byte[] nameBytes = entry.getKey().getBytes(StandardCharsets.UTF_8);
                raf.writeInt(nameBytes.length);
                raf.write(nameBytes);
                raf.writeLong(entry.getValue().offset);
                raf.writeLong(entry.getValue().size);
            }

            return files.size();
        } catch (IOException e) {
            System.err.println("Error creating ao file: " + e.getMessage());
            return -1;
        }
    }

    public static void convertZipToAo(String zipPath, String aoPath) {
        System.out.println("Converting " + zipPath + " to " + aoPath + "...");
        try (InputStream is = Files.newInputStream(Paths.get(zipPath));
             ZipInputStream zis = new ZipInputStream(is);
             RandomAccessFile raf = new RandomAccessFile(aoPath, "rw")) {
            
            raf.setLength(0);
            raf.writeInt(MAGIC);
            
            Map<String, byte[]> dataMap = new LinkedHashMap<>();
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                if (ze.isDirectory()) continue;
                String name = getBaseName(getFileName(ze.getName())).toLowerCase();
                dataMap.put(name, zis.readAllBytes());
            }
            
            raf.writeInt(dataMap.size());
            long indexPos = raf.getFilePointer();
            
            int indexSize = 0;
            for (String name : dataMap.keySet()) {
                indexSize += 4 + name.getBytes(StandardCharsets.UTF_8).length + 8 + 8;
            }
            
            raf.seek(indexPos + indexSize);
            Map<String, ResourceEntry> entries = new LinkedHashMap<>();
            for (Map.Entry<String, byte[]> entry : dataMap.entrySet()) {
                long offset = raf.getFilePointer();
                raf.write(entry.getValue());
                entries.put(entry.getKey(), new ResourceEntry(offset, entry.getValue().length));
            }
            
            raf.seek(indexPos);
            for (Map.Entry<String, ResourceEntry> entry : entries.entrySet()) {
                byte[] nameBytes = entry.getKey().getBytes(StandardCharsets.UTF_8);
                raf.writeInt(nameBytes.length);
                raf.write(nameBytes);
                raf.writeLong(entry.getValue().offset);
                raf.writeLong(entry.getValue().size);
            }
            
            System.out.println("Done. Converted " + dataMap.size() + " files.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static synchronized void loadAndMap(String aoPath) {
        if (mappedFiles.containsKey(aoPath)) return;

        // Si el path ya empieza con assets/, lo usamos, sino lo agregamos.
        String normalizedPath = aoPath.startsWith("assets/") ? aoPath : "assets/" + aoPath;
        File file = new File(normalizedPath);

        if (!file.exists()) {
             // Fallback a la ruta original por si acaso
             file = new File(aoPath);
        }

        if (!file.exists()) {
            System.err.println("AO file not found: " + normalizedPath);
            return;
        }

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            if (raf.readInt() != MAGIC) {
                throw new IOException("Invalid AO file format (Magic mismatch): " + aoPath);
            }

            int count = raf.readInt();
            Map<String, ResourceEntry> index = new HashMap<>();
            for (int i = 0; i < count; i++) {
                int nameLen = raf.readInt();
                byte[] nameBytes = new byte[nameLen];
                raf.readFully(nameBytes);
                String name = new String(nameBytes, StandardCharsets.UTF_8);
                long offset = raf.readLong();
                long size = raf.readLong();
                index.put(name, new ResourceEntry(offset, size));
            }

            FileChannel channel = raf.getChannel();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            
            fileIndexes.put(aoPath, index);
            mappedFiles.put(aoPath, buffer);

        } catch (IOException e) {
            System.err.println("Error mapping AO resource: " + e.getMessage() + " (" + aoPath + ")");
        }
    }

    public static byte[] readResource(String aoPath, String resourceName) {
        ByteBuffer buffer = readResourceAsBuffer(aoPath, resourceName);
        if (buffer == null) return null;

        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        return data;
    }

    public static ByteBuffer readResourceAsBuffer(String aoPath, String resourceName) {
        if (aoPath == null || resourceName == null || aoPath.isEmpty() || resourceName.isEmpty()) {
            return null;
        }

        if (!mappedFiles.containsKey(aoPath)) {
            loadAndMap(aoPath);
        }

        MappedByteBuffer fileBuffer = mappedFiles.get(aoPath);
        Map<String, ResourceEntry> index = fileIndexes.get(aoPath);

        if (fileBuffer != null && index != null) {
            ResourceEntry entry = index.get(resourceName.toLowerCase());
            if (entry != null) {
                return fileBuffer.slice((int)entry.offset, (int)entry.size).asReadOnlyBuffer();
            }
        }

        return null;
    }

    private static String getFileName(String path) {
        int lastSeparatorIndex = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
        return lastSeparatorIndex >= 0 ? path.substring(lastSeparatorIndex + 1) : path;
    }

    private static String getBaseName(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 ? fileName.substring(0, dotIndex) : fileName;
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("convert")) {
            System.out.println("Starting conversion process...");
            String[] files = {"graphics", "gui", "inits", "maps"};
            for (String f : files) {
                String zipPath = "src/main/resources/resources/" + f + ".ao";
                String outPath = "assets/" + f + ".ao";
                new File("assets").mkdirs();
                File zipFile = new File(zipPath);
                if (zipFile.exists()) {
                    convertZipToAo(zipPath, outPath);
                } else {
                    System.err.println("ZIP source not found for conversion: " + zipPath);
                }
            }
            System.out.println("Conversion process finished.");
            System.exit(0); // Terminar después de convertir
        }
    }
}