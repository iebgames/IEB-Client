package dev.iebgames.iebclient.module.modules.render;

import dev.iebgames.iebclient.event.EventHook;
import dev.iebgames.iebclient.event.events.EventRender2D;
import dev.iebgames.iebclient.module.Category;
import dev.iebgames.iebclient.module.Module;
import dev.iebgames.iebclient.util.RenderUtils;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Keyboard;

public class ESP extends Module {

    public ESP() {
        super("ESP", "Oyuncuların yerini gösterir.", Category.RENDER, Keyboard.KEY_NONE);
    }

    // Logic is handled in MixinRenderGlobal or via EventRender3D (not yet implemented)
    // We'll use RenderUtils for basic 2D overlay if needed.
}
