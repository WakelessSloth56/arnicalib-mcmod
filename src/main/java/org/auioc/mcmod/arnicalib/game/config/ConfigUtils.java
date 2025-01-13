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

package org.auioc.mcmod.arnicalib.game.config;


import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ConfigUtils {

    public static final Joiner LINE_JOINER = Joiner.on("\n");
    public static final Joiner DOT_JOINER = Joiner.on(".");
    public static final Splitter DOT_SPLITTER = Splitter.on(".");

    public static List<String> split(String path) {
        return Lists.newArrayList(DOT_SPLITTER.split(path));
    }


    public static void pushWithoutPop(Builder specBuilder, String path, Consumer<Builder> subBuilder) {
        specBuilder.push(path);
        subBuilder.accept(specBuilder);
    }

    public static void push(Builder specBuilder, String path, Consumer<Builder> subBuilder) {
        specBuilder.push(path);
        subBuilder.accept(specBuilder);
        specBuilder.pop();
    }


    public static <T> ConfigValue<List<? extends T>> defineList(Builder specBuilder, String path, Supplier<List<? extends T>> defaultSupplier, Predicate<Object> elementValidator) {
        return specBuilder.defineListAllowEmpty(split(path), defaultSupplier, elementValidator);
    }

    public static <T> ConfigValue<List<? extends T>> defineList(Builder specBuilder, String path, Predicate<Object> elementValidator) {
        return defineList(specBuilder, path, List::of, elementValidator);
    }

    public static ConfigValue<List<? extends String>> defineStringList(Builder specBuilder, String path) {
        return defineList(specBuilder, path, String.class::isInstance);
    }

    public static ConfigValue<List<? extends String>> defineStringList(Builder specBuilder, String path, List<String> allowedValues) {
        return defineList(specBuilder, path, (o) -> o instanceof String && allowedValues.contains(o));
    }

    public static ConfigValue<List<? extends String>> defineStringList(Builder specBuilder, String path, Supplier<List<? extends String>> defaultSupplier, Predicate<String> stringValidator) {
        return defineList(specBuilder, path, defaultSupplier, (o) -> (o instanceof String) && stringValidator.test((String) o));
    }

    public static ConfigValue<List<? extends String>> defineStringList(Builder specBuilder, String path, Supplier<List<? extends String>> defaultSupplier) {
        return defineStringList(specBuilder, path, defaultSupplier, (o) -> true);
    }


    public static boolean validateListSize(Object o, int size) {
        if (o instanceof List<?> l) {
            return l.size() == size;
        }
        return false;
    }

    public static <T> boolean validateListElements(Object o, Class<T> type) {
        if (o instanceof List<?> l) {
            for (var e : l) {
                if (!type.isInstance(e)) return false;
            }
            return true;
        }
        return false;
    }

    public static <T> boolean validateListElements(Object o, Class<T> type, List<T> allowed) {
        if (o instanceof List<?> l) {
            for (var e : l) {
                if (type.isInstance(e) && allowed.contains(type.cast(o))) {
                    continue;
                } else {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean validateStringList(Object o, List<String> allowed) {
        return validateListElements(o, String.class, allowed);
    }

}
