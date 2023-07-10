package com.vuzz.forgestory.common.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@SuppressWarnings("all")
public class ClientProxy implements IProxy {

    @Override
    public Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }

    @Override
    public Player getPlayer() {
        return getMinecraft().player;
    }

    @Override
    public Level getWorld() {
        return getMinecraft().level;
    }
    
}