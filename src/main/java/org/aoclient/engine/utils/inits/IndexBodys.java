package org.aoclient.engine.utils.inits;

/**
 * <p>
 * Simula el "Type" o la estructura de {@code IndexBodys}, utilizada para almacenar los indices graficos de los cuerpos de los
 * personajes.
 * <p>
 * Esta clase gestiona los diferentes indices graficos correspondientes a los cuerpos de los personajes, organizados segun la
 * orientacion o direccion del movimiento, asi como los desplazamientos necesarios para posicionar correctamente la cabeza sobre
 * el cuerpo.
 * <p>
 * Es utilizada durante la carga de datos de personajes para mapear correctamente los recursos graficos de los cuerpos y calcular
 * la posicion adecuada de la cabeza sobre estos.
 *
 * @see org.aoclient.engine.utils.inits.BodyData
 * @see org.aoclient.engine.utils.GameData
 */

public final class IndexBodys {

    private short[] body = new short[5];
    private short headOffsetX;
    private short headOffsetY;

    public short getBody(int index) {
        return body[index];
    }

    public void setBody(int index, short body) {
        this.body[index] = body;
    }

    public short getHeadOffsetX() {
        return headOffsetX;
    }

    public void setHeadOffsetX(short headOffsetX) {
        this.headOffsetX = headOffsetX;
    }

    public short getHeadOffsetY() {
        return headOffsetY;
    }

    public void setHeadOffsetY(short headOffsetY) {
        this.headOffsetY = headOffsetY;
    }

}
