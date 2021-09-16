package me.centauri07.command.event;

import lombok.Getter;
import me.centauri07.command.text.TextCommandHandler;
import me.centauri07.command.text.subcommand.TextSubCommandHandler;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class TextSubCommandEvent extends TextCommandEvent {
    @Getter private final TextSubCommandHandler subCommand;

    public TextSubCommandEvent(GuildMessageReceivedEvent event, TextCommandHandler command, String[] args, TextSubCommandHandler subCommand) {
        super(event, command, args);
        this.subCommand = subCommand;
    }
}