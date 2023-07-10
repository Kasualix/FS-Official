package com.vuzz.forgestory.common.networking;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.PacketDistributor;

import java.util.Set;
import java.util.UUID;

public class NBTBank {

    public enum Type {
        STRING,
        DOUBLE,
        INT,
        UUID
    }

    private final CompoundTag clientData = new CompoundTag();

    public void postOnClient(String nbt, Object val, Type type) {
        switch (type) {
            case STRING -> clientData.putString(nbt, (String) val);
            case DOUBLE -> clientData.putDouble(nbt, (double) val);
            case INT -> clientData.putInt(nbt, (int) val);
            case UUID -> clientData.putUUID(nbt, (UUID) val);
        }
    }

    public void flush(Entity entity) {
        Set<String> keys = clientData.getAllKeys();
        if(keys.size() == 0) return;
        String[] clientKeys = keys.toArray(new String[0]);
        ClientPacket clientPacket = new ClientPacket(clientData,entity.getId());
        Networking.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity),clientPacket);
        for(String cKey : clientKeys) clientData.remove(cKey);
    }
}
