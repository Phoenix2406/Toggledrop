package org.phoenix.toggledrop.tabcommand;

import org.phoenix.toggledrop.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabComplete implements TabCompleter {

    private Main plugin;

    // Register the plugin variable to get variable in Main Class
    public TabComplete(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();

        // Tab Completer for command in argument 1
        if (args.length == 1) {
            if (sender.isOp() || sender.hasPermission("toggledrop.help")) {
                commands.add("help");
            }
            if (sender.isOp() || sender.hasPermission("toggledrop.toggle")) {
                commands.add("toggle");
            }
            if (sender.isOp() || sender.hasPermission("toggledrop.reload")) {
                commands.add("reload");
            }
            if (sender.isOp() || sender.hasPermission("toggledrop.version")) {
                commands.add("version");
            }
            StringUtil.copyPartialMatches(args[0], commands, completions);

        // Tab Completer for command in argument 2
        } else if (args.length == 2) {
            if (args[0].equals("toggle")) {
                if (sender.isOp() || sender.hasPermission("toggledrop.toggle.other")) {
                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                        commands.add(onlinePlayers.getName());
                    }
                }
            }
            StringUtil.copyPartialMatches(args[1], commands, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
