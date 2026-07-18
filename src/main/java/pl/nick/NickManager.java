package pl.nick;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class NickManager {

    private final Nick plugin;
    private final File file;
    private final FileConfiguration config;

    private final Map<UUID, String> nicks = new HashMap<>();
    private final Set<UUID> hidden = new HashSet<>();

    public NickManager(Nick plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "data.yml");

        if (!file.exists()) {
            plugin.getDataFolder().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to create data.yml: " + e.getMessage());
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
        load();
    }

    private void load() {
        if (config.isConfigurationSection("nicks")) {
            for (String key : config.getConfigurationSection("nicks").getKeys(false)) {
                nicks.put(UUID.fromString(key), config.getString("nicks." + key));
            }
        }

        for (String key : config.getStringList("hidden")) {
            hidden.add(UUID.fromString(key));
        }
    }

    public void save() {
        config.set("nicks", null);

        for (Map.Entry<UUID, String> entry : nicks.entrySet()) {
            config.set("nicks." + entry.getKey(), entry.getValue());
        }

        config.set("hidden", hidden.stream().map(UUID::toString).toList());

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save data.yml: " + e.getMessage());
        }
    }

    public void setNick(UUID uuid, String nick) {
        if (nick == null) {
            nicks.remove(uuid);
        } else {
            nicks.put(uuid, nick);
        }
        save();
    }

    public String getNick(UUID uuid) {
        return nicks.get(uuid);
    }

    public void setHidden(UUID uuid, boolean value) {
        if (value) {
            hidden.add(uuid);
        } else {
            hidden.remove(uuid);
        }
        save();
    }

    public boolean isHidden(UUID uuid) {
        return hidden.contains(uuid);
    }

    public String getDisplay(Player player) {
        if (isHidden(player.getUniqueId())) {
            return "???";
        }
        String nick = getNick(player.getUniqueId());
        return nick != null ? nick : player.getName();
    }
}
