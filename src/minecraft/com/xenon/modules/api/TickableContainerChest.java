package com.xenon.modules.api;

import com.xenon.Tickable;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class TickableContainerChest extends ContainerChest implements Tickable {

    protected boolean dirty;

    protected TickableContainerChest(IInventory playerInventory, IInventory chestInventory, EntityPlayer player) {
        super(playerInventory, chestInventory, player);
    }

    @Override
    public void putStackInSlot(int slotID, ItemStack stack) {
        super.putStackInSlot(slotID, stack);
        dirty = true;
    }

    /**
     * Simulates a click on a slot.
     * @param slot the slot to simulate a click on
     */
    public void clickSlot(Slot slot) {
        Minecraft.getMinecraft().playerController.windowClick(
                windowId,
                slot.slotNumber,
                0,
                0,
                Minecraft.getMinecraft().thePlayer
        );
    }

    /**
     * Simulates a shift click on a slot.
     * @param slot the slot to simulate a click on
     */
    public void shiftClickSlot(Slot slot) {
        Minecraft.getMinecraft().playerController.windowClick(
                windowId,
                slot.slotNumber,
                0,
                1,
                Minecraft.getMinecraft().thePlayer
        );
    }

}
