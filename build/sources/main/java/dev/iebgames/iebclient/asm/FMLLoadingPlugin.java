package dev.iebgames.iebclient.asm;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.8.9")
@IFMLLoadingPlugin.SortingIndex(Integer.MAX_VALUE - 2)
public class FMLLoadingPlugin implements IFMLLoadingPlugin {

    @Override public String[] getASMTransformerClass() { return new String[0]; }
    @Override public String   getModContainerClass()   { return null; }
    @Override public String   getSetupClass()          { return null; }
    public FMLLoadingPlugin() {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.iebclient.json");
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
    }

    @Override public void     injectData(Map<String, Object> data) {}
    @Override public String   getAccessTransformerClass() { return null; }
}
