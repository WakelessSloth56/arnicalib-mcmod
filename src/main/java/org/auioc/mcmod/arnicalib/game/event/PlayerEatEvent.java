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

package org.auioc.mcmod.arnicalib.game.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.auioc.mcmod.arnicalib.mod.event.AHEventHooks;

/**
 * Fired on the {@link NeoForge#EVENT_BUS} on <b>BOTH</b> logical sides.
 *
 * @see FoodProperties#onConsume
 * @see AHEventHooks#onPlayerEat
 */
public class PlayerEatEvent extends PlayerEvent implements ICancellableEvent {

    private final ItemStack food;
    private int nutrition = 0;
    private float saturation = 0.0F;

    public PlayerEatEvent(Player player, ItemStack food, int nutrition, float saturation) {
        super(player);
        this.food = food;
        this.nutrition = nutrition;
        this.saturation = saturation;
    }

    public ItemStack getFood() {
        return food;
    }

    public int getNutrition() {
        return this.nutrition;
    }

    public float getSaturation() {
        return this.saturation;
    }

    public void setNutrition(int nutrition) {
        this.nutrition = nutrition;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }


}
