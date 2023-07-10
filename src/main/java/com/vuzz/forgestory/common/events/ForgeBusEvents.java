package com.vuzz.forgestory.common.events;

import com.vuzz.forgestory.ForgeStory;
import com.vuzz.forgestory.client.overlay.FadeScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.HashMap;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ForgeStory.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
@SuppressWarnings("unused")
public class ForgeBusEvents {

    public static int testTicks = 0;

    public static HashMap<UUID,Integer> fadeScreenTimers = new HashMap<>();
    public static HashMap<UUID,Integer> fadeScreenColors = new HashMap<>();

    @SubscribeEvent//TODO: Quite not sure.
    public static void renderGameOverlay(RenderGuiOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        UUID playerUuid = mc.player.getUUID();
        FadeScreen render = new FadeScreen(mc);
        int hexColor = fadeScreenColors.getOrDefault(playerUuid, 0xFF000000);
        int playerTicks = fadeScreenTimers.getOrDefault(playerUuid, -1);
        if(playerTicks >= 0) {
            render.renderFadeScreen(event.getPoseStack(),playerTicks, hexColor);
            playerTicks--;
            fadeScreenTimers.put(playerUuid, playerTicks);
        }

        /*if(event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            FadeScreen render = new FadeScreen(mc);
        }*/
    }
}
