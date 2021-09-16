package me.centauri07.command.text;

import lombok.Getter;
import me.centauri07.command.event.TextCommandEvent;
import me.centauri07.command.text.subcommand.TextSubCommandHandler;
import me.centauri07.command.util.ReflectionUtil;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.*;
import java.util.List;

abstract public class TextCommandHandler {
    @Getter
    private Map<String, TextSubCommandHandler> subCommands = new HashMap<>();

    public TextCommandHandler() {
        if (!ReflectionUtil.getSubCommandsOf(getClass()).isEmpty())
            subCommands = ReflectionUtil.getSubCommandsOf(getClass());
    }

    public abstract void perform(@NotNull TextCommandEvent event);

    public TextSubCommandHandler getSubCommand(String name) {
        return subCommands.get(name);
    }

    public void onNoPermission(@NotNull TextCommandEvent event) {
        event.replyAndDelete("You don't have the permission to execute this command.", Color.RED);
    }

    public void onWrongChannel(@NotNull TextCommandEvent event) {
        event.replyAndDelete("You can't use this command in this channel.", Color.RED);
    }

    public void onWrongUsage(@NotNull TextCommandEvent event) {
        event.replyAndDelete("Wrong Usage.", Color.RED);
    }

    public void onNoRole(@NotNull TextCommandEvent event) {
        event.replyAndDelete("You don't have the role to execute this command.", Color.RED);
    }

    @NotNull
    public abstract String getName();

    @NotNull
    public abstract String getDescription();

    @NotNull
    public abstract List<String> getAliases();

    @NotNull
    public abstract List<Permission> getPermission();

    @NotNull
    public abstract List<String> getChannels();

    @NotNull
    public abstract List<String> getRoles();

    public abstract int getRequiredArgsSize();
}
