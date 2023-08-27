package com.xenon.client.gui.modulesconf.components;

public abstract class UpDown extends Component {

    protected final boolean autonomous;
    protected final int[] values;
    protected int currentValueIndex;

    public UpDown(boolean autonomous, int[] values, int width, int height) {
        super(width, height);
        this.autonomous = autonomous;
        this.values = values;
    }

    public UpDown(boolean autonomous, int[] values, int width) {
        this(autonomous, values, width, 15);
    }


    public UpDown setInitialValue(int value) {
        int i = 0;
        for (; i < values.length; i++)
            if (values[i] == value)
                break;

        if (i == values.length)
            throw new IllegalArgumentException("Cannot set initial value to something that's not" +
                    " in constructor possible values");

        currentValueIndex = i;
        return this;
    }

    @Override
    public final void paint(Graphics g) {
        super.paint(g);
        g.drawRect(width, height, 0xFF555555);
        final int midHeight = height >> 1;
        final int midWidth = width >> 1;

        int x1, y1;

        x1 = x + midWidth;
        y1 = y;
        g.drawGradientRect(x1, y1, midWidth, midHeight + 1, 0xFF999999, 0xFF444444);
        g.drawCenteredString("+", x1 + (midWidth >> 1), y1 - 2, 0xFFFFFFFF);

        if (x1 <= g.mouseX && g.mouseX <= x1 + midWidth && y1 <= g.mouseY && g.mouseY <= y1 + midHeight + 1)
            g.drawRect(x1, y1, midWidth, midHeight + 1, 0x30FFFFFF);

        y1 += midHeight + 1;
        g.drawGradientRect(x1, y1, midWidth, midHeight, 0xFF999999, 0xFF444444);
        g.drawCenteredString("-", x1 + (midWidth >> 1) - 1, y1 - 3, 0xFFFFFFFF);

        if (x1 <= g.mouseX && g.mouseX <= x1 + midWidth && y1 <= g.mouseY && g.mouseY <= y1 + midHeight)
            g.drawRect(x1, y1, midWidth, midHeight, 0x30FFFFFF);

        g.drawCenteredString(String.valueOf(values[currentValueIndex]), x + (width >> 2), y + midHeight - 5,
                0xFFFFFFFF);

        if (autonomous)
            g.translatebrush(0, height);
    }

    @Override
    public void clickListener(int mouseX, int mouseY) {

        if (x + width - (width >> 1) <= mouseX && mouseX <= x + width) {
            if (y <= mouseY && mouseY <= y + (height >> 1))
                currentValueIndex = Math.min(currentValueIndex + 1, values.length - 1);
            else if (y + (height >> 1) <= mouseY && mouseY <= y + height)
                currentValueIndex = Math.max(currentValueIndex - 1, 0);
        }
    }
}
