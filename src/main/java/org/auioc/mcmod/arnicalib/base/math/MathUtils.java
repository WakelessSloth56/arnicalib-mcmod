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

package org.auioc.mcmod.arnicalib.base.math;

import java.util.function.DoubleUnaryOperator;
import java.util.function.IntUnaryOperator;

public class MathUtils {

    /**
     * @param n upper bound of the summation
     * @param k lower bound of the summation
     * @param f function to produce each term of the summation, the <i><code>index</code></i> is passed as parameter
     */
    public static int sigma(int n, int k, IntUnaryOperator f) {
        int r = 0;
        for (int i = k, _n = n + 1; i < _n; ++i) r += f.applyAsInt(i);
        return r;
    }

    /**
     * @param n upper bound of the summation
     * @param k lower bound of the summation
     * @param f function to produce each term of the summation, the <i><code>index</code></i> is passed as parameter
     */
    public static double sigma(int n, int k, DoubleUnaryOperator f) {
        double r = 0.0D;
        for (int i = k, _n = n + 1; i < _n; ++i) r += f.applyAsDouble((double) i);
        return r;
    }

    /**
     * @param a base
     * @param n value
     * @return the base-<i><code>a</code></i> logarithm of <i><code>n</code></i>
     */
    public static double log(double a, double n) {
        return Math.log(n) / Math.log(a);
    }

    /**
     * @param n value
     * @return the binary logarithm of <i><code>n</code></i>
     */
    public static double log2(double n) {
        return log(2.0D, n);
    }

}
