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

package org.auioc.mcmod.arnicalib.base.text;

import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
public class WordUtils extends org.apache.commons.lang3.text.WordUtils {

    public static String toSnakeCase(String str) {
        return str.replaceAll("[A-Z]", "_$0").toLowerCase();
    }

    public static String toCamelCase(String str) {
        return Pattern.compile("_([a-z])").matcher(str).replaceAll(m -> m.group(1).toUpperCase());
    }

}
