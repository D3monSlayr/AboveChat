// AboveChat.java
package me.kyth.aboveChat;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.kyth.aboveChat.commands.AcCommand;
import me.kyth.aboveChat.listeners.MessageListener;
import me.kyth.aboveChat.objects.MessageDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public final class AboveChat extends JavaPlugin {

    private static AboveChat plugin;
    private static final List<MessageDisplay> displays = Collections.synchronizedList(new ArrayList<>());
    private static int visibilitySeconds;

    @Override
    public void onEnable() {
        plugin = this;

        saveDefaultConfig();

        visibilitySeconds = getConfig().getInt("visibility-seconds", 0);

        getServer().getPluginManager().registerEvents(new MessageListener(), this);

        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(AcCommand.acCommand().build());
        });

        getComponentLogger().info(Component.text("AboveChat has been enabled!").color(NamedTextColor.GREEN));
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    public static int getVisibilitySeconds() {
        return visibilitySeconds;
    }

    public static void setVisibilitySeconds(int seconds) {
        visibilitySeconds = seconds;
    }

    public static AboveChat getPlugin() {
        return plugin;
    }

    public static List<MessageDisplay> getDisplays() {
        return displays;
    }

}
