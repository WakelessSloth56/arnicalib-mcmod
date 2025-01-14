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

package org.auioc.mcmod.arnicalib.game.event;

import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.common.ItemAbility;
import org.auioc.mcmod.arnicalib.base.event.EventResult;

/**
 * @since 7.0.0
 */
public class ItemAbilityCheckEvent extends Event implements ICancellableEvent {

    private final ItemStack item;
    private final ItemAbility ability;
    private EventResult result = EventResult.DEFAULT;

    public ItemAbilityCheckEvent(ItemStack item, ItemAbility ability) {
        this.item = item;
        this.ability = ability;
    }

    public ItemStack getItem() {
        return item;
    }

    public ItemAbility getAbility() {
        return ability;
    }

    public void setResult(EventResult result) {
        this.result = result;
    }

    public boolean canPerformAction() {
        if (this.isCanceled()) {
            return false;
        }
        if (result == EventResult.DEFAULT) {
            return item.getItem().canPerformAction(item, ability);
        } else if (result == EventResult.ALLOW) {
            return true;
        } else if (result == EventResult.DENY) {
            return false;
        }
        throw new IllegalStateException();
    }

}
