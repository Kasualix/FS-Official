package com.vuzz.forgestory;

import com.vuzz.forgestory.client.renderer.entity.NPCRenderer;
import com.vuzz.forgestory.common.config.FSCommonConfig;
import com.vuzz.forgestory.common.entity.Entities;
import com.vuzz.forgestory.common.items.ItemsFS;
import com.vuzz.forgestory.common.networking.Networking;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.GeckoLib;

@Mod(ForgeStory.MOD_ID)
public class ForgeStory {
    public static final String MOD_ID = "forgestory";

    public static final CreativeModeTab MOD_TAB = new CreativeModeTab("forgestory") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(ItemsFS.STORY_REFRESHER.get());
        }
    };

    public ForgeStory() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(Type.COMMON,FSCommonConfig.SPEC,"forgestory.toml");
        Networking.register();
        Entities.register(eventBus);
        ItemsFS.register(eventBus);
        GeckoLib.initialize();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public static void onEntityRendererRegister(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(Entities.NPC.get(), NPCRenderer::new);
    }
}