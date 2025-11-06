// AcCommand.java
package me.kyth.aboveChat.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.kyth.aboveChat.AboveChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

@SuppressWarnings("UnstableApiUsage")
public class AcCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> acCommand() {
        return Commands.literal("ac")

                .then(Commands.literal("help")
                        .executes(context -> {
                            CommandSender sender = context.getSource().getSender();

                            sender.sendMessage(MiniMessage.miniMessage().deserialize("<green><underlined>AboveChat 1.0-ALPHA</underlined></green>"));
                            sender.sendMessage(Component.newline());
                            sender.sendMessage(MiniMessage.miniMessage().deserialize("<gold>Commands:"));
                            sender.sendMessage(MiniMessage.miniMessage().deserialize("<yellow><click:run_command:'/ac reload'><b>/ac reload</b></click> <dark_gray>- <aqua>Reloads the configuration."));
                            sender.sendMessage(MiniMessage.miniMessage().deserialize("<yellow><click:run_command:'/ac report'><b>/ac report</b></click><dark_gray>- <aqua>Sends a detailed report about the plugin and what it's doing."));
                            sender.sendMessage(MiniMessage.miniMessage().deserialize("<yellow><click:run_command:'/ac help'><b>/ac help</b></click> <dark_gray>- <aqua>Shows this menu"));
                            sender.sendMessage(Component.newline());
                            sender.sendMessage(MiniMessage.miniMessage().deserialize("<gold>Usage:"));
                            sender.sendMessage(MiniMessage.miniMessage().deserialize("<dark_gray>- <yellow><b>Chat</b> to show your message."));
                            sender.sendMessage(MiniMessage.miniMessage().deserialize("<dark_gray>- <yellow><b>Shift</b> to hide your message."));

                            return 1;
                        })
                )

                .then(Commands.literal("reload")
                        .executes(context -> {
                            AboveChat.getPlugin().reloadConfig();
                            AboveChat.setVisibilitySeconds(AboveChat.getPlugin().getConfig().getInt("visibility-seconds", 0));
                            context.getSource().getSender().sendMessage(Component.text("Plugin reloaded!").color(NamedTextColor.GREEN));
                            return 1;
                        })
                )

                .then(Commands.literal("report")
                        .executes(context -> {
                            CommandSender sender = context.getSource().getSender();
                            var displays = AboveChat.getDisplays();
                            var plugin = AboveChat.getPlugin();
                            var pluginVersion = plugin.getPluginMeta().getVersion();
                            var tps = plugin.getServer().getTPS();

                            TextComponent.Builder report = Component.text()
                                    .append(Component.text("AC Debug Report")
                                            .color(NamedTextColor.GOLD)
                                            .decorate(TextDecoration.BOLD, TextDecoration.UNDERLINED)
                                            .append(Component.newline()));

                            if (displays.isEmpty()) {
                                report.append(Component.text("No displays have been registered")
                                        .color(NamedTextColor.RED)
                                        .append(Component.newline()));
                            } else {
                                for (var display : displays) {
                                    boolean updating = display.isUpdating();
                                    boolean isNull = display == null;
                                    String line = String.format("%s : %s : updating? %s : null? %s",
                                            display.getPlayer().getName(),
                                            display.getDisplay().getUniqueId(),
                                            updating,
                                            isNull);
                                    report.append(Component.text(line).append(Component.newline()));
                                }
                            }

                            report.append(Component.newline());

                            report.append(Component.text(String.format("Visibility Seconds: %d", AboveChat.getVisibilitySeconds()))
                                    .color(NamedTextColor.GREEN)
                                    .append(Component.newline()));
                            report.append(Component.text("Above Chat Version: " + pluginVersion)
                                    .color(NamedTextColor.GREEN)
                                    .append(Component.newline()));
                            report.append(Component.text("TPS: " + Arrays.toString(tps))
                                    .color(NamedTextColor.GREEN)
                                    .append(Component.newline()));

                            sender.sendMessage(report.build());

                            return 1;
                        })
                );
    }
}
