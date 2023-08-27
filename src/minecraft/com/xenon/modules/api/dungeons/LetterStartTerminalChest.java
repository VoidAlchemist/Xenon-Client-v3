package com.xenon.modules.api.dungeons;

import com.xenon.XenonClient;
import com.xenon.client.gui.GuiXenonChest;
import com.xenon.modules.api.TickableContainerChest;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;

public class LetterStartTerminalChest extends GuiXenonChest {


    public static LetterStartTerminalChest of(IInventory upperInv, IInventory lowerInv, String upperName,
                                                         String lowerName) {
        return new LetterStartTerminalChest(
                new LetterContainer(
                        lowerName.charAt(lowerName.indexOf('\'') + 1),
                        upperInv, lowerInv, Minecraft.getMinecraft().thePlayer
                ),
                lowerInv.getSizeInventory() / 9,
                upperName,
                lowerName
        );
    }


    private LetterStartTerminalChest(
            Container container,
            int inventoryRows,
            String upperName,
            String lowerName) {
        super(container, inventoryRows, upperName, lowerName);

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private static class LetterContainer extends TickableContainerChest {

        private final char letter;

        public LetterContainer(
                char letter,
                IInventory playerInventory,
                IInventory chestInventory,
                EntityPlayer player
        ) {
            super(playerInventory, chestInventory, player);

            this.letter = letter;
        }

        @Override
        public void tick() {
            if (dirty) {
                dirty = false;

                Slot slotToClick = null;

                loop:
                for (int i = 1; i < 5; i++)
                    for (int j = 1; j < 8; j++) {
                        Slot slot = inventorySlots.get(i * 9 + j);
                        ItemStack stack = slot.getStack();
                        if (stack != null && !stack.isItemEnchanted() &&
                                StringUtils.stripControlCodes(stack.getDisplayName()).charAt(0) == letter) {
                            slotToClick = slot;
                            break loop;
                        }
                    }

                if (XenonClient.instance.settings.dungeonF7PlusPlus && slotToClick != null)
                    clickSlot(slotToClick);

            }
        }
    }
}
