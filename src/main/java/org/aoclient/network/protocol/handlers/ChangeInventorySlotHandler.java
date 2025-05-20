package org.aoclient.network.protocol.handlers;

import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.E_ObjType;
import org.aoclient.network.PacketBuffer;

public class ChangeInventorySlotHandler implements PacketHandler {

    private final User user = User.INSTANCE;

    @Override
    public void handle(PacketBuffer buffer) {
        if (buffer.checkBytes(22)) return;
        PacketBuffer tempBuffer = new PacketBuffer();
        tempBuffer.copy(buffer);
        tempBuffer.readByte();

        int slot = tempBuffer.readByte();
        short objIndex = tempBuffer.readInteger();
        String name = tempBuffer.readCp1252String();
        int amount = tempBuffer.readInteger();
        boolean equipped = tempBuffer.readBoolean();
        short grhIndex = tempBuffer.readInteger();
        int objType = tempBuffer.readByte();
        short maxHit = tempBuffer.readInteger();
        short minHit = tempBuffer.readInteger();
        short maxDef = tempBuffer.readInteger();
        short minDef = tempBuffer.readInteger();
        float value = tempBuffer.readFloat();

        if (equipped) {
            switch (E_ObjType.values()[objType - 1]) {
                case otWeapon:
                    user.setUserWeaponEqpHit(minHit + "/" + maxHit);
                    user.setUserWeaponEqpSlot(slot);
                    break;
                case otArmor:
                    user.setUserArmourEqpDef(minDef + "/" + maxDef);
                    user.setUserArmourEqpSlot(slot);
                    break;
                case otShield:
                    user.setUserShieldEqpDef(minDef + "/" + maxDef);
                    user.setUserShieldEqpSlot(slot);
                    break;
                case otHelmet:
                    user.setUserHelmEqpDef(minDef + "/" + maxDef);
                    user.setUserHelmEqpSlot(slot);
                    break;
            }
        } else {
            if (slot == user.getUserWeaponEqpSlot()) {
                user.setUserWeaponEqpHit("0/0");
                user.setUserWeaponEqpSlot((byte) 0);

            } else if (slot == user.getUserArmourEqpSlot()) {
                user.setUserArmourEqpDef("0/0");
                user.setUserArmourEqpSlot((byte) 0);

            } else if (slot == user.getUserShieldEqpSlot()) {
                user.setUserShieldEqpDef("0/0");
                user.setUserShieldEqpSlot((byte) 0);

            } else if (slot == user.getUserHelmEqpSlot()) {
                user.setUserHelmEqpDef("0/0");
                user.setUserHelmEqpSlot((byte) 0);
            }
        }

        user.getUserInventory().setItem(slot - 1, objIndex, amount, equipped, grhIndex, objType, maxHit, minHit, maxDef, minDef, value, name);

        buffer.copy(tempBuffer);
    }

}
