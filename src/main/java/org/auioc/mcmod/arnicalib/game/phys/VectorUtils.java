/*
 * Copyright (C) 2024-2025 AUIOC.ORG
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

package org.auioc.mcmod.arnicalib.game.phys;

import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;

public class VectorUtils {

    public static Vec3[] fromDoubleArray(double[] d) {
        if (d.length % 3 != 0) throw new IllegalStateException();

        Vec3[] v = new Vec3[d.length / 3];
        for (int i = 0, j = 0; i < v.length; ++i, j = i * 3) {
            v[i] = new Vec3(d[j], d[j + 1], d[j + 2]);
        }
        return v;
    }

    public static double[] toDoubleArray(Vec3[] v) {
        double[] d = new double[v.length * 3];
        for (int i = 0, j = 0; i < v.length; ++i, j = i * 3) {
            d[j] = v[i].x;
            d[j + 1] = v[i].y;
            d[j + 2] = v[i].z;
        }
        return d;
    }

    /**
     * <pre>
     *              ^ Normal
     *              |
     *              |    7 BaseA
     *              |   /
     *        ______|__/________
     *       /......|./......../
     *      /.......|/......../
     *  ------------x----------->  BaseB
     *    /......../......../
     *   /......../......../
     *  /________/________/
     *          /
     * </pre>
     *
     * @param normalVector Normal vector of a plane
     * @return Two base vectors in this plane
     */
    public static Pair<Vec3, Vec3> getBaseVector(Vec3 normalVector) {
        Vec3 N = normalVector.normalize();
        double x = Math.abs(N.x);
        double y = Math.abs(N.y);
        double z = Math.abs(N.z);
        // y > z > x  ( 0,  z, -y)
        // z > y > x  ( 0, -z,  y)
        // x > z > y  ( z,  0, -x)
        // z > x > y  (-z,  0,  x)
        // x > y > z  ( y, -x,  0)
        // y > x > z  (-y,  x,  0)

        Vec3 vA;
        if (y >= z && z >= x) vA = new Vec3(0, N.z, -N.y);
        else if (z >= y && y >= x) vA = new Vec3(0, -N.z, N.y);
        else if (x >= z && z >= y) vA = new Vec3(N.z, 0, -N.x);
        else if (z >= x && x >= y) vA = new Vec3(-N.z, 0, N.x);
        else if (x >= y && y >= z) vA = new Vec3(N.y, -N.x, 0);
        else if (y >= x && x >= z) vA = new Vec3(-N.y, N.x, 0);
        else throw new IllegalStateException();

        Vec3 vB = N.cross(vA);

        return Pair.of(vA.normalize(), vB.normalize());
    }

}
