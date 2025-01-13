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

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.NullObject;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author LainIO24
 * @since 7.0.0
 */
public class ModConfigOps implements DynamicOps<Object> {

    public static final ModConfigOps INSTANCE = new ModConfigOps();

    @Override
    public Object empty() {
        return NullObject.NULL_OBJECT;
    }

    @Override
    public <U> U convertTo(DynamicOps<U> ops, Object input) {
        if (input instanceof Config) {
            return this.convertMap(ops, input);
        }
        if (input instanceof Collection) {
            return this.convertList(ops, input);
        }
        if (input == null || input instanceof NullObject) {
            return ops.empty();
        }
        if (input instanceof Enum) {
            return ops.createString(((Enum<?>) input).name());
        }
        if (input instanceof Temporal) {
            return ops.createString(input.toString());
        }
        if (input instanceof String s) {
            return ops.createString(s);
        }
        if (input instanceof Boolean b) {
            return ops.createBoolean(b);
        }
        if (input instanceof Number n) {
            return ops.createNumeric(n);
        }
        throw new UnsupportedOperationException("Unsupported value: " + input);
    }

    @Override
    public DataResult<Number> getNumberValue(Object input) {
        return input instanceof Number n
               ? DataResult.success(n)
               : DataResult.error(() -> "Not a number: " + input);
    }

    @Override
    public DataResult<Boolean> getBooleanValue(Object input) {
        if (input instanceof Boolean b) {
            return DataResult.success(b);
        } else if (input instanceof Number n) {
            return DataResult.success(n.intValue() > 0);
        } else {
            return DataResult.error(() -> "Not a boolean: " + input);
        }
    }

    @Override
    public Object createBoolean(boolean value) {
        return Boolean.valueOf(value);
    }

    @Override
    public boolean compressMaps() {
        return false;
    }

    @Override
    public Object createNumeric(Number i) {
        return i;
    }

    @Override
    public DataResult<String> getStringValue(Object input) {
        if (input instanceof Config || input instanceof Collection) {
            return DataResult.error(() -> "Not a string: " + input);
        } else {
            return DataResult.success(String.valueOf(input));
        }
    }

    @Override
    public Object createString(String value) {
        return value;
    }

    @Override
    public DataResult<Object> mergeToList(Object list, List<Object> values) {
        return DynamicOps.super.mergeToList(list, values)
            .map(o -> this.empty() == o ? new ArrayList<>() : o);
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataResult<Object> mergeToList(Object list, Object value) {
        if (!(list instanceof Collection) && this.empty() != list) {
            return DataResult.error(() -> "mergeToList called with not a list: " + list, list);
        }
        var result = new ArrayList<>();
        if (this.empty() != list && list instanceof Collection) {
            result.addAll((Collection<Object>) list);
        }
        result.add(value);
        return DataResult.success(result);
    }

    @Override
    public DataResult<Object> mergeToMap(Object map, Object key, Object value) {
        if (!(map instanceof Config) && this.empty() != map) {
            return DataResult.error(() -> "mergeToMap called with not a map: " + map, map);
        }
        var keyResult = this.getStringValue(key);
        if (keyResult.error().isPresent()) {
            return DataResult.error(() -> "key is not a string: " + key, map);
        }
        return keyResult.flatMap(s -> {
            var result = TomlFormat.newConfig();
            if (this.empty() != map && map instanceof Config) {
                result.addAll((Config) map);
            }
            result.add(s, value);
            return DataResult.success(result);
        });
    }

    @Override
    public DataResult<Stream<Pair<Object, Object>>> getMapValues(Object input) {
        return input instanceof Config config
               ? DataResult.success(config.entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue())))
               : DataResult.error(() -> "Not a Config: " + input);
    }

    @Override
    public Object createMap(Stream<Pair<Object, Object>> map) {
        var result = TomlFormat.newConfig();
        map.forEach(pair -> result.add(
            this.getStringValue(pair.getFirst()).getOrThrow(),
            pair.getSecond()
        ));
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataResult<Stream<Object>> getStream(Object input) {
        return input instanceof Collection
               ? DataResult.success(((Collection<Object>) input).stream())
               : DataResult.error(() -> "Not a collection: " + input);
    }

    @Override
    public Object createList(Stream<Object> input) {
        return input.toList();
    }

    @Override
    public Object remove(Object input, String key) {
        if (input instanceof Config config) {
            var result = TomlFormat.newConfig();
            for (var entry : config.entrySet()) {
                if (!Objects.equals(entry.getKey(), key)) {
                    result.add(entry.getKey(), entry.getValue());
                }
            }
            return result;
        }
        return input;
    }

    @Override
    public String toString() {
        return "ModConfig";
    }

}
