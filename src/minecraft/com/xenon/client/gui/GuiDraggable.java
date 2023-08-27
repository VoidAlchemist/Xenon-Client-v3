package com.xenon.client.gui;

import com.xenon.XenonClient;
import com.xenon.modules.ModSettings;
import com.xenon.modules.ScreenPos;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

/**
 *
 *
 * @author Zenon
 * @since v2.0
 */
public class GuiDraggable extends GuiScreen {

    private final Set<Draggable> renderList;
    private int prevMouseX, prevMouseY;
    private Optional<Draggable> selected;
    private ScaledResolution sr;
    private final MouseOverFinder mouseOverFinder = new MouseOverFinder();

    public GuiDraggable() {
        renderList = new HashSet<>(8);
        createModuleDummies();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            this.mc.displayGuiScreen(null);
            System.gc();
        }
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawDefaultBackground();

        sr = new ScaledResolution(this.mc);

        for (Draggable drag : renderList) {
            int x = drag.pos.getAbsX(sr);
            int y = drag.pos.getAbsY(sr);

            drawBoundingRect(x - 1, y - 1, drag.width + 1, drag.height + 1);
        }
    }



    private void drawBoundingRect(int x, int y, int w, int h) {
        this.drawHorizontalLine(x, x + w, y, -1);
        this.drawHorizontalLine(x, x + w, y + h, -1);

        this.drawVerticalLine(x, y, y + h, -1);
        this.drawVerticalLine(x + w, y, y + h, -1);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        prevMouseX = mouseX;
        prevMouseY = mouseY;
        mouseOverFinder.update(mouseX, mouseY, sr);
        selected = renderList.stream().filter(mouseOverFinder).findFirst();
    }



    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (!selected.isPresent()) return;
        Draggable drag = selected.get();

        int newX = drag.pos.getAbsX(sr) + mouseX - prevMouseX;
        int newY = drag.pos.getAbsY(sr) + mouseY - prevMouseY;

        newX = Math.max(0, Math.min(sr.getScaledWidth() - drag.width, newX));
        newY = Math.max(0, Math.min(sr.getScaledHeight() - drag.height, newY));

        drag.pos.setFromAbs(newX, newY, sr);
        prevMouseX = mouseX;
        prevMouseY = mouseY;
    }



    private void createModuleDummies() {
        ModSettings settings = XenonClient.instance.settings;

        if (settings.fpsScale > 0.02f)
            renderList.add(new Draggable(settings.fpsxy, 30, 11, settings.fpsScale));

        if (settings.posScale > 0.02f)
            renderList.add(new Draggable(settings.posxy, 30, 30, settings.posScale));

        if (settings.biomeScale > 0.02f)
            renderList.add(new Draggable(settings.biomexy, 60, 11, settings.biomeScale));

        if (settings.armorstatusScale > 0.02f)
            renderList.add(new Draggable(settings.armorstatusxy, 18, 78, settings.armorstatusScale));

        if (settings.keystrokesScale > 0.02f)
            renderList.add(new Draggable(settings.keystrokesxy, 61, 61, settings.keystrokesScale));

        if (settings.potionoverlayScale > 0.02f)
            renderList.add(new Draggable(settings.potionoverlayxy, 130, 50, settings.potionoverlayScale));

        if (settings.mapScale > 0.02f)
            renderList.add(new Draggable(settings.mapxy, 128, 128, settings.mapScale));
    }

    private static class Draggable {
        final ScreenPos pos;
        final int width, height;

        Draggable(ScreenPos pos, int width, int height, float scale) {
            this.pos = pos;
            this.width = (int) ((float) width * scale);
            this.height = (int) ((float) height * scale);
        }

    }

    static class MouseOverFinder implements Predicate<Draggable> {

        private int x, y;
        private ScaledResolution sr;

        public void update(int x, int y, ScaledResolution sr) {
            this.x = x;
            this.y = y;
            this.sr = sr;
        }

        @Override
        public boolean test(Draggable d) {
            int x1 = d.pos.getAbsX(sr);
            int y1 = d.pos.getAbsY(sr);

            return x1 <= x && x <= x1 + d.width && y1 <= y && y <= y1 + d.height;
        }
    }

}