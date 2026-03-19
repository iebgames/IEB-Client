package dev.iebgames.iebclient.mixin;

import dev.iebgames.iebclient.IEBClient;
import dev.iebgames.iebclient.module.modules.render.BlockOverlay;
import dev.iebgames.iebclient.module.modules.render.ClickGUIModule;
import dev.iebgames.iebclient.util.ColorUtils;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderGlobal.class)
public class MixinRenderGlobal {

    @Inject(method = "drawSelectionBox", at = @At("HEAD"), cancellable = true)
    public void onDrawSelectionBox(EntityPlayer player, MovingObjectPosition movingObjectPositionIn, int execute, float partialTicks, CallbackInfo ci) {
        BlockOverlay blockOverlay = IEBClient.moduleManager.getModule(BlockOverlay.class);
        if (blockOverlay != null && blockOverlay.isToggled()) {
            ci.cancel();

            if (movingObjectPositionIn.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glLineWidth(2.0f);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDepthMask(false);

                BlockPos pos = movingObjectPositionIn.getBlockPos();
                double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
                double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
                double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
                
                ClickGUIModule theme = IEBClient.moduleManager.getModule(ClickGUIModule.class);
                int color = theme.blockOverlayColor.isChroma() ? ColorUtils.getChroma(3000, 0) : theme.blockOverlayColor.getColor();
                float r = ((color >> 16) & 0xFF) / 255f;
                float g = ((color >> 8) & 0xFF) / 255f;
                float b = (color & 0xFF) / 255f;
                float a = ((color >> 24) & 0xFF) / 255f;
                if (a == 0) a = 0.4f;

                GL11.glColor4f(r, g, b, a);
                
                net.minecraft.util.AxisAlignedBB bb = net.minecraft.client.Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock()
                    .getSelectedBoundingBox(net.minecraft.client.Minecraft.getMinecraft().theWorld, pos)
                    .expand(0.002, 0.002, 0.002)
                    .offset(-x, -y, -z);

                net.minecraft.client.renderer.RenderGlobal.drawSelectionBoundingBox(bb);

                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_BLEND);
            }
        }
    }
}
