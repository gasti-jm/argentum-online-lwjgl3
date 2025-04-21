package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

import static org.aoclient.engine.Sound.SND_DICE;
import static org.aoclient.engine.Sound.playSound;

public class DiceRollHandler implements PacketHandler {
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(6)) return;

        data.readByte();

        int fuerza = data.readByte();
        int agilidad = data.readByte();
        int inteligencia = data.readByte();
        int carisma = data.readByte();
        int constitucion = data.readByte();

        //UserAtributos(eAtributos.Fuerza) = data.ReadByte()
        //    UserAtributos(eAtributos.Agilidad) = data.ReadByte()
        //    UserAtributos(eAtributos.Inteligencia) = data.ReadByte()
        //    UserAtributos(eAtributos.Carisma) = data.ReadByte()
        //    UserAtributos(eAtributos.Constitucion) = data.ReadByte()
        //
        //    With frmCrearPersonaje
        //        .lblAtributos(eAtributos.Fuerza) = UserAtributos(eAtributos.Fuerza)
        //        .lblAtributos(eAtributos.Agilidad) = UserAtributos(eAtributos.Agilidad)
        //        .lblAtributos(eAtributos.Inteligencia) = UserAtributos(eAtributos.Inteligencia)
        //        .lblAtributos(eAtributos.Carisma) = UserAtributos(eAtributos.Carisma)
        //        .lblAtributos(eAtributos.Constitucion) = UserAtributos(eAtributos.Constitucion)
        //
        //        .UpdateStats
        //    End With

        playSound(SND_DICE);

        Logger.debug("handleDiceRoll Cargado! - FALTA TERMINAR!");
    }
}
