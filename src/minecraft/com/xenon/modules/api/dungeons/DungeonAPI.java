package com.xenon.modules.api.dungeons;

import com.xenon.Tickable;
import com.xenon.XenonClient;
import com.xenon.util.readability.Hook;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLever;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.*;
import net.minecraft.util.BlockPos;

public class DungeonAPI {

    public static boolean dungeonRunning, inLividBoss, inNecronBoss;

    @Hook("com.xenon.XenonClient#onJoinGame --> line 129")
    public static void onJoinGame() {
        dungeonRunning = false;
        inLividBoss = false;
        inNecronBoss = false;
        Riddle.reset();
        Livid.reset();
        Blaze.reset();
    }

    @Hook("com.xenon.modules.api.TextAPI#onClientChatReceived --> line 77")
    public static void onChatMSG(String chatMessage) {
        switch(chatMessage) {
            case "[NPC]Mort:Here,IfoundthismapwhenIfirstenteredthedungeon.":
                XenonClient.instance.sendmsg("entering dungeon");
                dungeonRunning = true;
                break;
            case "[BOSS]Livid:Irespectyouformakingittohere,butI'llbeyourundoing.":
                inLividBoss = true;
                XenonClient.instance.sendmsg("entered f5/m5 boss");
                break;
            case "[BOSS]Maxor:DON'TDISAPPOINTME,IHAVEN'THADAGOODFIGHTINAWHILE.":
                inNecronBoss = true;
                XenonClient.instance.sendmsg("entered f7/m7 boss");
                break;
            default:
                if (dungeonRunning) {
                    if (chatMessage.startsWith("[NPC]"))
                        Riddle.trysolve(chatMessage);
                    Trivia.trysolve(chatMessage);
                }
                break;
        }
    }

    /**
     * Determines whether a right click on a lever should be cancelled, in the case of F7 light device.
     * @param leverPos the lever position in the world
     * @param leverState the lever state
     * @return whether the right click should be canceled
     */
    @Hook("net.minecraft.client.multiplayer.PlayerControllerMP#onPlayerRightClick --> line 345")
    public static boolean shouldCancelLeverFlip(BlockPos leverPos, IBlockState leverState) {
        if (XenonClient.instance.settings.dungeonHelper && inNecronBoss && leverState.getBlock() == Blocks.lever) {
            Block wall = Minecraft.getMinecraft().theWorld.getBlockState(
                    leverPos.offset(leverState.getValue(BlockLever.FACING).getFacing().getOpposite())
            ).getBlock();

            return wall == Blocks.lit_redstone_lamp;
        }
        return false;
    }

    /**
     * Determines whether a right click on an item frame should be cancelled, in the case of F7 arrow device.
     * @param entity the (perhaps) item frame entity
     * @return whether the interaction should be canceled
     */
    @Hook("net.minecraft.client.multiplayer.PlayerControllerMP#interactWithEntitySendPacket --> line 428")
    public static boolean shouldCancelItemFrameInteraction(Entity entity) {
        if (entity instanceof EntityItemFrame && XenonClient.instance.settings.dungeonHelper && inNecronBoss) {
            EntityItemFrame itemFrame = (EntityItemFrame) entity;
            double x = itemFrame.posX,
                    y = itemFrame.posY,
                    z = itemFrame.posZ;

            BlockPos pos;
            switch (itemFrame.facingDirection) {
                case NORTH:
                    pos = new BlockPos(x, y, z + 1);
                    break;
                case SOUTH:
                    pos = new BlockPos(x, y, z - 1);
                    break;
                case EAST:
                    pos = new BlockPos(x - 1, y, z - 0.5);
                    break;
                case WEST:
                    pos = new BlockPos(x + 1, y, z - 0.5);
                    break;
                default:
                    return false;
            }
            return Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() == Blocks.sea_lantern;
        }

        return false;
    }


    private static int tickCount;

    @Hook("com.xenon.XenonClient#tick --> line 116")
    public static void update() {
        if (dungeonRunning) {

            if ( (tickCount = (tickCount + 1) & (XenonClient.instance.settings.dungeonUpdateDelay - 1)) == 0 ) {

                if (inLividBoss)
                    Livid.trysolve();

                Container container = Minecraft.getMinecraft().thePlayer.openContainer;
                if (container instanceof Tickable)
                    ((Tickable) container).tick();
            }

        }
    }

}
