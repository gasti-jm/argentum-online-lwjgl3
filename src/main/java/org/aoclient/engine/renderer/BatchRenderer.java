package org.aoclient.engine.renderer;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

/**
 * Clase Batch Renderer <br> <br>
 *
 * Que es mas rapido? Hacer dibujos 1x1 o tener toda la info necesaria para dibujar de una y una sola vez? <br> <br>
 *
 * Esto es lo que hace el batch rendering, antes de dibujar empieza a colocar cada textura en su posicion
 * con su recorte, blend, color, etc. Para luego al momento de dibujar solo tenga que hacerlo sabiendo donde se encuentra
 * cada textura y como tiene que estar.
 */
public class BatchRenderer {
    private Texture currentTexture = null;
    private boolean drawing = false;
    private boolean currentBlend = false;

    private static final int MAX_QUADS = 10000;
    private static final int VERTICES_PER_QUAD = 6;
    private static final int FLOATS_PER_VERTEX = 8; // la concha de tu madre.

    private int vao, vbo;
    private FloatBuffer buffer;
    private int vertexCount;

    public BatchRenderer() {
        buffer = MemoryUtil.memAllocFloat(
                MAX_QUADS * VERTICES_PER_QUAD * FLOATS_PER_VERTEX
        );

        vao = glGenVertexArrays();
        vbo = glGenBuffers();

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        glBufferData(
                GL_ARRAY_BUFFER,
                buffer.capacity() * Float.BYTES,
                GL_DYNAMIC_DRAW
        );

        int stride = FLOATS_PER_VERTEX * Float.BYTES;

        // position
        glVertexAttribPointer(0, 2, GL_FLOAT, false, stride, 0);
        glEnableVertexAttribArray(0);

        // uv
        glVertexAttribPointer(1, 2, GL_FLOAT, false, stride, 2L * Float.BYTES);
        glEnableVertexAttribArray(1);

        // color
        glVertexAttribPointer(
                2,
                4,
                GL_FLOAT,
                false,
                stride,
                4L * Float.BYTES
        );
        glEnableVertexAttribArray(2);

        glBindVertexArray(0);
    }

    // =========================
    // FRAME
    // =========================

    public void begin() {
        buffer.clear();
        vertexCount = 0;
        currentTexture = null;
        currentBlend = false;
        drawing = true;
    }

    // =========================
    // DRAW
    // =========================

    public void submitQuad(
            Texture texture,
            float x, float y,
            float w, float h,
            float u0, float v0,
            float u1, float v1,
            boolean blend,
            float r, float g, float b, float a
    ) {
        if (!drawing) {
            throw new IllegalStateException("Batch no iniciado");
        }

        if (texture == null ||  texture.getId() == 0) {
            return;
        }

        if (currentBlend != blend) {
            flush();
            currentBlend = blend;

            if (blend) {
                glBlendFunc(GL_SRC_ALPHA, GL_ONE);
            } else {
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            }
        }

        if (currentTexture != texture) {
            flush();
            currentTexture = texture;
            currentTexture.bind();
        }

        push(x, y,         u0, v1, r, g, b, a);
        push(x + w, y,     u1, v1, r, g, b, a);
        push(x + w, y + h, u1, v0, r, g, b, a);

        push(x, y + h,     u0, v0, r, g, b, a);
        push(x, y,         u0, v1, r, g, b, a);
        push(x + w, y + h, u1, v0, r, g, b, a);
    }

    private void flush() {
        if (vertexCount == 0) return;

        buffer.flip();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);

        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, vertexCount);
        glBindVertexArray(0);

        buffer.clear();
        vertexCount = 0;
    }


    private void push(
            float x, float y,
            float u, float v,
            float r, float g, float b, float a
    ) {
        buffer.put(x).put(y);
        buffer.put(u).put(v);
        buffer.put(r).put(g).put(b).put(a);
        vertexCount++;
    }

    // =========================
    // UPLOAD + RENDER
    // =========================

    public void end() {
        if (!drawing) return;
        flush();
        drawing = false;
        currentTexture = null;
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public void render() {
        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, vertexCount);
        glBindVertexArray(0);
    }

    // =========================
    // CLEANUP
    // =========================

    public void dispose() {
        MemoryUtil.memFree(buffer);
        glDeleteBuffers(vbo);
        glDeleteVertexArrays(vao);
    }
}