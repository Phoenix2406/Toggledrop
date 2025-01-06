package org.phoenix.toggledrop.command;

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

import java.util.HashSet;
import java.util.UUID;

public class TDCommand implements CommandExecutor, Listener {

    private Main plugin;

    public TDCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("toggledrop").setExecutor(this);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    HashSet<UUID> selfToggle = new HashSet<>();
    HashSet<UUID> playerToggle = new HashSet<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("toggledrop") || label.equalsIgnoreCase("td")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + "&cSorry! Only player can execute this command."));
                return true;
            }
        }

        Player p = (Player) sender;

        if (args.length == 0) {
            sender.sendMessage(ColorCodeTranslate.chat(""));
            sender.sendMessage(ColorCodeTranslate.chat("&#00E6FF&lToggledrop Plugin"));
            sender.sendMessage(ColorCodeTranslate.chat("&dTo see all command use /toggledrop help."));
            sender.sendMessage(ColorCodeTranslate.chat(""));
        }

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("help")) {
                if (p.isOp() || p.hasPermission("toggledrop.help")) {
                    sender.sendMessage(ColorCodeTranslate.chat("&#00E6FF&l&m------------&r&#00E6FF&lToggledrop &#00E6FF&l&m------------"));
                    sender.sendMessage(ColorCodeTranslate.chat(""));
                    sender.sendMessage(ColorCodeTranslate.chat("&d/giftaway help &f▶ &7To see all command in this plugin."));
                    sender.sendMessage(ColorCodeTranslate.chat("&d/giftaway toggle [player] &f▶ &7To turn ON/OFF your or other player toggledrop."));
                    sender.sendMessage(ColorCodeTranslate.chat("&d/giftaway reload &f▶ &7To reload this plugin."));
                    sender.sendMessage(ColorCodeTranslate.chat("&d/giftaway version &f▶ &7See version of this plugin."));
                    sender.sendMessage(ColorCodeTranslate.chat(""));
                    sender.sendMessage(ColorCodeTranslate.chat("&#00E6FF&l&m--------------------------------"));
                } else {
                    sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + "&cYou need permission toggledrop.help to use this command."));
                }
            } else if (args[0].equalsIgnoreCase("toggle")) {
                if (p.isOp() || p.hasPermission("toggledrop.toggle")) {
                    if (plugin.getConfig().getStringList("disableWorld").contains(p.getWorld().getName())){
                        sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + plugin.getMessagesConfig().getString("messages.disableWorld")));
                    } else {
                        if (args.length != 2) {
                            if (!selfToggle.contains(p.getUniqueId())) {
                                selfToggle.add(p.getUniqueId());
                                sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + plugin.getMessagesConfig().getString("messages.toggleEnable.selfToggle")));
                            } else if (selfToggle.contains(p.getUniqueId())) {
                                selfToggle.remove(p.getUniqueId());
                                sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + plugin.getMessagesConfig().getString("messages.toggleDisable.selfToggle")));
                            }
                        } else {
                            if (p.isOp() || p.hasPermission("toggledrop.toggle.other")) {
                                Player target = Bukkit.getPlayer(args[1]);

                                if (Bukkit.getPlayer(args[1]) != null) {
                                    if (!playerToggle.contains(target.getUniqueId())) {
                                        playerToggle.add(target.getUniqueId());
                                        sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + plugin.getMessagesConfig().getString("messages.toggleEnable.playerToggle") + args[1] + "&a!"));
                                    } else if (playerToggle.contains(target.getUniqueId())) {
                                        playerToggle.remove(target.getUniqueId());
                                        sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + plugin.getMessagesConfig().getString("messages.toggleDisable.playerToggle") + args[1] + "&a!"));
                                    }
                                } else {
                                    p.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + "&cSorry! Player " + args[1] + " is not online."));
                                }
                            } else {
                                sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + "&cYou need permission toggledrop.toggle.other to use this command."));
                            }
                        }
                    }
                } else {
                    sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + "&cYou need permission toggledrop.toggle to use this command."));
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (p.isOp() || p.hasPermission("toggledrop.reload")) {
                    if (!plugin.getConfig().getString("version").equals(Main.getInstance().getVersion())) {
                        sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + "&cPlugin can't reload properly. Try to see console for more information!"));
                        throw new RuntimeException("Plugin version is corrupted on config.yml");
                    } else {
                        sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + "&aPlugin successfully reloaded!"));
                        plugin.saveDefaultConfig();
                        plugin.reloadConfig();
                    }
                } else {
                    sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + "&cYou need permission toggledrop.reload to use this command."));
                }
            } else if (args[0].equalsIgnoreCase("version")) {
                if (p.isOp() || p.hasPermission("toggledrop.version")) {
                    sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + "&aThis plugin running on &b&nversion " + Main.getInstance().getVersion()));
                } else {
                    sender.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + "&cYou need permission toggledrop.version to use this command."));
                }
            }
        }
        return false;
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();

        if (selfToggle.contains(p.getUniqueId()) || playerToggle.contains(p.getUniqueId())) {
            p.sendMessage(ColorCodeTranslate.chat(plugin.getConfig().getString("prefix") + plugin.getMessagesConfig().getString("messages.cantDrop")));
            e.setCancelled(true);
        } else if (!selfToggle.contains(p.getUniqueId()) || !playerToggle.contains(p.getUniqueId())) {
            e.setCancelled(false);
        }
    }
}
