package com.xenon.modules.api.dungeons;

import com.xenon.XenonClient;
import com.xenon.client.gui.GuiXenonChest;
import com.xenon.modules.api.TickableContainerChest;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Locale;

public class SecretChest extends GuiXenonChest {

    private static final Item tripwire = Item.getItemFromBlock(Blocks.tripwire_hook);

    public static SecretChest of(IInventory upperInv, IInventory lowerInv, String upperName, String lowerName) {
        return new SecretChest(upperInv, lowerInv, upperName, lowerName);
    }

    private SecretChest(IInventory upperInv, IInventory lowerInv, String upperName, String lowerName) {
        super(new SecretContainer(
                upperInv,
                lowerInv,
                Minecraft.getMinecraft().thePlayer
        ), lowerInv.getSizeInventory() / 9, upperName, lowerName);
    }


    private static final class SecretContainer extends TickableContainerChest {


        public SecretContainer(IInventory playerInventory, IInventory chestInventory, EntityPlayer player) {
            super(playerInventory, chestInventory, player);
        }

        @Override
        public void tick() {
            if (dirty && XenonClient.instance.settings.dungeonF7PlusPlus) {
                Slot secretSlot = inventorySlots.get(13);
                ItemStack stack = secretSlot.getStack();

                if (stack != null) {
                    Item item = stack.getItem();
                    if (
                            stack.getDisplayName().toUpperCase(Locale.ROOT).contains("TALISMAN") ||
                                    item == tripwire ||
                                    item == Items.potionitem ||
                                    item == Items.ender_pearl
                    )
                        shiftClickSlot(secretSlot);

                    Minecraft.getMinecraft().thePlayer.closeScreen();

                }
            }

        }
    }
}
