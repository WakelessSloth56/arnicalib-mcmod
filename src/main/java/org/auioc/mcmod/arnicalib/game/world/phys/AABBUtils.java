package org.auioc.mcmod.arnicalib.game.world.phys;

import net.minecraft.core.Position;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class AABBUtils {

    public static AABB moveTo(AABB aabb, double x, double y, double z) {
        return aabb.move(
            x - ((aabb.minX + aabb.maxX) / 2), // (x+0.5)-(min+(max-min)/2)
            y - aabb.minY,
            z - ((aabb.minZ + aabb.maxZ) / 2)
        );
    }

    public static AABB moveTo(AABB aabb, Position pos) {
        return moveTo(aabb, pos.x(), pos.y(), pos.z());
    }

    public static AABB moveTo(AABB aabb, int x, int y, int z) {
        return moveTo(aabb, x + 0.5D, y, x + 0.5D);
    }

    public static AABB moveTo(AABB aabb, Vec3i pos) {
        return moveTo(aabb, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
    }

    public static boolean isEmpty(AABB aabb, Level level) {
        return level.getBlockStates(aabb).allMatch(BlockState::isAir);
    }

    @SuppressWarnings("deprecation")
    public static boolean containsSolid(AABB aabb, Level level) {
        return level.getBlockStates(aabb).anyMatch(BlockState::isSolid);
    }

    @SuppressWarnings("deprecation")
    public static boolean containsLiquid(AABB aabb, Level level) {
        return level.getBlockStates(aabb).anyMatch(BlockState::liquid);
    }

    public static double[][] getEdges(AABB aabb) {
        return new double[][] {
            {aabb.minX, aabb.minY, aabb.minZ, aabb.minX, aabb.minY, aabb.maxZ},
            {aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.minY, aabb.minZ},
            {aabb.minX, aabb.minY, aabb.minZ, aabb.minX, aabb.maxY, aabb.minZ},

            {aabb.maxX, aabb.maxY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ},
            {aabb.minX, aabb.maxY, aabb.maxZ, aabb.maxX, aabb.maxY, aabb.maxZ},
            {aabb.maxX, aabb.minY, aabb.maxZ, aabb.maxX, aabb.maxY, aabb.maxZ},

            {aabb.minX, aabb.minY, aabb.maxZ, aabb.maxX, aabb.minY, aabb.maxZ},

            {aabb.minX, aabb.maxY, aabb.minZ, aabb.minX, aabb.maxY, aabb.maxZ},
            {aabb.minX, aabb.maxY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.minZ},

            {aabb.maxX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.minZ},

            {aabb.maxX, aabb.minY, aabb.maxZ, aabb.maxX, aabb.minY, aabb.minZ},
            {aabb.minX, aabb.minY, aabb.maxZ, aabb.minX, aabb.maxY, aabb.maxZ},
        };
    }

}
