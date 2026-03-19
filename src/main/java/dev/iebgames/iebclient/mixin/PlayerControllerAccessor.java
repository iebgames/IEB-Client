package dev.iebgames.iebclient.mixin;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerControllerMP.class)
public interface PlayerControllerAccessor {
    @Accessor("curBlockDamageMP")
    float getCurBlockDamageMP();

    @Accessor("curBlockDamageMP")
    void setCurBlockDamageMP(float value);

    @Accessor("blockHitDelay")
    int getBlockHitDelay();

    @Accessor("blockHitDelay")
    void setBlockHitDelay(int value);

    @Accessor("isHittingBlock")
    boolean isIsHittingBlock();
}
