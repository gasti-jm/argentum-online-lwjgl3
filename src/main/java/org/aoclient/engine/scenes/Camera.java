package org.aoclient.engine.scenes;

public class Camera {
    public static final int TILE_PIXEL_SIZE = 32;
    public static final int TileBufferSize = 9;

    public static final int XMaxMapSize = 99;
    public static final int XMinMapSize = 0;
    public static final int YMaxMapSize = 99;
    public static final int YMinMapSize = 0;

    // Tiles visibles segun la pantalla
    private int halfWindowTileWidth;
    private int halfWindowTileHeight;

    // tiles a dibujar fuera de la pantalla


    public Camera() {

    }

    public int getHalfWindowTileWidth() {
        return halfWindowTileWidth;
    }

    public void setHalfWindowTileWidth(int halfWindowTileWidth) {
        this.halfWindowTileWidth = halfWindowTileWidth;
    }

    public int getHalfWindowTileHeight() {
        return halfWindowTileHeight;
    }

    public void setHalfWindowTileHeight(int halfWindowTileHeight) {
        this.halfWindowTileHeight = halfWindowTileHeight;
    }
}
