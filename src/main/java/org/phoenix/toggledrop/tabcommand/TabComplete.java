/**
 * LICENSE
 * Toggledrop for prevent items being dropped
 * Copyright (C) 2025 Phoenix
 * ----------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 * END
 */

package org.phoenix.toggledrop.tabcommand;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, String[] args) {
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
