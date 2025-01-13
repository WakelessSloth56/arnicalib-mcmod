/*
 * Copyright (C) 2022-2025 AUIOC.ORG
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

package org.auioc.mcmod.arnicalib;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.auioc.mcmod.arnicalib.game.util.BuildInfo;
import org.auioc.mcmod.arnicalib.mod.Initialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

@Mod(ArnicaLib.MOD_ID)
public final class ArnicaLib {

    public static final String MOD_ID = "arnicalib";
    public static final String MOD_NAME = "ArnicaLib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    public static final Marker MARKER = MarkerFactory.getMarker("CORE");
    public static final BuildInfo BUILD_INFO = BuildInfo.fromPackage(ArnicaLib.class);

    private static IEventBus modEventBus;

    public ArnicaLib(IEventBus modEventBus) {
        ArnicaLib.modEventBus = modEventBus;
        BUILD_INFO.log(LOGGER, MARKER);
        Initialization.init();
    }

    public static IEventBus modEventBus() {
        return modEventBus;
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

}
