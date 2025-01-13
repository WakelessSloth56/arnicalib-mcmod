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

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.auioc.mcmod.arnicalib.game.phys.AABBUtils;

import java.util.Optional;

public class RandomTeleporter {

    public static Optional<BlockPos> findSafePosition(Level level, BlockPos center, int radius, boolean surface, AABB aabb, RandomSource random) {
        var pos = PositionUtils.random(center, radius, random).mutable();
        if (PositionUtils.isInWorldBounds(pos, level)) {
            var y = surface
                    ? PositionUtils.findStandableY(level, pos.getX(), pos.getZ())
                    : PositionUtils.findStandableY(level, pos.getX(), pos.getZ(), center.getY() - radius, pos.getY());
            if (y.isPresent()) {
                pos.setY(y.get());
            } else {
                return Optional.empty();
            }
            if (AABBUtils.isEmpty(AABBUtils.moveTo(aabb, pos), level)) {
                return Optional.of(pos.immutable());
            }
        }
        return Optional.empty();
    }

    public static Optional<BlockPos> findSafePosition(LivingEntity living, BlockPos center, int radius, boolean surface) {
        return findSafePosition(living.level(), center, radius, surface, living.getBoundingBox(), living.getRandom());
    }

    public static Optional<BlockPos> findSafePosition(LivingEntity living, BlockPos center, int radius, boolean surface, int maxTries) {
        for (int t = 0; t < maxTries; t++) {
            var pos = findSafePosition(living, center, radius, surface);
            if (pos.isPresent()) return pos;
        }
        return Optional.empty();
    }

    public static boolean teleport(LivingEntity living, BlockPos center, int radius, boolean surface, int maxTries) {
        if (living.level().isClientSide()) return false;

        return findSafePosition(living, center, radius, surface, maxTries)
            .map((pos) -> {
                living.teleportTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
                return true;
            }).orElse(false);
    }

    public static boolean teleport(LivingEntity living, int radius, int maxTries) {
        return teleport(living, living.blockPosition(), radius, false, maxTries);
    }

    public static void teleportUnsafe(LivingEntity living, Vec3 center, double radius) {
        var pos = PositionUtils.random(center, radius, living.getRandom());
        living.teleportTo(pos.x, pos.y, pos.z);
    }

    public static void teleportUnsafe(LivingEntity living, double radius) {
        teleportUnsafe(living, living.position(), radius);
    }

}
