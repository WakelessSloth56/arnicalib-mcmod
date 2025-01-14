/*
 * Copyright (C) 2025 AUIOC.ORG
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

package org.auioc.mcmod.arnicalib.mod.mixin;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.extensions.IItemStackExtension;
import org.auioc.mcmod.arnicalib.game.event.ItemAbilityCheckEvent;
import org.auioc.mcmod.arnicalib.mod.event.AHEventHooks;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Debug(export = true)
@Mixin(IItemStackExtension.class)
public interface MixinIItemStackExtension {

    /**
     * @author WakelessSloth56
     * @reason {@link ItemAbilityCheckEvent}
     */
    @Overwrite
    default boolean canPerformAction(ItemAbility ability) {
        return AHEventHooks.onCheckItemAbility(((ItemStack) (Object) this), ability);
    }

}
