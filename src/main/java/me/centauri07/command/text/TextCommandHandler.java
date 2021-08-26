package me.centauri07.command.text;

import lombok.Getter;
import me.centauri07.command.CommandInformation;
import me.centauri07.command.CommandManager;
import me.centauri07.command.attributes.*;
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
    private final CommandInformation commandInfo;

    @Nullable @Getter
    private final RequiredArgs requiredArgs;
    @Nullable @Getter
    private final RequiredArgsRange requiredArgsRange;

    @Getter private List<Permission> permissions = new ArrayList<>();
    @Getter private List<String> aliases = new ArrayList<>();
    @Getter private List<String> allowedChannels = new ArrayList<>();
    @Getter private List<String> allowedRoles = new ArrayList<>();
    @Getter private Map<String, TextSubCommandHandler> subCommands = new HashMap<>();

    public TextCommandHandler() {
        commandInfo = getClass().getDeclaredAnnotation(CommandInformation.class);
        Objects.requireNonNull(commandInfo, "Command information must not be null.");

        @Nullable me.centauri07.command.attributes.Permission permission = getClass().getDeclaredAnnotation(me.centauri07.command.attributes.Permission.class);
        if (permission != null) permissions = Arrays.asList(permission.permissions());

        @Nullable Aliases aliasesAnnotation = getClass().getDeclaredAnnotation(Aliases.class);
        if (aliasesAnnotation != null) aliases = Arrays.asList(aliasesAnnotation.aliases());

        @Nullable AllowedChannels allowedChannelsAnnotation = getClass().getDeclaredAnnotation(AllowedChannels.class);
        if (allowedChannelsAnnotation != null) allowedChannels = Arrays.asList(allowedChannelsAnnotation.channels());

        @Nullable AllowedRoles allowedRolesAnnotation = getClass().getDeclaredAnnotation(AllowedRoles.class);
        if (allowedRolesAnnotation != null) allowedRoles = Arrays.asList(allowedRolesAnnotation.roles());

        requiredArgs = getClass().getDeclaredAnnotation(RequiredArgs.class);

        if (!ReflectionUtil.getSubCommandsOf(getClass()).isEmpty()) subCommands = ReflectionUtil.getSubCommandsOf(getClass());

        requiredArgsRange = getClass().getDeclaredAnnotation(RequiredArgsRange.class);
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
        event.replyAndDelete("Wrong Usage. Usage: " + CommandManager.getPrefix() + getCommandInfo().usage(), Color.RED);
    }

    public void onNoRole(@NotNull TextCommandEvent event) {
        event.replyAndDelete("You don't have the role to execute this command.", Color.RED);
    }

}
