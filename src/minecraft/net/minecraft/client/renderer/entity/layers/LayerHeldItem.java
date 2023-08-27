package net.minecraft.client.renderer.entity.layers;

import com.xenon.modules.api.OldAnimationsAPI;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;

public class LayerHeldItem implements LayerRenderer<EntityLivingBase> {
    public final RendererLivingEntity<?> livingEntityRenderer;

    public LayerHeldItem(RendererLivingEntity<?> livingEntityRendererIn) {
        this.livingEntityRenderer = livingEntityRendererIn;
    }

    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
        OldAnimationsAPI.handleLayerHeldItem(this, entitylivingbaseIn);
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
