package com.xenon.modules.api;

import com.sun.scenario.effect.Glow;
import com.xenon.XenonClient;
import com.xenon.util.readability.Hook;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class MurderMysteryAPI {

    public static boolean murderMysteryHelperRunning;
    private static final Pattern startMessagePattern = Pattern.compile("(themurderer(has|shave)receivedtheirswords?!)" +
            "|(youhavereceivedyoursword!)" +
            "|(thealphainfectedhasbeenchosen!theyaretemporarilyinvincible\\.)" +
            "|(youhavebecomethealphainfected!ifyoudie,theotherinfectedcannolongerrespawn!" +
            "youareinvinciblefor15secondsoruntilrevealed\\.)",
            Pattern.CASE_INSENSITIVE);
    private static final Set<Item> notWeapons;

    static {
        notWeapons = new HashSet<>(13);

        notWeapons.add(Items.map);
        notWeapons.add(Items.filled_map);
        notWeapons.add(Items.bow);
        notWeapons.add(Items.arrow);
        notWeapons.add(Items.snowball);
        notWeapons.add(Items.potionitem);
        notWeapons.add(Items.gold_ingot);
        notWeapons.add(Items.wooden_shovel);
        notWeapons.add(Items.armor_stand);
        notWeapons.add(Items.firework_charge);
        notWeapons.add(Items.slime_ball);
        notWeapons.add(Item.getItemFromBlock(Blocks.pumpkin));
        notWeapons.add(Item.getItemFromBlock(Blocks.skull));
        notWeapons.add(Item.getItemFromBlock(Blocks.torch));
        notWeapons.add(Item.getItemFromBlock(Blocks.web));
        notWeapons.add(Item.getItemFromBlock(Blocks.stained_glass));
        notWeapons.add(Item.getItemFromBlock(Blocks.tnt));
    }

    private static final Set<Entity> murderers = new HashSet<>(2);


    @Hook("com.xenon.XenonClient#onJoinGame --> line 129")
    public static void onJoinGame() {
        murderers.clear();
        murderMysteryHelperRunning = false;
    }

    @Hook("com.xenon.modules.api.TextAPI#onClientChatReceived --> line 76")
    public static void checkStartGame(String chatMessage) {
        if (XenonClient.instance.settings.murderMysteryHelper && startMessagePattern.matcher(chatMessage).matches())
            murderMysteryHelperRunning = true;
    }

    @Hook("com.xenon.XenonClient#tick --> line 115")
    public static void update() {
        if (!murderMysteryHelperRunning || Minecraft.getMinecraft().theWorld == null)
            return;

        for (EntityPlayer player : Minecraft.getMinecraft().theWorld.playerEntities)
            if (!player.isSpectator() && !murderers.contains(player)) {
                ItemStack holding = player.getHeldItem();
                if (holding != null) {
                    if (holding.getItem() == Items.bow)
                        GlowingAPI.colorForEntity.put(player, GlowingAPI.blue);

                    else if (isWeapon(holding)) {
                        murderers.add(player);
                        GlowingAPI.colorForEntity.put(player, GlowingAPI.red);
                    }
                }
            }
    }

    private static boolean isWeapon(ItemStack i) {
        return !(i.getItem() == Items.fish && i.getItemDamage() == 2) && !notWeapons.contains(i.getItem());
    }
}
