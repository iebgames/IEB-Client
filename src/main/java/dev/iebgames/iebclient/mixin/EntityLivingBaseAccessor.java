package dev.iebgames.iebclient.mixin;

import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityLivingBase.class)
public interface EntityLivingBaseAccessor {
    @Accessor("jumpTicks")
    int getJumpTicks();

    @Accessor("jumpTicks")
    void setJumpTicks(int ticks);
}
