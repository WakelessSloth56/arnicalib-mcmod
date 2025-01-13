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


import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerHandshakePacketListenerImpl;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.auioc.mcmod.arnicalib.mod.event.AHEventHooks;

/**
 * Fired on the {@link NeoForge#EVENT_BUS} on <b>SERVER</b> side only.
 *
 * @see ServerHandshakePacketListenerImpl#handleIntention
 * @see AHEventHooks#onServerLogin
 */
public class ServerLoginEvent extends Event implements ICancellableEvent {

    private final ClientIntentionPacket packet;
    private final MinecraftServer server;
    private final Connection connection;
    private Component message;

    public ServerLoginEvent(ClientIntentionPacket packet, MinecraftServer server, Connection connection) {
        super();
        this.packet = packet;
        this.server = server;
        this.connection = connection;
    }

    public ClientIntentionPacket getPacket() {
        return packet;
    }

    public MinecraftServer getServer() {
        return server;
    }

    public Connection getConnection() {
        return connection;
    }

    public Component getMessage() {
        return message;
    }

    public void cancel() {
        this.message = Component.literal("Disconnected because the ServerLoginEvent was cancelled.");
        this.setCanceled(true);
    }

    public void cancel(Component message) {
        this.message = message;
        this.setCanceled(true);
    }

}
