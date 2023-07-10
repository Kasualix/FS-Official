package com.vuzz.forgestory.common.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface IProxy {
    Minecraft getMinecraft();
    Player getPlayer();
    Level getWorld();
}
