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

package org.auioc.mcmod.arnicalib.mod;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import org.auioc.mcmod.arnicalib.ArnicaLib;
import org.auioc.mcmod.arnicalib.mod.loot.AHLootItemConditions;

public class Initialization {

    public static void init() {
        modSetup();
        forgeSetup();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            //            ClientInitialization.init();
        } else {
            //            ExtensionPointUtils.serverOnly();
        }
    }

    private static final IEventBus modEventBus = ArnicaLib.modEventBus();
    private static final IEventBus forgeEventBus = NeoForge.EVENT_BUS;

    private static void modSetup() {
        AHLootItemConditions.TYPES.register(modEventBus);
        //        AHGlobalLootModifiers.GLOBAL_LOOT_MODIFIERS.register(modEventBus);
        //        AHLootItemFunctions.LOOT_FUNCTION_TYPES.register(modEventBus);
        //        HTags.init();
    }

    private static void forgeSetup() {
        //        forgeEventBus.register(AHServerEventHandler.class);
    }

}
