package com.xenon.modules.api;

import com.xenon.XenonClient;
import com.xenon.client.entity.EntityOtherPlayerMP;
import com.xenon.util.readability.Hook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;

public class PhantomCamAPI {

    /**
     * If enabled, do not update thePlayer movement input
     * (optional since the results are discarded if thePlayer isn't the renderViewEntity) and do not
     * take inputs such as item drop, attack, place, open chat...
     */
    @Hook("net.minecraft.client.entity.EntityPlayerSP#onLivingUpdate --> line 631" +
            "net.minecraft.client.Minecraft#runTick --> line 1677")
    public static boolean enabled;


    /**
     * Third entity which is a simple copy of thePlayer, for the user to see himself even as a camera.
     */
    private static EntityOtherPlayerMP thePlayerAvatar;
    public static Camera camera;

    @Hook("com.xenon.XenonClient#onJoinGame")
    public static void onJoinGame() {
        enabled = false;
    }


    @Hook("com.xenon.XenonClient#tick")
    public static void toggle() {
        enabled = !enabled;

        final Minecraft mc = Minecraft.getMinecraft();
        if (enabled) {
            camera = new Camera();
            camera.copyLocationAndAnglesFrom(mc.thePlayer);

            thePlayerAvatar = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
            thePlayerAvatar.copyLocationAndAnglesFrom(mc.thePlayer);
            thePlayerAvatar.clonePlayer(mc.thePlayer, true);
            thePlayerAvatar.rotationYawHead = mc.thePlayer.rotationYawHead;
            thePlayerAvatar.renderYawOffset = mc.thePlayer.renderYawOffset;

            thePlayerAvatar.chasingPosX = thePlayerAvatar.posX;
            thePlayerAvatar.chasingPosY = thePlayerAvatar.posY;
            thePlayerAvatar.chasingPosZ = thePlayerAvatar.posZ;

            mc.theWorld.addEntityToWorld(thePlayerAvatar.getEntityId(), thePlayerAvatar);
            mc.theWorld.addEntityToWorld(camera.getEntityId(), camera);
            mc.setRenderViewEntity(camera);
        } else {
            mc.setRenderViewEntity(mc.thePlayer);
            mc.theWorld.removeEntityFromWorld(camera.getEntityId());
            mc.theWorld.removeEntityFromWorld(thePlayerAvatar.getEntityId());
            camera = null;
            thePlayerAvatar = null;
        }
    }



    private static class Camera extends Entity {

        Camera() {
            super(Minecraft.getMinecraft().theWorld);
            noClip = true;
        }

        @Override
        public boolean isInRangeToRender3d(double x, double y, double z) {
            return false;
        }

        @Override
        protected void readEntityFromNBT(NBTTagCompound tagCompund) {}

        @Override
        protected void writeEntityToNBT(NBTTagCompound tagCompound) {}

        @Override
        public boolean isEntityAlive() {
            return true;
        }

        @Override
        public boolean isEntityInvulnerable(DamageSource source) {
            return true;
        }

        @Override
        protected void entityInit() {}

        @Override
        public void onUpdate() {
            prevPosX = posX;
            prevPosY = posY;
            prevPosZ = posZ;

            GameSettings gs = Minecraft.getMinecraft().gameSettings;
            int flyboost = Math.max(XenonClient.instance.settings.flyBoost, 1);

            boolean fwd = gs.keyBindForward.isKeyDown();
            boolean bwd = gs.keyBindBack.isKeyDown();
            float forward = fwd && !bwd ? 0.5f : (!fwd && bwd ? -0.5f : 0);

            boolean left = gs.keyBindLeft.isKeyDown();
            boolean right = gs.keyBindRight.isKeyDown();
            float strafe = left && !right ? 0.5f : (!left && right ? -0.5f : 0);

            boolean up = gs.keyBindJump.isKeyDown();
            boolean down = gs.keyBindSneak.isKeyDown();
            float upward = up && !down ? 0.5f : (!up && down ? -0.5f : 0);

            float dx = MathHelper.sin(rotationYaw * (float) Math.PI / 180.0F);
            float dy = MathHelper.cos(rotationYaw * (float) Math.PI / 180.0F);

            moveEntity(
                    flyboost * (strafe * dy - forward * dx),
                    flyboost * upward,
                    flyboost * (forward * dy + strafe * dx)
            );
        }
    }

}
