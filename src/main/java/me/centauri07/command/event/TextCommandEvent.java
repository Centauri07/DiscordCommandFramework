package me.centauri07.command.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.centauri07.command.text.TextCommandHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class TextCommandEvent {
    private final GuildMessageReceivedEvent event;
    @Getter final TextCommandHandler command;

    @Getter
    private final String[] args;

    public JDA getJDA() {
        return event.getJDA();
    }

    public Message getMessage() {
        return event.getMessage();
    }

    public User getAuthor() {
        return event.getAuthor();
    }

    public Member getMember() {
        return event.getMember();
    }

    public TextChannel getChannel() {
        return event.getChannel();
    }

    public Guild getGuild() {
        return event.getGuild();
    }

    public final void reply(Boolean mentionUser, String reply, Color color) {
        getMessage().replyEmbeds(new EmbedBuilder().setColor(color).setDescription(reply).build()).mentionRepliedUser(mentionUser).queue();
    }

    public final void reply(String reply, Color color) {
        getMessage().replyEmbeds(new EmbedBuilder().setColor(color).setDescription(reply).build()).mentionRepliedUser(false).queue();
    }

    public final void replyAndDelete(boolean mentionUser, String reply, Color color, int time, TimeUnit timeUnit) {
        getMessage().replyEmbeds(new EmbedBuilder().setColor(color).setDescription(reply).build()).mentionRepliedUser(mentionUser).queue( it ->
                Executors.newSingleThreadScheduledExecutor().schedule(() -> it.delete().queue(), time, timeUnit)
        );
    }

    public final void replyAndDelete(boolean mentionUser, String reply, Color color, int time) {
        getMessage().replyEmbeds(new EmbedBuilder().setColor(color).setDescription(reply).build()).mentionRepliedUser(mentionUser).queue( it ->
                Executors.newSingleThreadScheduledExecutor().schedule(() -> it.delete().queue(), time, TimeUnit.SECONDS)
        );
    }

    public final void replyAndDelete(boolean mentionUser, String reply, Color color) {
        getMessage().replyEmbeds(new EmbedBuilder().setColor(color).setDescription(reply).build()).mentionRepliedUser(mentionUser).queue( it ->
                Executors.newSingleThreadScheduledExecutor().schedule(() -> it.delete().queue(), 3, TimeUnit.SECONDS)
        );
    }

    public final void replyAndDelete(String reply, Color color) {
        getMessage().replyEmbeds(new EmbedBuilder().setColor(color).setDescription(reply).build()).mentionRepliedUser(false).queue( it ->
                Executors.newSingleThreadScheduledExecutor().schedule(() -> it.delete().queue(), 3, TimeUnit.SECONDS)
        );
    }

    public final void replyPrivately(User user, String reply, Color color) {
        user.openPrivateChannel().queue( dm ->
                dm.sendMessageEmbeds(new EmbedBuilder().setDescription(reply).setColor(color).build()).queue()
        );
    }

    public final void replyAndDeletePrivately(String reply, Color color, int time, TimeUnit timeUnit) {
        getAuthor().openPrivateChannel().queue( dm -> dm.sendMessageEmbeds(new EmbedBuilder().setColor(color).setDescription(reply).build()).queue(msg ->
                Executors.newSingleThreadScheduledExecutor().schedule(() -> msg.delete().queue(), time, timeUnit)));
    }

    public final void replyAndDeletePrivately(String reply, Color color, int time) {
        getAuthor().openPrivateChannel().queue( dm -> dm.sendMessageEmbeds(new EmbedBuilder().setColor(color).setDescription(reply).build()).queue(msg ->
                Executors.newSingleThreadScheduledExecutor().schedule(() -> msg.delete().queue(), time, TimeUnit.SECONDS)));
    }

    public final void replyAndDeletePrivately(String reply, Color color) {
        getAuthor().openPrivateChannel().queue( dm -> dm.sendMessageEmbeds(new EmbedBuilder().setColor(color).setDescription(reply).build()).queue(msg ->
                Executors.newSingleThreadScheduledExecutor().schedule(() -> msg.delete().queue(), 3, TimeUnit.SECONDS)));
    }
}
