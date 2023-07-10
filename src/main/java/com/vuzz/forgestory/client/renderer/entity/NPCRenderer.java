package com.vuzz.forgestory.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vuzz.forgestory.client.model.entity.NPCModel;
import com.vuzz.forgestory.common.entity.NPCEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class NPCRenderer extends GeoEntityRenderer<NPCEntity> {

    public NPCRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new NPCModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(NPCEntity instance) {
        String path = instance.getPersistentData().getString("texturePath");
        return parsePath(path.equals("") ? "forgestory:textures/entity/npc" : path.toLowerCase());
    }

    public ResourceLocation parsePath(String path) {
        String modId = "forgestory";
        String name;
        if(path.contains(":")) {
            modId = path.substring(0, path.indexOf(":"));
        }
        name = path.substring(path.indexOf(":")+1);
        return new ResourceLocation(modId,name+(name.endsWith(".png") ? "" : ".png"));
    }

    @Override
    public void render(@NotNull NPCEntity entity, float entityYaw, float partialTicks, @NotNull PoseStack stack, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        try {
            stack.pushPose();
            super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
            stack.popPose();   
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
