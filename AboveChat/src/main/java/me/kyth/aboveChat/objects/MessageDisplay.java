// MessageDisplay.java
package me.kyth.aboveChat.objects;

import me.kyth.aboveChat.AboveChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.scheduler.BukkitTask;

public class MessageDisplay {

    private Player player;
    private Component message;
    private final TextDisplay display;
    private BukkitTask updateTask;
    private boolean isUpdating;

    public MessageDisplay(Component message, Player player) {
        this.message = message;
        this.player = player;

        display = this.player.getWorld().spawn(player.getLocation().add(0, 2.3, 0), TextDisplay.class, textDisplay -> {
            textDisplay.setAlignment(TextDisplay.TextAlignment.CENTER);
            textDisplay.setSeeThrough(true);
            textDisplay.setShadowed(false);
            textDisplay.setBillboard(Display.Billboard.CENTER);
            textDisplay.setViewRange(32f);
            textDisplay.setVisibleByDefault(true);
            textDisplay.text(message.decorate(TextDecoration.ITALIC));
        });

        AboveChat.getDisplays().add(this);

        update();
    }

    public void update() {
        if (!AboveChat.getDisplays().contains(this)) return;
        if (player == null || display == null) return;

        isUpdating = true;

        // Cancel previous task if exists
        if (updateTask != null && !updateTask.isCancelled()) {
            updateTask.cancel();
        }

        updateTask = Bukkit.getScheduler().runTaskTimer(AboveChat.getPlugin(), () -> {
            if (!player.isOnline() || !display.isValid()) {
                isUpdating = false;
                if (updateTask != null && !updateTask.isCancelled()) {
                    updateTask.cancel();
                    updateTask = null;
                }
                return;
            }
            display.teleport(player.getLocation().add(0, 2.3, 0));
        }, 0L, 1L);
    }

    public void hide() {
        display.setVisibleByDefault(false);
    }

    public void show() {
        display.setVisibleByDefault(true);
    }

    public void delete() {
        if (player == null || display == null) return;
        if (!AboveChat.getDisplays().contains(this)) return;

        AboveChat.getDisplays().remove(this);
        isUpdating = false;
        if (updateTask != null && !updateTask.isCancelled()) {
            updateTask.cancel();
            updateTask = null;
        }
        if (display.isValid()) {
            display.remove();
        }
        message = Component.empty();
        player = null;
    }

    public Player getPlayer() {
        return player;
    }

    public TextDisplay getDisplay() {
        return display;
    }

    public Component getMessage() {
        return message;
    }

    public boolean isUpdating() {
        return isUpdating;
    }
}
