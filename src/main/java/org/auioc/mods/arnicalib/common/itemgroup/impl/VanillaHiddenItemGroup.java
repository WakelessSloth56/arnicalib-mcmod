package org.auioc.mods.arnicalib.common.itemgroup.impl;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

public class VanillaHiddenItemGroup extends CreativeModeTab {

    public VanillaHiddenItemGroup() {
        super("vanilla_hidden_item");
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(Items.COMMAND_BLOCK);
    }

    @Override
    public void fillItemList(NonNullList<ItemStack> list) {
        ForgeRegistries.ITEMS.getValues().stream()
            .filter((item) -> item != Items.AIR)
            .filter((item) -> item.getItemCategory() == null)
            .filter((item) -> item.getRegistryName().getNamespace().equals("minecraft"))
            .forEach((item) -> list.add(new ItemStack(item)));

    }

}
