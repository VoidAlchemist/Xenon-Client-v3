package com.xenon.modules.api.dungeons;

import com.xenon.XenonClient;
import com.xenon.client.gui.GuiXenonChest;
import com.xenon.modules.api.TickableContainerChest;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorTerminalChest extends GuiXenonChest {

    private static final Pattern colorPattern = Pattern.compile("[A-Z]{2,}");

    public static ColorTerminalChest of(IInventory upperInv, IInventory lowerInv, String upperName, String lowerName) {
        List<String> colorWords = new ArrayList<>(2);
        // "Select all the " is excluded
        Matcher matcher = colorPattern.matcher(lowerName.substring(15));
        while (matcher.find())
            colorWords.add(matcher.group());


        return new ColorTerminalChest(
                new ColorContainer(
                        String.join(" ", colorWords),
                        upperInv,
                        lowerInv,
                        Minecraft.getMinecraft().thePlayer
                ),
                lowerInv.getSizeInventory() / 9,
                upperName,
                lowerName);
    }



    private ColorTerminalChest(Container container, int inventoryRows, String upperName, String lowerName) {
        super(container, inventoryRows, upperName, lowerName);

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private static class ColorContainer extends TickableContainerChest {

        private final String askedColor;

        public ColorContainer(
                String askedColor,
                IInventory playerInventory,
                IInventory chestInventory,
                EntityPlayer player) {
            super(playerInventory, chestInventory, player);

            this.askedColor = askedColor;
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
                        if (stack != null && !stack.isItemEnchanted()) {

                            String itemName = StringUtils.stripControlCodes(stack.getDisplayName())
                                    .toUpperCase(Locale.ROOT);

                            boolean isDye = stack.getItem() == Items.dye;
                            int damage = stack.getItemDamage();

                            boolean found = itemName.contains(askedColor);

                            if (!found)
                                switch (askedColor) {
                                    case "SILVER":
                                        found = itemName.contains("LIGHT GRAY");
                                        break;
                                    case "WHITE":
                                        found = (isDye && damage == 15) || itemName.equals("WOOL");
                                        break;
                                    case "BLACK":
                                        found = isDye && damage == 0;
                                        break;
                                    case "BLUE":
                                        found = isDye && damage == 4;
                                        break;
                                    case "BROWN":
                                        found = isDye && damage == 3;
                                        break;
                                }

                            if (found) {
                                slotToClick = slot;
                                break loop;
                            }
                        }
                    }

                if (XenonClient.instance.settings.dungeonF7PlusPlus && slotToClick != null)
                    clickSlot(slotToClick);

            }

        }
    }
}
