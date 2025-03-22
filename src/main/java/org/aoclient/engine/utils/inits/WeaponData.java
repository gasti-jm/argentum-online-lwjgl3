package org.aoclient.engine.utils.inits;

/**
 * <p>
 * Encapsula la informacion grafica necesaria para representar un arma equipada por un personaje. Almacena especificamente las
 * distintas animaciones que se muestran cuando el personaje camina en las cuatro direcciones posibles mientras lleva el arma.
 * <p>
 * Esta clase es esencial para el sistema de renderizado de personajes, ya que permite que las armas se visualicen correctamente
 * superpuestas sobre el cuerpo del personaje y se animen de forma sincronizada con sus movimientos.
 * <p>
 * Los datos de armas se cargan desde archivos de inicializacion durante el arranque del juego a traves del sistema
 * {@code GameData}, especificamente mediante el metodo {@code loadWeapons()} que lee los datos del archivo {@code weapons.ind}.
 * <p>
 * Cada arma tiene asociada una instancia de esta clase, que define su apariencia visual cuando es equipada por un personaje.
 *
 * @see GrhInfo
 * @see Character
 * @see org.aoclient.engine.utils.GameData GameData
 */

public final class WeaponData {

    private final GrhInfo[] WeaponWalk = new GrhInfo[5];

    public WeaponData() {
        WeaponWalk[1] = new GrhInfo();
        WeaponWalk[2] = new GrhInfo();
        WeaponWalk[3] = new GrhInfo();
        WeaponWalk[4] = new GrhInfo();
    }

    public WeaponData(WeaponData other) {
        WeaponWalk[1] = new GrhInfo(other.WeaponWalk[1]);
        WeaponWalk[2] = new GrhInfo(other.WeaponWalk[2]);
        WeaponWalk[3] = new GrhInfo(other.WeaponWalk[3]);
        WeaponWalk[4] = new GrhInfo(other.WeaponWalk[4]);
    }

    public GrhInfo getWeaponWalk(int index) {
        return WeaponWalk[index];
    }

    public void setWeaponWalk(int index, GrhInfo weaponWalk) {
        WeaponWalk[index] = weaponWalk;
    }

}
