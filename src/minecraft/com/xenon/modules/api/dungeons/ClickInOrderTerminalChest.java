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

public class ClickInOrderTerminalChest extends GuiXenonChest {

    private static final Item stainedPane = Item.getItemFromBlock(Blocks.stained_glass_pane);

    public static ClickInOrderTerminalChest of(IInventory upperInv, IInventory lowerInv, String upperName,
                                                          String lowerName) {
        return new ClickInOrderTerminalChest(
                new ClickInOrderContainer(upperInv, lowerInv, Minecraft.getMinecraft().thePlayer),
                lowerInv.getSizeInventory() / 9, upperName, lowerName);
    }

    private ClickInOrderTerminalChest(
            Container container,
            int inventoryRows,
            String upperName,
            String lowerName
    ) {
        super(container, inventoryRows, upperName, lowerName);
    }

    private static class ClickInOrderContainer extends TickableContainerChest {


        public ClickInOrderContainer(IInventory playerInventory, IInventory chestInventory, EntityPlayer player) {
            super(playerInventory, chestInventory, player);
        }


        @Override
        public void tick() {
            if (!XenonClient.instance.settings.dungeonF7PlusPlus)
                return;
            if (dirty) {
                dirty = false;

                Slot slotToClick = null;
                int minStackSize = 65;

                for (int i = 1; i < 3; i++)
                    for (int j = 1; j < 8; j++) {
                        Slot slot = inventorySlots.get(i * 9 + j);
                        ItemStack stack = slot.getStack();
                        if (stack != null &&
                                stack.getItem() == stainedPane &&
                                stack.getItemDamage() == 14 &&
                                stack.stackSize < minStackSize) {
                            minStackSize = stack.stackSize;
                            slotToClick = slot;
                        }
                    }

                if (slotToClick != null)
                    clickSlot(slotToClick);

            }

        }
    }
}
