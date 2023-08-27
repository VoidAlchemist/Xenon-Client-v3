package com.xenon.modules.api.dungeons;

import com.xenon.XenonClient;
import com.xenon.client.gui.GuiXenonChest;
import com.xenon.modules.api.TickableContainerChest;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TimingTerminalChest extends GuiXenonChest {

    private static final Item stainedPane = Item.getItemFromBlock(Blocks.stained_glass_pane);
    private static final Item stainedClay = Item.getItemFromBlock(Blocks.stained_hardened_clay);

    public static TimingTerminalChest of(IInventory upperInv, IInventory lowerInv, String upperName,
                                                    String lowerName) {
        return new TimingTerminalChest(
                new TimingContainer(upperInv, lowerInv, Minecraft.getMinecraft().thePlayer),
                lowerInv.getSizeInventory() / 9, upperName, lowerName);
    }

    private TimingTerminalChest(
            Container container,
            int inventoryRows,
            String upperName,
            String lowerName
    ) {
        super(container, inventoryRows, upperName, lowerName);
    }

    private static class TimingContainer extends TickableContainerChest {


        public TimingContainer(IInventory playerInventory, IInventory chestInventory, EntityPlayer player) {
            super(playerInventory, chestInventory, player);
        }


        @Override
        public void tick() {
            if (!XenonClient.instance.settings.dungeonF7PlusPlus)
                return;
            if (dirty) {
                dirty = false;

                int column = 1;

                for (; column < 6; column++) {
                    ItemStack stack = inventorySlots.get(column).getStack();
                    // magenta pane
                    if (stack != null && stack.getItem() == stainedPane && stack.getItemDamage() == 2)
                        break;
                }

                int row = 1;
                Slot buttonSlot = null;

                for (; row < 5; row++) {
                    Slot slot = inventorySlots.get(row * 9 + 7);
                    ItemStack stack = slot.getStack();
                    // lime hardened clay
                    if (stack != null && stack.getItem() == stainedClay && stack.getItemDamage() == 5) {
                        buttonSlot = slot;
                        break;
                    }
                }


                ItemStack paneOfInterest = inventorySlots.get(row * 9 + column).getStack();


                // lime pane
                if (paneOfInterest != null &&
                        buttonSlot != null &&
                        paneOfInterest.getItem() == stainedPane &&
                        paneOfInterest.getItemDamage() == 5)
                    clickSlot(buttonSlot);

            }
        }
    }
}
