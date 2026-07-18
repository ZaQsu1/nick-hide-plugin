package pl.nick;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final NickManager nickManager;

    public PlayerListener(NickManager nickManager) {
        this.nickManager = nickManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        NickUtil.apply(event.getPlayer(), nickManager);

        String display = nickManager.getDisplay(event.getPlayer());
        event.joinMessage(Component.text(display + " joined the game", NamedTextColor.YELLOW));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        String display = nickManager.getDisplay(event.getPlayer());
        event.quitMessage(Component.text(display + " left the game", NamedTextColor.YELLOW));
    }
}
