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

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Supplier;

import static org.auioc.mcmod.arnicalib.ArnicaLib.LOGGER;

/**
 * @author LainIO24
 * @since 7.0.0
 */
public class CodecConfigValue<T> implements Supplier<T> {

    private static final Marker MARKER = MarkerFactory.getMarker(CodecConfigValue.class.getName());

    public static <T> CodecConfigValue<T> define(ModConfigSpec.Builder builder, String name, Codec<T> codec, T defaultValue) {
        var encodedDefaultValue = codec
            .encodeStart(ModConfigOps.INSTANCE, defaultValue)
            .getOrThrow(s -> new IllegalArgumentException(String.format("Failed to encode default value %s: %s", defaultValue, s)));
        var configValue = builder.define(name, encodedDefaultValue);
        return new CodecConfigValue<>(configValue, codec, defaultValue, encodedDefaultValue);
    }

    private final @Nonnull ModConfigSpec.ConfigValue<Object> configValue;
    private final @Nonnull Codec<T> codec;
    private @Nonnull Object cached;
    private @Nonnull T parsed;
    private final @Nonnull T default_;

    private CodecConfigValue(@NotNull ModConfigSpec.ConfigValue<Object> configValue, @NotNull Codec<T> codec, @NotNull T defaultValue, @NotNull Object encodedDefaultValue) {
        this.configValue = configValue;
        this.codec = codec;
        this.default_ = defaultValue;
        this.parsed = defaultValue;
        this.cached = encodedDefaultValue;
    }

    @Override
    @Nonnull
    public T get() {
        Object freshObject = this.configValue.get();
        if (!Objects.equals(this.cached, freshObject)) {
            this.cached = freshObject;
            this.parsed = this.decode(freshObject);
        }
        return this.parsed;
    }

    public void set(T value) {
        this.codec.encodeStart(ModConfigOps.INSTANCE, value)
            .resultOrPartial((e) -> LOGGER.error(MARKER, "Could not save value {} due to encoding error: {}", value, e))
            .ifPresent((result) -> {
                this.configValue.set(result);
                this.configValue.save();
                this.parsed = value;
                this.cached = result;
            });
    }

    private T decode(Object obj) {
        DataResult<T> parseResult = this.codec.parse(ModConfigOps.INSTANCE, obj);
        return parseResult.mapOrElse(
            (result) -> result,
            (failure) -> {
                LOGGER.error(MARKER, "Rollback to default value due to decoding error: {}", failure.message());
                return this.default_;
            }
        );
    }

}
