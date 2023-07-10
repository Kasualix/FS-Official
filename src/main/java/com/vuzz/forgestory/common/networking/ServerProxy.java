package com.vuzz.forgestory.common.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ServerProxy implements IProxy {

    @Override
    public Minecraft getMinecraft() {
        return null;
    }

    @Override
    public Player getPlayer() {
        return null;
    }

    @Override
    public Level getWorld() {
        return null;
    }
    
}