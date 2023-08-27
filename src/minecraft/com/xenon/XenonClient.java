package com.xenon;

import com.xenon.client.gui.GuiDraggable;
import com.xenon.client.gui.SplashScreen;
import com.xenon.modules.ModSettings;
import com.xenon.modules.api.*;
import com.xenon.modules.api.dungeons.DungeonAPI;
import com.xenon.util.FileManager;
import com.xenon.util.PRNG;
import com.xenon.util.Printer;
import com.xenon.util.RenderUtils;
import com.xenon.util.readability.Hook;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

/**
 * XenonClient v2.0 now released.<br>
 * The source code now contains a Hook annotation which indicates the target where we call the function.<br>
 * This is mainly a replacement for an event system.<br><br>
 * <p>
 * <h1>Silent Patches (the user can't disable them) : </h1><br>
 *
 * <br>
 * <ul>
 *     <li>In {@link net.minecraft.client.entity.AbstractClientPlayer}, overrode getLook, added the method:<pre><code>
 * public Vec3 getLook(float partialTicks) {
 *     return this.getVectorForRotation(this.rotationPitch, this.rotationYaw);
 * }</code></pre></li>
 *
 *      <li>In net.minecraft.client.gui.GuiMainMenu, removed <pre><code>
 * private static final AtomicInteger field_175373_f = new AtomicInteger(0);</pre></code>
 *          which isn't used anywhere (in the entire project).</li>
 *
 *      <li>Replace every single occurrence of {@link java.util.Random} with {@link com.xenon.util.PRNG},
 *          in order for MC to no longer use a thread-safe version.
 *          Note : you cannot set the seed with {@link java.util.concurrent.ThreadLocalRandom}.</li>
 *
 *      <li>Chat messages now have their own rectangle background width, allowing for less hindrance HUD-wise :<br>
 *          In {@link net.minecraft.client.gui.GuiNewChat#drawChat(int)}, line 80 is now <br><pre><code>
 * drawRect(0, j2 - 9, mc.fontRendererObj.getStringWidth(s) + 4, j2, l1 / 2 << 24);</code></pre></li>
 *
 *      <li>Removed <code>INetHandlerPlayServer</code> and <code>INetHandlerPlayClient</code> in favor of their direct
 *          implementation for less pointless dynamic dispatch.</li>
 *      <li>Removed every occurrence of {@link net.minecraft.client.entity.EntityPlayerSP#sprintingTicksLeft}
 *      which causes sprint to be reset every 30 seconds.</li>
 * </ul>
 *
 * @author Zenon
 * @version 2.0
 * @see com.xenon.client.patches
 */
public final class XenonClient {


    public static final XenonClient instance = new XenonClient();
    public final Printer printer = new Printer();
    public CPSCounter cpsCounter = new CPSCounter();
    public boolean shouldToggleSprint = true;
    public boolean glowingEnabled;
    public ModSettings settings;
    public SplashScreen splashScreen;
    public Minecraft minecraft;
    public PRNG random = new PRNG();


    private XenonClient() {
    }

    public static void main(String[] args) {

        net.minecraft.client.main.Main.main(args);
    }

    @Hook("net.minecraft.client.main.Main#main -> line 117")
    public void preInit() {
        printer.info("initializing client without GL11 context...");
        FileManager.init();
        settings = FileManager.instanciateModSettings();
        AntiGriefAPI.init();
    }

    @Hook("net.minecraft.client.Minecraft#startGame() -> line 504")
    public void init() {
        printer.info("initializing client with GL11 context...");
        RenderUtils.init();
        splashScreen = new SplashScreen();
        splashScreen.setProgress("XenonClient | Finishing initialization");
    }

    @Hook("net.minecraft.client.Minecraft#startGame() -> line 583")
    public void postInit() {
        printer.info("Finished initializing client");
        splashScreen = null;    //discard the splashScreen object.
        minecraft = Minecraft.getMinecraft();
    }

    @Hook("net.minecraft.client.Minecraft#shutdownMinecraftApplet() -> line 1058")
    public void shutdown() {
        printer.info("shutting down client...");
        FileManager.writeToJson(FileManager.main_conf, settings);
    }

    @Hook("net.minecraft.client.Minecraft#onTick() -> line 2277")
    public void tick() {

        if (Minecraft.getMinecraft().theWorld == null)
            return;

        if (settings.glintChroma)
            GlintEnchantAPI.call();

        if (minecraft.gameSettings.xenonGlowingToggled.isPressed())
            glowingEnabled = !glowingEnabled;

        if (minecraft.gameSettings.xenonPhantomCamToggled.isPressed())
            PhantomCamAPI.toggle();

        MurderMysteryAPI.update();
        DungeonAPI.update();
        SwordsAPI.tick();

        if (minecraft.gameSettings.xenonBindModules.isPressed())
            minecraft.displayGuiScreen(new GuiDraggable());
    }

    @Hook("net.minecraft.client.network.NetHandlerPlayClient#handleJoinGame --> line 154")
    public void onJoinGame() {
        PhantomCamAPI.onJoinGame();
        MurderMysteryAPI.onJoinGame();
        DungeonAPI.onJoinGame();
        GlowingAPI.onJoinGame();
    }




    /**
     * Displays a client-side message in the chat.
     * @param s the message to display
     */
    public void sendmsg(String s) {
        minecraft.thePlayer.addChatComponentMessage(new ChatComponentText(s));
    }

    /**
     * Displays a client-side error message in the chat.
     * @param s the error message to display.
     */
    public void senderr(String s) {
        sendmsg(EnumChatFormatting.RED + s);
    }

}