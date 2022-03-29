package org.auioc.mods.arnicalib.common.command.impl;

import static net.minecraft.commands.Commands.literal;
import static org.auioc.mods.arnicalib.ArnicaLib.LOGGER;
import java.util.function.Function;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.CommandNode;
import org.apache.logging.log4j.Marker;
import org.auioc.mods.arnicalib.ArnicaLib;
import org.auioc.mods.arnicalib.utils.LogUtil;
import org.auioc.mods.arnicalib.utils.game.TextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.MutableComponent;

public class VersionCommand {

    private static final Marker MARKER = LogUtil.getMarker(VersionCommand.class);

    private static final SimpleCommandExceptionType GET_VERSION_REFLECTION_ERROR = new SimpleCommandExceptionType(i18n("failure.reflection"));

    public static final Function<Class<?>, Command<CommandSourceStack>> HANDLER_BUILDER = (modClass) -> (ctx) -> getModVersion(ctx, modClass);

    public static final Function<Class<?>, CommandNode<CommandSourceStack>> NODE_BUILDER = (modClass) -> literal("version").executes(HANDLER_BUILDER.apply(modClass)).build();

    public static void addVersionNode(CommandNode<CommandSourceStack> node, Class<?> modClass) {
        node.addChild(NODE_BUILDER.apply(modClass));
    }

    public static int getModVersion(CommandContext<CommandSourceStack> ctx, String mainVersion, String fullVersion, String modName) {
        MutableComponent message = TextUtils.EmptyText();

        message.append(TextUtils.StringText("[" + modName + "] ").withStyle(ChatFormatting.AQUA));

        if (mainVersion.equals("0") && fullVersion.equals("0")) {
            message.append(i18n("failure.zero"));
        } else {
            message.append(i18n("success", mainVersion, fullVersion));
        }

        ctx.getSource().sendSuccess(message, false);
        return Command.SINGLE_SUCCESS;
    }

    public static int getModVersion(CommandContext<CommandSourceStack> ctx, Class<?> modClazz) throws CommandSyntaxException {
        try {
            return getModVersion(
                ctx,
                (String) modClazz.getField("MAIN_VERSION").get(modClazz),
                (String) modClazz.getField("FULL_VERSION").get(modClazz),
                (String) modClazz.getField("MOD_NAME").get(modClazz)
            );
        } catch (Exception e) {
            var commandException = GET_VERSION_REFLECTION_ERROR.create();
            LOGGER.error(MARKER, commandException.getMessage(), e);
            throw commandException;
        }
    }

    private static MutableComponent i18n(String key, Object... args) {
        return TextUtils.I18nText(ArnicaLib.i18n("command.version." + key), args);
    }

    private static MutableComponent i18n(String key) {
        return i18n(key, TextUtils.NO_ARGS);
    }

}
