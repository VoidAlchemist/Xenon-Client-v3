package com.xenon.modules.api.dungeons;

import com.xenon.XenonClient;
import com.xenon.modules.api.GlowingAPI;
import com.xenon.util.readability.Hook;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Blaze {

    private enum State {
        LtoH, HtoL, NaN
    }

    private static final Comparator<EntityBlaze> healthComparator = (b1, b2) ->
            Float.compare(b1.getHealth(), b2.getHealth());
    private static final Set<EntityBlaze> blazes = new HashSet<>();

    private static EntityBlaze nextBlazeToKill;

    private static State blazePuzzleState = State.NaN;

    static void reset() {
        blazes.clear();
        nextBlazeToKill = null;
        blazePuzzleState = State.NaN;
    }

    @Hook("net.minecraft.entity.monster.EntityBlaze#EntityBlaze() --> line 40")
    public static void onSpawnBlaze(EntityBlaze blaze) {
        if (DungeonAPI.dungeonRunning && XenonClient.instance.settings.dungeonHelper) {

            if (blazePuzzleState == State.NaN) {
                blazePuzzleState = scanBlocks(blaze);
                nextBlazeToKill = blaze;
                onNextBlazeChange(blaze);
            } else {
                float prevHealth = nextBlazeToKill.getHealth();
                float newHealth = blaze.getHealth();

                if (
                        (blazePuzzleState == State.LtoH && newHealth < prevHealth) ||
                        (blazePuzzleState == State.HtoL && newHealth > prevHealth)
                )
                    onNextBlazeChange(blaze);
            }


            blazes.add(blaze);
        }
    }

    private static State scanBlocks(EntityBlaze blaze) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos((int) blaze.posX, 70, (int) blaze.posZ);

        State result = State.NaN;

        loop:
        for (int i = -4; i < 4; i++) {
            pos.x += i;
            for (int j = -4; j < 4; j++) {
                pos.z += j;

                Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();

                if (block == Blocks.chest) {
                    result = State.LtoH;
                    break loop;
                } else if (block == Blocks.iron_bars) {
                    result = State.HtoL;
                    break loop;
                }
            }
        }

        XenonClient.instance.sendmsg(EnumChatFormatting.DARK_GREEN +
                "[XenonClient] Blaze puzzle configuration: " + result);
        return result;
    }

    private static void onNextBlazeChange(EntityBlaze newNextBlaze) {
        if (nextBlazeToKill != null)
            GlowingAPI.colorForEntity.remove(nextBlazeToKill);

        GlowingAPI.colorForEntity.put(nextBlazeToKill = newNextBlaze, GlowingAPI.green);
    }

    @Hook("net.minecraft.entity.monster.EntityBlaze#onDeath --> line 92")
    public static void onKillBlaze(EntityBlaze blaze) {
        if (DungeonAPI.dungeonRunning && XenonClient.instance.settings.dungeonHelper &&
                blazePuzzleState != State.NaN && blaze == nextBlazeToKill) {

            blazes.remove(blaze);

            Optional<EntityBlaze> bestBlaze = blazePuzzleState == State.LtoH ?
                    blazes.stream().min(healthComparator) :
                    blazes.stream().max(healthComparator);

            bestBlaze.ifPresent(Blaze::onNextBlazeChange);
        }
    }
}
