import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class AnimationRunnable implements Runnable{
    private Color currentColor;
    private Color targetColor;
    private long sleepInterval;
    private int type;
    private JComponent component;
    private boolean reversible;
    private boolean toNull;

    AnimationRunnable (@Nullable Color currentColor, @NotNull Color targetColor, long sleepInterval, @NotNull JComponent component, boolean reversible, boolean toNull) {
        this.currentColor = currentColor == null ? UIManager.getColor("Button.background") : currentColor;
        this.targetColor = targetColor;
        this.sleepInterval = sleepInterval;
        this.component = component;
        this.reversible = reversible;
        this.toNull = toNull;
        type = 0;
    }
    @Override
    public void run() {
        switch (type) {
            case 0 -> {
                System.out.println(String.valueOf(Thread.currentThread())+currentColor.getRed()+", "+currentColor.getGreen()+", "+currentColor.getRed());
                colorGradient(currentColor, targetColor, sleepInterval, component);
                if (reversible) colorGradient(targetColor, currentColor, sleepInterval, component);
                if (toNull) component.setBackground(null);
            }
        }
    }

    public static void colorGradient(@Nullable Color currentColor, @NotNull Color targetColor, long sleepInterval, JComponent component) {
        component.setBackground(currentColor);
        while (component.getBackground().getRGB() != targetColor.getRGB()) {
            component.setBackground(new Color(component.getBackground().getRed() + (int)Math.signum(targetColor.getRed() - component.getBackground().getRed()), component.getBackground().getGreen() + (int)Math.signum(targetColor.getGreen() - component.getBackground().getGreen()), component.getBackground().getBlue() + (int)Math.signum(targetColor.getBlue() - component.getBackground().getBlue())));
            try {
                Thread.sleep(sleepInterval);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
