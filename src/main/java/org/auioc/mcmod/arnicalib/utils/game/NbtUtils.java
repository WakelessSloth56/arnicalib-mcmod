package org.auioc.mcmod.arnicalib.utils.game;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.phys.AABB;

public class NbtUtils {

    public static ListTag writeDoubleArray(double... values) {
        var listTag = new ListTag();
        for (int i = 0; i < values.length; i++) {
            listTag.addTag(i, DoubleTag.valueOf(values[i]));
        }
        return listTag;
    }

    public static double[] readDoubleArray(ListTag nbt) {
        int size = nbt.size();
        var result = new double[size];
        for (int i = 0; i < size; i++) {
            result[i] = nbt.getDouble(i);
        }
        return result;
    }

    public static ListTag getDoubleListTag(CompoundTag nbt, String key) {
        return nbt.getList(key, 6);
    }

    public static double[] getDoubleArray(CompoundTag nbt, String key) {
        return readDoubleArray(getDoubleListTag(nbt, key));
    }


    public static ListTag writeAABB(AABB aabb) {
        return writeDoubleArray(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
    }

    public static AABB readAABB(ListTag nbt) {
        double[] p = readDoubleArray(nbt);
        return new AABB(p[0], p[1], p[2], p[3], p[4], p[5]);
    }

    public static AABB getAABB(CompoundTag nbt, String key) {
        return readAABB(getDoubleListTag(nbt, key));
    }

}
