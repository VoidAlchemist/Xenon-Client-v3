package com.xenon.modules.api;

import com.xenon.XenonClient;
import com.xenon.modules.api.dungeons.DungeonAPI;
import com.xenon.util.readability.Hook;
import static net.minecraft.client.renderer.GlStateManager.*;
import static org.lwjgl.opengl.GL11.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.*;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.BufferUtils;


import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlowingAPI {

    /**
     * Whether we can use outlining shaders or not. If they are not, the glowing effect will entirely cover the entity
     * , just like in pre-snapshot versions of 1.9.<br><br>
     *
     * Affected once, value: RenderGlobal.entityOutlineShader != null && RenderGlobal.entityOutlineFramebuffer != null
     */
    public static boolean outlineShaderSupported;
    private static final List<Entity> glowingEntities = new ArrayList<>();
    private static boolean outlineFrameBufferDirty;

    private static GlowingDraw actualDrawFunction;

    /**
     * Inits Xenon's Glowing API, caching whether outlining shader is supported and dispatching actual draw code accordingly.
     * @param outlineShaderSupported whether RenderGlobal managed to create the outlining shader
     */
    @Hook("net.minecraft.client.renderer.RenderGlobal#makeEntityOutlineShader --> line 321")
    public static void init(boolean outlineShaderSupported) {
        GlowingAPI.outlineShaderSupported = outlineShaderSupported;

        if (outlineShaderSupported) {
            actualDrawFunction = (renderGlobal, partialTicks) -> {
                if (outlineFrameBufferDirty)
                    renderGlobal.entityOutlineFramebuffer.framebufferClear();

                outlineFrameBufferDirty = !glowingEntities.isEmpty();

                if (glowingEntities.isEmpty()) {
                    renderGlobal.mc.getFramebuffer().bindFramebuffer(false);
                    return;
                }


                // contrary to 1.9+, just setting depth function to GL_ALWAYS doesn't disable depth test.
                // probably because some Render<T> class overrides the depth function somewhere.
                // so we manually disable it.
                disableDepth();
                disableFog();
                renderGlobal.entityOutlineFramebuffer.bindFramebuffer(false);
                RenderHelper.disableStandardItemLighting();

                for (Entity e : glowingEntities)
                    renderEntityOutline(renderGlobal.renderManager, e, partialTicks);


                RenderHelper.enableStandardItemLighting();
                depthMask(false);

                renderGlobal.entityOutlineShader.loadShaderGroup(partialTicks);

                enableLighting();
                depthMask(true);
                enableFog();
                enableBlend();
                enableColorMaterial();
                depthFunc(GL_LEQUAL);
                enableDepth();
                enableAlpha();

                renderGlobal.mc.getFramebuffer().bindFramebuffer(false);
                glowingEntities.clear();
                RenderHelper.enableStandardItemLighting();
            };
        } else {
            actualDrawFunction = (renderGlobal, partialTicks) -> {
                if (glowingEntities.isEmpty())  return;


                // contrary to 1.9+, just setting depth function to GL_ALWAYS doesn't disable depth test.
                // probably because some Render<T> class overrides the depth function somewhere.
                // so we manually disable it.
                disableDepth();
                disableFog();
                RenderHelper.disableStandardItemLighting();

                for (Entity e : glowingEntities) {
                    renderEntityOutline(renderGlobal.renderManager, e, partialTicks);
                }


                RenderHelper.enableStandardItemLighting();

                enableLighting();
                enableFog();
                enableBlend();
                enableColorMaterial();
                depthFunc(GL_LEQUAL);
                enableDepth();
                enableAlpha();

                glowingEntities.clear();
                RenderHelper.enableStandardItemLighting();
            };
        }
    }

    @Hook("com.xenon.XenonClient#onJoinGame --> line 123")
    public static void onJoinGame() {
        colorForEntity.clear();
    }

    @Hook("net.minecraft.client.renderer.RenderGlobal#renderEntities --> line 727")
    public static void scanEntityForGlow(Entity entity) {
        if (XenonClient.instance.glowingEnabled &&
                (entity instanceof EntityPlayer || entity instanceof EntityTNTPrimed ||
                        !MurderMysteryAPI.murderMysteryHelperRunning) &&
                (entity instanceof EntityPlayer || entity instanceof EntityBat || !DungeonAPI.dungeonRunning)
        )
            glowingEntities.add(entity);
    }


    @Hook("net.minecraft.client.renderer.RenderGlobal#renderEntities --> line 754 (after labeled loop)")
    public static void drawGlowingEntities(RenderGlobal renderGlobal, float partialTicks) {
        if (XenonClient.instance.glowingEnabled) {

            actualDrawFunction.drawGlowingEntities(renderGlobal, partialTicks);
        }
    }


    private static void renderEntityOutline(RenderManager renderManager, Entity entity, float partialTicks) {

        if (entity.ticksExisted == 0) {
            entity.lastTickPosX = entity.posX;
            entity.lastTickPosY = entity.posY;
            entity.lastTickPosZ = entity.posZ;
        }

        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks;
        float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
        int i = entity.isBurning() ? 15728880 : entity.getBrightnessForRender(partialTicks);

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i & 65535, i >> 16);
        GlStateManager.color(1, 1, 1, 1);

        Render<Entity> renderer = renderManager.getEntityRenderObject(entity);


        x -= renderManager.renderPosX;
        y -= renderManager.renderPosY;
        z -= renderManager.renderPosZ;

        renderer.setRenderOutlines(true);
        renderer.doRender(entity, x, y, z, yaw, partialTicks);
    }


    private static final FloatBuffer fb = BufferUtils.createFloatBuffer(4);
    public static final float[] white = {1, 1, 1, 1};
    public static final float[] green = {0, 1, 0.5373f, 1};
    public static final float[] red = {1, 0, 0, 1};
    public static final float[] blue = {0, 0, 1, 1};
    public static final Map<Entity, float[]> colorForEntity = new HashMap<>();
    private static void colorForEntity(Entity entity) {
        float[] color = entity == Minecraft.getMinecraft().thePlayer ? green :
                colorForEntity.getOrDefault(entity, white);

        fb.put(color);
        fb.rewind();
    }

    @Hook("net.minecraft.client.renderer.entity.RenderArrow#doRender --> line 56" +
            "net.minecraft.client.renderer.entity.RenderBoat#doRender --> line 50" +
            "net.minecraft.client.renderer.entity.RenderEntityItem#doRender --> line 96" +
            "net.minecraft.client.renderer.RenderFallingBlock#doRender --> line 46" +
            "net.minecraft.client.renderer.RenderFireball#doRender --> line 44" +
            "net.minecraft.client.renderer.RenderFish#doRender --> line 37" +
            "net.minecraft.client.renderer.RenderLeashKnot#doRender --> line 32" +
            "net.minecraft.client.renderer.RenderMinecart#doRender --> line 89" +
            "net.minecraft.client.renderer.RenderPainting#doRender --> line 37" +
            "net.minecraft.client.renderer.RenderSnowball#doRender --> line 36" +
            "net.minecraft.client.renderer.Render.TNT.Primed#doRender --> line 45")
    public static void xeEnableOutlineMode(Entity entity) {
        colorForEntity(entity); // edits fb

        glTexEnv(8960, 8705, fb);
        glTexEnvi(8960, 8704, 34160);
        glTexEnvi(8960, 34161, 7681);
        glTexEnvi(8960, 34176, 34166);
        glTexEnvi(8960, 34192, 768);
        glTexEnvi(8960, 34162, 7681);
        glTexEnvi(8960, 34184, 5890);
        glTexEnvi(8960, 34200, 770);
    }

    @Hook("net.minecraft.client.renderer.entity.RenderArrow#doRender --> line 86" +
            "net.minecraft.client.renderer.entity.RenderBoat#doRender --> line 56" +
            "net.minecraft.client.renderer.entity.RenderEntityItem#doRender --> line 127" +
            "net.minecraft.client.renderer.RenderFallingBlock#doRender --> line 61" +
            "net.minecraft.client.renderer.RenderFireball#doRender --> line 55" +
            "net.minecraft.client.renderer.RenderFish#doRender --> line 48" +
            "net.minecraft.client.renderer.RenderLeashKnot#doRender --> line 38" +
            "net.minecraft.client.renderer.RenderMinecart#doRender --> line 111" +
            "net.minecraft.client.renderer.RenderPainting#doRender --> line 43" +
            "net.minecraft.client.renderer.RenderSnowball#doRender --> line 42" +
            "net.minecraft.client.renderer.Render.TNT.Primed#doRender --> line 49")
    public static void xeDisableOutlineMode() {
        glTexEnvi(8960, 8704, 8448);
        glTexEnvi(8960, 34161, 8448);
        glTexEnvi(8960, 34162, 8448);
        glTexEnvi(8960, 34176, 5890);
        glTexEnvi(8960, 34184, 5890);
        glTexEnvi(8960, 34192, 768);
        glTexEnvi(8960, 34200, 770);
    }



    private interface GlowingDraw {
        void drawGlowingEntities(RenderGlobal renderGlobal, float partialTicks);
    }
}
