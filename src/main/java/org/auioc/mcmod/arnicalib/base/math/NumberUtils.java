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

@SuppressWarnings("deprecation")
public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils {

    public static String toBinaryString(int i, int s) {
        return String.format("0b%" + s + "s", Integer.toBinaryString(i)).replace(" ", "0");
    }

    public static String toOctalString(int i, int s) {
        return String.format("0o%" + s + "s", Integer.toOctalString(i)).replace(" ", "0");
    }

    public static String toHexString(int i, int s) {
        return String.format("0x%" + s + "s", Integer.toHexString(i)).replace(" ", "0");
    }

    public static String toBinaryString(long i, int s) {
        return String.format("0b%" + s + "s", Long.toBinaryString(i)).replace(" ", "0");
    }

    public static String toOctalString(long i, int s) {
        return String.format("0o%" + s + "s", Long.toOctalString(i)).replace(" ", "0");
    }

    public static String toHexString(long i, int s) {
        return String.format("0x%" + s + "s", Long.toHexString(i)).replace(" ", "0");
    }

}
