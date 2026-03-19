package dev.iebgames.iebclient.module.modules.combat;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventUpdate;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.setting.BooleanSetting;
import dev.iebgames.iebclient.setting.NumberSetting;
import dev.iebgames.iebclient.util.TimerUtils;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class AutoClicker extends Module {

    private final NumberSetting  cpsMin = register(new NumberSetting("CPS Min", 9, 1, 20, 1));
    private final NumberSetting  cpsMax = register(new NumberSetting("CPS Max", 14, 1, 20, 1));
    private final BooleanSetting left   = register(new BooleanSetting("Left Click", true));
    private final BooleanSetting right  = register(new BooleanSetting("Right Click", false));

    private final TimerUtils timerL = new TimerUtils();
    private final TimerUtils timerR = new TimerUtils();
    private long delayL = 100, delayR = 100;

    public AutoClicker() {
        super("AutoClicker", "Sol/Sağ tık otomatik basar.", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onUpdate(EventUpdate e) {
        if (left.isEnabled() && Mouse.isButtonDown(0)) {
            if (timerL.hasReached(delayL)) {
                KeyBinding.onTick(mc.gameSettings.keyBindAttack.getKeyCode());
                delayL = computeDelay();
                timerL.reset();
            }
        }
        if (right.isEnabled() && Mouse.isButtonDown(1)) {
            if (timerR.hasReached(delayR)) {
                KeyBinding.onTick(mc.gameSettings.keyBindUseItem.getKeyCode());
                delayR = computeDelay();
                timerR.reset();
            }
        }
    }

    private long computeDelay() {
        int min = cpsMin.getInt(), max = cpsMax.getInt();
        if (min > max) { int t = min; min = max; max = t; }
        int cps = min + (int)(Math.random() * (max - min + 1));
        return 1000L / Math.max(1, cps);
    }
}
