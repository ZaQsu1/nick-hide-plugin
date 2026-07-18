package pl.nick;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class PingListener implements Listener {

    private final NickManager nickManager;

    public PingListener(NickManager nickManager) {
        this.nickManager = nickManager;
    }

    @EventHandler
    public void onPing(PaperServerListPingEvent event) {
        List<PaperServerListPingEvent.ListedPlayerInfo> listed = event.getListedPlayers();

        if (listed.isEmpty()) {
            return;
        }

        List<PaperServerListPingEvent.ListedPlayerInfo> result = new ArrayList<>();

        for (PaperServerListPingEvent.ListedPlayerInfo info : listed) {
            Player online = getServer().getPlayer(info.id());

            if (online != null) {
                String display = nickManager.getDisplay(online);
                result.add(new PaperServerListPingEvent.ListedPlayerInfo(display, info.id()));
            } else {
                result.add(info);
            }
        }

        listed.clear();
        listed.addAll(result);
    }
}