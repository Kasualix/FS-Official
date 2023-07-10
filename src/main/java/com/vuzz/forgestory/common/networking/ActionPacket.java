package com.vuzz.forgestory.common.networking;

import com.vuzz.forgestory.api.plotter.story.Root;
import com.vuzz.forgestory.api.plotter.story.Story;
import com.vuzz.forgestory.api.plotter.story.data.ActionPacketData;
import com.vuzz.forgestory.api.plotter.story.instances.SceneInstance;
import com.vuzz.forgestory.common.config.FSCommonConfig;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;


public class ActionPacket {
    public boolean isKeyPressed;
    public String messageChatted;

    public ActionPacket(ActionPacketData data) {
        isKeyPressed = data.playKeyPressed;
        messageChatted = data.messageSent;
    }

    public ActionPacket(boolean isKeyPressed, String messageChatted) {
        this.isKeyPressed = isKeyPressed;
        this.messageChatted = messageChatted;
    }

    public ActionPacket(FriendlyByteBuf buffer) {
        isKeyPressed = buffer.readBoolean();
        messageChatted = buffer.readUtf();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBoolean(isKeyPressed);
        buffer.writeUtf(messageChatted);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if(context.get().getSender() == null) return;
            Player sender = context.get().getSender();
            if(FSCommonConfig.DEBUG_MODE.get())
                if (sender != null) {
                    sender.sendSystemMessage(MutableComponent.create(new LiteralContents("ActionPacket received: "+messageChatted+" | "+isKeyPressed)));
                }
            Story story = Root.getActiveStory();
                if(story == null) return;
            SceneInstance playerScene = story.getActiveSceneForPlayer(Objects.requireNonNull(sender).getUUID());
                if(playerScene == null) return;
            ActionPacketData packetData = new ActionPacketData();
                packetData.messageSent = messageChatted;
                packetData.playKeyPressed = isKeyPressed;
                packetData.scene = playerScene;
            playerScene.applyActPacket(packetData);
        });
        context.get().setPacketHandled(true);
    }
}
