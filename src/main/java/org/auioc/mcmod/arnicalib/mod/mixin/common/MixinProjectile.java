package org.auioc.mcmod.arnicalib.mod.mixin.common;

import javax.annotation.Nullable;
import org.auioc.mcmod.arnicalib.game.nbt.NbtUtils;
import org.auioc.mcmod.arnicalib.mod.mixinapi.common.IMixinProjectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

@Mixin(value = Projectile.class)
public class MixinProjectile implements IMixinProjectile {

    @Shadow
    private boolean hasBeenShot;

    @Nullable
    private Vec3 shootingPosition;

    @Nullable
    @Override
    public Vec3 getShootingPosition() {
        return this.shootingPosition;
    }

    @Inject(
        method = "Lnet/minecraft/world/entity/projectile/Projectile;tick()V",
        at = @At(value = "HEAD"),
        require = 1,
        allow = 1
    )
    private void tick(CallbackInfo ci) {
        if (!this.hasBeenShot) {
            var pos = ((Projectile) (Object) this).position();
            this.shootingPosition = new Vec3(pos.x, pos.y, pos.z);
        }
    }

    @Inject(
        method = "Lnet/minecraft/world/entity/projectile/Projectile;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V",
        at = @At(value = "TAIL"),
        require = 1,
        allow = 1
    )
    private void readAdditionalSaveData(CompoundTag p_37262_, CallbackInfo ci) {
        if (p_37262_.contains("ShootingPosition", 6)) {
            this.shootingPosition = NbtUtils.readVec3(p_37262_.getList("ShootingPosition", 6));
        }
    }

    @Inject(
        method = "Lnet/minecraft/world/entity/projectile/Projectile;addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V",
        at = @At(value = "TAIL"),
        require = 1,
        allow = 1
    )
    private void addAdditionalSaveData(CompoundTag p_37265_, CallbackInfo ci) {
        if (this.shootingPosition != null) {
            p_37265_.put("ShootingPosition", NbtUtils.writeVec3(this.shootingPosition));
        }
    }

}