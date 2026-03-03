package dev.iebgames.iebclient.mixin;

import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.module.modules.render.XRay;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderGlobal.class)
public class MixinRenderGlobal {

    @Inject(method = "shouldSideBeRendered", at = @At("HEAD"), cancellable = true)
    private void onSideRender(Block block, BlockPos pos, EnumFacing side, CallbackInfoReturnable<Boolean> cir) {
        XRay xray = (XRay) IEBClient.INSTANCE.moduleManager.getModule("XRay");
        if (xray != null && xray.isEnabled()) {
            cir.setReturnValue(xray.isXRayBlock(block));
        }
    }
}
