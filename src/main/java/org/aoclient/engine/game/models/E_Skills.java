package org.aoclient.engine.game.models;

public enum E_Skills {
    Magia(1),
    Robar( 2),
    Tacticas( 3),
    Arma( 4),
    Medita( 5),
    Stab( 6),
    Ocultarse( 7),
    Supervivencia( 8),
    Talar( 9),
    Comerciar(10),
    Defensa(11),
    Pesca(12),
    Mineria(13),
    Carpinteria(14),
    Herreria(15),
    Liderazgo(16),
    Domar(17),
    Proyectiles(18),
    Wrestling(19),
    Navegacion(20);

    public static final int FundirMetal = 88;
    public final byte value;

    E_Skills(int value) {
        this.value = (byte) value;
    }
}
