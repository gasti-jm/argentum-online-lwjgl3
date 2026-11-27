package org.aoclient.engine.game.intervals;


/**
 * Aca nos encargamos de crear todos los timers del juego y utilizarlos cuando sea necesario.
 */
public enum Timers {
    INT_MACRO_HECHIS(new IntervalTimer(2.5f)),
    INT_MACRO_TRABAJO(new IntervalTimer(0.9f)),
    INT_ATTACK(new IntervalTimer(1.5f)),
    INT_ARROWS(new IntervalTimer(1.4f)),
    INT_CAST_SPELL(new IntervalTimer(1.4f)),
    INT_CAST_ATTACK(new IntervalTimer(1.0f)),
    INT_WORK(new IntervalTimer(0.7f)),
    INT_USEITEMDCK(new IntervalTimer(0.125f)),
    INT_SENTRPU(new IntervalTimer(2.0f)),
    INT_EQUIP_ITEM(new IntervalTimer(0.15f)),
    INT_USE_ITEM(new IntervalTimer(0.240f));

    private IntervalTimer t;

    Timers(IntervalTimer t) {
        this.t = t;
    }

    public IntervalTimer getTimer() {
        return t;
    }

}
