package dev.iebgames.iebclient.asm;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.8.9")
@IFMLLoadingPlugin.SortingIndex(Integer.MAX_VALUE - 2)
public class FMLLoadingPlugin implements IFMLLoadingPlugin {

    @Override public String[] getASMTransformerClass() { return new String[0]; }
    @Override public String   getModContainerClass()   { return null; }
    @Override public String   getSetupClass()          { return null; }
    @Override public void     injectData(Map<String, Object> data) {}
    @Override public String   getAccessTransformerClass() { return null; }
}
