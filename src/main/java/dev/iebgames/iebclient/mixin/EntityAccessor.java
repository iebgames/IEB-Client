package dev.iebgames.iebclient.mixin;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Accessor("isInWeb")
    boolean getIsInWeb();

    @Accessor("isInWeb")
    void setIsInWeb(boolean value);
}
