package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.E_ObjType;
import org.aoclient.network.ByteQueue;
import org.aoclient.network.protocol.PacketHandler;

public class ChangeInventorySlotHandler implements PacketHandler {
    
    @Override
    public void handle(ByteQueue data) {
        if (data.checkPacketData(22)) return;

        ByteQueue buffer = new ByteQueue();
        buffer.copyBuffer(data);

        // Remove packet
        buffer.readByte();

        final int slot = buffer.readByte();
        final short objIndex = buffer.readInteger();
        final String name = buffer.readASCIIString();
        final int amount = buffer.readInteger();
        final boolean equipped = buffer.readBoolean();
        final short grhIndex = buffer.readInteger();
        final int objType = buffer.readByte();
        final short maxHit = buffer.readInteger();
        final short minHit = buffer.readInteger();
        final short maxDef = buffer.readInteger();
        final short minDef = buffer.readInteger();
        final float value = buffer.readFloat();

        if (equipped) {
            switch (E_ObjType.values()[objType - 1]) {
                case otWeapon:
                    User.get().setUserWeaponEqpHit(minHit + "/" + maxHit);
                    User.get().setUserWeaponEqpSlot(slot);
                    break;
                case otArmor:
                    User.get().setUserArmourEqpDef(minDef + "/" + maxDef);
                    User.get().setUserArmourEqpSlot(slot);
                    break;
                case otShield:
                    User.get().setUserShieldEqpDef(minDef + "/" + maxDef);
                    User.get().setUserShieldEqpSlot(slot);
                    break;
                case otHelmet:
                    User.get().setUserHelmEqpDef(minDef + "/" + maxDef);
                    User.get().setUserHelmEqpSlot(slot);
                    break;
            }
        } else {
            if (slot == User.get().getUserWeaponEqpSlot()) {
                User.get().setUserWeaponEqpHit("0/0");
                User.get().setUserWeaponEqpSlot((byte) 0);

            } else if (slot == User.get().getUserArmourEqpSlot()) {
                User.get().setUserArmourEqpDef("0/0");
                User.get().setUserArmourEqpSlot((byte) 0);

            } else if (slot == User.get().getUserShieldEqpSlot()) {
                User.get().setUserShieldEqpDef("0/0");
                User.get().setUserShieldEqpSlot((byte) 0);

            } else if (slot == User.get().getUserHelmEqpSlot()) {
                User.get().setUserHelmEqpDef("0/0");
                User.get().setUserHelmEqpSlot((byte) 0);
            }
        }

        User.get().getUserInventory().setItem(slot - 1, objIndex, amount, equipped, grhIndex, objType,
                maxHit, minHit, maxDef, minDef, value, name);

        data.copyBuffer(buffer);
    }
    
}
