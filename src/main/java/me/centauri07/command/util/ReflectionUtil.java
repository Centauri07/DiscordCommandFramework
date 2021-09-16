package me.centauri07.command.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import me.centauri07.command.exception.TextSubCommandAlreadyExistsException;
import me.centauri07.command.text.subcommand.TextSubCommandHandler;
import me.centauri07.command.text.subcommand.TextSubCommand;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class ReflectionUtil {
    @SneakyThrows
    public Map<String, TextSubCommandHandler> getSubCommandsOf(final Class<?> type) {
        final Map<String, TextSubCommandHandler> subCommands = new HashMap<>();
        Class<?> clazz = type;
        while (clazz != Object.class) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(TextSubCommand.class)) {
                    TextSubCommandHandler subCommand = new TextSubCommandHandler(method);
                    if (subCommands.get(subCommand.getTextSubCommand().name()) != null) {
                        throw new TextSubCommandAlreadyExistsException("Command with name " + subCommand.getTextSubCommand().name() + " already exists");
                    }
                    subCommands.put(subCommand.getTextSubCommand().name(), subCommand);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return subCommands;
    }
}
