# nick-hide plugin

A lightweight Paper plugin for hiding or replacing a player's displayed name across the tab list, nameplate, server list ping, and join/quit messages.

## Requirements

- Paper 1.21.11 (or a compatible fork such as Purpur, Pufferfish, Leaf)
- Java 21

This plugin relies on Paper-specific API (`joinMessage`/`quitMessage`, `PaperServerListPingEvent`) and will **not** work on plain Spigot or vanilla Bukkit.

## Features

- Hide a player's real name everywhere (tab list, nameplate, server list ping) and show `???` instead
- Set a custom nick per player, persisted across restarts
- Reset a player back to their real name
- Custom nick/hidden state is reflected in join and quit messages

## Commands

| Command | Description |
|---|---|
| `/nick <player> hide` | Hides the player's real name (`???`) |
| `/nick <player> reveal` | Reveals the player's real name |
| `/nick <player> change <new_nick>` | Sets a custom nick (max 16 characters) |
| `/nick <player> reset` | Restores the default name and visibility |

## Permissions

| Permission | Default | Description |
|---|---|---|
| `nick.admin` | op | Access to the `/nick` command |

## License

GPL-3.0, see [LICENSE](LICENSE). This is required because the plugin shades [PacketEvents](https://github.com/retrooper/packetevents) (GPL-3.0) directly into the built jar.
