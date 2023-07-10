package com.vuzz.forgestory.common.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class ClientPacket {

    public CompoundTag nbt;
    public int uuid;

    public ClientPacket(CompoundTag nbt, int uuid) { this.nbt = nbt; this.uuid = uuid; }
    public ClientPacket(FriendlyByteBuf buffer) { this.nbt = buffer.readNbt(); this.uuid = buffer.readInt(); }
    public void encode(FriendlyByteBuf buffer) { buffer.writeNbt(nbt); buffer.writeInt(uuid); }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) return;
            Entity entity = mc.level.getEntity(uuid);
            if(entity == null) return;
            String[] nbtKeys = nbt.getAllKeys().toArray(new String[0]);
            for(String key : nbtKeys) {
                entity.getPersistentData().put(key, Objects.requireNonNull(nbt.get(key)));
            }
        });
        context.get().setPacketHandled(true);
    }
    
}