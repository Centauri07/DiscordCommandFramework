package me.centauri07.command;

import lombok.Getter;
import lombok.Setter;
import me.centauri07.command.event.TextCommandEvent;
import me.centauri07.command.event.TextSubCommandEvent;
import me.centauri07.command.exception.IllegalArgumentCountException;
import me.centauri07.command.exception.TextCommandAlreadyExistsException;
import me.centauri07.command.text.TextCommandHandler;
import me.centauri07.command.text.subcommand.TextSubCommandHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;

public class CommandManager extends ListenerAdapter {
    @Getter
    private final List<TextCommandHandler> commands = new ArrayList<>();

    @Getter
    private final Map<String, List<TextCommandHandler>> commandGroupMap = new HashMap<>();

    @Getter
    private final JDA jda;

    @Getter
    @Setter
    private static String prefix = "/";

    public CommandManager(JDA jda) {
        this.jda = jda;
        jda.addEventListener(this);
    }

    public void registerTextCommand(TextCommandHandler textCommandHandler) {
        if (getTextCommand(textCommandHandler.getName()) != null) {
            try {
                throw new TextCommandAlreadyExistsException("Command with name " + textCommandHandler.getName() + " already exists");
            } catch (TextCommandAlreadyExistsException e) {
                e.printStackTrace();
            }
        }
        commands.add(textCommandHandler);
    }

    public TextCommandHandler getTextCommand(String name) {
        return commands.stream().filter(command -> command.getName().equals(name) || command.getAliases().contains(name)).findFirst().orElse(null);
    }

    private Consumer<GuildMessageReceivedEvent> unknownCommandAction;

    public void onUnknownCommand(Consumer<GuildMessageReceivedEvent> action) {
        unknownCommandAction = action;
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().startsWith(prefix)) {
            String[] message = event.getMessage().getContentRaw().split(" ");

            TextCommandHandler command = getTextCommand(message[0].replace(prefix, ""));

            if (command == null) {
                if (unknownCommandAction != null) unknownCommandAction.accept(event);
                return;
            }

            String[] args = Arrays.copyOfRange(message, 1, message.length);

            TextCommandEvent commandEvent = new TextCommandEvent(event, command, args);

            if (args.length >= 1) {
                TextSubCommandHandler subCommand = command.getSubCommand(args[0]);

                if (subCommand != null) {
                    TextSubCommandEvent subCommandEvent = new TextSubCommandEvent(event, command, Arrays.copyOfRange(args, 1, args.length), subCommand);

                    if (validateRole(subCommandEvent) && validateChannel(subCommandEvent) && validatePermission(subCommandEvent) && validateArguments(subCommandEvent)) {
                        subCommand.getMethod().setAccessible(true);
                        try {
                            subCommand.getMethod().invoke(command, subCommandEvent);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    return;
                }
            }

            if (validateRole(commandEvent) && validateChannel(commandEvent) && validatePermission(commandEvent) && validateArguments(commandEvent)) {
                command.perform(commandEvent);
            }
        }
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        // TODO
    }

    private boolean validateChannel(TextCommandEvent event) {
        if (event.getCommand().getChannels().isEmpty()) return true;

        if (event.getCommand().getChannels().contains(event.getChannel().getId())) return true;

        event.getCommand().onWrongChannel(event);
        return false;
    }

    private boolean validateRole(TextCommandEvent event) {
        if (event.getCommand().getRoles().isEmpty()) return true;

        List<Role> roles = new ArrayList<>();
        for (String roleId : event.getCommand().getRoles()) {
            Role role = event.getGuild().getRoleById(roleId);
            if (role != null)
                roles.add(role);
        }

        if (event.getGuild().getMembersWithRoles(roles).contains(event.getMember())) return true;

        event.getCommand().onNoRole(event);
        return false;
    }

    private boolean validateRole(TextSubCommandEvent event) {
        if (event.getSubCommand().getRoles().isEmpty()) return true;

        List<Role> roles = new ArrayList<>();
        for (String roleId : event.getSubCommand().getRoles()) {
            Role role = event.getGuild().getRoleById(roleId);
            if (role != null)
                roles.add(role);
        }

        if (event.getGuild().getMembersWithRoles(roles).contains(event.getMember())) return true;

        event.getCommand().onNoRole(event);
        return false;
    }

    private boolean validateArguments(TextCommandEvent event) {
        if (event.getArgs().length == event.getCommand().getRequiredArgsSize()) {
            if (event.getCommand().getRequiredArgsSize() < 0) {
                try {
                    throw new IllegalArgumentCountException("Required argument count cannot be less than 0");
                } catch (IllegalArgumentCountException e) {
                    e.printStackTrace();
                }
                return false;
            }
            return true;
        }

        event.getCommand().onWrongUsage(event);
        return false;
    }

    private boolean validateArguments(TextSubCommandEvent event) {
        if (event.getSubCommand().getRequiredArgsRange() == null && event.getSubCommand().getRequiredArgs() == null) return true;

        if (event.getSubCommand().getRequiredArgsRange() != null) {
            if (event.getSubCommand().getRequiredArgsRange().lowerBound() > event.getSubCommand().getRequiredArgsRange().upperBound()) {
                try {
                    throw new IllegalArgumentCountException("Upper bound can't be more than Lower bound!");
                } catch (IllegalArgumentCountException e) {
                    e.printStackTrace();
                }
                return false;
            }

            if (event.getSubCommand().getRequiredArgsRange().lowerBound() < 0) {
                try {
                    throw new IllegalArgumentCountException("Lower bound can't be less than 0");
                } catch (IllegalArgumentCountException e) {
                    e.printStackTrace();
                }
                return false;
            }

            if (event.getArgs().length >= event.getSubCommand().getRequiredArgsRange().lowerBound() && event.getArgs().length <= event.getSubCommand().getRequiredArgsRange().upperBound())
                return true;

        } else if (event.getSubCommand().getRequiredArgs() != null && event.getArgs().length == event.getSubCommand().getRequiredArgs().requiredArguments()) {
            if (event.getSubCommand().getRequiredArgs().requiredArguments() < 0) {
                try {
                    throw new IllegalArgumentCountException("Required argument count cannot be less than 0");
                } catch (IllegalArgumentCountException e) {
                    e.printStackTrace();
                }
                return false;
            }
            return true;
        }

        event.getCommand().onWrongUsage(event);
        return false;
    }

    private boolean validatePermission(TextCommandEvent event) {
        if (event.getCommand().getPermission().isEmpty()) return true;

        if (hasPermission(event.getCommand().getPermission(), event.getMember())) return true;

        event.getCommand().onNoPermission(event);
        return false;
    }

    private boolean validatePermission(TextSubCommandEvent event) {
        if (event.getSubCommand().getPermissions().isEmpty()) return true;

        if (hasPermission(event.getSubCommand().getPermissions(), event.getMember())) return true;

        event.getCommand().onNoPermission(event);
        return false;
    }

    private boolean hasPermission(List<Permission> permissions, Member member) {
        for (Permission permission : permissions) {
            if (member.getPermissions().contains(permission)) return true;
        }

        return false;
    }
}