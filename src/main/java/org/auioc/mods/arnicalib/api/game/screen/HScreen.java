package org.auioc.mods.arnicalib.api.game.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class HScreen extends Screen {

    public static final int DEFAULT_TEXTURE_SIZE = 256;

    public HScreen(Component title) {
        super(title);
    }

    public static int center(int a) {
        return a / 2;
    }

    public static int center(int a, int b) {
        return (a - b) / 2;
    }

    public static void blitSquare(PoseStack poseStack, int x, int y, int size) {
        //              X, Y, U, V, W,    H,    TW,   TH
        blit(poseStack, x, y, 0, 0, size, size, DEFAULT_TEXTURE_SIZE, DEFAULT_TEXTURE_SIZE);
    }

    public static void blitSquare(PoseStack poseStack, int x, int y, int size, int textureSize) {
        blit(poseStack, x, y, 0, 0, size, size, textureSize, textureSize);
    }

    public static void blitSquare(PoseStack poseStack, int x, int y, int u, int v, int size, int textureSize) {
        blit(poseStack, x, y, u, v, size, size, textureSize, textureSize);
    }

    public static void closeScreen() {
        Minecraft.getInstance().setScreen((Screen) null);
    }

}
