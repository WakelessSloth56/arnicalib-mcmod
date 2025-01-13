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

package org.auioc.mcmod.arnicalib.game.advancement;


import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.Optional;

public class DisplayInfoBuilder {

    private Component title = Component.literal("advancement");
    private Component description = Component.literal("advancement.description");
    private ItemStack icon = ItemStack.EMPTY;
    @Nullable
    private ResourceLocation background = null;
    private AdvancementType frame = AdvancementType.TASK;
    private boolean showToast = false;
    private boolean announceChat = false;
    private boolean hidden = false;

    public DisplayInfoBuilder() { }

    public DisplayInfo build() {
        return new DisplayInfo(icon, title, description, Optional.ofNullable(background), frame, showToast, hidden, announceChat);
    }

    public DisplayInfoBuilder title(Component title) {
        this.title = title;
        return this;
    }

    public DisplayInfoBuilder title(String key) {
        title(Component.translatable(key));
        return this;
    }

    public DisplayInfoBuilder description(Component description) {
        this.description = description;
        return this;
    }

    public DisplayInfoBuilder description(String key) {
        description(Component.translatable(key));
        return this;
    }

    public DisplayInfoBuilder titleAndDescription(String key) {
        title(key);
        description(key + ".description");
        return this;
    }

    public DisplayInfoBuilder icon(ItemStack icon) {
        this.icon = icon;
        return this;
    }

    public DisplayInfoBuilder icon(ItemLike icon) {
        icon(new ItemStack(icon.asItem()));
        return this;
    }

    public DisplayInfoBuilder background(ResourceLocation background) {
        this.background = background;
        return this;
    }

    public DisplayInfoBuilder background(String background) {
        background(ResourceLocation.parse(background));
        return this;
    }

    public DisplayInfoBuilder frame(AdvancementType frame) {
        this.frame = frame;
        return this;
    }

    public DisplayInfoBuilder taskFrame() {
        frame(AdvancementType.TASK);
        return this;
    }

    public DisplayInfoBuilder challengeFrame() {
        frame(AdvancementType.CHALLENGE);
        return this;
    }

    public DisplayInfoBuilder goalFrame() {
        frame(AdvancementType.GOAL);
        return this;
    }

    public DisplayInfoBuilder showToast() {
        this.showToast = true;
        return this;
    }

    public DisplayInfoBuilder announceChat() {
        this.announceChat = true;
        return this;
    }

    public DisplayInfoBuilder hidden() {
        this.hidden = true;
        return this;
    }

}
