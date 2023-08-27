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

public class RubikTerminalChest extends GuiXenonChest {

    private static final Item stainedPane = Item.getItemFromBlock(Blocks.stained_glass_pane);

    public static RubikTerminalChest of(IInventory upperInv, IInventory lowerInv, String upperName,
                                                   String lowerName) {
        return new RubikTerminalChest(
                new RubikContainer(upperInv, lowerInv, Minecraft.getMinecraft().thePlayer),
                lowerInv.getSizeInventory() / 9, upperName, lowerName);
    }

    private RubikTerminalChest(
            Container container,
            int inventoryRows,
            String upperName,
            String lowerName
    ) {
        super(container, inventoryRows, upperName, lowerName);
    }

    private static class RubikContainer extends TickableContainerChest {

        private boolean solved;
        private int targetColor;

        public RubikContainer(IInventory playerInventory, IInventory chestInventory, EntityPlayer player) {
            super(playerInventory, chestInventory, player);
        }


        @Override
        public void tick() {
            if (!XenonClient.instance.settings.dungeonF7PlusPlus)
                return;
            if (dirty) {
                dirty = false;

                if (!solved) {

                    int[] colorCount = new int[14];

                    for (int i = 1; i < 4; i++)
                        for (int j = 3; j < 6; j++) {
                            Slot slot = inventorySlots.get(i * 9 + j);
                            ItemStack stack = slot.getStack();

                            if (stack == null || stack.getItem() != stainedPane)
                                return; // incomplete terminal. waiting for more packets

                            switch (stack.getItemDamage()) {
                                case 1: // orange
                                case 4: // yellow
                                case 11:    // blue
                                case 13:    // green
                                case 14:    // red
                                    colorCount[stack.getItemDamage() - 1]++;
                                    break;
                                default:
                                    return; // incomplete terminal. waiting for more packets
                            }
                        }


                    int max = -1;
                    int max_color = -1;

                    if (colorCount[0] > max) {
                        max = colorCount[0];
                        max_color = 1;
                    }
                    if (colorCount[3] > max) {
                        max = colorCount[3];
                        max_color = 4;
                    }
                    if (colorCount[10] > max) {
                        max = colorCount[10];
                        max_color = 11;
                    }
                    if (colorCount[12] > max) {
                        max = colorCount[12];
                        max_color = 13;
                    }
                    if (colorCount[13] > max) {
                        max_color = 14;
                    }

                    targetColor = max_color;
                    solved = true;
                }

                Slot slotToClick = null;

                loop:
                for (int i = 1; i < 4; i++)
                    for (int j = 3; j < 6; j++) {
                        Slot slot = inventorySlots.get(i * 9 + j);
                        ItemStack stack = slot.getStack();

                        if (stack != null && stack.getItemDamage() != targetColor) {
                            slotToClick = slot;
                            break loop;
                        }
                    }

                if (slotToClick != null)
                    clickSlot(slotToClick);
            }
        }
    }
}
