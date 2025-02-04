package com.vuzz.forgestory.common.networking;

import com.vuzz.forgestory.common.events.ForgeBusEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class FadeScreenPacket {
    
    public UUID uuid;
    public int ticks;
    public int color;

    public FadeScreenPacket(UUID uuid, int ticks, int color) { this.ticks = ticks; this.uuid = uuid; this.color = color; }
    public FadeScreenPacket(FriendlyByteBuf buffer) { this.uuid = buffer.readUUID(); this.ticks = buffer.readInt(); this.color = buffer.readInt(); }
    public void encode(FriendlyByteBuf buffer) { buffer.writeUUID(uuid); buffer.writeInt(ticks); buffer.writeInt(color); }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ForgeBusEvents.fadeScreenTimers.put(uuid, ticks);
            ForgeBusEvents.fadeScreenColors.put(uuid, color);
        });
        context.get().setPacketHandled(true);
    }

}
