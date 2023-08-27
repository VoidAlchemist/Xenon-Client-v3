package com.xenon.modules.api;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.xenon.XenonClient;
import com.xenon.client.patches.Potions;
import com.xenon.util.RenderUtils;
import com.xenon.util.readability.Hook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import static net.minecraft.client.renderer.GlStateManager.*;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.storage.MapData;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Zenon
 * @see net.minecraft.client.gui.GuiIngame
 */
public class GuiIngameAPI {

    /**
     * @param instance
     * @param mc
     * @param sr
     */
    @Hook("net.minecraft.client.gui.GuiIngame#renderGameOverlay(float partialTicks) -> line 149")
    public static void handlePumpkinOverlay(GuiIngame instance, Minecraft mc, ScaledResolution sr) {
        ItemStack stack = mc.thePlayer.inventory.armorItemInSlot(3);

        if (mc.gameSettings.thirdPersonView == 0 && stack != null && stack.getItem() == Item.getItemFromBlock(Blocks.pumpkin) &&
                XenonClient.instance.settings.pumpkinOverlay)

            instance.renderPumpkinOverlay(sr);
    }

    /**
     * @param instance
     * @param mc
     */
    @Hook("net.minecraft.client.gui.GuiIngame#renderGameOverlay(float partialTicks) -> line ")
    public static void handleBossBar(GuiIngame instance, Minecraft mc) {
        if (XenonClient.instance.settings.bossBar) {
            mc.mcProfiler.startSection("bossHealth");
            instance.renderBossHealth();
            mc.mcProfiler.endSection();
        }
    }

    /**
     * @param instance
     * @param sr
     * @param startX
     */
    @Hook("net.minecraft.client.gui.GuiIngame#renderGameOverlay(float partialTicks) -> line 219")
    public static void handleHorseJumpBar(GuiIngame instance, Minecraft mc, ScaledResolution sr, int startX) {

        if (XenonClient.instance.settings.horsejumpbarScale == 1f)
            instance.renderHorseJumpBar(sr, startX);
        else if (XenonClient.instance.settings.horsejumpbarScale > 0.2f)
            renderHorseJumpBar(instance, sr, mc, XenonClient.instance.settings.horsejumpbarScale);
    }

    /**
     * @param instance
     * @param sr
     * @param mc
     * @param scale
     * @see #handleHorseJumpBar(GuiIngame, Minecraft, ScaledResolution, int)
     */
    public static void renderHorseJumpBar(GuiIngame instance, ScaledResolution sr, Minecraft mc, float scale) {
        pushMatrix();
        scale(scale, scale, 1);
        float scaleInverse = 1f / scale;

        mc.mcProfiler.startSection("jumpBar");
        mc.getTextureManager().bindTexture(Gui.icons);
        float f = mc.thePlayer.getHorseJumpPower();
        int j = (int) (f * 183f);
        int k = sr.getScaledHeight() - 32 + 3;
        int x = (sr.getScaledWidth() >> 1) - (int) (91f * scale);
        instance.drawTexturedModalRect(rescale(x, scaleInverse), rescale(k, scaleInverse), 0, 84, 182, 5);

        if (j > 0) {
            instance.drawTexturedModalRect(rescale(x, scaleInverse), rescale(k, scaleInverse), 0, 89, j, 5);
        }

        mc.mcProfiler.endSection();
        popMatrix();
    }

    /**
     * @param instance
     * @param mc
     * @param screenWidth
     * @param screenHeight
     * @param partialTicks
     */
    @Hook("net.minecraft.client.gui.GuiIngame#renderGameOverlay(float partialTicks) -> line 245")
    public static void handleRecordPlaying(GuiIngame instance, Minecraft mc, int screenWidth, int screenHeight, float partialTicks) {
        if (instance.recordPlayingUpFor > 0 && XenonClient.instance.settings.discDisplayed) {
            mc.mcProfiler.startSection("overlayMessage");
            float f2 = (float) instance.recordPlayingUpFor - partialTicks;
            int l1 = (int) (f2 * 255.0F / 20.0F);

            if (l1 > 255) {
                l1 = 255;
            }

            if (l1 > 8) {
                pushMatrix();
                translate((float) (screenWidth / 2), (float) (screenHeight - 68), 0.0F);
                enableBlend();
                tryBlendFuncSeparate(770, 771, 1, 0);
                int l = 16777215;

                if (instance.recordIsPlaying) {
                    l = MathHelper.hsvToRGB(f2 / 50.0F, 0.7F, 0.6F) & 16777215;
                }

                instance.getFontRenderer().drawString(instance.recordPlaying, -instance.getFontRenderer().getStringWidth(instance.recordPlaying) / 2, -4, l + (l1 << 24 & -16777216));
                disableBlend();
                popMatrix();
            }

            mc.mcProfiler.endSection();
        }
    }

    /**
     * @param instance
     * @param mc
     * @param screenWidth
     * @param screenHeight
     * @param partialTicks
     */
    @Hook("net.minecraft.client.gui.GuiIngame#renderGameOverlay(float partialTicks) -> line 247")
    public static void handleTitle(GuiIngame instance, Minecraft mc, int screenWidth, int screenHeight, float partialTicks) {
        if (instance.titlesTimer > 0 && XenonClient.instance.settings.titleScale > 0) {
            float newScale = XenonClient.instance.settings.titleScale;

            mc.mcProfiler.startSection("titleAndSubtitle");
            float f3 = (float) instance.titlesTimer - partialTicks;
            int alpha;

            if (XenonClient.instance.settings.titlealpha < 247)
                alpha = XenonClient.instance.settings.titlealpha;

            else {
                alpha = 255;
                if (instance.titlesTimer > instance.titleFadeOut + instance.titleDisplayTime) {
                    float f4 = (float) (instance.titleFadeIn + instance.titleDisplayTime + instance.titleFadeOut) - f3;
                    alpha = (int) (f4 * 255.0F / (float) instance.titleFadeIn);
                }

                if (instance.titlesTimer <= instance.titleFadeOut) {
                    alpha = (int) (f3 * 255.0F / (float) instance.titleFadeOut);
                }

                alpha = MathHelper.clamp_int(alpha, 0, 255);
            }


            if (alpha > 8) {
                pushMatrix();
                translate((float) (screenWidth / 2), (float) (screenHeight / 2), 0.0F);
                enableBlend();
                tryBlendFuncSeparate(770, 771, 1, 0);
                pushMatrix();
                float realScale = newScale * 4f;
                scale(realScale, realScale, 1);

                int finalColor = alpha << 24 | 0x00FFFFFF;

                instance.getFontRenderer().drawString(instance.displayedTitle, (float) (-instance.getFontRenderer().getStringWidth(instance.displayedTitle) / 2), -10.0F, finalColor, true);

                scale(0.5f, 0.5f, 1);

                instance.getFontRenderer().drawString(instance.displayedSubTitle, (float) (-instance.getFontRenderer().getStringWidth(instance.displayedSubTitle) / 2), 5.0F, finalColor, true);
                popMatrix();
                disableBlend();
                popMatrix();
            }

            mc.mcProfiler.endSection();
        }
    }

    /**
     * Handles drawing scoreboard and the chat.
     *
     * @param instance
     * @param mc
     * @param sr
     * @param screenWidth
     * @param screenHeight
     */
    @Hook("net.minecraft.client.gui.GuiIngame#renderGameOverlay(float partialTicks) -> line 249")
    public static void handleScoreboard(GuiIngame instance, Minecraft mc, ScaledResolution sr, int screenWidth, int screenHeight) {
        Scoreboard scoreboard = mc.theWorld.getScoreboard();
        ScoreObjective scoreobjective1;

        if (XenonClient.instance.settings.scoreboardScale > 0) {
            ScoreObjective scoreobjective = null;
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(mc.thePlayer.getName());

            if (scoreplayerteam != null) {
                int i1 = scoreplayerteam.getChatFormat().getColorIndex();

                if (i1 >= 0)
                    scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i1);
            }

            scoreobjective1 = scoreobjective != null ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);

            if (scoreobjective1 != null) {
                if (XenonClient.instance.settings.scoreboardScale != 1)
                    renderScoreboardScaled(instance.getFontRenderer(), scoreobjective1, sr, 1f / XenonClient.instance.settings.scoreboardScale);
                else
                    instance.renderScoreboard(scoreobjective1, sr);
            }

        }


        enableBlend();
        tryBlendFuncSeparate(770, 771, 1, 0);
        disableAlpha();
        pushMatrix();
        translate(0.0F, (float) (screenHeight - 48), 0.0F);
        mc.mcProfiler.startSection("chat");
        instance.persistantChatGUI.drawChat(instance.updateCounter);
        mc.mcProfiler.endSection();
        popMatrix();
        scoreobjective1 = scoreboard.getObjectiveInDisplaySlot(0);

        if (mc.gameSettings.keyBindPlayerList.isKeyDown() && (!mc.isIntegratedServerRunning() || mc.thePlayer.sendQueue.getPlayerInfoMap().size() > 1 || scoreobjective1 != null)) {
            instance.overlayPlayerList.updatePlayerList(true);
            instance.overlayPlayerList.renderPlayerlist(screenWidth, scoreboard, scoreobjective1);
        } else {
            instance.overlayPlayerList.updatePlayerList(false);
        }

        color(1.0F, 1.0F, 1.0F, 1.0F);
        disableLighting();
        enableAlpha();
    }

    /**
     * Copy paste of {@link net.minecraft.client.gui.GuiIngame#renderScoreboard(ScoreObjective, ScaledResolution)}
     *
     * @param fr
     * @param objective
     * @param scaledRes
     * @param scaleInverse
     * @see #handleScoreboard(GuiIngame, Minecraft, ScaledResolution, int, int)
     */
    private static void renderScoreboardScaled(FontRenderer fr, ScoreObjective objective, ScaledResolution scaledRes, float scaleInverse) {
        float scaleFactor = XenonClient.instance.settings.scoreboardScale;

        int newFONT_HEIGHT = (int) ((float) fr.FONT_HEIGHT * scaleFactor);

        pushMatrix();
        scale(scaleFactor, scaleFactor, 1);

        Scoreboard scoreboard = objective.getScoreboard();
        Collection<Score> collection = scoreboard.getSortedScores(objective);
        List<Score> list = collection.stream()
                .filter(score -> score.getPlayerName() != null && !score.getPlayerName().startsWith("#"))
                .collect(Collectors.toList());

        if (list.size() > 15) {
            collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
        } else {
            collection = list;
        }

        int i = (int) ((float) fr.getStringWidth(objective.getDisplayName()) * scaleFactor);

        for (Score score : collection) {
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
            String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
            i = Math.max(i, (int) ((float) fr.getStringWidth(s) * scaleFactor));
        }

        int i1 = collection.size() * newFONT_HEIGHT;
        int j1 = scaledRes.getScaledHeight() / 2 + i1 / 3;
        int k1 = 3;
        int l1 = scaledRes.getScaledWidth() - i - k1;
        int j = 0;


        for (Score score1 : collection) {
            ++j;
            ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
            String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
            String s2 = EnumChatFormatting.RED + String.valueOf(score1.getScorePoints());
            int k = j1 - j * newFONT_HEIGHT;
            int krescale = rescale(j1, scaleInverse) - j * fr.FONT_HEIGHT;

            int l = scaledRes.getScaledWidth() - k1 + 2;


            Gui.drawRect(rescale(l1 - 2, scaleInverse), krescale, rescale(l, scaleInverse), krescale + fr.FONT_HEIGHT, 1342177280);


            fr.drawString(s1, rescale(l1, scaleInverse), krescale, 553648127);
            fr.drawString(s2, rescale(l, scaleInverse) - fr.getStringWidth(s2), krescale, 553648127);

            if (j == collection.size()) {
                String s3 = objective.getDisplayName();
                Gui.drawRect(rescale(l1 - 2, scaleInverse), krescale - fr.FONT_HEIGHT - rescale(1, scaleInverse), rescale(l, scaleInverse), (int) (krescale - scaleInverse), 1610612736);
                Gui.drawRect(rescale(l1 - 2, scaleInverse), (int) (krescale - scaleInverse), rescale(l, scaleInverse), krescale, 1342177280);

                fr.drawString(s3, rescale(l1 + i / 2, scaleInverse) - fr.getStringWidth(s3) / 2, krescale - fr.FONT_HEIGHT, 553648127);
            }
        }
        popMatrix();
    }

    /**
     * @param init
     * @param factor
     * @return the init value rescaled correctly
     * @see #renderScoreboardScaled(FontRenderer, ScoreObjective, ScaledResolution, float)
     */
    public static int rescale(int init, float factor) {
        return (int) ((float) init * factor);
    }

    /**
     * Draws all the non-vanilla stuff to screen.
     *
     * @param instance : Gui instance used to draw textures, rectangles, etc.
     * @param mc
     * @param sr
     */
    @Hook("net.minecraft.client.gui.GuiIngame#renderGameOverlay(float partialTicks) -> line 251")
    public static void handleAddons(Gui instance, Minecraft mc, ScaledResolution sr) {
        drawFPS(sr);
        BlockPos posPlayer = new BlockPos(
                MathHelper.floor_double(mc.getRenderViewEntity().posX),
                MathHelper.floor_double(mc.getRenderViewEntity().getEntityBoundingBox().minY),
                MathHelper.floor_double(mc.getRenderViewEntity().posZ)
        );
        drawPlayerPos(sr, posPlayer);
        drawBiome(mc, sr, posPlayer);
        RenderUtils.resetTextureState();
        drawPotions(mc, instance, sr);
        drawArmorStatus(mc, sr);
        drawKeystrokes(mc, instance, sr);

        RenderUtils.resetTextureState();

        drawMap(mc, sr);
    }

    /**
     * Draws FPS information overlay.
     *
     * @param sr
     * @see # handleAddons()
     */
    private static void drawFPS(ScaledResolution sr) {
        if (XenonClient.instance.settings.fpsScale <= 0.02f) return;
        float scaleInverse = 1f / XenonClient.instance.settings.fpsScale;

        pushMatrix();
        scale(
                XenonClient.instance.settings.fpsScale,
                XenonClient.instance.settings.fpsScale,
                1
        );
        RenderUtils.font.drawString(Minecraft.getDebugFPS() + " fps",
                rescale(XenonClient.instance.settings.fpsxy.getAbsX(sr), scaleInverse),
                rescale(XenonClient.instance.settings.fpsxy.getAbsY(sr), scaleInverse),
                0xFFFFFFFF
        );
        popMatrix();
    }

    /**
     * Draws player position information overlay.
     *
     * @param sr
     * @param pos
     * @see # handleAddons()
     * @see net.minecraft.client.gui.GuiOverlayDebug# call()
     */
    private static void drawPlayerPos(ScaledResolution sr, BlockPos pos) {
        if (XenonClient.instance.settings.posScale <= 0.02f) return;
        float scaleInverse = 1f / XenonClient.instance.settings.posScale;
        pushMatrix();
        scale(
                XenonClient.instance.settings.posScale,
                XenonClient.instance.settings.posScale,
                1
        );
        int x = rescale(XenonClient.instance.settings.posxy.getAbsX(sr), scaleInverse);
        int y = rescale(XenonClient.instance.settings.posxy.getAbsY(sr), scaleInverse);
        RenderUtils.font.drawString("x: " + pos.getX(), x, y, 0xFFFFFFFF);
        RenderUtils.font.drawString("y: " + pos.getY(), x, y + 9 * XenonClient.instance.settings.posScale,
                0xFFFFFFFF);
        RenderUtils.font.drawString("z: " + pos.getZ(), x, y + 18 * XenonClient.instance.settings.posScale,
                0xFFFFFFFF);
        popMatrix();
    }


    /**
     * Draws biome information overlay.
     *
     * @param mc
     * @param sr
     * @param pos
     * @see # handleAddons(Minecraft, ScaledResolution)
     * @see net.minecraft.client.gui.GuiOverlayDebug# call()
     */
    private static void drawBiome(Minecraft mc, ScaledResolution sr, BlockPos pos) {
        if (XenonClient.instance.settings.biomeScale <= 0.02f) return;

        float scaleInverse = 1f / XenonClient.instance.settings.biomeScale;
        pushMatrix();
        scale(
                XenonClient.instance.settings.biomeScale,
                XenonClient.instance.settings.biomeScale,
                1
        );
        RenderUtils.font.drawString(
                mc.theWorld.getChunkFromBlockCoords(pos).getBiome(pos, mc.theWorld.getWorldChunkManager()).biomeName,
                rescale(XenonClient.instance.settings.biomexy.getAbsX(sr), scaleInverse),
                rescale(XenonClient.instance.settings.biomexy.getAbsY(sr), scaleInverse), 0xFFFFFFFF);
        popMatrix();
    }

    /**
     * Draws all active potion effect information overlay.
     *
     * @param mc
     * @param gui
     * @param sr
     * @see #handleAddons(Gui, Minecraft, ScaledResolution)
     * @see net.minecraft.client.renderer.InventoryEffectRenderer# drawActivePotionEffects()
     **/
    private static void drawPotions(Minecraft mc, Gui gui, ScaledResolution sr) {
        if (XenonClient.instance.settings.potionoverlayScale <= 0.02f) return;

        final float scale = XenonClient.instance.settings.potionoverlayScale;
        final float scaleInverse = 1f / scale;
        pushMatrix();
        scale(scale, scale, 1);

        int baseX = XenonClient.instance.settings.potionoverlayxy.getAbsX(sr);
        int baseY = XenonClient.instance.settings.potionoverlayxy.getAbsY(sr);
        baseX = rescale(baseX, scaleInverse);
        baseY = rescale(baseY, scaleInverse);

        int delta5 = (int) (5f * scale);
        int delta20 = (int) (20f * scale);

        for (PotionEffect potef : mc.thePlayer.getActivePotionEffects()) {
            Potion pot = Potions.potionTypes[potef.getPotionID()];

            mc.getTextureManager().bindTexture(GuiContainer.inventoryBackground);

            if (pot.hasStatusIcon()) {
                int i = pot.getStatusIconIndex();
                gui.drawTexturedModalRect(baseX, baseY, i % 8 * 18, 198 + i / 8 * 18, 18, 18);
            }
            baseY += delta5;
            int x = baseX + delta20;
            String name = I18n.format(pot.getName());

            int amplifierindex = potef.getAmplifier();
            // Potion effect real level is  potef.getAmplifier() + 1.
            if (amplifierindex >= 0 && amplifierindex < 20)
                name += " " + RenderUtils.roman[amplifierindex];

            mc.fontRendererObj.drawString(name, rescale(x, scaleInverse), baseY, 0xFFFFFFFF, false);
            mc.fontRendererObj.drawString(Potion.getDurationString(potef), rescale(x + delta5 + mc.fontRendererObj.getStringWidth(name), scaleInverse), baseY, 0xFFFFFFFF, false);
            baseY += (int) (15f * scale);
        }

        popMatrix();
    }

    /**
     * Draws armor & held item on screen.
     *
     * @param mc
     * @param sr
     * @see #handleAddons(Gui, Minecraft, ScaledResolution)
     */
    private static void drawArmorStatus(Minecraft mc, ScaledResolution sr) {
        if (XenonClient.instance.settings.armorstatusScale <= 0.02f) return;

        final float scale = XenonClient.instance.settings.armorstatusScale;
        final float scaleInverse = 1f / scale;
        pushMatrix();
        scale(scale, scale, 1);

        int x = XenonClient.instance.settings.armorstatusxy.getAbsX(sr);
        int y = XenonClient.instance.settings.armorstatusxy.getAbsY(sr);
        int rescaledX = rescale(x, scaleInverse);

        ItemStack holding = mc.thePlayer.inventory.getStackInSlot(mc.thePlayer.inventory.currentItem);
        if (holding != null) renderItemInGui(holding, mc, rescaledX, rescale(y, scaleInverse));
        y += (int) (16f * scale);

        if (mc.thePlayer.inventory.armorInventory.length > 0)
            for (int i = 3; i >= 0; i--) {
                ItemStack is = mc.thePlayer.inventory.armorItemInSlot(i);
                if (is != null)
                    renderItemInGui(is, mc, rescaledX, rescale(y + (int) (16f * (float) (3 - i) * scale), scaleInverse));
            }

        popMatrix();
    }

    /**
     * @param s
     * @param m
     * @param x
     * @param y
     * @see #drawArmorStatus(Minecraft, ScaledResolution)
     */
    private static void renderItemInGui(ItemStack s, Minecraft m, int x, int y) {

        if (XenonClient.instance.settings.armorstatusDurability && s.getItem().isDamageable()) {
            double prc = (1 - ((double) s.getItemDamage() / (double) s.getMaxDamage())) * 100;
            String se = Math.round(prc) + " %";
            m.fontRendererObj.drawString(se, x - m.fontRendererObj.getStringWidth(se) - 2, y + 5, 0xFFFFFFFF);
        }
        // Allows the correct lighting (Cf Unpleasant lands' API where the items in GUI weren't rendering well.)
        RenderHelper.enableGUIStandardItemLighting();
        m.getRenderItem().renderItemAndEffectIntoGUI(s, x, y);
    }

    /**
     * @param mc
     * @param gui
     * @param sr
     * @see #handleAddons(Gui, Minecraft, ScaledResolution)
     */
    private static void drawKeystrokes(Minecraft mc, Gui gui, ScaledResolution sr) {
        if (XenonClient.instance.settings.keystrokesScale <= 0.01f) return;

        float scaleInverse = 1f / XenonClient.instance.settings.keystrokesScale;
        pushMatrix();
        scale(
                XenonClient.instance.settings.keystrokesScale,
                XenonClient.instance.settings.keystrokesScale,
                1
        );

        int x = XenonClient.instance.settings.keystrokesxy.getAbsX(sr);
        int y = XenonClient.instance.settings.keystrokesxy.getAbsY(sr);
        int rescaledX = rescale(x, scaleInverse), rescaledY = rescale(y, scaleInverse);

        if (Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown())
            Gui.drawRect(rescaledX + 21, rescaledY, rescaledX + 41, rescaledY + 20, 0xaaF0F0F0);
        else Gui.drawRect(rescaledX + 21, rescaledY, rescaledX + 41, rescaledY + 20, 0xaa505050);

        gui.drawCenteredString(mc.fontRendererObj, "W", rescaledX + 31, rescaledY + 7, 0xFFFFFF);

        rescaledY += 21;

        if (Minecraft.getMinecraft().gameSettings.keyBindLeft.isKeyDown())
            Gui.drawRect(rescaledX, rescaledY, rescaledX + 20, rescaledY + 20, 0xaaF0F0F0);
        else Gui.drawRect(rescaledX, rescaledY, rescaledX + 20, rescaledY + 20, 0xaa505050);

        gui.drawCenteredString(mc.fontRendererObj, "A", rescaledX + 10, rescaledY + 7, 0xFFFFFF);

        rescaledX += 21;

        if (Minecraft.getMinecraft().gameSettings.keyBindBack.isKeyDown())
            Gui.drawRect(rescaledX, rescaledY, rescaledX + 20, rescaledY + 20, 0xaaF0F0F0);
        else Gui.drawRect(rescaledX, rescaledY, rescaledX + 20, rescaledY + 20, 0xaa505050);

        gui.drawCenteredString(mc.fontRendererObj, "S", rescaledX + 10, rescaledY + 7, 0xFFFFFF);

        rescaledX += 21;

        if (Minecraft.getMinecraft().gameSettings.keyBindRight.isKeyDown())
            Gui.drawRect(rescaledX, rescaledY, rescaledX + 20, rescaledY + 20, 0xaaF0F0F0);
        else Gui.drawRect(rescaledX, rescaledY, rescaledX + 20, rescaledY + 20, 0xaa505050);

        gui.drawCenteredString(mc.fontRendererObj, "D", rescaledX + 10, rescaledY + 7, 0xFFFFFF);

        rescaledY += 21;
        rescaledX -= 10;

        if (Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown())
            Gui.drawRect(rescaledX, rescaledY, rescaledX + 30, rescaledY + 20, 0xaaF0F0F0);
        else Gui.drawRect(rescaledX, rescaledY, rescaledX + 30, rescaledY + 20, 0xaa505050);

        gui.drawCenteredString(mc.fontRendererObj, "RC", rescaledX + 15, rescaledY + 5, 0xFFFFFF);

        rescaledX -= 32;

        if (Minecraft.getMinecraft().gameSettings.keyBindAttack.isKeyDown())
            Gui.drawRect(rescaledX, rescaledY, rescaledX + 30, rescaledY + 20, 0xaaF0F0F0);
        else Gui.drawRect(rescaledX, rescaledY, rescaledX + 30, rescaledY + 20, 0xaa505050);

        gui.drawCenteredString(mc.fontRendererObj, "LC", rescaledX + 15, rescaledY + 5, 0xFFFFFF);


        if (XenonClient.instance.settings.keystrokesCPS) {
            long time = System.currentTimeMillis();
            XenonClient.instance.cpsCounter.call(time);

            int Rcps = XenonClient.instance.cpsCounter.getRCPS(time);
            int Lcps = XenonClient.instance.cpsCounter.getLCPS(time);

            if (Rcps > 0)
                RenderUtils.font.drawCenteredTextScaled(Integer.toString(Rcps),
                        rescaledX + 47, rescaledY + 13,
                        0xFFFFFFFF, 0.6d);
            if (Lcps > 0)
                RenderUtils.font.drawCenteredTextScaled(Integer.toString(Lcps),
                        rescaledX + 15, rescaledY + 13,
                        0xFFFFFFFF, 0.6d);

            RenderUtils.resetTextureState();
        }

        popMatrix();
    }

    /**
     * Draws the first map in the player's hotbar.
     * @param mc Minecraft instance
     * @param sr the current screen resolution
     * @see #handleAddons(Gui, Minecraft, ScaledResolution)
     */
    public static void drawMap(Minecraft mc, ScaledResolution sr) {
        if (XenonClient.instance.settings.mapScale <= 0.01f)
            return;

        MapData map = null;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.mainInventory[i];
            if (stack != null && stack.getItem() instanceof ItemMap) {
                map = ((ItemMap)stack.getItem()).getMapData(stack, mc.theWorld);
                break;
            }
        }

        if (map == null)
            return;


        pushMatrix();
        translate(
                XenonClient.instance.settings.mapxy.getAbsX(sr),
                XenonClient.instance.settings.mapxy.getAbsY(sr),
                1
        );
        scale(
                XenonClient.instance.settings.mapScale,
                XenonClient.instance.settings.mapScale,
                1
        );

        disableDepth();
        mc.entityRenderer.getMapItemRenderer().renderMap(map, false);
        enableDepth();


        popMatrix();
    }

    @Hook("net.minecraft.client.gui.GuiIngame#renderPlayerStats(ScaledResolution) -> line 701")
    public static void handleSaturation(GuiIngame instance, EntityPlayer player, int k1, int j1, float saturation) {
        if (XenonClient.instance.settings.saturation && saturation != 0.0f)
            for (int l6 = 0; (float) l6 < saturation / 2.0F; ++l6) {
                int k7 = k1 - 10;
                int j8 = 16;
                int j9 = 0;

                if (player.isPotionActive(Potions.hunger)) {
                    j8 += 36;
                    j9 = 13;
                }

                if (saturation <= 0.0F && (float) instance.updateCounter % (saturation * 3.0F + 1.0F) == 0.0F)
                    k7 = k1 + (instance.rand.nextInt(3) - 1);

                int j10 = j1 - l6 * 8 - 9;
                instance.drawTexturedModalRect(j10, k7, 16 + j9 * 9, 27, 9, 9);

                if ((float) (l6 * 2 + 1) < saturation)
                    instance.drawTexturedModalRect(j10, k7, j8 + 36, 27, 9, 9);

                if ((float) (l6 * 2 + 1) == saturation)
                    instance.drawTexturedModalRect(j10, k7, j8 + 45, 27, 9, 9);
            }
    }
}
