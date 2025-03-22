package org.aoclient.engine.utils.inits;

/**
 * Simula el "Type" o la estructura de {@code Obj}, representando los objetos y sus propiedades basicas.
 * <p>
 * Esta clase implementa la estructura de datos necesaria para gestionar objetos dentro del juego, manteniendo informacion sobre
 * su tipo y cantidad.
 * <p>
 * Es utilizada principalmente por la clase {@code MapData} para representar los objetos presentes en cada celda del mapa,
 * permitiendo su correcta visualizacion e interaccion en el juego.
 *
 * @see MapData
 */

public final class Obj {

    private int objIndex;
    private int amount;

    public int getObjIndex() {
        return objIndex;
    }

    public void setObjIndex(int objIndex) {
        this.objIndex = objIndex;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
