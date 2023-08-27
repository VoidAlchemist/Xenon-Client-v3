package com.xenon.modules.api;

import com.xenon.XenonClient;
import com.xenon.util.readability.Hook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.resources.IResourceManager;

import java.util.Arrays;

/**
 * {@link EntityRenderer} class accommodated for Fullbright feature.
 *
 * @author Zenon
 */
public final class XenonEntityRenderer extends EntityRenderer {

    /**
     * Creates an EntityRenderer instance and fill the lightmapColors with -1 (0xFFFFFFFF).<br>
     * If fullbright isn't on, the lightmapColors will get updated anyway, so no use checking anything in the constructor.
     * It's just that if the user starts MC with fullbright on, he won't need to update the lights value any time soon.
     *
     * @param mcIn
     * @param resourceManagerIn
     */
    @Hook("net.minecraft.client.Minecraft#startGame() -> line 580")
    public XenonEntityRenderer(Minecraft mcIn, IResourceManager resourceManagerIn) {
        super(mcIn, resourceManagerIn);

        setFullBright();
    }

    /**
     * Used to set the light map colors to white.
     */
    @Hook("com.xenon.client.gui.GuiModulesConf#initGlobalIfNeeded() -> line 284")
    public void setFullBright() {
        Arrays.fill(this.lightmapColors, -1);
    }

    @Override
    protected void updateLightmap(float partialTicks) {
        if (this.lightmapUpdateNeeded) {
            if (XenonClient.instance.settings.fullbright)
                this.lightmapTexture.updateDynamicTexture();    //we completely void light colors calculation
            else super.updateLightmap(partialTicks);
        }
    }

}