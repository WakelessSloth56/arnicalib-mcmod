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

package org.auioc.mcmod.arnicalib.game.codec;


import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.util.ExtraCodecs;
import org.auioc.mcmod.arnicalib.base.function.StringFunction;
import org.auioc.mcmod.arnicalib.base.function.ToStringFunction;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

public class EnumCodec<E extends Enum<E>> implements Codec<E> {

    private final Codec<E> codec;

    private EnumCodec(Codec<E> codec) {
        this.codec = codec;
    }

    @Override
    public <T> DataResult<T> encode(E input, DynamicOps<T> ops, T prefix) {
        return this.codec.encode(input, ops, prefix);
    }

    @Override
    public <T> DataResult<Pair<E, T>> decode(DynamicOps<T> ops, T input) {
        return this.codec.decode(ops, input);
    }

    // ============================================================================================================== //

    private static <E extends Enum<E>> EnumCodec<E> of(IntFunction<E> decoder) {
        return new EnumCodec<>(ExtraCodecs.idResolverCodec(Enum::ordinal, decoder, -1));
    }

    private static <E extends Enum<E>> EnumCodec<E> of(ToStringFunction<E> encoder, StringFunction<E> decoder) {
        return new EnumCodec<>(Codec.stringResolver(encoder, (s) -> (s != null) ? decoder.apply(s) : null));
    }

    private static <E extends Enum<E>> EnumCodec<E> of(E[] values, ToStringFunction<E> encoder, int threshold) {
        if (values.length > threshold) {
            var map = Arrays.stream(values)
                .collect(Collectors.toMap(encoder, (e) -> e));
            return of(encoder, map::get);
        }
        return of(encoder, (s) -> {
            for (E e : values) if (encoder.apply(e).equals(s)) return e;
            return null;
        });
    }

    private static final int THRESHOLD = 8;

    // ====================================================================== //

    public static <E extends Enum<E>> EnumCodec<E> byString(Class<E> enumClass, ToStringFunction<E> encoder, int threshold) {
        return of(enumClass.getEnumConstants(), encoder, threshold);
    }

    public static <E extends Enum<E>> EnumCodec<E> byString(Class<E> enumClass, ToStringFunction<E> encoder) {
        return byString(enumClass, encoder, THRESHOLD);
    }

    public static <E extends Enum<E>> EnumCodec<E> byName(Class<E> enumClass) {
        return byString(enumClass, E::name, THRESHOLD);
    }

    public static <E extends Enum<E>> EnumCodec<E> byNameLowerCase(Class<E> enumClass) {
        return byString(enumClass, (e) -> e.name().toLowerCase(), THRESHOLD);
    }

    public static <E extends Enum<E>> EnumCodec<E> byNameUpperCase(Class<E> enumClass) {
        return byString(enumClass, (e) -> e.name().toUpperCase(), THRESHOLD);
    }

    // ====================================================================== //

    public static <E extends Enum<E>> EnumCodec<E> byOrdinal(Class<E> enumClass) {
        var values = enumClass.getEnumConstants();
        return of((i) -> (i >= 0 && i < values.length) ? values[i] : null);
    }

}
