package org.aoclient.engine.utils.inits;

/**
 * <p>
 * Simula el "Type" o la estructura de {@code ShieldData}, encargandose de gestionar la informacion grafica de los escudos de los
 * personajes.
 * <p>
 * Esta clase almacena la informacion necesaria para representar visualmente los escudos equipados por los personajes, manteniendo
 * referencias a los graficos correspondientes para cada direccion de movimiento posible.
 * <p>
 * Es utilizada por la clase Character para renderizar adecuadamente  el escudo del personaje mientras se desplaza por el mapa.
 *
 * @see GrhInfo
 * @see Character
 */

public final class ShieldData {

    private final GrhInfo[] shieldWalk = new GrhInfo[5];

    public ShieldData() {
        shieldWalk[1] = new GrhInfo();
        shieldWalk[2] = new GrhInfo();
        shieldWalk[3] = new GrhInfo();
        shieldWalk[4] = new GrhInfo();
    }

    public ShieldData(ShieldData other) {
        shieldWalk[1] = new GrhInfo(other.shieldWalk[1]);
        shieldWalk[2] = new GrhInfo(other.shieldWalk[2]);
        shieldWalk[3] = new GrhInfo(other.shieldWalk[3]);
        shieldWalk[4] = new GrhInfo(other.shieldWalk[4]);
    }

    public GrhInfo getShieldWalk(int index) {
        return shieldWalk[index];
    }

    public void setShieldWalk(int index, GrhInfo shieldWalk) {
        this.shieldWalk[index] = shieldWalk;
    }

}
