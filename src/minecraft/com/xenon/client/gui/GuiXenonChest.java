package com.xenon.client.gui;

import com.xenon.XenonClient;
import com.xenon.modules.api.dungeons.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiXenonChest extends GuiContainer {


    public static GuiXenonChest of(IInventory upperInv, IInventory lowerInv) {
        String upperName = upperInv.getDisplayName().getUnformattedText().trim();
        String lowerName = lowerInv.getDisplayName().getUnformattedText().trim();

        if (XenonClient.instance.settings.dungeonHelper && DungeonAPI.dungeonRunning) {
            if (lowerName.startsWith("Select all the "))
                return ColorTerminalChest.of(upperInv, lowerInv, upperName, lowerName);
            if (lowerName.startsWith("What starts with:"))
                return LetterStartTerminalChest.of(upperInv, lowerInv, upperName, lowerName);
            if (lowerName.startsWith("Correct all the panes!"))
                return AllGreenTerminalChest.of(upperInv, lowerInv, upperName, lowerName);
            if (lowerName.startsWith("Click in order!"))
                return ClickInOrderTerminalChest.of(upperInv, lowerInv, upperName, lowerName);
            if ((lowerName.startsWith("Click the button on time!")))
                return TimingTerminalChest.of(upperInv, lowerInv, upperName, lowerName);
            if (lowerName.startsWith("Change all to same color!"))
                return RubikTerminalChest.of(upperInv, lowerInv, upperName, lowerName);
            if (lowerName.startsWith("Chest"))
                return SecretChest.of(upperInv, lowerInv, upperName, lowerName);
        }
        return new GuiXenonChest(upperInv, lowerInv, upperName, lowerName);
    }



    /**
     * The ResourceLocation containing the chest GUI texture.
     */
    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");

    /**
     * window height is calculated with these values; the more rows, the higher
     */
    public final int inventoryRows;

    public final String upperName, lowerName;

    protected GuiXenonChest(IInventory upperInv, IInventory lowerInv, String upperName, String lowerName) {
        this(
                new ContainerChest(upperInv, lowerInv, Minecraft.getMinecraft().thePlayer),
                lowerInv.getSizeInventory() / 9, upperName, lowerName);
    }

    protected GuiXenonChest(Container container, int inventoryRows, String upperName, String lowerName) {
        super(container);
        allowUserInput = false;
        this.inventoryRows = inventoryRows;
        ySize = 114 + inventoryRows * 18;
        this.upperName = upperName;
        this.lowerName = lowerName;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items). Args : mouseX, mouseY
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRendererObj.drawString(lowerName,
                8, 6, 4210752);
        this.fontRendererObj.drawString(upperName,
                8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Args : renderPartialTicks, mouseX, mouseY
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.drawTexturedModalRect(i, j + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }
}
