package org.aoclient.network.protocol.handlers;

import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;
import org.tinylog.Logger;

public class FameHandler implements PacketHandler {
    @Override
    public void handle(ByteQueue data) {

        if (data.checkPacketData(29)) return;

        data.readByte();

        data.readLong();
        data.readLong();
        data.readLong();
        data.readLong();
        data.readLong();
        data.readLong();
        data.readLong();


        //With UserReputacion
        //        .AsesinoRep = data.ReadLong()
        //        .BandidoRep = data.ReadLong()
        //        .BurguesRep = data.ReadLong()
        //        .LadronesRep = data.ReadLong()
        //        .NobleRep = data.ReadLong()
        //        .PlebeRep = data.ReadLong()
        //        .Promedio = data.ReadLong()
        //    End With
        // LlegoFama = True

        Logger.debug("handleFame Cargado! - FALTA TERMINAR!");
    }
}
