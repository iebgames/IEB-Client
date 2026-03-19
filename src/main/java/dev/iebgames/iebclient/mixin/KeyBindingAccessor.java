package dev.iebgames.iebclient.mixin;

import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyBinding.class)
public interface KeyBindingAccessor {
    @Accessor("pressed")
    boolean isPressed();

    @Accessor("pressed")
    void setPressed(boolean value);
}
