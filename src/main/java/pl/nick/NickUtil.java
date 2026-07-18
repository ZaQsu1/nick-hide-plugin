package pl.nick;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class NickUtil {

    public static void apply(Player player, NickManager nickManager) {
        String display = nickManager.getDisplay(player);
        player.playerListName(Component.text(display));
        player.displayName(Component.text(display));
    }
}
