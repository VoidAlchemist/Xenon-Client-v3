package net.minecraft.client.renderer.entity.layers;

import com.xenon.client.cape.CapeManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;

public class LayerCape implements LayerRenderer<AbstractClientPlayer> {
    private final RenderPlayer playerRenderer;

    public LayerCape(RenderPlayer playerRendererIn) {
        this.playerRenderer = playerRendererIn;
    }

    public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount,
                              float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        CapeManager.doRenderCape(playerRenderer, entitylivingbaseIn, partialTicks, scale);
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
