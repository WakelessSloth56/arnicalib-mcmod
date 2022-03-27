package org.auioc.mods.arnicalib;

import org.auioc.mods.arnicalib.client.config.ClientConfig;
import org.auioc.mods.arnicalib.client.event.ClientEventHandler;
import org.auioc.mods.arnicalib.common.command.AHCommandArguments;
import org.auioc.mods.arnicalib.common.itemgroup.AHItemGroups;
import org.auioc.mods.arnicalib.common.network.AHPacketHandler;
import org.auioc.mods.arnicalib.server.event.ServerEventHandler;
import org.auioc.mods.arnicalib.server.loot.AHGlobalLootModifiers;
import org.auioc.mods.arnicalib.server.loot.AHLootItemConditions;
import org.auioc.mods.arnicalib.server.loot.AHLootItemFunctions;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@SuppressWarnings("unused")
public final class Initialization {

    public static void init() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        final IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        final ClientSideOnlySetup ClientSideOnlySetup = new ClientSideOnlySetup(modEventBus, forgeEventBus);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientSideOnlySetup::registerConfig);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientSideOnlySetup::modSetup);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientSideOnlySetup::forgeSetup);

        final CommonSetup CommonSetup = new CommonSetup(modEventBus, forgeEventBus);
        CommonSetup.registerConfig();
        CommonSetup.modSetup();
        CommonSetup.forgeSetup();
    }


    private final static class CommonSetup {

        private final IEventBus modEventBus;
        private final IEventBus forgeEventBus;

        public CommonSetup(final IEventBus modEventBus, final IEventBus forgeEventBus) {
            this.modEventBus = modEventBus;
            this.forgeEventBus = forgeEventBus;
        }

        public void registerConfig() {}

        @SuppressWarnings(value = {"unchecked", "rawtypes"})
        private void modSetup() {
            AHPacketHandler.init();
            AHCommandArguments.init();
            modEventBus.register(AHGlobalLootModifiers.class);
            modEventBus.addGenericListener(
                GlobalLootModifierSerializer.class, // It works within any RegistryEvent.Register
                (RegistryEvent.Register event) -> {
                    AHLootItemConditions.init();
                    AHLootItemFunctions.init();
                }
            );
        }

        private void forgeSetup() {
            AHItemGroups.init();
            forgeEventBus.register(ServerEventHandler.class);
        }

    }


    private final static class ClientSideOnlySetup {

        private final IEventBus modEventBus;
        private final IEventBus forgeEventBus;

        public ClientSideOnlySetup(final IEventBus modEventBus, final IEventBus forgeEventBus) {
            this.modEventBus = modEventBus;
            this.forgeEventBus = forgeEventBus;
        }

        public void registerConfig() {
            ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.CONFIG);
        }

        public void modSetup() {}

        public void forgeSetup() {
            forgeEventBus.register(ClientEventHandler.class);
        }

    }

}