package com.vuzz.forgestory.common.networking;

import com.vuzz.forgestory.ForgeStory;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Networking {
    public static SimpleChannel CHANNEL;
    public static int ID;

    public static int nextID(){return ID++;}

    public static void register() {
        CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(ForgeStory.MOD_ID, "network"), () -> "1.0", s -> true, s -> true);
        CHANNEL.registerMessage(nextID(), ActionPacket.class, ActionPacket::encode, ActionPacket::new, ActionPacket::handle);
        CHANNEL.registerMessage(nextID(), ClientPacket.class, ClientPacket::encode, ClientPacket::new, ClientPacket::handle);
        CHANNEL.registerMessage(nextID(), FadeScreenPacket.class, FadeScreenPacket::encode, FadeScreenPacket::new, FadeScreenPacket::handle);
    }

}