package org.aoclient.engine.renderer;

import org.aoclient.engine.renderer.Texture;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class BatchRenderer {

    private static class Quad {
        float x, y, width, height;
        float srcX, srcY;
        float texWidth, texHeight;
        float r, g, b, a;
        boolean blend;
        Texture texture;
    }



    private List<Quad> quads = new ArrayList<>();


    public void begin() {
        quads.clear();
    }

    public void draw(Texture texture, float x, float y, float srcX, float srcY, float width, float height, boolean blend, float alpha, RGBColor color) {
        Quad quad = new Quad();
        quad.x = x;
        quad.y = y;
        quad.width = width;
        quad.height = height;
        quad.srcX = srcX;
        quad.srcY = srcY;
        quad.texWidth = texture.getTex_width();
        quad.texHeight = texture.getTex_height();
        quad.r = color.getRed();
        quad.g = color.getGreen();
        quad.b = color.getBlue();
        quad.a = alpha;
        quad.texture = texture;
        quad.blend = blend;

        quads.add(quad);
    }

    public void end() {
        Texture lastTexture = null;

        for (Quad quad : quads) {
            if (lastTexture != quad.texture) {
                if (lastTexture != null) {
                    glEnd();
                    //if (quad.blend) glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                }

                quad.texture.bind();


                // Cambiar blendFunc solo si cambia el modo de blending
                if (quad.blend) {
                    glBlendFunc(GL_SRC_ALPHA, GL_ONE); // Additive blending
                } else {
                    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); // Normal alpha blending
                }

                glBegin(GL_QUADS);
                lastTexture = quad.texture;
            }

            float u0 = quad.srcX / quad.texWidth;
            float v0 = (quad.srcY + quad.height) / quad.texHeight;
            float u1 = (quad.srcX + quad.width) / quad.texWidth;
            float v1 = quad.srcY / quad.texHeight;

            glColor4f(quad.r, quad.g, quad.b, quad.a);

            glTexCoord2f(u0, v0); glVertex2f(quad.x, quad.y + quad.height);
            glTexCoord2f(u0, v1); glVertex2f(quad.x, quad.y);
            glTexCoord2f(u1, v1); glVertex2f(quad.x + quad.width, quad.y);
            glTexCoord2f(u1, v0); glVertex2f(quad.x + quad.width, quad.y + quad.height);
        }

        if (lastTexture != null) {
            glEnd();
        }
    }

}