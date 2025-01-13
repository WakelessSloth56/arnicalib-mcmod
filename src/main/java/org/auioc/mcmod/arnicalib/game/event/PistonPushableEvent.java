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

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.auioc.mcmod.arnicalib.mod.event.AHEventHooks;

/**
 * Fired on the {@link NeoForge#EVENT_BUS} on <b>BOTH</b> logical sides.
 *
 * @see PistonBaseBlock#isPushable
 * @see AHEventHooks#onPistonCheckPushable
 */
public class PistonPushableEvent extends BlockEvent implements ICancellableEvent {

    private final Direction movement;
    private final boolean allowDestroy;
    private final Direction facing;

    public PistonPushableEvent(BlockState blockState, Level level, BlockPos blockPos, Direction movement, boolean allowDestroy, Direction facing) {
        super(level, blockPos, blockState);
        this.movement = movement;
        this.allowDestroy = allowDestroy;
        this.facing = facing;
    }

    public Direction getMovement() {
        return movement;
    }

    public boolean isAllowDestroy() {
        return allowDestroy;
    }

    public Direction getFacing() {
        return facing;
    }

}
