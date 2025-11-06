// MessageListener.java
package me.kyth.aboveChat.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.kyth.aboveChat.AboveChat;
import me.kyth.aboveChat.objects.MessageDisplay;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.ArrayList;

public class MessageListener implements Listener {

    @EventHandler
    public void onChat(AsyncChatEvent event) {

        Player player = event.getPlayer();

        Bukkit.getScheduler().runTask(AboveChat.getPlugin(), () -> {
            // Remove old displays
            for (MessageDisplay display : new ArrayList<>(AboveChat.getDisplays())) {
                if (display.getPlayer().equals(player)) {
                    display.delete();
                    AboveChat.getDisplays().remove(display);
                }
            }

            // Create the new display
            MessageDisplay display = new MessageDisplay(event.message(), player);

            // Delete it after the visibility seconds
            Bukkit.getScheduler().runTaskLater(AboveChat.getPlugin(), display::delete, AboveChat.getVisibilitySeconds() * 20L);
        });
    }

    @EventHandler
    public void onCrouch(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        AboveChat.getDisplays().stream()
                .filter(display -> display.getPlayer().equals(player))
                .forEach(display -> {
                    if (event.isSneaking()) {
                        display.hide();
                    } else {
                        display.show();
                    }
                });
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        // Delete the display
        AboveChat.getDisplays().removeIf(display -> {
            if (display.getPlayer().equals(player)) {
                display.delete();
                return true;
            }
            return false;
        });
    }


}
