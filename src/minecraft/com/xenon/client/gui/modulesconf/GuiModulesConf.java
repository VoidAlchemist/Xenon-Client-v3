package com.xenon.client.gui.modulesconf;

import com.xenon.XenonClient;
import com.xenon.client.gui.components.IconButton;
import com.xenon.client.gui.modulesconf.components.*;
import com.xenon.modules.ModSettings;
import com.xenon.modules.api.GlintEnchantAPI;
import com.xenon.modules.api.XenonEntityRenderer;
import com.xenon.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zenon
 */
public class GuiModulesConf extends GuiScreen {

    private static final ResourceLocation background = new ResourceLocation("xenon/background_settings.png");
    private static final String quitInfo = "Click to quit Xenon settings GUI.";

    private final GuiScreen prev;
    public final ModSettings settings;
    private int currentTab = 1; //"Global Settings";
    private ConfFrame f;
    private final List<Component> globalTab = new ArrayList<>(5);
    private final List<Component> hudTab = new ArrayList<>(5);
    private final List<Component> qolTab = new ArrayList<>(1);

    /**
     * Creates a new GuiTest object with the current Xenon settings.
     */
    public GuiModulesConf(GuiScreen prev) {
        this.prev = prev;
        settings = XenonClient.instance.settings;
    }

    /**
     * Creates the frame and its content. To swap tab, call {@link #initFrame()}, not this method.
     */
    @Override
    public void initGui() {
        f = new ConfFrame(
                width / 5,
                height / 10,
                3 * width / 5,
                8 * height / 10,
                5);    //capacity is the maximum of globalTab capacity & hudTab capacity.
        buttonList.add(new IconButton(0, width - 40, 20, 25, 25, 0, quitInfo));
        buttonList.add(new Tab(1, "Global Settings", (width >> 1) - 121, 5, 80, 15));
        buttonList.add(new Tab(2, "HUD Related", (width >> 1) - 40, 5, 80, 15));
        buttonList.add(new Tab(3, "QOL", (width >> 1) + 41, 5, 80, 15));
        initFrame();
    }

    /**
     * Initialize the frame with the current tab configuration. Call this method each time you swap tabs.
     */
    public void initFrame() {
        f.reset();
        unselectAllTabs();

        ((Tab) this.buttonList.get(currentTab)).isSelected = true;
        List<Component> tabComponents;

        switch (currentTab) {
            case 1: // "Global Settings"
                tabComponents = globalTab;
                initGlobalIfNeeded();
                break;
            case 2: // "HUD Related"
                tabComponents = hudTab;
                initHUDIfNeeded();
                break;
            case 3:
                tabComponents = qolTab;
                initQOLIfNeeded();
                break;
            default:
                throw new RuntimeException("Should never happen");
        }
        f.addAllComponent(tabComponents);
    }

    /**
     * Draws the gui to screen.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawbackground();
        f.updateBounds(this.width / 5, this.height / 10, 3 * this.width / 5, 8 * this.height / 10);
        f.updateMousePos(mouseX, mouseY);
        f.draw();
        super.drawScreen(mouseX, mouseY, partialTicks);    //draws vanilla buttons to screen.
    }

    private void drawbackground() {
        GlStateManager.color(1f, 1f, 1f, 1f);
        mc.getTextureManager().bindTexture(background);
        RenderUtils.drawTexturedRect((int) this.zLevel, 0, 0, width, height);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton != 0) return;
        super.mouseClicked(mouseX, mouseY, mouseButton);

        f.onclick();
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        f.mouseReleased();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0)
            this.mc.displayGuiScreen(prev);
        else if (button instanceof Tab) {
            Tab tab = (Tab) button;
            currentTab = tab.id;
            initFrame();
        }
    }

    /**
     * Sets the selected state of all the tab instances in this gui to the <code>selected</code> value.
     */
    protected void unselectAllTabs() {
        for (GuiButton b : this.buttonList) {
            if (b instanceof Tab)
                ((Tab) b).isSelected = false;
        }
    }

    /**
     * Creates Category objects for the global tab only if not done yet.
     */
    private void initGlobalIfNeeded() {
        if (globalTab.size() > 0) return;

        Category c2 = new Category(63, 3, "Toggle Sprint");
        c2.addLine("Toggle Sprint enabled", new OnOff(false, settings.toggleSprint) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.toggleSprint = this.value;
            }
        });
        c2.addLine("Toggle Sneak enabled", new OnOff(false, settings.toggleSneak) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.toggleSneak = this.value;
            }
        });
        c2.addLine("Fly boost factor", new Slider(false, 30) {
            @Override
            public String onSlide() {
                return Integer.toString(settings.flyBoost = (int) (this.value * 10f));
            }
        }.setInitialValue(settings.flyBoost, 10));
        globalTab.add(c2);

        Category c3 = new Category(111, 7, "1.7 PvP");
        c3.addLine("1.7 BlockHit Animation", new OnOff(false, settings.oldBlockhit) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.oldBlockhit = this.value;
            }
        });
        c3.addLine("> 1.7 Hit Registration (Risky on 1.8+)", new OnOff(false, settings.oldHitreg) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.oldHitreg = this.value;
            }
        });
        c3.addLine("1.7 Sneaking Animation (Only works solo)", new OnOff(false, settings.oldSneak) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.oldSneak = this.value;
            }
        });
        c3.addLine("1.7 Hearts Animation", new OnOff(false, settings.oldhearts) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.oldhearts = this.value;
            }
        });
        c3.addLine("1.7 Damage Animation", new OnOff(false, settings.oldDamages) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.oldDamages = this.value;
            }
        });
        c3.addLine("1.7 Bow Rendering", new OnOff(false, settings.oldBow) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.oldBow = this.value;
            }
        });
        c3.addLine("1.7 Fishing Rod Rendering", new OnOff(false, settings.oldFishingRod) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.oldFishingRod = this.value;
            }
        });
        globalTab.add(c3);

        Category c4 = new GlintCategory(119, 5, "Glint Colorizer", new Component(40, 20) {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.drawGradientRect(width, height, 0xFF404040, 0xFF090909);
                g.drawRect(x + 1, y + 1, width - 2, height - 2, settings.glintColor);
            }

            @Override
            public void clickListener(int mouseX, int mouseY) {
            }

        });
        c4.addLine("RED Amount", new Slider(false, 50) {
            @Override
            public String onSlide() {
                int newRed = (int) (this.value * 255f);
                settings.glintColor = (settings.glintColor & 0xFF00FFFF) | (newRed << 16);

                return Integer.toString(newRed);
            }
        }.setInitialValue((settings.glintColor >> 16 & 255), 255));
        c4.addLine("GREEN Amount", new Slider(false, 50) {
            @Override
            public String onSlide() {
                int newGreen = (int) (this.value * 255f);
                settings.glintColor = (settings.glintColor & 0xFFFF00FF) | (newGreen << 8);

                return Integer.toString(newGreen);
            }
        }.setInitialValue((settings.glintColor >> 8 & 255), 255));
        c4.addLine("BLUE Amount", new Slider(false, 50) {
            @Override
            public String onSlide() {
                int newBlue = (int) (this.value * 255f);
                settings.glintColor = (settings.glintColor & 0xFFFFFF00) | (newBlue);

                return Integer.toString(newBlue);
            }
        }.setInitialValue((settings.glintColor & 255), 255));
        c4.addLine("> Chroma enabled", new OnOff(false, settings.glintChroma) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.glintChroma = this.value;
            }
        });
        c4.addLine("RESET Glint Color", new Component(30, 15) {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.drawGradientRect(width, height, 0xFF303030, 0xFF000000);
                if (isMouseOver(g.mouseX, g.mouseY))
                    g.drawRect(width, height, 0x30FFFFFF);
            }

            @Override
            public void clickListener(int mouseX, int mouseY) {
                GlintEnchantAPI.resetColor();    //little glitch here, the RGB sliders won't get updated.
            }
        });
        globalTab.add(c4);


        Category c5 = new Category(199, 11, "Misc");
        c5.addLine("> Fullbright enabled", new OnOff(false, settings.fullbright) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.fullbright = this.value;
                if (settings.fullbright)
                    ((XenonEntityRenderer) Minecraft.getMinecraft().entityRenderer).setFullBright();
            }
        });
        c5.addLine("Show own name", new OnOff(false, settings.showOwnName) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.showOwnName = this.value;
            }
        });
        c5.addLine("> Show own armor", new OnOff(false, settings.showOwnArmor) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.showOwnArmor = this.value;
            }
        });
        c5.addLine("> Show other capes", new OnOff(false, settings.showCapes) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.showCapes = this.value;
            }
        });
        c5.addLine("> Cape particles enabled", new OnOff(false, settings.capeParticles) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.capeParticles = this.value;
            }
        });
        c5.addLine("> FOV Speed Mofidier enabled", new OnOff(false, settings.speedFOV) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.speedFOV = this.value;
            }
        });
        c5.addLine("Show saturation", new OnOff(false, settings.saturation) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.saturation = this.value;
            }
        });
        c5.addLine("> Dark Background in menu enabled", new OnOff(false, settings.darkBackground) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.darkBackground = this.value;
            }
        });
        c5.addLine("> Reequip Animation delay (%)", new Slider(false, 50) {
            @Override
            public String onSlide() {
                return Integer.toString((int) ((settings.reequipTime = this.value) * 100f));
            }
        }.setInitialValuePercent(settings.reequipTime));
        c5.addLine("Enhanced Tooltip", new OnOff(false, settings.enhancedToolTip) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.enhancedToolTip = this.value;
            }
        });
        globalTab.add(c5);
    }


    /**
     * Creates Category objects for the hud tab only if not done yet.
     */
    public void initHUDIfNeeded() {
        if (hudTab.size() > 0) return;

        Category c1 = new Category(155, 9, "Vanilla Overlays");
        c1.addLine("> Nausea effect", new OnOff(false, settings.nausea) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.nausea = this.value;
            }
        });
        c1.addLine("> Pumpkin Overlay", new OnOff(false, settings.pumpkinOverlay) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.pumpkinOverlay = this.value;
            }
        });
        c1.addLine("BossBar displayed", new OnOff(false, settings.bossBar) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.bossBar = this.value;
            }
        });
        c1.addLine("Horse jump bar displayed", new Slider(false, 40) {

            @Override
            public String onSlide() {
                return Integer.toString((int) ((settings.horsejumpbarScale = this.value) * 100f));
            }
        }.setInitialValuePercent(settings.horsejumpbarScale));
        c1.addLine("> Disc name displayed", new OnOff(false, settings.discDisplayed) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.discDisplayed = this.value;
            }
        });
        c1.addLine("Scoreboard scale", new Slider(false, 40) {
            @Override
            public String onSlide() {
                return Integer.toString((int) ((settings.scoreboardScale = this.value) * 100f));
            }
        }.setInitialValuePercent(settings.scoreboardScale));
        c1.addLine("Titles scale", new Slider(false, 40) {
            @Override
            public String onSlide() {
                return Integer.toString((int) ((settings.titleScale = this.value) * 100f));
            }
        }.setInitialValuePercent(settings.titleScale));
        c1.addLine("Titles Opacity", new Slider(false, 40) {
            @Override
            public String onSlide() {
                return Integer.toString(settings.titlealpha = (int) (this.value * 100f));
            }
        }.setInitialValue(settings.titlealpha, 255));
        c1.addLine("Chat Background enabled", new OnOff(false, settings.chatBackground) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.chatBackground = this.value;
            }
        });
        hudTab.add(c1);

        Category c2 = new Category(80, 4, "Extra Information");
        c2.addLine("FPS Text scale", new Slider(false, 40) {
            @Override
            public String onSlide() {
                return Integer.toString((int) ((settings.fpsScale = this.value) * 100f));
            }
        }.setInitialValuePercent(settings.fpsScale));
        c2.addLine("Position Text scale", new Slider(false, 40) {
            @Override
            public String onSlide() {
                return Integer.toString((int) ((settings.posScale = this.value) * 100f));
            }
        }.setInitialValuePercent(settings.posScale));
        c2.addLine("Biome Text scale", new Slider(false, 40) {
            @Override
            public String onSlide() {
                return Integer.toString((int) ((settings.biomeScale = this.value) * 100f));
            }
        }.setInitialValuePercent(settings.biomeScale));
        c2.addLine("Potion Overlay scale", new Slider(false, 40) {
            @Override
            public String onSlide() {
                return Integer.toString((int) ((settings.potionoverlayScale = this.value) * 100f));
            }
        }.setInitialValuePercent(settings.potionoverlayScale));
        hudTab.add(c2);

        Category c3 = new Category(46, 2, "Keystrokes");
        c3.addLine("Keystrokes scale", new Slider(false, 40) {
            @Override
            public String onSlide() {
                return Integer.toString((int) ((settings.keystrokesScale = this.value) * 100f));
            }
        }.setInitialValuePercent(settings.keystrokesScale));
        c3.addLine("> CPS Counter enabled", new OnOff(false, settings.keystrokesCPS) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.keystrokesCPS = this.value;
            }
        });
        hudTab.add(c3);

        Category c4 = new Category(46, 2, "ArmorStatus");
        c4.addLine("ArmorStatus scale", new Slider(false, 40) {
            @Override
            public String onSlide() {
                return Integer.toString((int) ((settings.armorstatusScale = this.value) * 100f));
            }
        }.setInitialValuePercent(settings.armorstatusScale));
        c4.addLine("Show Durability", new OnOff(false, settings.armorstatusDurability) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.armorstatusDurability = this.value;
            }
        });
        hudTab.add(c4);

        Category c5 = new Category(29, 1, "Map");
        c5.addLine("Map scale", new Slider(false, 40) {
            @Override
            public String onSlide() {
                return Integer.toString((int) ((settings.mapScale = this.value) * 100f));
            }
        }.setInitialValuePercent(settings.mapScale));
        hudTab.add(c5);
    }

    private void initQOLIfNeeded() {
        if (qolTab.size() > 0)  return;

        Category c = new Category(216, 12, "QOL");
        c.addLine("> Show invisible entities", new OnOff(false, settings.showInvisibleEntities) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.showInvisibleEntities = this.value;
            }
        });
        c.addLine("Enable blindness effect", new OnOff(false, settings.blindness) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.blindness = this.value;
            }
        });
        c.addLine("> Enable Murder Mystery helper", new OnOff(false, settings.murderMysteryHelper) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.murderMysteryHelper = this.value;
            }
        });
        c.addLine("> Enable SB Dungeons helper", new OnOff(false, settings.dungeonHelper) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.dungeonHelper = this.value;
            }
        });
        c.addLine("> Enable SB Dungeon F7++", new OnOff(false, settings.dungeonF7PlusPlus) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.dungeonF7PlusPlus = this.value;
            }
        });
        c.addLine("Ticks between SB Dungeon updates", new UpDown(false, new int[]{1, 2, 4, 8, 16}, 30) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.dungeonUpdateDelay = this.values[this.currentValueIndex];
            }
        }.setInitialValue(settings.dungeonUpdateDelay));
        c.addLine("> Hide lightning", new OnOff(false,
                settings.hideLightning) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.hideLightning = this.value;
            }
        });
        c.addLine("> Show barrier blocks", new OnOff(false, settings.blockBarrierRenderType == 3) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.blockBarrierRenderType = this.value ? 3 : -1;
            }
        });
        c.addLine("1.9+ sword blocking (no slow)", new OnOff(false, settings.noSlowSword) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.noSlowSword = this.value;
            }
        });
        c.addLine("Delay between iron sword blocks duplication (set to 0 to disable)", new UpDown(
                false,
                new int[]{0, 1, 2, 4, 8, 16}, 30
        ) {
            @Override
            public void clickListener(int mouseX, int mouseY) {
                super.clickListener(mouseX, mouseY);
                settings.duplicateRightClickTicks = this.values[this.currentValueIndex];
            }
        }.setInitialValue(settings.duplicateRightClickTicks));
        c.addLine("Enable better single player fly (no drift & no undo flying by sneaking)",
                new OnOff(false, settings.betterFly) {
                    @Override
                    public void clickListener(int mouseX, int mouseY) {
                        super.clickListener(mouseX, mouseY);
                        settings.betterFly = this.value;
                    }
        });
        c.addLine("Ticks between block placement (4 is default) (risky on servers)",
                new UpDown(false, new int[]{0, 1, 2, 3, 4}, 30) {
                    @Override
                    public void clickListener(int mouseX, int mouseY) {
                        super.clickListener(mouseX, mouseY);
                        settings.placeDelay = this.values[this.currentValueIndex];
                    }
                }.setInitialValue(settings.placeDelay));
        qolTab.add(c);

    }

}
