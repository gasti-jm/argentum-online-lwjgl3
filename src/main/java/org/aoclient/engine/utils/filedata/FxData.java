package org.aoclient.engine.utils.filedata;

public class FxData {
    private short Animacion;
    private short OffsetX;
    private short OffsetY;

    public void setAnimacion(short animacion) {
        Animacion = animacion;
    }

    public short getAnimacion() {
        return Animacion;
    }

    public short getOffsetX() {
        return OffsetX;
    }

    public void setOffsetX(short offsetX) {
        OffsetX = offsetX;
    }

    public short getOffsetY() {
        return OffsetY;
    }

    public void setOffsetY(short offsetY) {
        OffsetY = offsetY;
    }
}
