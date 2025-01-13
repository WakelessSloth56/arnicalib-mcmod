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

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import org.auioc.mcmod.arnicalib.mod.event.AHEventHooks;

/**
 * Fired on the {@link NeoForge#EVENT_BUS} on <b>SERVER</b> side only.
 *
 * @see FishingRodItem#use
 * @see AHEventHooks#preFishingRodCast
 */
public class FishingRodCastEvent extends ServerPlayerEvent {

    private final ItemStack fishingRod;
    private int timeReduction;
    private int luckBonus;

    public FishingRodCastEvent(ServerPlayer player, ItemStack fishingRod, int timeReduction, int luckBonus) {
        super(player);
        this.fishingRod = fishingRod;
        this.timeReduction = timeReduction;
        this.luckBonus = luckBonus;
    }

    public ItemStack getFishingRod() {
        return fishingRod;
    }

    public int getTimeReduction() {
        return timeReduction;
    }

    public void setTimeReduction(int timeReduction) {
        this.timeReduction = timeReduction;
    }

    public int getLuckBonus() {
        return luckBonus;
    }

    public void setLuckBonus(int luckBonus) {
        this.luckBonus = luckBonus;
    }

}
