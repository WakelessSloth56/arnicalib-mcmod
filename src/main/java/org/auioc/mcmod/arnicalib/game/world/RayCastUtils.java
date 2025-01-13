/*
 * Copyright (C) 2022-2025 AUIOC.ORG
 *
 * This file is part of ArnicaLib, a mod made for Minecraft.
 *
 * ArnicaLib is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.auioc.mcmod.arnicalib.game.world;


import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public class RayCastUtils {

    private static final float DEFAULT_PICK_INFLATION = 0.3F;
    private static final ToIntFunction<HitResult> DEFAULT_ON_MISS = (m) -> -1;


    // ====================================================================== //

    public record Ray(Vec3 from, Vec3 to) {

        public static Ray view(Entity entity, double rayLength) {
            var from = entity.getEyePosition();
            var view = entity.getViewVector(1.0F);
            var to = from.add(view.x * rayLength, view.y * rayLength, view.z * rayLength);
            return new Ray(from, to);
        }

    }

    public static BlockHitResult block(Entity source, Ray ray, ClipContext.Block blockMode, ClipContext.Fluid fluidMode) {
        return (source.level()).clip(new ClipContext(ray.from, ray.to, blockMode, fluidMode, source));
    }

    @Nullable
    public static EntityHitResult entity(Entity source, Ray ray, AABB aabb, Predicate<Entity> predicate, float pickInflation) {
        double d0 = Double.MAX_VALUE;
        Entity target = null;

        for (var entity : (source.level()).getEntities(source, aabb, predicate)) {
            var entity1aabb = entity.getBoundingBox().inflate((double) (entity.getPickRadius() + pickInflation));
            Optional<Vec3> optional = entity1aabb.clip(ray.from, ray.to);
            if (optional.isPresent()) {
                double d1 = (ray.from).distanceToSqr(optional.get());
                if (d1 < d0) {
                    target = entity;
                    d0 = d1;
                }
            }
        }

        return target == null ? null : new EntityHitResult(target);
    }

    @Nullable
    public static EntityHitResult entity(Entity source, Ray ray, Predicate<Entity> predicate, float pickInflation) {
        return entity(source, ray, (new AABB(ray.from, ray.to)).inflate(1.0D), predicate, pickInflation);
    }

    // ====================================================================== //

    public static BlockHitResult blockOnView(Entity entity, double rayLength, ClipContext.Block blockMode, ClipContext.Fluid fluidMode) {
        return block(entity, Ray.view(entity, rayLength), blockMode, fluidMode);
    }

    public static BlockHitResult blockOnView(Entity entity, double rayLength) {
        return blockOnView(entity, rayLength, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY);
    }

    // ====================================================================== //

    @Nullable
    public static EntityHitResult entityOnView(Entity entity, double rayLength, Predicate<Entity> predicate, float pickInflation, boolean blockMode) {
        var view = Ray.view(entity, rayLength);
        if (blockMode) {
            var rayHitBlock = block(entity, view, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE);
            if (rayHitBlock.getType() != HitResult.Type.MISS) {
                view = new Ray(view.from, rayHitBlock.getLocation());
            }
        }
        return entity(entity, view, predicate, pickInflation);
    }

    @Nullable
    public static EntityHitResult entityOnView(Entity entity, double rayLength, Predicate<Entity> predicate, float pickInflation) {
        return entity(entity, Ray.view(entity, rayLength), predicate, pickInflation);
    }

    @Nullable
    public static EntityHitResult entityOnView(Entity entity, double rayLength, Predicate<Entity> predicate) {
        return entityOnView(entity, rayLength, predicate, DEFAULT_PICK_INFLATION);
    }

    @Nullable
    public static EntityHitResult entityOnView(Entity entity, double rayLength, float pickInflation) {
        return entityOnView(entity, rayLength, EntitySelector.NO_SPECTATORS, pickInflation);
    }

    @Nullable
    public static EntityHitResult entityOnView(Entity entity, double rayLength) {
        return entityOnView(entity, rayLength, EntitySelector.NO_SPECTATORS, DEFAULT_PICK_INFLATION);
    }

    // ====================================================================== //

    public static int onView(
        Entity entity, double rayLength,
        Predicate<Entity> predicate, float pickInflation, ToIntFunction<EntityHitResult> onHitEntity,
        ClipContext.Block blockMode, ClipContext.Fluid fluidMode, ToIntFunction<BlockHitResult> onHitBlock,
        ToIntFunction<HitResult> onMiss
    ) {
        var view = Ray.view(entity, rayLength);

        var hitEntity = entity(entity, view, predicate, pickInflation);
        if (hitEntity != null) {
            return onHitEntity.applyAsInt(hitEntity);
        }

        var hitBlock = block(entity, view, blockMode, fluidMode);
        if (hitBlock.getType() == HitResult.Type.BLOCK) {
            return onHitBlock.applyAsInt(hitBlock);
        }

        return onMiss.applyAsInt(hitBlock);
    }

    public static int onView(
        Entity entity, double rayLength,
        Predicate<Entity> predicate, float pickInflation, ToIntFunction<EntityHitResult> onHitEntity,
        ToIntFunction<BlockHitResult> onHitBlock
    ) {
        return onView(
            entity, rayLength,
            predicate, pickInflation, onHitEntity,
            ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, onHitBlock,
            DEFAULT_ON_MISS
        );
    }

    public static int onView(
        Entity entity, double rayLength,
        ToIntFunction<EntityHitResult> onHitEntity,
        ToIntFunction<BlockHitResult> onHitBlock
    ) {
        return onView(
            entity, rayLength,
            EntitySelector.NO_SPECTATORS, DEFAULT_PICK_INFLATION, onHitEntity,
            onHitBlock
        );
    }

}
