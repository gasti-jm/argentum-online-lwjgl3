package org.aoclient.engine.scenes;

public final class Camera {
    public static final int TILE_PIXEL_SIZE = 32;
    public static final int TILE_BUFFER_SIZE = 9;

    // Rango maximo de la matriz del mapa.
    public static final int XMaxMapSize = 99;
    public static final int XMinMapSize = 0;
    public static final int YMaxMapSize = 99;
    public static final int YMinMapSize = 0;

    // Tiles visibles segun la pantalla
    private int halfWindowTileWidth;
    private int halfWindowTileHeight;

    int screenminY, screenmaxY;
    int screenminX, screenmaxX;
    int minY, maxY;
    int minX, maxX;
    int ScreenX = 0, ScreenY = 0;
    int minXOffset = 0, minYOffset = 0;

    public Camera() {

    }

    public void update() {

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
