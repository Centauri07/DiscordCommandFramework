# CommandFramework
## Framework for Discord Commands

### Make making commands easier

# How to use it?

[![](https://jitpack.io/v/GodlyCoder07/CommandFramework.svg)](https://jitpack.io/#GodlyCoder07/CommandFramework)


```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.Centauri07</groupId>
    <artifactId>CommandFramework</artifactId>
    <version>VERSION</version>
</dependency>
```

# Main Class
```java
public class DiscordBot {
    public static void main(String[] args) {
        JDA jda = null;
        try {
            jda = JDABuilder.createDefault("ODU2NzA0NjY4OTI3NjU1OTU2.YNE6ZQ.tHqf9laHvwIbOkB_sbQBxoxVBik").build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        CommandManager commandManager = new CommandManager(jda);
        commandManager.registerTextCommand(new HelloCommand());
    }
}
```

# Making a text command

```java
@Aliases(aliases = "hi") /* <- Not Required */
@AllowedChannels(channels = "Channel IDs") /* <- Not Required */
@AllowedRoles(roles = "Roles IDs") /* <- Not Required */
@Permission(permissions = {net.dv8tion.jda.api.Permission.ADMINISTRATOR}) /* <- Not Required */
@RequiredArgs(requiredArguments = 0) /* <- Not Required */
@CommandInformation(name = "hello", description = "says hello to you", usage = "")
public class HelloCommand extends TextCommandHandler {
    @Override
    public void perform(@NotNull TextCommandEvent event) {
        event.reply("Hello!", Color.GREEN);
    }

    @Aliases(aliases = "hi") /* <- Not Required */
    @AllowedRoles(roles = "Roles IDs") /* <- Not Required */
    @Permission(permissions = {net.dv8tion.jda.api.Permission.ADMINISTRATOR}) /* <- Not Required */
    @RequiredArgs(requiredArguments = 0) /* <- Not Required */
    @TextSubCommand(name = "test")
    public void test(TextSubCommandEvent event) {
        event.replyAndDelete("Hello there, you use the subcommand `" + event.getSubCommand().getTextSubCommandInfo().name() + "`", Color.GREEN);
    }

    @Aliases(aliases = "hi") /* <- Not Required */
    @AllowedRoles(roles = "Roles IDs") /* <- Not Required */
    @Permission(permissions = {net.dv8tion.jda.api.Permission.ADMINISTRATOR}) /* <- Not Required */
    @RequiredArgs(requiredArguments = 0) /* <- Not Required */
    @TextSubCommand(name = "test2")
    public void test2(TextSubCommandEvent event) {
        event.replyAndDelete("Hello there, you use the subcommand `" + event.getSubCommand().getTextSubCommandInfo().name() + "`", Color.GREEN);
    }

    @Override /* Not Required */
    public void onNoPermission(@NotNull TextCommandEvent event) {
        event.replyAndDelete("You don't have the permission to execute this command." , Color.RED);
    }

    @Override /* Not Required */
    public void onNoRole(@NotNull TextCommandEvent event) {
        event.replyAndDelete("You don't have the role to execute this command.", Color.RED);
    }

    @Override /* Not Required */
    public void onWrongChannel(@NotNull TextCommandEvent event) {
        event.replyAndDelete("You can't use that command in + " + event.getChannel().getAsMention(), Color.RED);
    }

    @Override /* Not Required */
    public void onWrongUsage(@NotNull TextCommandEvent event) {
        event.replyAndDelete("Usage: " + CommandManager.getPrefix() + getCommandInfo().usage(), Color.RED);
    }
}
```

# Making a slash command
## Coming Soon...
