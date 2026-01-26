package org.aoclient.engine.renderer;

import org.aoclient.engine.utils.inits.GrhInfo;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.aoclient.engine.scenes.Camera.TILE_PIXEL_SIZE;
import static org.aoclient.engine.utils.GameData.grhData;
import static org.aoclient.engine.utils.GameData.mapData;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class MapGraph {

    static class StaticBatch {
        int vao, vbo;
        int vertexCount;
        int textureID; // fileNum
        FloatBuffer buffer;

        public StaticBatch(int textureID, List<Float> vertices) {
            this.textureID = textureID;
            this.vertexCount = vertices.size() / 8; // 8 floats per vertex

            this.buffer = MemoryUtil.memAllocFloat(vertices.size());
            for (float f : vertices) {
                this.buffer.put(f);
            }
            this.buffer.flip();

            initGL();
        }

        private void initGL() {
            vao = glGenVertexArrays();
            vbo = glGenBuffers();

            glBindVertexArray(vao);
            glBindBuffer(GL_ARRAY_BUFFER, vbo);

            glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

            int stride = 8 * Float.BYTES;

            // position
            glVertexAttribPointer(0, 2, GL_FLOAT, false, stride, 0);
            glEnableVertexAttribArray(0);

            // uv
            glVertexAttribPointer(1, 2, GL_FLOAT, false, stride, 2L * Float.BYTES);
            glEnableVertexAttribArray(1);

            // color
            glVertexAttribPointer(2, 4, GL_FLOAT, false, stride, 4L * Float.BYTES);
            glEnableVertexAttribArray(2);

            glBindVertexArray(0);
        }

        public void render() {
            if (vertexCount == 0) return;
            Texture tex = TextureManager.getTexture(textureID);
            if (tex == null) return;

            glActiveTexture(GL_TEXTURE0);
            tex.bind();
            glBindVertexArray(vao);
            glDrawArrays(GL_TRIANGLES, 0, vertexCount);
            glBindVertexArray(0);
        }

        public void dispose() {
            MemoryUtil.memFree(buffer);
            glDeleteBuffers(vbo);
            glDeleteVertexArrays(vao);
        }
    }

    private static final Map<Integer, StaticBatch> layer1Batches = new HashMap<>();
    private static final Map<Integer, StaticBatch> layer2Batches = new HashMap<>();
    private static boolean mapBuilt = false;

    public static void build() {
        // Asegurar que las texturas esten cargadas
        TextureManager.processPending();

        dispose();

        Map<Integer, List<Float>> l1Data = new HashMap<>();
        Map<Integer, List<Float>> l2Data = new HashMap<>();

        for (int y = 1; y <= 100; y++) {
            for (int x = 1; x <= 100; x++) {
                // Layer 1
                processTile(x, y, 1, l1Data);
                // Layer 2
                processTile(x, y, 2, l2Data);
            }
        }

        // Crear batches
        for (Map.Entry<Integer, List<Float>> entry : l1Data.entrySet()) {
            layer1Batches.put(entry.getKey(), new StaticBatch(entry.getKey(), entry.getValue()));
        }

        for (Map.Entry<Integer, List<Float>> entry : l2Data.entrySet()) {
            layer2Batches.put(entry.getKey(), new StaticBatch(entry.getKey(), entry.getValue()));
        }

        mapBuilt = true;
    }

    private static void processTile(int x, int y, int layer, Map<Integer, List<Float>> batchMap) {
        if (mapData[x][y] == null) return;
        GrhInfo grh = mapData[x][y].getLayer(layer);
        
        if (grh.getGrhIndex() == 0) return;
        if (grh.isStarted()) return; // Ignorar animados (se renderizan dinamicamente)

        int grhIndex = grh.getGrhIndex();
        int fileNum = grhData[grhIndex].getFileNum();
        if (fileNum == 0) return;

        Texture tex = TextureManager.getTexture(fileNum);
        if (tex == null) return; // No se puede construir sin textura (necesitamos dimensions)

        batchMap.putIfAbsent(fileNum, new ArrayList<>());
        List<Float> verts = batchMap.get(fileNum);

        float drawX = x * TILE_PIXEL_SIZE;
        float drawY = y * TILE_PIXEL_SIZE;
        
        // Ajuste segun tamaño del grafico (si es mas grande que 32x32)
        // La logica original en GameScene:
        /*
        if (center) {
            if (grhData[currentGrhIndex].getTileWidth() != 1)
                x = x - (int) (grhData[currentGrhIndex].getTileWidth() * TILE_PIXEL_SIZE / 2) + TILE_PIXEL_SIZE / 2;
            if (grhData[currentGrhIndex].getTileHeight() != 1)
                y = y - (int) (grhData[currentGrhIndex].getTileHeight() * TILE_PIXEL_SIZE) + TILE_PIXEL_SIZE;
        }
        */
        // GameScene usa center=true.
        // Pero espera, x e y en GameScene son indices de tiles * 32.
        // Si tileWidth != 1, ajusta.
        
        // En World Space:
        // x, y son indices del tile.
        // pos real = x * 32, y * 32 (esquina superior izquierda del tile 1x1).
        
        // Vamos a replicar la logica de 'center'
        float finalX = drawX;
        float finalY = drawY;

        if (grhData[grhIndex].getTileWidth() != 1) {
            finalX = finalX - (grhData[grhIndex].getTileWidth() * TILE_PIXEL_SIZE / 2) + TILE_PIXEL_SIZE / 2f;
        }
        if (grhData[grhIndex].getTileHeight() != 1) {
             finalY = finalY - (grhData[grhIndex].getTileHeight() * TILE_PIXEL_SIZE) + TILE_PIXEL_SIZE;
        }

        float w = grhData[grhIndex].getPixelWidth();
        float h = grhData[grhIndex].getPixelHeight();
        float srcX = grhData[grhIndex].getsX();
        float srcY = grhData[grhIndex].getsY();

        float u0 = srcX / tex.getTex_width();
        float v0 = (srcY + h) / tex.getTex_height(); // V invertida? Renderer usa v0 = (srcY+h)/H
        float u1 = (srcX + w) / tex.getTex_width();
        float v1 = srcY / tex.getTex_height();
        
        // Renderer.java:
        // v1 = srcY / tex.getTex_height();
        // v0 = (srcY + srcH) / tex.getTex_height();
        // push: u0, v1 (top left?) -> No, v1 is TOP in OpenGL usually if 0,0 is bottom-left. 
        // But Renderer projection is Ortho(0, W, H, 0). 0 is TOP.
        // So v=0 is top of texture?
        // Typically texture coords: 0,0 is bottom-left of texture image.
        // If loaded with stbi and flipped?
        
        // Renderer.draw push logic:
        // push(x, y,         u0, v1 ...); // Top Left
        // push(x + w, y,     u1, v1 ...); // Top Right
        // push(x + w, y + h, u1, v0 ...); // Bottom Right
        
        // push(x, y + h,     u0, v0 ...); // Bottom Left
        // ...
        
        // So (x,y) corresponds to v1.
        // (x,y+h) corresponds to v0.
        // If projection 0 is Top, then (x,y) is top-left on screen.
        // So v1 should be the top-coord of the texture.
        // In standard OpenGL texture coords, 0 is bottom.
        // If the texture is loaded normally, top is 1 (or close to 1).
        // BUT Renderer calculates: v1 = srcY / H.
        // If srcY is 0 (top of image file), then v1 = 0.
        // So v1 (0) is mapped to (x,y) (Top of screen).
        // This means texture coordinates 0,0 match top-left of screen.
        // This implies the texture data is likely NOT flipped vertically on load, OR the shader/setup expects 0,0 at top.
        // I will stick to Renderer logic.
        
        addQuad(verts, finalX, finalY, w, h, u0, v0, u1, v1);
    }
    
    private static void addQuad(List<Float> verts, float x, float y, float w, float h, float u0, float v0, float u1, float v1) {
        float r=1, g=1, b=1, a=1;
        
        // Top Left
        push(verts, x, y, u0, v1, r, g, b, a);
        // Top Right
        push(verts, x + w, y, u1, v1, r, g, b, a);
        // Bottom Right
        push(verts, x + w, y + h, u1, v0, r, g, b, a);
        
        // Bottom Left
        push(verts, x, y + h, u0, v0, r, g, b, a);
        // Top Left
        push(verts, x, y, u0, v1, r, g, b, a);
        // Bottom Right
        push(verts, x + w, y + h, u1, v0, r, g, b, a);
    }

    private static void push(List<Float> verts, float x, float y, float u, float v, float r, float g, float b, float a) {
        verts.add(x);
        verts.add(y);
        verts.add(u);
        verts.add(v);
        verts.add(r);
        verts.add(g);
        verts.add(b);
        verts.add(a);
    }

    public static void renderLayer(int layer) {
        if (!mapBuilt) {
            build();
        }

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        Map<Integer, StaticBatch> batches = (layer == 1) ? layer1Batches : layer2Batches;
        for (StaticBatch batch : batches.values()) {
            batch.render();
        }

        glDisable(GL_BLEND);
    }

    public static void dispose() {
        for (StaticBatch b : layer1Batches.values()) b.dispose();
        for (StaticBatch b : layer2Batches.values()) b.dispose();
        layer1Batches.clear();
        layer2Batches.clear();
        mapBuilt = false;
    }
}
