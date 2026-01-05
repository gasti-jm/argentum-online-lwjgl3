package org.aoclient.engine.renderer;

import org.joml.Matrix4f;
import org.joml.Vector2f;

/**
 * la verdadera, pero AO tiene su propia hardcodeada xd.
 */
public class Camera2D {
    private Vector2f position;

    public Camera2D(float x, float y) {
        position = new Vector2f(x, y);
    }

    public Matrix4f getViewMatrix() {
        // La cámara mueve el mundo al revés
        return new Matrix4f()
                .translate(-position.x, -position.y, 0);
    }

    public void move(float dx, float dy) {
        position.add(dx, dy);
    }

    public Vector2f getPosition() {
        return position;
    }

}
