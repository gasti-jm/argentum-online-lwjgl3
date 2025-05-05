package org.aoclient.scripts;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * <p>
 * Esta clase proporciona funcionalidades para comprimir directorios completos en archivos con extension <b>.ao</b> (que son
 * archivos zip con extension personalizada) y para extraer recursos especificos de estos archivos comprimidos.
 * <p>
 * Permite comprimir carpetas enteras manteniendo su estructura de directorios, lo que resulta util para empaquetar recursos del
 * juego como graficos, sonidos, o datos de inicializacion. Tambien ofrece la capacidad de leer archivos individuales dentro de
 * estos paquetes comprimidos buscando coincidencias por nombre base.
 * <p>
 * El formato ao se utiliza como un sistema de empaquetado de recursos para el juego, permitiendo una gestion eficiente de los
 * archivos y reduciendo el espacio de almacenamiento necesario. La clase incluye un metodo main que proporciona un ejemplo de
 * compresion y extraccion de recursos.
 * <p>
 * Esta utilidad es fundamental para el sistema de carga de recursos del juego, ya que muchos componentes como sonidos, graficos y
 * datos de inicializacion se almacenan en archivos comprimidos ao.
 * <p>
 * El sistema operativo puede cachear los accesos a recursos mas eficientemente que el ClassLoader. Por esa razon y porque el
 * proyecto tiene muchas dependencias (classpath extenso), no se usa el ClassLoader, ya que la task {@code processResources} de
 * Gradle tarda mucho tiempo en procesar los recursos.
 * <p>
 * TODO Deberia configurar el nivel de compresion?
 */

public class Compressor {

    /**
     * <p>
     * Comprime todos los archivos y subcarpetas de una carpeta en un archivo con formato ao (extension zip personalizada).
     * <p>
     * <b>Por ahora este metodo no admite la compresion de archivos individuales.</b>
     * <p>
     * Este metodo preserva la estructura jerarquica original de la carpeta dentro del archivo comprimido y utiliza codificacion
     * UTF-8 para garantizar la compatibilidad de caracteres especiales en los nombres de archivos y carpetas.
     * <p>
     * El proceso interno funciona de la siguiente manera:
     * <ol>
     * <li>Convierte {@code resourceName} en un objeto {@link Path} para la manipulacion eficiente de rutas
     * <li>Valida que el nombre del recurso (que es una carpeta en este caso) exista y sea un directorio
     * <li>Crea un {@link FileOutputStream} hacia el archivo ao especificado
     * <li>Envuelve este stream en un {@link ZipOutputStream} para manejar la compresion ao
     * <li>Crea un flujo de ruta ({@code pathStream}) utilizando {@link Files#walk} envuelto en <i>try-with-resources</i>
     * <li>Recorre recursivamente todos los archivos regulares encontrados en la carpeta y subcarpetas
     * <li>Para cada archivo encontrado, delega la compresion al metodo {@link #compressFile} que utiliza un buffer optimizado
     * </ol>
     * <p>
     * Si ocurre un error durante la compresion, se retorna un valor negativo (-1) con informacion sobre la causa del problema,
     * evitando que se cierre el compresor.
     * <p>
     * Es importante aclarar que siempre sobrescribe el archivo ao si existe.
     * <p>
     * La codificacion UTF-8 asegura que los archivos o carpetas con nombres que contienen caracteres especiales (acentos, ñ,
     * caracteres no latinos, etc.) se manejen correctamente y puedan ser extraidos en cualquier sistema operativo.
     * <p>
     * Ejemplo de uso:
     * <pre>{@code
     * String resourceName = "resources/sounds";
     * String aoName = "resources/sounds.ao";
     * int filesCompressed = Compressor.compressResource(resourceName, aoName);
     * }</pre>
     * <p>
     * TODO Implementar la compresion de archivos individuales
     *
     * @param resourceName nombre del recurso a comprimir (solo carpetas)
     * @param aoName       nombre del archivo ao resultante
     * @return el numero de archivos comprimidos exitosamente, o -1 si ocurrio un error
     */
    public static int compressResource(String resourceName, String aoName) {

        if (resourceName == null || resourceName.isEmpty() || aoName == null || aoName.isEmpty()) {
            System.err.println("resourceName or aoName cannot be null or empty");
            return -1;
        }

        Path path = Paths.get(resourceName);

        if (!Files.exists(path)) {
            System.err.println("'" + resourceName + "' does not exist");
            return -1;
        }

        if (!Files.isDirectory(path)) {
            System.err.println("'" + resourceName + "' is not a directory");
            return -1;
        }

        final AtomicInteger fileCount = new AtomicInteger(0);
        try (FileOutputStream fos = new FileOutputStream(aoName); ZipOutputStream zos = new ZipOutputStream(fos, StandardCharsets.UTF_8)) {
            try (Stream<Path> pathStream = Files.walk(path)) {
                pathStream.filter(Files::isRegularFile).forEach(file -> {
                    if (compressFile(path, file, zos)) fileCount.incrementAndGet();
                });
            } catch (IOException e) {
                System.err.println("Error walking directory structure: " + e.getMessage());
                return -1;
            } catch (Exception e) {
                System.err.println("Unexpected error during directory traversal: " + e.getClass().getName() + ": " + e.getMessage());
                return -1;
            }
            return fileCount.get();
        } catch (IOException e) {
            System.err.println("Error creating ao file: " + e.getMessage());
            return -1;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getClass().getName() + ": " + e.getMessage());
            return -1;
        }
    }

    /**
     * Lee un recurso especifico desde un archivo comprimido ao.
     * <p>
     * Este metodo busca dentro del archivo ao especificado un recurso cuyo nombre base (sin la extension) coincida con el nombre
     * del recurso proporcionado. Si se encuentra una coincidencia, el contenido del recurso se lee y se devuelve como un array de
     * bytes. Si ocurre un error, se registra en la consola y se devuelve null.
     * <pre>{@code
     * // Lee un archivo desde un ao
     * byte[] data = readResource("resources/inits.ao", "heads");
     * if (data != null) {
     *     // Procesa el recurso normalmente
     *     reader.init(data);
     *     // ...
     * } else {
     *     // Maneja el caso de error
     *      System.err.println("Could not load heads data!");
     *      return;
     * }
     * }</pre>
     *
     * @param aoName       nombre del archivo ao que contiene el recurso
     * @param resourceName nombre del recurso a leer (sin extension)
     * @return bytes del recurso encontrado, o null si ocurre un error
     */
    public static byte[] readResource(String aoName, String resourceName) {

        if (aoName == null || resourceName == null) {
            System.err.println("aoName and resourceName cannot be null");
            return null;
        }

        if (aoName.isEmpty() || resourceName.isEmpty()) {
            System.err.println("aoName and resourceName cannot be empty");
            return null;
        }

        try (ZipFile zipFile = new ZipFile(aoName, StandardCharsets.UTF_8)) {

            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {

                ZipEntry entry = entries.nextElement();

                // Ignora directorios
                if (entry.isDirectory()) continue;

                String fileName = getFileName(entry.getName());
                String baseName = getBaseName(fileName);

                if (baseName.equalsIgnoreCase(resourceName)) {
                    // Lee los datos del recurso encontrado desde un try-with-resources garantizando que el stream se cierre automaticamente al finalizar
                    try (InputStream is = zipFile.getInputStream(entry)) {
                        return is.readAllBytes();
                    } catch (IOException e) {
                        System.err.println("Error reading resource data for '" + resourceName + "': " + e.getMessage());
                        return null;
                    }
                }

            }

            System.err.println("No files were found matching '" + resourceName + "' in " + aoName);

        } catch (IOException e) {
            System.err.println("Error accessing ao file '" + aoName + "'");
        }

        return null;
    }

    /**
     * Extrae el nombre del archivo de una ruta completa.
     * <p>
     * Este metodo obtiene unicamente el nombre del archivo a partir de una ruta completa, independientemente del sistema
     * operativo utilizado para crear la ruta. Funciona con separadores de ruta tanto de estilo Unix/Linux (/) como Windows (\).
     * <p>
     * Se utiliza en lugar de acceder directamente a {@code entry.getName()} porque:
     * <ul>
     * <li>{@code entry.getName()} devuelve la ruta completa del archivo dentro del ao, no solo el nombre
     * <li>Para buscar coincidencias por nombre base necesitamos extraer solo el archivo sin la estructura de directorios
     * <li>Es mas eficiente que utilizar {@code Paths.get(entry.getName()).getFileName().toString()}
     * <li>No requiere importar dependencias adicionales como {@code java.nio.file.Paths}
     * </ul>
     * <pre>{@code
     * // Ejemplos de uso:
     * getFileName("carpeta/archivo.txt") // devuelve "archivo.txt"
     * getFileName("carpeta\archivo.txt") // devuelve "archivo.txt"
     * getFileName("archivo.txt") // devuelve "archivo.txt"
     * getFileName("carpeta/subcarpeta/a.txt") // devuelve "a.txt"
     * }</pre>
     *
     * @param path la ruta completa del archivo, que puede contener separadores / o \
     * @return el nombre del archivo sin la estructura de directorios
     */
    private static String getFileName(String path) {
        /* Detecta el ultimo separador ya sea de Unix/Linux (/) o de Windows (\), y toma el indice mas alto de los dos, que
         * correspondera al ultimo separador de cualquier tipo en la cadena. */
        int lastSeparatorIndex = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
        /* Si encontro algun separador (lastSeparatorIndex >= 0), extrae la subcadena desde la posicion justo despues del ultimo
         * separador hasta el final. Si no se encontro ningun separador (lastSeparatorIndex == -1), devuelve la cadena original
         * completa, asumiendo que ya es solo un nombre de archivo. */
        return lastSeparatorIndex >= 0 ? path.substring(lastSeparatorIndex + 1) : path;
    }

    /**
     * Obtiene el nombre base de un archivo (sin extension).
     * <p>
     * Este metodo extrae el nombre base de un archivo eliminando la extension. El nombre base es la parte del nombre del archivo
     * antes del ultimo punto (.). Si el archivo no tiene extension (no contiene un punto), se devuelve el nombre completo.
     * <p>
     * Es importante destacar que el metodo:
     * <ul>
     * <li>Solo considera el ultimo punto como separador de extension
     * <li>Requiere que el parametro sea solo el nombre del archivo, no una ruta completa
     * <li>Devuelve el nombre completo si no hay punto o si el punto es el primer caracter
     * </ul>
     * <pre>{@code
     * // Ejemplos de uso:
     * getBaseName("archivo.txt") // devuelve "archivo"
     * getBaseName("imagen.original.png") // devuelve "imagen.original"
     * getBaseName("config") // devuelve "config" (sin extension)
     * getBaseName(".htaccess") // devuelve ".htaccess" (el punto es parte del nombre)
     * }</pre>
     *
     * @param fileName el nombre del archivo del que se extraera el nombre base
     * @return el nombre base del archivo sin la extension
     */
    private static String getBaseName(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 ? fileName.substring(0, dotIndex) : fileName;
    }

    /**
     * <p>
     * Comprime un archivo individual dentro del archivo ao utilizando un buffer optimizado.
     * <p>
     * El proceso funciona de la siguiente manera:
     * <ol>
     * <li>Crea una entrada zip con la ruta relativa del archivo respecto a la carpeta base
     * <li>Establece un buffer de 8KB para transferir el contenido del archivo por fragmentos
     * <li>Lee el archivo de entrada utilizando un {@link InputStream} con <i>try-with-resources</i>
     * <li>Transfiere los datos por bloques, optimizando el uso de memoria
     * <li>Cierra la entrada zip actual una vez completada la transferencia
     * </ol>
     * <p>
     * La implementacion del buffer personalizado de 8KB (8192 bytes) ofrece un equilibrio optimo entre:
     * <ul>
     * <li>Rendimiento de compresion: evita lecturas/escrituras por byte que serian ineficientes
     * <li>Uso de memoria: no ocupa demasiado espacio en RAM durante la operacion
     * <li>Compatibilidad: funciona bien con la mayoria de tipos de sistemas de archivos
     * </ul>
     * <p>
     * El buffer es esencial para archivos grandes, ya que evita cargar todo el contenido en memoria a la vez. Esto previene
     * posibles errores de {@code OutOfMemoryError} y mejora significativamente el rendimiento al reducir el numero de operaciones
     * de I/O necesarias.
     * <p>
     * Realize las siguientes compresiones de la carpeta sounds y estos fueron los resultados:
     * <pre>{@code
     * Primera prueba
     * Se comprimio en 3550 ms sin buffer
     * Se comprimio en 755 ms con buffer
     * Segunda prueba
     * Se comprimio en 2580 ms sin buffer
     * Se comprimio en 782 ms con buffer
     * }</pre>
     * <p>
     * Por lo tanto, usar buffer mejora la velocidad de compresion aproximadamente un <b>70%</b>, siendo 4 veces mas rapida que
     * sin buffer!
     *
     * @param path carpeta que se esta comprimiendo
     * @param file archivo que se va a comprimir
     * @param zos  stream de salida zip donde se escribira el archivo
     * @return {@code true} si el archivo se comprimio exitosamente, {@code false} si ocurrio algun error
     */
    private static boolean compressFile(Path path, Path file, ZipOutputStream zos) {

        final int defaultBufferSize = 8192; // 8KB

        try {
            zos.putNextEntry(new ZipEntry(path.relativize(file).toString()));
            byte[] buffer = new byte[defaultBufferSize];
            int bytesRead;
            try (InputStream in = Files.newInputStream(file)) {
                while ((bytesRead = in.read(buffer)) != -1)
                    zos.write(buffer, 0, bytesRead);
            }
            zos.closeEntry();
            return true;
        } catch (IOException e) {
            System.err.println("Error compressing '" + file + "' - I/O error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error compressing '" + file + "' - Unexpected error: " + e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {

        String resourceName = "resources/gui-descompressed";
        String aoName = "resources/gui.ao";

        long start = System.nanoTime();
        int filesCompressed = compressResource(resourceName, aoName);
        long time = (System.nanoTime() - start) / 1_000_000;

        if (filesCompressed > 0)
            System.out.println("Compressed " + filesCompressed + " file" + (filesCompressed > 1 ? "s" : "") + " in " + time + " ms from '" + resourceName + "' in '" + aoName + "'");
        else if (filesCompressed == 0) System.out.println("The folder has no files!");
        else System.err.println("Compression failed!");

        String resourceNameToRead = "2"; // 2.ogg
        // byte[] bytes = readResource(aoName, resourceNameToRead);
        //if (bytes != null) {
        // System.out.println(bytes.length + " bytes were read from resource '" + resourceNameToRead + "'");
        // Opcional: guarda el recurso leido para verificar
            /* try {
                Files.write(Paths.get("output_" + Paths.get(resourceNameToRead).getFileName() + ".map"), bytes);
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            } */
        //}

    }

}
