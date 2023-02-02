package org.auioc.mcmod.arnicalib.mod.mixin;

import java.util.List;
import java.util.Set;
import org.auioc.mcmod.arnicalib.game.mod.EnvironmentUtils;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class MixinConfigPlugin implements IMixinConfigPlugin {

    private boolean runData;

    @Override
    public void onLoad(String mixinPackage) {
        this.runData = EnvironmentUtils.getLaunchTarget().toLowerCase().startsWith("forgedata");
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return !this.runData;
    }

    // ====================================================================== //

    @Override
    public String getRefMapperConfig() { return null; }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() { return null; }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

}
