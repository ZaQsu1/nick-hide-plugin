package pl.nick;

import com.github.retrooper.packetevents.PacketEvents;
// ADDED IMPORT: Needed to build the platform for Spigot/Paper
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public class Nick extends JavaPlugin {

    private NickManager nickManager;

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        PacketEvents.getAPI().init();
        nickManager = new NickManager(this);
        NickRefresher refresher = new NickRefresher(this, nickManager);
        getCommand("nick").setExecutor(new NickCommand(nickManager, refresher));
        getServer().getPluginManager().registerEvents(new PlayerListener(nickManager), this);
        getServer().getPluginManager().registerEvents(new PingListener(nickManager), this);
        PacketEvents.getAPI().getEventManager().registerListener(new PacketNickListener(nickManager));
    }

    @Override
    public void onDisable() {
        if (nickManager != null) {
            nickManager.save();
        }
        PacketEvents.getAPI().terminate();
    }


    public NickManager getNickManager() {
        return nickManager;
    }
}