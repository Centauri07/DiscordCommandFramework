# CommandFramework
## Framework for Discord Commands

### Make making commands easier

# How to use it?

[![](https://jitpack.io/v/Centauri07/DiscordCommandFramework.svg)](https://jitpack.io/#Centauri07/DiscordCommandFramework)

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
public class HelloCommand extends TextCommandHandler {
    @Override
    public void perform(@NotNull TextCommandEvent event) {
        event.reply("Test success", Color.GREEN);
    }

    @TextSubCommand(name = "hello")
    public void hello(TextSubCommandEvent event) {
        event.reply("Hi", Color.GREEN);
    }

    @Override
    public @NotNull String getName() {
        return "test";
    }

    @Override
    public @NotNull String getDescription() {
        return "test command";
    }

    @Override
    public @NotNull List<String> getAliases() {
        return Collections.emptyList();
    }

    @Override
    public @NotNull List<Permission> getPermission() {
        return Collections.emptyList();
    }

    @Override
    public @NotNull List<String> getChannels() {
        return Collections.emptyList();
    }

    @Override
    public @NotNull List<String> getRoles() {
        return Collections.emptyList();
    }

    @Override
    public int getRequiredArgsSize() {
        return 0;
    }
}
```

# Making a slash command
## Coming Soon...
