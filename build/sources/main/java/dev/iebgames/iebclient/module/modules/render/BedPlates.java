package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventRender3D;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import net.minecraft.block.BlockBed;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class BedPlates extends Module {

    public BedPlates() {
        super("BedPlates", "Yatakların üzerinde durum bilgisi gösterir.", Category.RENDER, Keyboard.KEY_NONE);
    }

    @EventHook
    public void onRender3D(EventRender3D e) {
        // Simplified bed scan logic
        for (int x = -10; x < 10; x++) {
            for (int y = -5; y < 5; y++) {
                for (int z = -10; z < 10; z++) {
                    BlockPos pos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z);
                    if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockBed) {
                        drawPlate(pos);
                    }
                }
            }
        }
    }

    private void drawPlate(BlockPos pos) {
        double x = pos.getX() - mc.getRenderManager().viewerPosX + 0.5;
        double y = pos.getY() - mc.getRenderManager().viewerPosY + 1.2;
        double z = pos.getZ() - mc.getRenderManager().viewerPosZ + 0.5;

        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glRotated(-mc.getRenderManager().playerViewY, 0, 1, 0);
        GL11.glScaled(-0.02, -0.02, 0.02);
        
        mc.fontRendererObj.drawStringWithShadow("§cBED", -mc.fontRendererObj.getStringWidth("BED") / 2, 0, -1);
        
        GL11.glPopMatrix();
    }
}
