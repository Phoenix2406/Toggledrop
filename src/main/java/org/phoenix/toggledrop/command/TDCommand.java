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

package org.phoenix.toggledrop.command;

import org.bukkit.Material;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.phoenix.toggledrop.Main;
import org.phoenix.toggledrop.utils.ColorCodeTranslate;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.*;

public class TDCommand implements CommandExecutor, Listener {

    private final Main plugin;
    private final HashSet<UUID> playerToggle = new HashSet<>();

    public TDCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, String label, @NonNull String[] args) {
        if (label.equalsIgnoreCase("toggledrop") || label.equalsIgnoreCase("td")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + " &cSorry! Only player can execute this command."));
                return true;
            }
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            sender.sendMessage(ColorCodeTranslate.chat(""));
            sender.sendMessage(ColorCodeTranslate.chat("&#00E6FF&lToggledrop Plugin"));
            sender.sendMessage(ColorCodeTranslate.chat("&dTo see all command use /toggledrop help."));
            sender.sendMessage(ColorCodeTranslate.chat(""));
        }

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("help")) {
                if (player.isOp() || player.hasPermission("toggledrop.help")) {
                    sender.sendMessage(ColorCodeTranslate.chat("&#00E6FF&l&m------------&r &#00E6FF&lToggledrop &#00E6FF&l&m------------"));
                    sender.sendMessage(ColorCodeTranslate.chat(""));
                    sender.sendMessage(ColorCodeTranslate.chat("&d/toggledrop help &f▶ &7To see all command in this plugin."));
                    sender.sendMessage(ColorCodeTranslate.chat("&d/toggledrop toggle [player] &f▶ &7To turn ON/OFF your or other player drop."));
                    sender.sendMessage(ColorCodeTranslate.chat("&d/toggledrop reload &f▶ &7To reload this plugin."));
                    sender.sendMessage(ColorCodeTranslate.chat("&d/toggledrop version &f▶ &7See current version of this plugin."));
                    sender.sendMessage(ColorCodeTranslate.chat(""));
                    sender.sendMessage(ColorCodeTranslate.chat("&#00E6FF&l&m-------------------------------------"));
                } else {
                    sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + " &cYou need permission toggledrop.help to use this command."));
                }
            } else if (args[0].equalsIgnoreCase("toggle")) {
                if (player.hasPermission("toggledrop.toggle")) {
                    if (args.length != 2) {
                        if (!playerToggle.contains(player.getUniqueId())) {
                            playerToggle.add(player.getUniqueId());
                            sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + " " + plugin.getMessagesConfig().getString("messages.toggleEnable.selfToggle")));
                        } else if (playerToggle.contains(player.getUniqueId())) {
                            playerToggle.remove(player.getUniqueId());
                            sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + " " + plugin.getMessagesConfig().getString("messages.toggleDisable.selfToggle")));
                        }
                    } else {
                        if (player.isOp() || player.hasPermission("toggledrop.toggle.other")) {
                            Player target = Bukkit.getPlayer(args[1]);

                            if (target != null) {
                                if (!playerToggle.contains(target.getUniqueId())) {
                                    playerToggle.add(target.getUniqueId());
                                    sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + " " + plugin.getMessagesConfig().getString("messages.toggleEnable.playerToggle") + args[1] + "&a!"));
                                } else if (playerToggle.contains(target.getUniqueId())) {
                                    playerToggle.remove(target.getUniqueId());
                                    sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + " " + plugin.getMessagesConfig().getString("messages.toggleDisable.playerToggle") + args[1] + "&a!"));
                                }
                            } else {
                                sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + " &cSorry! Player " + args[1] + " is not online."));
                            }
                        } else {
                            sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + " &cYou need permission toggledrop.toggle.other to use this command."));
                        }
                    }
                } else {
                    sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + " &cYou need permission toggledrop.toggle to use this command."));
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (player.isOp() || player.hasPermission("toggledrop.reload")) {
                    if (!plugin.getConfig().getString("version").equals(Main.getInstance().getVersion())) {
                        sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + " &cPlugin can't reload properly. Try to see console for more information!"));
                        throw new IllegalArgumentException("Plugin version is corrupted on config.yml");
                    } else {
                        sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + " &aPlugin successfully reloaded!"));
                        plugin.saveDefaultConfig();
                        plugin.reloadConfig();
                    }
                } else {
                    sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + " &cYou need permission toggledrop.reload to use this command."));
                }
            } else if (args[0].equalsIgnoreCase("version")) {
                if (player.isOp() || player.hasPermission("toggledrop.version")) {
                    sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + " &aThis plugin running on &b&nversion " + Main.getInstance().getVersion()));
                } else {
                    sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + " &cYou need permission toggledrop.version to use this command."));
                }
            }
        }
        return false;
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        Set<Material> materialList = new HashSet<>();

        if (!plugin.getConfig().getStringList("item-filter.list").isEmpty()) {
            for (String rawItem : plugin.getConfig().getStringList("item-filter.list")) {
                Material material = Material.matchMaterial(rawItem);
                materialList.add(material);
            }
        }

        if (playerToggle.contains(player.getUniqueId())) {
            if (!plugin.getConfig().getStringList("disableWorld").contains(player.getWorld().getName())) {
                if (plugin.getConfig().getString("item-filter.type").toUpperCase().equals("EXCLUDE") &&
                        !materialList.contains(e.getItemDrop().getItemStack().getType()) ||
                        plugin.getConfig().getString("item-filter.type").toUpperCase().equals("INCLUDE") &&
                                materialList.contains(e.getItemDrop().getItemStack().getType()) || materialList.isEmpty()) {
                    player.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + " " + plugin.getMessagesConfig().getString("messages.cantDrop")));
                    e.setCancelled(true);
                }
            }
        }
    }
}
