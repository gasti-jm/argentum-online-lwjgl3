package org.aoclient.engine.utils.inits;

/**
 * Simula el "Type" o la estructura de Grh.
 */
public final class GrhInfo {
    private short grhIndex;
    private float frameCounter;
    private float speed;
    private boolean started;
    private int loops;
    private float angle;

    public GrhInfo() {
        this.started         = false;
        this.grhIndex        = 0;
        this.frameCounter    = 1.0f;
        this.loops           = 0;
        this.speed           = 0.0f;
        this.angle           = 0.0f;
    }

    public GrhInfo(GrhInfo other) {
        this.grhIndex        = other.grhIndex;
        this.frameCounter    = other.frameCounter;
        this.speed           = other.speed;
        this.started         = other.started;
        this.loops           = other.loops;
        this.angle           = other.angle;
    }

    public short getGrhIndex() {
        return grhIndex;
    }

    public void setGrhIndex(int grhIndex) {
        this.grhIndex = (short) grhIndex;
    }

    public float getFrameCounter() {
        return frameCounter;
    }

    public void setFrameCounter(float frameCounter) {
        this.frameCounter = frameCounter;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public int getLoops() {
        return loops;
    }

    public void setLoops(int loops) {
        this.loops = loops;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
