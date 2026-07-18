package pl.nick;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class NickCommand implements CommandExecutor, TabCompleter {

    private final NickManager nickManager;
    private final NickRefresher refresher;

    public NickCommand(NickManager nickManager, NickRefresher refresher) {
        this.nickManager = nickManager;
        this.refresher = refresher;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("nick.admin")) {
            sender.sendMessage(Component.text("You do not have permission.", NamedTextColor.RED));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(Component.text("Usage: /nick <player> <hide|reveal|change|reset> [new_nick]", NamedTextColor.YELLOW));
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage(Component.text("The player must be online.", NamedTextColor.RED));
            return true;
        }

        switch (args[1].toLowerCase()) {
            case "hide" -> {
                nickManager.setHidden(target.getUniqueId(), true);
                refresher.refresh(target);
                sender.sendMessage(Component.text(target.getName() + " is now hidden as ???.", NamedTextColor.GREEN));
            }
            case "reveal" -> {
                nickManager.setHidden(target.getUniqueId(), false);
                refresher.refresh(target);
                sender.sendMessage(Component.text(target.getName() + " has been revealed.", NamedTextColor.GREEN));
            }
            case "change" -> {
                if (args.length < 3) {
                    sender.sendMessage(Component.text("Usage: /nick <player> change <new_nick>", NamedTextColor.RED));
                    return true;
                }

                String nick = args[2];
                if (nick.length() > 16) {
                    sender.sendMessage(Component.text("The nick can be at most 16 characters long.", NamedTextColor.RED));
                    return true;
                }

                nickManager.setNick(target.getUniqueId(), nick);
                refresher.refresh(target);
                sender.sendMessage(Component.text(target.getName() + " now has the nick: " + nick, NamedTextColor.GREEN));
            }
            case "reset" -> {
                nickManager.setNick(target.getUniqueId(), null);
                nickManager.setHidden(target.getUniqueId(), false);
                refresher.refresh(target);
                sender.sendMessage(Component.text("Default nick and visibility have been restored for player " + target.getName() + ".", NamedTextColor.GREEN));
            }
            default -> sender.sendMessage(Component.text("Usage: /nick <player> <hide|reveal|change|reset> [new_nick]", NamedTextColor.YELLOW));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> result = new ArrayList<>();

        if (args.length == 1) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                    result.add(p.getName());
                }
            }
            return result;
        }

        if (args.length == 2) {
            for (String option : List.of("hide", "reveal", "change", "reset")) {
                if (option.startsWith(args[1].toLowerCase())) {
                    result.add(option);
                }
            }
            return result;
        }

        return result;
    }
}
