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

package org.auioc.mcmod.arnicalib.base.collection;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class ListUtils {

    public static <T> Optional<T> getOptional(List<T> list, int index) {
        return (index >= 0 && index < list.size()) ? Optional.ofNullable(list.get(index)) : Optional.empty();
    }

    public static <T> T getOrDefault(List<T> list, int index, T defaultValue) {
        return (index >= 0 && index < list.size()) ? list.get(index) : defaultValue;
    }

    // ============================================================================================================== //

    public static <T> int indexOf(List<T> list, Predicate<T> predicate, int ordinal) {
        for (int i = 0, l = list.size(); i < l; ++i) {
            if (predicate.test(list.get(i))) {
                if (ordinal <= 0) return i;
                ordinal--;
            }
        }
        return -1;
    }

    public static <T> int indexOf(List<T> list, Predicate<T> predicate) {
        return indexOf(list, predicate, 0);
    }

    public static <T> int lastIndexOf(List<T> list, Predicate<T> predicate, int ordinal) {
        for (int i = list.size() - 1; i >= 0; --i) {
            if (predicate.test(list.get(i))) {
                if (ordinal <= 0) return i;
                ordinal--;
            }
        }
        return -1;
    }

    public static <T> int lastIndexOf(List<T> list, Predicate<T> predicate) {
        return lastIndexOf(list, predicate, 0);
    }

    public static <T> int[] allIndexesOf(List<T> list, Predicate<T> predicate) {
        return IntStream
            .range(0, list.size())
            .filter((i) -> predicate.test(list.get(i)))
            .toArray();
    }

    // ============================================================================================================== //

    public static <T> List<T> findAll(List<T> list, Predicate<T> predicate) {
        return list.stream().filter(predicate).toList();
    }

    public static <T> Optional<T> findFirst(List<T> list, Predicate<T> predicate) {
        return list.stream().filter(predicate).findFirst();
    }

    public static <T> Optional<T> findFirst(List<T> list, Predicate<T> predicate, int ordinal) {
        return getOptional(list, indexOf(list, predicate, ordinal));
    }

    public static <T> Optional<T> findLast(List<T> list, Predicate<T> predicate, int ordinal) {
        return getOptional(list, lastIndexOf(list, predicate, ordinal));
    }

    // ============================================================================================================== //

    public static <T> boolean add(List<T> list, int index, T element) {
        if (index > list.size() || index < 0) return false;
        list.add(index, element);
        return true;
    }

    public static <T> boolean addBefore(List<T> list, Predicate<T> refPredicate, T element) {
        return add(list, indexOf(list, refPredicate), element);
    }

    public static <T> boolean addAfter(List<T> list, Predicate<T> refPredicate, T element) {
        return add(list, indexOf(list, refPredicate) + 1, element);
    }

}
