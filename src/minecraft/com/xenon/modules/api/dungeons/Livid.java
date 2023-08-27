package com.xenon.modules.api.dungeons;

import com.xenon.modules.api.GlowingAPI;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class Livid {

    static boolean solved;
    private static final BlockPos woolPos = new BlockPos(5, 108, 25);

    static void reset() {
        solved = false;
    }

    public static void trysolve() {
        if (solved)
            return;

        World world = Minecraft.getMinecraft().theWorld;
        Block woolBlock = world.getBlockState(woolPos).getBlock();
        if (woolBlock == Blocks.wool) {
            String prefix = "";
            switch (woolBlock.getDamageValue(world, woolPos)) {
                case 0:
                    prefix = String.valueOf(EnumChatFormatting.WHITE);
                    break;
                case 2:
                    prefix = String.valueOf(EnumChatFormatting.LIGHT_PURPLE);
                    break;
                case 4:
                    prefix = String.valueOf(EnumChatFormatting.YELLOW);
                    break;
                case 5:
                    prefix = String.valueOf(EnumChatFormatting.GREEN);
                    break;
                case 7:
                    prefix = String.valueOf(EnumChatFormatting.GRAY);
                    break;
                case 10:
                    prefix = String.valueOf(EnumChatFormatting.DARK_PURPLE);
                    break;
                case 11:
                    prefix = String.valueOf(EnumChatFormatting.BLUE);
                    break;
                case 13:
                    prefix = String.valueOf(EnumChatFormatting.DARK_GREEN);
                    break;
                case 14:
                    prefix = String.valueOf(EnumChatFormatting.RED);
                    break;
            }
            prefix += '﴾';

            for (Entity entity : world.getLoadedEntityList()) {
                if (entity instanceof EntityArmorStand &&
                        entity.hasCustomName() &&
                        entity.getCustomNameTag().replaceAll("\\s", "").contains(prefix)
                ) {
                    GlowingAPI.colorForEntity.put(
                            entity,
                            GlowingAPI.green
                    );
                    solved = true;
                    break;
                }
            }

        }
    }

}
