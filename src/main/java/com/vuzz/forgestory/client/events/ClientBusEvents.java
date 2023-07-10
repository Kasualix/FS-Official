package com.vuzz.forgestory.client.events;

import com.mojang.blaze3d.platform.InputConstants;
import com.vuzz.forgestory.ForgeStory;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = ForgeStory.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientBusEvents {
    
    public static KeyMapping keyStory;

    @SubscribeEvent
    public static void register(final RegisterKeyMappingsEvent event) {
        keyStory = new KeyMapping("key.forgestory.play_act", InputConstants.Type.KEYSYM, 72, "keylist.forgestory");
        event.register(keyStory);
    }



}
