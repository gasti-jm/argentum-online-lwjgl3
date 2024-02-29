package org.aoclient.engine.scenes;

/**
 * Aqui es donde se van guardar ciertas variables relacionadas con el campo de vision del juego.
 *
 * Tenemos: La distancia de dibujado, el tamaño del render del frmMain, el tamaño del tile (32x32), los bordes del mapa,
 *          Despues tenemos la funcion de update que actualiza las variables de la distancia del dibujado. Esto es segun
 *          la posicion de donde este parado el usuario. Tambien sirve para MainScene para su "conectar renderizado" y
 *          por ultimo, el resto de funciones son getters y setters que pueden ser leidos en el resto del programa.
 */
public final class Camera {
    public static final int SCREEN_SIZE_X = 546;
    public static final int SCREEN_SIZE_Y = 416;
    public static final int POS_SCREEN_X = 11; // 11
    public static final int POS_SCREEN_Y = 147; // 147

    public static final int TILE_PIXEL_SIZE = 32;
    public static final int TILE_BUFFER_SIZE = 7;

    // Rango maximo de la matriz del mapa.
    public static final int XMaxMapSize = 100;
    public static final int XMinMapSize = 1;
    public static final int YMaxMapSize = 100;
    public static final int YMinMapSize = 1;

    // Tiles visibles segun la pantalla
    private int halfWindowTileWidth;
    private int halfWindowTileHeight;

    private int screenminY, screenmaxY;
    private int screenminX, screenmaxX;
    private int minY, maxY;
    private int minX, maxX;
    private int ScreenX, ScreenY;
    private int minXOffset, minYOffset;

    // limites del mapa
    public static final int minXBorder = XMinMapSize + ( (SCREEN_SIZE_X / 32) / 2);
    public static final int maxXBorder = XMaxMapSize - ( (SCREEN_SIZE_X / 32) / 2);
    public static final int minYBorder = YMinMapSize + ( (SCREEN_SIZE_Y / 32) / 2);
    public static final int maxYBorder = YMaxMapSize - ( (SCREEN_SIZE_Y / 32) / 2);

    public Camera() {

    }

    /**
     *
     * @param tileX: Posicion X donde este parado nuestro usuario.
     * @param tileY: Posicion Y donde este parado nuestro usuario.
     *
     * @desc: Esta es toda la logica que se encontraba al principio del "RenderScreen", permite actualizar la distancia
     *        de dibujado segun la posicion en la que se encuentre el usuario. Esto sirve para el recorrido de la matriz
     *        del MapData, cada uno tiene distinto rango segun la capa que se va a dibujar.
     */
    public void update(int tileX, int tileY) {
        ScreenX = 0;
        ScreenY = 0;
        minXOffset = 0;
        minYOffset = 0;

        // esto es un rango de vision segun la pantalla y donde este parado el user
        screenminY = tileY - halfWindowTileHeight;
        screenmaxY = tileY + halfWindowTileHeight;
        screenminX = tileX - halfWindowTileWidth;
        screenmaxX = tileX + halfWindowTileWidth;

        // este es el rango minimo que se va a recorrer
        // este es el rango minimo que se va a recorrer
        minY = screenminY - TILE_BUFFER_SIZE;
        maxY = screenmaxY + TILE_BUFFER_SIZE;
        minX = screenminX - TILE_BUFFER_SIZE;
        maxX = screenmaxX + TILE_BUFFER_SIZE;

        if (minY < XMinMapSize) {
            minYOffset = YMinMapSize - minY;
            minY = YMinMapSize;
        }

        if (maxY > YMaxMapSize) maxY = YMaxMapSize;

        if (minX < XMinMapSize) {
            minXOffset = XMinMapSize - minX;
            minX = XMinMapSize;
        }

        if (maxX > XMaxMapSize) maxX = XMaxMapSize;

        if (screenminY > YMinMapSize) {
            screenminY--;
        } else {
            screenminY = 1;
            ScreenY = 1;
        }

        if (screenmaxY < YMaxMapSize) screenmaxY++;

        if (screenminX > XMinMapSize) {
            screenminX--;
        } else {
            screenminX = 1;
            ScreenX = 1;
        }

        if (screenmaxX < XMaxMapSize) screenmaxX++;
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

    public int getScreenminY() {
        return screenminY;
    }

    public void setScreenminY(int screenminY) {
        this.screenminY = screenminY;
    }

    public int getScreenmaxY() {
        return screenmaxY;
    }

    public void setScreenmaxY(int screenmaxY) {
        this.screenmaxY = screenmaxY;
    }

    public int getScreenminX() {
        return screenminX;
    }

    public void setScreenminX(int screenminX) {
        this.screenminX = screenminX;
    }

    public int getScreenmaxX() {
        return screenmaxX;
    }

    public void setScreenmaxX(int screenmaxX) {
        this.screenmaxX = screenmaxX;
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public int getMinX() {
        return minX;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getScreenX() {
        return ScreenX;
    }

    public void incrementScreenX(){
        this.ScreenX++;
    }

    public void incrementScreenY(){
        this.ScreenY++;
    }

    public void setScreenX(int screenX) {
        this.ScreenX = screenX;
    }

    public int getScreenY() {
        return ScreenY;
    }

    public void setScreenY(int screenY) {
        this.ScreenY = screenY;
    }

    public int getMinXOffset() {
        return minXOffset;
    }

    public int getMinYOffset() {
        return minYOffset;
    }

}
