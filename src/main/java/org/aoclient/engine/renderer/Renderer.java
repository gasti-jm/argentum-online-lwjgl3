package org.aoclient.engine.renderer;

import org.aoclient.engine.Window;
import org.aoclient.engine.scenes.Scene;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Renderer {
    private Camera2D camera;
    private int vao, vbo, ebo;
    private Shader shader;
    private Matrix4f projection;

    private BatchRenderer batch = new BatchRenderer();

    public Renderer() {
        this.camera = new Camera2D(0,0); // y no se toca mas.


        // shaders vitales para nuestro dibujado.
        shader = new Shader(
                "resources/shaders/default.vert",
                "resources/shaders/default.frag"
        );

        projection = new Matrix4f().ortho2D(0, Window.SCREEN_WIDTH, Window.SCREEN_HEIGHT, 0);


        float[] vertices = {
                // x, y,   u, v
                0f,   0f,   0f, 0f,
                32f,  0f,   1f, 0f,
                32f, 32f,   1f, 1f,
                0f,  32f,   0f, 1f
        };

        int[] indices = {
                0, 1, 2,
                2, 3, 0
        };

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        // VBO
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        FloatBuffer vertexBuffer = MemoryUtil.memAllocFloat(vertices.length);
        vertexBuffer.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // EBO
        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);

        IntBuffer indexBuffer = MemoryUtil.memAllocInt(indices.length);
        indexBuffer.put(indices).flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

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

        MemoryUtil.memFree(vertexBuffer);
        MemoryUtil.memFree(indexBuffer);
    }

    public void draw(
            Texture tex,
            float x, float y,           // posición en pantalla
            float srcX, float srcY,     // recorte: posición en textura
            float srcW, float srcH,     // recorte: tamaño en textura
            boolean blend,
            float alpha,
            RGBColor color
    ) {

        float u0 = srcX / tex.getTex_width();
        float u1 = (srcX + srcW) / tex.getTex_width();
        float v1 =   (srcY / tex.getTex_height());
        float v0 =   ((srcY + srcH) / tex.getTex_height());

        batch.submitQuad(tex,
                x, y,
                srcW, srcH,
                u0, v0, u1, v1,
                blend,
                color.getRed(), color.getGreen(), color.getBlue(), alpha
        );
    }



    public void render(Scene scene) {
        shader.bind();

        // TEXTURE
        int texLoc = glGetUniformLocation(shader.getId(), "uTexture");
        glUniform1i(texLoc, 0);

        // PROJECTION
        int projLoc = glGetUniformLocation(shader.getId(), "uProjection");
        FloatBuffer projBuffer = MemoryUtil.memAllocFloat(16);
        projection.get(projBuffer);
        glUniformMatrix4fv(projLoc, false, projBuffer);

        glBindVertexArray(vao);
        int modelLoc = glGetUniformLocation(shader.getId(), "uModel");

        int viewLoc = glGetUniformLocation(shader.getId(), "uView");
        FloatBuffer viewBuffer = MemoryUtil.memAllocFloat(16);
        camera.getViewMatrix().get(viewBuffer);
        glUniformMatrix4fv(viewLoc, false, viewBuffer);


        batch.begin();

        // dibujamos todo aca.
        scene.render();

        batch.end();
        batch.render();


        glBindVertexArray(0);
        shader.unbind();
        MemoryUtil.memFree(projBuffer);
    }

}
