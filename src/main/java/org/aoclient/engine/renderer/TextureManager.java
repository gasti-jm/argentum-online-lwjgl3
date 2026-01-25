package org.aoclient.engine.renderer;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.aoclient.engine.utils.GameData.grhData;

public final class TextureManager {

    private static final Map<Integer, Texture> textures = new HashMap<>();
    private static final Set<Integer> pending = new HashSet<>();
    private static final Set<Integer> loading = new HashSet<>();
    private static final Queue<Runnable> uploadQueue = new ConcurrentLinkedQueue<>();

    private static final ExecutorService executor = Executors.newFixedThreadPool(
            Math.max(1, Runtime.getRuntime().availableProcessors() - 1),
            r -> {
                Thread t = new Thread(r);
                t.setDaemon(true);
                t.setName("TextureLoader");
                return t;
            }
    );

    // =========================
    // API PUBLICA
    // =========================

    /**
     * Pide una textura (NO la crea)
     * */
    public static void requestTextureFile(int fileNum) {
        if (!textures.containsKey(fileNum) && !loading.contains(fileNum)) {
            pending.add(fileNum);
        }
    }

    /**
     * Precarga de animacion en varias texturas.
     */
    public static void requestTexture(int grhIndex) {
        if (grhIndex == 0) return;

        if (grhData[grhIndex].getNumFrames() > 1) {
            for (int i = 1; i <= grhData[grhIndex].getNumFrames(); i++) {
                final int frame = grhData[grhIndex].getFrame(i);
                final int fileNum = grhData[frame].getFileNum();
                if (!textures.containsKey(fileNum) && !loading.contains(fileNum)) {
                    pending.add(fileNum);
                }
            }
        } else {
            final int fileNum = grhData[grhIndex].getFileNum();

            if (!textures.containsKey(fileNum) && !loading.contains(fileNum)) {
                pending.add(fileNum);
            }
        }
    }


    /**
     * Devuelve la textura SOLO si ya existe
     **/
    public static Texture getTexture(int fileNum) {
        return textures.get(fileNum);
    }

    public static boolean hasTexture(int fileNum) {
        return textures.containsKey(fileNum);
    }

    // =========================
    // LOAD PHASE
    // =========================

    /**
     * Carga TODAS las texturas pendientes (llamar antes de render)
     **/
    public static void processPending() {
        processUploadQueue();

        if (pending.isEmpty()) return;

        for (int fileNum : pending) {
            if (fileNum == 0) continue;
            submitTask(fileNum);
        }

        pending.clear();
    }

    /**
     * Carga TODAS las texturas pendientes (llamar antes de render) / GUI
     **/
    public static void processPending(boolean isGUI) {
        processPending();
    }

    private static void submitTask(int fileNum) {
        if (loading.contains(fileNum) || textures.containsKey(fileNum)) return;

        loading.add(fileNum);
        executor.submit(() -> {
            TextureData data = Texture.loadToCPU("graphics.ao", String.valueOf(fileNum));
            uploadQueue.add(() -> {
                if (data != null) {
                    Texture tex = new Texture();
                    tex.uploadToGPU(data);
                    textures.put(fileNum, tex);
                }
                loading.remove(fileNum);
            });
        });
    }

    private static void processUploadQueue() {
        // Limitamos la subida por frame para evitar picos de lag en la GPU
        // aunque upload es rápido, subir 50 texturas de golpe puede notarse.
        // Por ahora sin limite, pero es un punto de optimizacion.
        Runnable task;
        while ((task = uploadQueue.poll()) != null) {
            task.run();
        }
    }

    // =========================
    // CLEANUP
    // =========================

    public static void clear() {
        textures.clear();
        pending.clear();
        loading.clear();
        uploadQueue.clear();
    }

    /**
     * Crea una nueva textura a partir de un archivo especificado.
     * <p>
     * La textura es inicializada con la informacion proporcionada, incluyendo el archivo comprimido, el archivo de textura y si
     * esta destinada a interfaces graficas de usuario (GUI).
     *
     * @param fileCompressed nombre del archivo comprimido que contiene la textura
     * @param file           nombre del archivo dentro del archivo comprimido que contiene los datos de la textura
     * @param isGUI          indica si la textura esta destinada a ser utilizada en interfaces graficas de usuario (GUI)
     * @return la textura creada, o {@code null} si el nombre del archivo especificado esta vacio
     */
    public static Texture createTexture(String fileCompressed, String file) {
        if (file.isEmpty()) return null;

        var texture = new Texture();
        // createTexture sigue siendo sincrono para casos especificos
        texture.loadTexture(texture, fileCompressed, file, true);
        return texture;
    }

    public static void debugLists() {
        System.err.println("Loaded: " + textures.size());
        System.err.println("Pending: " + pending.size());
        System.err.println("Loading: " + loading.size());
        System.err.println("Upload Q: " + uploadQueue.size());
    }
}
