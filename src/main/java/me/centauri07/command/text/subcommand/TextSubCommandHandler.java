package me.centauri07.command.text.subcommand;

import lombok.Getter;
import me.centauri07.command.attributes.Aliases;
import me.centauri07.command.attributes.AllowedRoles;
import me.centauri07.command.attributes.RequiredArgs;
import me.centauri07.command.attributes.RequiredArgsRange;
import org.jetbrains.annotations.Nullable;
import net.dv8tion.jda.api.Permission;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TextSubCommandHandler {
    @Getter private final Method method;

    @Getter
    private final TextSubCommand textSubCommand;
    @Getter private List<Permission> permissions = new ArrayList<>();
    @Getter private List<String> aliases = new ArrayList<>();
    @Getter private List<String> roles = new ArrayList<>();

    @Nullable @Getter
    private final RequiredArgs requiredArgs;
    @Nullable @Getter
    private final RequiredArgsRange requiredArgsRange;

    @Getter private final String name;

    public TextSubCommandHandler(Method method) {
        this.method = method;

        name = method.getName();

        textSubCommand = method.getDeclaredAnnotation(TextSubCommand.class);
        Objects.requireNonNull(textSubCommand, "Handler cannot be null");

        @Nullable me.centauri07.command.attributes.Permission permission = getClass().getDeclaredAnnotation(me.centauri07.command.attributes.Permission.class);
        if (permission != null) permissions = Arrays.asList(permission.permissions());

        @Nullable Aliases aliases = method.getDeclaredAnnotation(Aliases.class);
        if (aliases != null) this.aliases = Arrays.asList(aliases.aliases());

        @Nullable AllowedRoles roles = method.getDeclaredAnnotation(AllowedRoles.class);
        if (roles != null) this.roles = Arrays.asList(roles.roles());

        requiredArgs = method.getDeclaredAnnotation(RequiredArgs.class);
        requiredArgsRange = method.getDeclaredAnnotation(RequiredArgsRange.class);
    }
}
