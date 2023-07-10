package com.vuzz.forgestory.client.overlay;

import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.gui.overlay.ForgeGui;

public class FadeScreen extends ForgeGui {

    public FadeScreen(Minecraft mc) {
        super(mc);
    }

    @SuppressWarnings("all")
    public void renderFadeScreen(PoseStack stack, int ticksPassed, int col) {
        Minecraft mc = Minecraft.getInstance();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
        
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		//RenderSystem.disableAlphaTest();TODO: Quite not sure.

        float radVal = (float) Math.toRadians(Math.min(ticksPassed,10) * 9);

        fill(stack, 0, 0, screenWidth, (int) (screenHeight*(Math.sin(radVal)+1)/2), col);
    }
}
