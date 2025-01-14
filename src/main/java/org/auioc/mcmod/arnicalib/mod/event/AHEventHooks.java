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

package org.auioc.mcmod.arnicalib.mod.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.NeoForge;
import org.auioc.mcmod.arnicalib.game.event.FishingRodCastEvent;
import org.auioc.mcmod.arnicalib.game.event.ItemAbilityCheckEvent;
import org.auioc.mcmod.arnicalib.game.event.PistonPushableEvent;
import org.auioc.mcmod.arnicalib.game.event.PlayerEatEvent;
import org.auioc.mcmod.arnicalib.game.event.ServerLoginEvent;
import org.auioc.mcmod.arnicalib.mod.mixin.MixinFoodProperties;
import org.auioc.mcmod.arnicalib.mod.mixin.MixinIItemStackExtension;
import org.auioc.mcmod.arnicalib.mod.mixin.MixinPistonBaseBlock;
import org.auioc.mcmod.arnicalib.mod.mixin.MixinServerHandshakePacketListenerImpl;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import static org.auioc.mcmod.arnicalib.ArnicaLib.LOGGER;


public class AHEventHooks {

    private static final Marker MARKER = MarkerFactory.getMarker("EventHooks");

    private static final IEventBus BUS = NeoForge.EVENT_BUS;

    // ====================================================================== //

    /**
     * @see MixinServerHandshakePacketListenerImpl#handleIntention
     */
    public static boolean onServerLogin(final ClientIntentionPacket packet, final MinecraftServer server, final Connection connection) {
        var event = new ServerLoginEvent(packet, server, connection);
        boolean cancelled = BUS.post(event).isCanceled();
        if (cancelled) {
            var message = event.getMessage();
            connection.send(new ClientboundLoginDisconnectPacket(message));
            connection.disconnect(message);
            LOGGER.info(MARKER,
                "Disconnecting {} connection attempt from {}: {}",
                event.getPacket().intention(), event.getConnection().getRemoteAddress(), event.getMessage()
            );
            return false;
        }
        return true;
    }


    /**
     * CoreMod: arnicalib.fishing_rod_item.use
     */
    public static FishingRodCastEvent preFishingRodCast(Player player, ItemStack fishingRod, int timeReduction, int luckBonus) {
        var event = new FishingRodCastEvent((ServerPlayer) player, fishingRod, timeReduction, luckBonus);
        return BUS.post(event);
    }


    /**
     * @see MixinFoodProperties
     */
    public static PlayerEatEvent onPlayerEat(Player player, ItemStack food, int nutrition, float saturation) {
        var event = new PlayerEatEvent(player, food, nutrition, saturation);
        BUS.post(event);
        return event;
    }

    /**
     * @see MixinPistonBaseBlock#isPushable
     */
    public static boolean onPistonCheckPushable(BlockState blockState, Level level, BlockPos blockPos, Direction movement, boolean allowDestroy, Direction facing) {
        var event = new PistonPushableEvent(blockState, level, blockPos, movement, allowDestroy, facing);
        return BUS.post(event).isCanceled();
    }

    /**
     * @see MixinIItemStackExtension#canPerformAction
     */
    public static boolean onCheckItemAbility(ItemStack item, ItemAbility ability) {
        var event = new ItemAbilityCheckEvent(item, ability);
        BUS.post(event);
        return event.canPerformAction();
    }

}
