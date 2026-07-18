package pl.nick;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class NickRefresher {

    private final Plugin plugin;
    private final NickManager nickManager;

    public NickRefresher(Plugin plugin, NickManager nickManager) {
        this.plugin = plugin;
        this.nickManager = nickManager;
    }

    public void refresh(Player target) {
        NickUtil.apply(target, nickManager);
        target.setPlayerListName(nickManager.getDisplay(target));
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            if (viewer.equals(target)) {
                continue;
            }

            viewer.hidePlayer(plugin, target);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (target.isOnline() && viewer.isOnline()) {
                    viewer.showPlayer(plugin, target);
                }
            }, 2L);
        }
    }
}