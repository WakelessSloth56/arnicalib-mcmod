/*
 * Copyright (C) 2023-2025 AUIOC.ORG
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

package org.auioc.mcmod.arnicalib.game.loot.predicate;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.apache.commons.lang3.function.FailableToDoubleFunction;
import org.auioc.mcmod.arnicalib.game.codec.EnumCodec;

public record EntityAttributeCondition(
    Holder<Attribute> attribute,
    ValueType valueType,
    MinMaxBounds.Doubles value,
    LootContext.EntityTarget entityTarget
) implements LootItemCondition {

    public static MapCodec<EntityAttributeCondition> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance.group(
            Attribute.CODEC.fieldOf("attribute").forGetter(o -> o.attribute),
            EnumCodec.byString(ValueType.class, e -> e.name).fieldOf("type").forGetter(o -> o.valueType),
            MinMaxBounds.Doubles.CODEC.fieldOf("value").forGetter(o -> o.value),
            LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(o -> o.entityTarget)
        ).apply(instance, EntityAttributeCondition::new));

    public static final LootItemConditionType TYPE = new LootItemConditionType(CODEC);

    @Override
    public LootItemConditionType getType() { return TYPE; }

    @Override
    public boolean test(LootContext ctx) {
        var entity = ctx.getOptionalParameter(this.entityTarget.getParam());
        if (entity instanceof LivingEntity living) {
            var instance = living.getAttribute(this.attribute);
            if (instance != null) {
                try {
                    double value = this.valueType.getValue(instance);
                    return this.value.matches(value);
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
        return false;
    }

    // ============================================================================================================== //

    public enum ValueType {

        DEFAULT("default", (i) -> i.getAttribute().value().getDefaultValue()),
        BASE("base", AttributeInstance::getBaseValue),
        CURRENT("current", AttributeInstance::getValue),
        MAX("max", (i) -> castToRangedAttribute(i).getMaxValue()),
        MIN("min", (i) -> castToRangedAttribute(i).getMinValue());

        private final String name;
        private final FailableToDoubleFunction<AttributeInstance, IllegalArgumentException> getter;

        ValueType(String name, FailableToDoubleFunction<AttributeInstance, IllegalArgumentException> getter) {
            this.name = name;
            this.getter = getter;
        }

        public double getValue(AttributeInstance instance) { return this.getter.applyAsDouble(instance); }

        private static RangedAttribute castToRangedAttribute(AttributeInstance instance) {
            var attr = instance.getAttribute();
            if (attr.value() instanceof RangedAttribute rangeAttr) return rangeAttr;
            throw new IllegalArgumentException("Attribute '" + attr.getRegisteredName() + "' is not a RangedAttribute");
        }

    }


}
