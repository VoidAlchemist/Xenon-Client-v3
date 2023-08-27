package com.xenon.modules.api;

import com.xenon.XenonClient;
import com.xenon.util.readability.Hook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SwordsAPI {

    private static final Set<Item> swords = Stream.of(
            Items.wooden_sword,
            Items.stone_sword,
            Items.iron_sword,
            Items.golden_sword,
            Items.diamond_sword
    ).collect(Collectors.toSet());

    @Hook("com.xenon.modules.api.XenonMovementInput#updatePlayerMoveState --> line 115" +
            "net.minecraft.client.entity.EntityPlayerSP#onLivingUpdate --> line 636")
    public static boolean shouldItemUsageSlowDown(EntityPlayerSP player) {
        if (!player.isUsingItem())
            return false;
        Item item = player.itemInUse.getItem();
        return !XenonClient.instance.settings.noSlowSword || !swords.contains(item);
    }

    private static int tickCount;

    @Hook("com.xenon.XenonClient#tick --> line 117")
    public static void tick() {
        if ((tickCount = (tickCount + 1) & (XenonClient.instance.settings.duplicateRightClickTicks - 1)) == 0) {
            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
            if (player.isUsingItem() && player.itemInUse.getItem() == Items.iron_sword)
                Minecraft.getMinecraft().rightClickMouse();

        }
    }

}
