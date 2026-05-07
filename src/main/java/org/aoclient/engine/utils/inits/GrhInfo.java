package org.aoclient.engine.utils.inits;

import org.aoclient.engine.renderer.TextureManager;

import static org.aoclient.engine.utils.GameData.grhData;
import static org.aoclient.engine.utils.Time.deltaTime;

/**
 * Simula el "Type" o la estructura de {@code Grh}, gestionando la informacion basica de los graficos y animaciones.
 * <p>
 * Esta clase almacena los datos necesarios para representar y controlar los graficos (Grh), tanto estaticos como animados,
 * manteniendo su estado y propiedades de animacion.
 *
 * @see GrhData
 * @see org.aoclient.engine.renderer.Drawn
 */

public final class GrhInfo {

    private short grhIndex;
    private float frameCounter;
    private float speed;
    private boolean started;
    private int loops;
    private float angle;

    public GrhInfo() {
        this.started = false;
        this.grhIndex = 0;
        this.frameCounter = 1.0f;
        this.loops = 0;
        this.speed = 0.0f;
        this.angle = 0.0f;
    }

    public GrhInfo(GrhInfo other) {
        this.grhIndex = other.grhIndex;
        this.frameCounter = other.frameCounter;
        this.speed = other.speed;
        this.started = other.started;
        this.loops = other.loops;
        this.angle = other.angle;
    }

    /**
     * Inicializa los graficos, ya sean animaciones o no.
     */
    public static GrhInfo initGrh(GrhInfo grh, short grhIndex, boolean started) {
        if (grh == null) throw new NullPointerException("Se esta intentando incializar un GrhInfo nulo...");

        grh.setGrhIndex(grhIndex);
        grh.setStarted(false);
        grh.setLoops(0);

        if (started) grh.setStarted(grhData[grh.getGrhIndex()].getNumFrames() > 1);

        if (grh.isStarted()) grh.setLoops(-1);

        grh.setFrameCounter(1);
        //grh.setSpeed( grhData[grhIndex].getSpeed() );
        grh.setSpeed(0.4f);

        // precarga de textura.
        TextureManager.requestTexture(grhIndex);

        return grh;
    }

    /**
     * Actualiza las animaciones, cambia a su siguiente frame según la velocidad de animacion.
     */
    public void updateAnim() {
        if (started) {
            frameCounter += (deltaTime * grhData[grhIndex].getNumFrames() / speed);
            if (frameCounter > grhData[grhIndex].getNumFrames()) {
                frameCounter = (frameCounter % grhData[grhIndex].getNumFrames()) + 1;

                if (loops != -1) {
                    if (loops > 0) loops--;
                    else started = false;
                }
            }
        }
    }

    /**
     * @return Indice de grafico actual en animacion.
     */
    public int getCurrentIndex() {
        return grhData[grhIndex].getFrame((int) (frameCounter));
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
