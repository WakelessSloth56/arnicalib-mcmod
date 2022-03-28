package org.auioc.mods.arnicalib.utils.game;

import static org.auioc.mods.arnicalib.ArnicaLib.i18n;
import static org.auioc.mods.arnicalib.utils.game.TextUtils.I18nText;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import org.auioc.mods.arnicalib.api.mixin.server.IMixinCommandSourceStack;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface CommandUtils {

    SimpleCommandExceptionType INTERNAL_ERROR = new SimpleCommandExceptionType(I18nText(i18n("command.failure.internal")));
    SimpleCommandExceptionType NOT_SERVER_ERROR = new SimpleCommandExceptionType(I18nText(i18n("command.failure.not_server")));
    SimpleCommandExceptionType NOT_DEDICATED_SERVER_ERROR = new SimpleCommandExceptionType(I18nText(i18n("command.failure.not_dedicated_server")));
    SimpleCommandExceptionType REFLECTION_ERROR = new SimpleCommandExceptionType(I18nText(i18n("command.failure.reflection")));

    /**
     * @param sourceStack
     * @return The real command source of the specified {@code CommandSourceStack}
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @since 4.0.0
     * @deprecated Use {@link IMixinCommandSourceStack} instead {@code ((IMixinCommandSourceStack)stack).getSource()}
     */
    @Deprecated(since = "4.1.5")
    static CommandSource getPrivateSource(CommandSourceStack sourceStack) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field privateSourceField = CommandSourceStack.class.getDeclaredField("source");
        privateSourceField.setAccessible(true);
        return (CommandSource) privateSourceField.get(sourceStack);
    }

    @OnlyIn(Dist.CLIENT)
    static LocalPlayer getLocalPlayerOrException(CommandSourceStack sourceStack) throws CommandSyntaxException {
        var entity = sourceStack.getEntity();
        if (entity instanceof LocalPlayer) {
            return (LocalPlayer) entity;
        }
        throw CommandSourceStack.ERROR_NOT_PLAYER.create();
    }

    /**
     * @param nodes List of {@link ParsedCommandNode}s, from {@link CommandContext#getNodes()}
     * @param fromIndex
     * @param toIndex
     * @param conventToSnakeCase
     * @return String that concatenates the literals (or in its snake case) of all (or some of) {@link LiteralCommandNode}s in the {@link ParsedCommandNode} list, separated by dots
     * @since 5.1.1
     */
    static String joinLiteralNodes(List<ParsedCommandNode<CommandSourceStack>> nodes, int fromIndex, int toIndex, boolean conventToSnakeCase) {
        return nodes
            .subList(fromIndex, toIndex)
            .stream()
            .map(ParsedCommandNode::getNode)
            .filter((node) -> node instanceof LiteralCommandNode)
            .map((node) -> (LiteralCommandNode<CommandSourceStack>) node)
            .map(LiteralCommandNode::getLiteral)
            .map((literal) -> conventToSnakeCase ? literal.replaceAll("[A-Z]", "_$0").toLowerCase() : literal)
            .collect(Collectors.joining("."));
    }

    /**
     * @see {@link #joinLiteralNodes(List, int, int, boolean)}
     */
    static String joinLiteralNodes(List<ParsedCommandNode<CommandSourceStack>> nodes, int fromIndex) {
        return joinLiteralNodes(nodes, fromIndex, nodes.size(), true);
    }

    /**
     * @see {@link #joinLiteralNodes(List, int, int, boolean)}
     */
    static String joinLiteralNodes(List<ParsedCommandNode<CommandSourceStack>> nodes) {
        return joinLiteralNodes(nodes, 0, nodes.size(), true);
    }

    public static class CommandFeedbackHelper {

        private final Function<String, String> i18n;

        public CommandFeedbackHelper(Function<String, String> i18n) {
            this.i18n = i18n;
        }

        public TranslatableComponent successMessage(String key) {
            return I18nText(this.i18n.apply("command." + key + ".success"));
        }

        public TranslatableComponent successMessage(String key, Object... args) {
            return I18nText(this.i18n.apply("command." + key + ".success"), args);
        }

        public TranslatableComponent failureMessage(String key) {
            return I18nText(this.i18n.apply("command." + key + ".failure"));
        }

        public TranslatableComponent failureMessage(String key, Object... args) {
            return I18nText(this.i18n.apply("command." + key + ".failure"), args);
        }

        public int success(CommandContext<CommandSourceStack> ctx, String key) {
            ctx.getSource().sendSuccess(this.successMessage(key), false);
            return Command.SINGLE_SUCCESS;
        }

        public int success(CommandContext<CommandSourceStack> ctx, String key, Object... args) {
            ctx.getSource().sendSuccess(this.successMessage(key, args), false);
            return Command.SINGLE_SUCCESS;
        }

        public int failure(CommandContext<CommandSourceStack> ctx, String key) {
            ctx.getSource().sendFailure(this.failureMessage(key));
            return Command.SINGLE_SUCCESS;
        }

        public int failure(CommandContext<CommandSourceStack> ctx, String key, Object... args) {
            ctx.getSource().sendFailure(this.failureMessage(key, args));
            return Command.SINGLE_SUCCESS;
        }

    }

}
