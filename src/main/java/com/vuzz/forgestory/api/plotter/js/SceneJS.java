package com.vuzz.forgestory.api.plotter.js;

import com.vuzz.forgestory.annotations.Documentate;
import com.vuzz.forgestory.api.plotter.js.ApiJS.CameraMode;
import com.vuzz.forgestory.api.plotter.js.ApiJS.NpcBuilder;
import com.vuzz.forgestory.api.plotter.story.Action;
import com.vuzz.forgestory.api.plotter.story.ActionEvent;
import com.vuzz.forgestory.api.plotter.story.data.ActionPacketData;
import com.vuzz.forgestory.api.plotter.story.instances.SceneInstance;
import com.vuzz.forgestory.common.entity.Entities;
import com.vuzz.forgestory.common.entity.NPCEntity;
import com.vuzz.forgestory.common.networking.FadeScreenPacket;
import com.vuzz.forgestory.common.networking.Networking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.function.Consumer;


public class SceneJS implements JSResource {

    private final SceneInstance sceneInstance;

    private final HashMap<String,NpcJS> localNpcs = new HashMap<>();

    public SceneJS(SceneInstance scInstance) {
        sceneInstance = scInstance;
    }

    @Documentate(desc = "Ends the scene")
    public void endScene() { sceneInstance.getScene().story.endScene(sceneInstance.getPlayer()); }

    @Documentate(desc = "Adds action to the scene.")
    public SceneJS addAction(Consumer<ActionPacketData> cb, ActionEvent ev) {
        Action act = new Action(cb,ev);
        sceneInstance.actsJs.add(act);
        return this;
    }

    @Documentate(desc = "Creates a smooth appearing colored rectangle that lasts for n ticks.")
    public void showFadeScreen(int time, String colorString) {
        int color = (int) Long.parseLong(colorString,16);
        FadeScreenPacket packet = new FadeScreenPacket(sceneInstance.getPlayer().getUUID(),time,color);
        Networking.CHANNEL.send(PacketDistributor.ALL.noArg(),packet);
    }

    @Documentate(desc = "Enters a cutscene.")
    public void enterCutscene(CameraMode camMode) {
        sceneInstance.inCutscene = true;
        sceneInstance.cutsceneCam = camMode;
    }

    @Documentate(desc = "Exits a cutscene.")
    public void exitCutscene() {
        sceneInstance.inCutscene = false;
        sceneInstance.getPlayer().setGameMode(GameType.SURVIVAL);
    }

    @Documentate(desc = "Creates a smooth appearing black rectangle that lasts for n ticks.")
    public void showFadeScreen(int time) { showFadeScreen(time,"FF000000"); }

    @Documentate(desc = "Creates an npc.")
    public void createNpc(Level world, NpcBuilder npc, double[] pos) {
        EntityType<NPCEntity> npcReg = Entities.NPC.get();
        NPCEntity npcEntity = (NPCEntity) npcReg.spawn((ServerLevel) world, ItemStack.EMPTY, null, new BlockPos(pos[0],pos[1],pos[2]),
            MobSpawnType.EVENT, false, false);
        npcEntity.setTexturePath(npc.texturePath);
        npcEntity.setModelPath(npc.modelPath);
        npcEntity.setAnimationPath(npc.animationPath);
        npcEntity.focusedEntity = sceneInstance.getPlayer();
        npcEntity.setGoTo(pos[0], pos[1], pos[2], 1D);
        npcEntity.getPersistentData().putBoolean("immortal", true);
        NpcJS npcJS = new NpcJS(npcEntity);
        localNpcs.put(npc.id, npcJS);
    }

    @Documentate(desc = "Creates an npc.")
    public void createNpc(WorldJS world, NpcBuilder npc, double[] pos)  {
        createNpc((Level) world.getNative(),npc,pos);                           }

    @Documentate(desc = "Gets npc by its id.")
    public NpcJS getNpc(String id) {
        return localNpcs.get(id);
    }


    @Override public Object getNative() { return sceneInstance; }
    @Override public String getResourceId() { return "scene"; }
    @Override public boolean isClient() { return true; }

}