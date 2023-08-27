package com.xenon.modules.api;

import com.google.common.collect.Multimap;
import com.xenon.XenonClient;
import com.xenon.client.patches.Enchants;
import com.xenon.util.readability.Hook;
import com.xenon.modules.api.dungeons.DungeonAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import static net.minecraft.client.renderer.GlStateManager.*;

/**
 * @author Zenon
 */
public class TextAPI {

    private static final HashMap<String, String> attributesmapping = new HashMap<>(10);

    static {
        attributesmapping.put("generic.attackDamage", "Damage: §c");
        attributesmapping.put("generic.movementSpeed", "Speed: §a");
        attributesmapping.put("generic.knockbackResistance", "Knockback Resistance: §c");
        attributesmapping.put("generic.followRange", "Follow Range: §a");
        attributesmapping.put("generic.maxHealth", "Health: §a");
        attributesmapping.put("zombie.spawnReinforcements", "Spawn Reinforcements: §a");
        attributesmapping.put("horse.jumpStrength", "Horse Jump Strength: §a");
    }

    /**
     * Handles the chat message before actually displaying it to the ChatGui.
     * To cancel the event, just return <code>null</code>.
     *
     */
    @Hook("net.minecraft.client.network.NetHandlerPlayClient#handleChat(S02PacketChat) -> line 857")
    public static IChatComponent onClientChatReceived(IChatComponent component) {
        String s = StringUtils.stripControlCodes(component.getFormattedText()).replaceAll("\\s", "");
        if (s.isEmpty())
            return null;

        MurderMysteryAPI.checkStartGame(s);
        DungeonAPI.onChatMSG(s);

        if (XenonClient.instance.settings.customnameEnabled) {
            String msg = component.getFormattedText();
            String username = Minecraft.getMinecraft().getSession().getUsername();
            if (msg.contains(username)) {
                msg = msg.replace(username, XenonClient.instance.settings.customName);
                return new ChatComponentText(msg).setChatStyle(component.getChatStyle());
            }
        }

        return component;
    }


    /**
     * Original is {@link net.minecraft.client.renderer.entity.RendererLivingEntity
     * #renderName(EntityLivingBase, double, double, double)}.
     * Only changed 1 line for the user's custom name to be rendered.
     *
     */
    @Hook("net.minecraft.client.renderer.entity.RendererLivingEntity#renderName(...) -> line 643")
    public static <T extends EntityLivingBase> void renderLivingName(
            RendererLivingEntity<T> instance, T entity, double x, double y, double z) {
        if (instance.canRenderName(entity) && !instance.renderOutlines) {
            double d0 = entity.getDistanceSqToEntity(instance.renderManager.livingPlayer);
            float f = entity.isSneaking() ?
                    RendererLivingEntity.NAME_TAG_RANGE_SNEAK :
                    RendererLivingEntity.NAME_TAG_RANGE;

            if (d0 < (double) (f * f)) {
                String s = entity == Minecraft.getMinecraft().thePlayer &&
                        XenonClient.instance.settings.customnameEnabled ?
                        XenonClient.instance.settings.customName : entity.getDisplayName().getFormattedText();
                alphaFunc(516, 0.1F);

                if (entity.isSneaking()) {
                    FontRenderer fontrenderer = instance.getFontRendererFromRenderManager();
                    pushMatrix();
                    translate((float) x, (float) y + entity.height + 0.5F - (entity.isChild() ? entity.height / 2.0F : 0.0F), (float) z);
                    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                    rotate(-instance.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                    rotate(instance.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                    scale(-0.02666667F, -0.02666667F, 0.02666667F);
                    translate(0.0F, 9.374999F, 0.0F);
                    disableLighting();
                    depthMask(false);
                    enableBlend();
                    disableTexture2D();
                    tryBlendFuncSeparate(770, 771, 1, 0);
                    int i = fontrenderer.getStringWidth(s) / 2;
                    Tessellator tessellator = Tessellator.getInstance();
                    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                    worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                    worldrenderer.pos((double) (-i - 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    worldrenderer.pos((double) (-i - 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    worldrenderer.pos((double) (i + 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    worldrenderer.pos((double) (i + 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    tessellator.draw();
                    enableTexture2D();
                    depthMask(true);
                    fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 553648127);
                    enableLighting();
                    disableBlend();
                    color(1.0F, 1.0F, 1.0F, 1.0F);
                    popMatrix();
                } else {
                    instance.renderOffsetLivingLabel(
                            entity,
                            x,
                            y - (entity.isChild() ? (double) (entity.height / 2.0F) : 0.0D),
                            z, s, 0.02666667F, d0);
                }
            }
        }
    }

    /**
     * Vanilla tooltip drawing method with a scrolling feature implemented.
     * As the scrolled amount shall be reset when the player swap GUI, it was necessary adding 2 fields <code>scrollX</code>
     * and <code>scrollY</code> to {@link net.minecraft.client.gui.GuiScreen}.
     *
     */
    @Hook("net.minecraft.client.gui.GuiScreen#drawHoveringText(...) -> line 198")
    public static void drawHoveringText(GuiScreen instance, FontRenderer font, List<String> textLines, int x, int y) {
        if (!textLines.isEmpty()) {
            disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            disableLighting();
            disableDepth();
            int tooltipWidth = 0;

            for (String s : textLines) {
                int j = font.getStringWidth(s);

                if (j > tooltipWidth)
                    tooltipWidth = j;
            }

            int tooltipX = x + 12;
            int tooltipY = y - 12;
            int tooltipHeight = 8;

            if (textLines.size() > 1)
                tooltipHeight += 2 + (textLines.size() - 1) * 10;

            /*Scrolling mechanics start*/
            label1:
            if (!(instance instanceof GuiContainerCreative)) {
                int wheel = Mouse.getDWheel();

                if (wheel == 0)
                    break label1;

                boolean positive = wheel > 0;

                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    if (positive)
                        instance.scrollX -= 10;
                    else
                        instance.scrollX += 10;
                } else {
                    if (positive && tooltipY + instance.scrollY < 0)
                        instance.scrollY += 10;
                    else if (!positive && tooltipY + tooltipHeight + instance.scrollY > instance.height)
                        instance.scrollY -= 10;
                }
            }

            tooltipY += instance.scrollY;
            tooltipX += instance.scrollX;
            /*Scrolling mechanics end*/


            instance.zLevel = 300.0F;
            instance.itemRender.zLevel = 300.0F;

            // Don't try too much to understand these coordinates.
            // Just take the vanilla method and cache every single int that is calculated more than 2 times.
            int x1 = tooltipX - 3;
            int y1 = tooltipY - 3;
            int x2 = tooltipX + tooltipWidth;
            int y2 = tooltipY + tooltipHeight;

            int x3 = x2 + 3;
            int y3 = y2 + 3;

            Tessellator t = Tessellator.getInstance();
            WorldRenderer w = t.getWorldRenderer();

            //int l = 0xF0100010;-267386864
            disableTexture2D();
            enableBlend();
            disableAlpha();
            tryBlendFuncSeparate(770, 771, 1, 0);
            shadeModel(7425);
            color(0.062745098f, 0f, 0.062745098f, 0.9411764706f);
            drawRect(w, instance.zLevel, x1, tooltipY - 4, x3, y1);
            t.draw();
            drawRect(w, instance.zLevel, x1, y3, x3, y2 + 4);
            t.draw();
            drawRect(w, instance.zLevel, x1, y1, x3, y3);
            t.draw();
            drawRect(w, instance.zLevel, tooltipX - 4, y1, x1, y3);
            t.draw();
            drawRect(w, instance.zLevel, x3, y1, x2 + 4, y3);
            t.draw();

            drawGradientRect(w, instance.zLevel, x1, y1 + 1, x1 + 1, y3 - 1
            );
            t.draw();
            drawGradientRect(w, instance.zLevel, x2 + 2, y1 + 1, x3, y3 - 1
            );
            t.draw();

            color(0.3137254902f, 0f, 1f, 0.3137254902f);
            drawRect(w, (int) instance.zLevel, x1, y1, x3, y1 + 1);
            t.draw();

            color(0.1568627451f, 0f, 0.4980392157f, 0.3137254902f);
            drawRect(w, (int) instance.zLevel, x1, y2 + 2, x3, y3);
            t.draw();

            color(1f, 1f, 1f, 1f);
            shadeModel(7424);
            disableBlend();
            enableAlpha();
            enableTexture2D();

            for (int k1 = 0; k1 < textLines.size(); ++k1) {
                font.drawStringWithShadow(textLines.get(k1), (float) tooltipX, (float) tooltipY, -1);

                if (k1 == 0)
                    tooltipY += 2;

                tooltipY += 10;
            }

            instance.zLevel = 0.0F;
            instance.itemRender.zLevel = 0.0F;
            enableLighting();
            enableDepth();
            RenderHelper.enableStandardItemLighting();
            enableRescaleNormal();
        }
    }

    private static void drawRect(WorldRenderer w, double z, double x, double y, double x2, double y2) {
        w.begin(7, DefaultVertexFormats.POSITION);
        w.pos(x, y, z).endVertex();
        w.pos(x, y2, z).endVertex();
        w.pos(x2, y2, z).endVertex();
        w.pos(x2, y, z).endVertex();
    }

    private static void drawGradientRect(WorldRenderer w, double z, double left, double top, double right, double bottom) {
        w.begin(7, DefaultVertexFormats.POSITION_COLOR);
        w.pos(right, top, z).color((float) 0.3137255, (float) 0.0, (float) 1.0, (float) 0.3137255).endVertex();
        w.pos(left, top, z).color((float) 0.3137255, (float) 0.0, (float) 1.0, (float) 0.3137255).endVertex();
        w.pos(left, bottom, z).color((float) 0.15686275, (float) 0.0, (float) 0.49803922, (float) 0.3137255).endVertex();
        w.pos(right, bottom, z).color((float) 0.15686275, (float) 0.0, (float) 0.49803922, (float) 0.3137255).endVertex();
    }

    @Hook("net.minecraft.item.ItemStack#getTooltip() -> line 651")
    public static List<String> getTooltip(ItemStack stack, EntityPlayer player) {
        List<String> list = new ArrayList<>();
        Item item = stack.getItem();

        boolean hasCompound = stack.hasTagCompound();
        boolean unbreakable = hasCompound && stack.getTagCompound().getBoolean("Unbreakable");

        int gearScore = item.getItemEnchantability();
        list.add(null);    //empty slot at index 0. will get filled later (ItemName)
        list.add(null);    //empty slot at index 1. will get filled later (ItemName)

        if (item instanceof ItemArmor)
            gearScore += handleArmor(list, (ItemArmor) item);
        else if (item instanceof ItemTool)
            gearScore += handleTool(list, (ItemTool) item);

        gearScore += handleModifiers(list, stack.getAttributeModifiers());

        if (hasCompound) {
            NBTTagList nbttaglist = stack.getEnchantmentTagList();

            gearScore += handleEnch(list, nbttaglist);


            if (stack.stackTagCompound.hasKey("display", 10)) {
                NBTTagCompound nbttagcompound = stack.stackTagCompound.getCompoundTag("display");
                list.add("");
                if (nbttagcompound.hasKey("color", 3))
                    list.add("Color: #" + Integer.toHexString(nbttagcompound.getInteger("color")).toUpperCase());

                if (nbttagcompound.getTagId("Lore") == 9) {
                    NBTTagList nbttaglist1 = nbttagcompound.getTagList("Lore", 8);

                    if (nbttaglist1.tagCount() > 0)
                        for (int j1 = 0; j1 < nbttaglist1.tagCount(); ++j1)
                            list.add(nbttaglist1.getStringTagAt(j1));

                }
            }
        }


        item.addInformation(stack, player, list, true);


        list.add("");

        /*Durability*/
        if (stack.isItemStackDamageable() && !unbreakable) {
            int dura = stack.getMaxDamage() - stack.getItemDamage();
            int maxDura = stack.getMaxDamage();
            gearScore += dura;
            String prefix;

            if (dura > (int) ((float) maxDura * 0.75f))
                prefix = "§aPerfect ";
            else if (dura > (maxDura >> 1))
                prefix = "§eUsed ";
            else if (dura > (int) ((float) maxDura * 0.25f))
                prefix = "§cDamaged ";
            else
                prefix = "§4Worn out ";

            list.add(prefix + dura + " / " + maxDura);
        }



        /*Rarity*/
        String rarity;
        String nameColor;
        if (unbreakable) {
            rarity = "§c§lSPECIAL";
            nameColor = "§c";
        } else if (gearScore > 2000) {
            rarity = "§d§lMYTHIC";
            nameColor = "§d";
        } else if (gearScore > 1000) {
            rarity = "§6§lLEGENDARY";
            nameColor = "§6";
        } else if (gearScore > 500) {
            rarity = "§5§lEPIC";
            nameColor = "§5";
        } else if (gearScore > 200) {
            rarity = "§a§lUNCOMMON";
            nameColor = "§A";
        } else {
            rarity = "§f§lCOMMON";
            nameColor = "§f";
        }


        String name = nameColor + stack.getDisplayName() + "§r";
        list.set(0, name);
        list.set(1, gearScore > 0 ? "Gear Score: §d" + gearScore : "");

        list.add(rarity);
        list.add(EnumChatFormatting.DARK_GRAY + Item.itemRegistry.getNameForObject(item).toString());
        return list;
    }

    private static int handleArmor(List<String> lines, ItemArmor item) {
        int protec = item.getArmorMaterial().getDamageReductionAmount(item.armorType) * 4;
        lines.add("§7Defense: §a+" + protec + "%");

        return protec * 30;
    }

    private static int handleTool(List<String> lines, ItemTool item) {
        int eff = (int) item.efficiencyOnProperMaterial;
        lines.add("§7Dig Efficiency: §a+" + eff);
        return eff * 50;
    }

    private static int handleEnch(List<String> lines, NBTTagList enchantList) {
        if (enchantList == null)
            return 0;

        int gearScore = 0;
        for (int j = 0; j < enchantList.tagCount(); ++j) {
            int k = enchantList.getCompoundTagAt(j).getShort("id");
            int level = enchantList.getCompoundTagAt(j).getShort("lvl");

            Enchantment ench = Enchants.getEnchantmentById(k);
            if (ench == null) continue;
            String s = ench.getTranslatedName(level);
            if (ench == Enchants.sharpness) {
                s += " (+" + ((float) level * 1.25f) + "⚔)";
                gearScore += level * 50;
            } else if (ench == Enchants.smite) {
                s += " (+" + ((float) level * 2.5f) + "⚔ undead)";
                gearScore += level * 40;
            } else if (ench == Enchants.baneOfArthropods) {
                s += " (+" + ((float) level * 2.5f) + "⚔ arthropods)";
                gearScore += level * 30;
            } else if (ench == Enchants.fireAspect) {
                s += " (+" + (level * 4) + "s on fire)";
                gearScore += level * 70;
            } else if (ench == Enchants.knockback) {
                s += " (+" + ((float) level * 0.3f) + " horizontal velocity)";
                gearScore += level * 60;
            } else if (ench == Enchants.power) {
                s += " (+" + ((float) level * 0.5f) + "➶)";
                gearScore += level * 150;
            } else if (ench == Enchants.flame) {
                s += " (+100s arrow on fire)";
                gearScore += 200;
            } else if (ench == Enchants.punch) {
                gearScore += level * 100;
            } else if (ench == Enchants.infinity) {
                s += " (no arrow consumption)";
                gearScore += 1000;
            } else if (ench == Enchants.unbreaking) {
                s += " (+" + (int) (((float) level / (float) (level + 1)) * 100f) + "% chance to not damage the item)";
                gearScore += level * 15;
            } else if (ench == Enchants.lure) {
                s += " (-" + (level * 5) + "s max catch time)";
                gearScore += level * 15;
            } else if (ench == Enchants.respiration) {
                s += " (+" + (int) (((float) level / (float) (level + 1)) * 100f) + "% chance not to consume oxygen)";
                gearScore += level * 50;
            } else if (ench == Enchants.aquaAffinity) {
                s += " (no water digging slowdown)";
                gearScore += 150;
            } else if (ench == Enchants.depthStrider) {
                switch (level) {
                    case 0:
                        break;
                    case 1:
                        s += " (-33% water slowness)";
                        gearScore += 30;
                        break;
                    case 2:
                        s += " (-67% water slowness)";
                        gearScore += 70;
                        break;
                    default:
                        s += " (no water slowness)";
                        gearScore += 150;
                }
            } else if (ench == Enchants.thorns) {
                s += " (+" + (int) ((float) level * 15f) + "% chance to deal thorn damage)";
                gearScore += level * 100;
            } else if (ench == Enchants.looting) {
                gearScore += level * 60;
            } else if (ench == Enchants.efficiency) {
                s += " (+" + (level * level + 1) + "⸕)";
                gearScore += level * 100;
            } else if (ench instanceof EnchantmentProtection) {
                gearScore += level * 150;
            }
            lines.add(s);
        }

        return gearScore;
    }

    /**
     * @see net.minecraft.item.ItemStack#getTooltip(EntityPlayer, boolean)
     */
    private static int handleModifiers(List<String> lines, Multimap<String, AttributeModifier> modifs) {
        int score = 0;

        if (modifs == null || modifs.isEmpty()) return score;

        StringBuilder builder = new StringBuilder();
        String gray = EnumChatFormatting.GRAY.toString();
        for (Entry<String, String> entry : attributesmapping.entrySet()) {
            Collection<AttributeModifier> collec = modifs.get(entry.getKey());
            if (collec != null && !collec.isEmpty()) {
                AttributeModifier mod = (AttributeModifier) collec.toArray()[0];
                builder.setLength(0);
                builder.append(gray);
                builder.append(entry.getValue());
                builder.append("+");
                double amount = mod.getAmount();
                score += ((int) amount - 2) * 40;
                builder.append(ItemStack.DECIMALFORMAT.format(amount));
                int op = mod.getOperation();
                if (op == 1)
                    builder.append("%");
                else if (op == 2)
                    builder.append("% base");

                lines.add(builder.toString());
            }
        }
        lines.add("");
        return score;
    }

}
