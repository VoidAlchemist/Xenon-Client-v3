package com.xenon.modules;

import net.minecraft.client.gui.ScaledResolution;

/**
 * Used for 2D positioning in GUIs.
 *
 * @author Zenon
 */
public class ScreenPos {

    public double x, y;

    /**
     * Basic constructor to set relative coordinates.
     *
     * @param x
     * @param y
     */
    public ScreenPos(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public int getAbsX(ScaledResolution r) {
        return (int) (x * r.getScaledWidth());
    }

    public int getAbsY(ScaledResolution r) {
        return (int) (y * r.getScaledHeight());
    }

    public void setFromAbs(int x, int y, ScaledResolution r) {
        this.x = (double) x / r.getScaledWidth();
        this.y = (double) y / r.getScaledHeight();
    }

    @Override
    public String toString() {
        return "Screen Position : x=" + this.x + ", y=" + this.y;
    }
}
