package pl.nick;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PacketNickListener extends PacketListenerAbstract {

    private final NickManager nickManager;

    public PacketNickListener(NickManager nickManager) {
        super(PacketListenerPriority.NORMAL);
        this.nickManager = nickManager;
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.PLAYER_INFO_UPDATE) {
            return;
        }

        WrapperPlayServerPlayerInfoUpdate packet = new WrapperPlayServerPlayerInfoUpdate(event);

        for (WrapperPlayServerPlayerInfoUpdate.PlayerInfo entry : packet.getEntries()) {
            UserProfile profile = entry.getGameProfile();
            if (profile == null) {
                continue;
            }

            Player target = Bukkit.getPlayer(profile.getUUID());
            if (target == null) {
                continue;
            }

            String display = nickManager.getDisplay(target);
            if (!display.equals(profile.getName())) {
                profile.setName(display);
            }
        }
    }
}
