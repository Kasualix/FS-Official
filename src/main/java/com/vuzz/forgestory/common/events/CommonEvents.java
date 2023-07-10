package com.vuzz.forgestory.common.events;

import com.vuzz.forgestory.FSC;
import com.vuzz.forgestory.ForgeStory;
import com.vuzz.forgestory.api.plotter.story.Root;
import com.vuzz.forgestory.api.plotter.story.data.ActionPacketData;
import com.vuzz.forgestory.client.events.ClientBusEvents;
import com.vuzz.forgestory.common.command.GeneralCommand;
import com.vuzz.forgestory.common.config.FSCommonConfig;
import com.vuzz.forgestory.common.networking.ActionPacket;
import com.vuzz.forgestory.common.networking.Networking;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = ForgeStory.MOD_ID)
public class CommonEvents {
    

    @SubscribeEvent
    public static void clientTick(ClientTickEvent event) {
        if(event.phase != Phase.START) return;
        boolean actBtnDown = ClientBusEvents.keyStory.consumeClick();
        if(actBtnDown) {
            ActionPacketData pack = new ActionPacketData();
            pack.playKeyPressed = actBtnDown;
            sendActionPacket(pack);
        }
    }

    @SubscribeEvent
    public static void onCommandsReg(RegisterCommandsEvent event) {
        GeneralCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void serverTick(ServerTickEvent event) { Root.tick(); }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void clientChat(ClientChatEvent event) {
        ActionPacketData pack = new ActionPacketData();
        pack.messageSent = event.getMessage().toLowerCase();
        event.setResult(Result.ALLOW);
        sendActionPacket(pack);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void playerJoined(PlayerLoggedInEvent event) {}

    @SubscribeEvent()
    public static void serverStarted(ServerStartedEvent event) {
        FSC.sendInformationMessage();
        Root.reloadStories();
    }

    public static void sendActionPacket(ActionPacketData pack) {
        Minecraft mc = Minecraft.getInstance();
        if(FSCommonConfig.DEBUG_MODE.get() && mc.player != null) mc.player.sendSystemMessage(MutableComponent.create(new LiteralContents("ActionPacket sent: "+pack.messageSent+" | "+pack.playKeyPressed)));
        Networking.CHANNEL.send(PacketDistributor.SERVER.noArg(), new ActionPacket(pack));
    }

}
