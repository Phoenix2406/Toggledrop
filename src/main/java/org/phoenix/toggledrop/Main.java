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

package org.phoenix.toggledrop;

import org.phoenix.toggledrop.command.TDCommand;
import org.phoenix.toggledrop.event.PlayerJoin;
import org.phoenix.toggledrop.tabcommand.TabComplete;
import org.phoenix.toggledrop.utils.ColorCodeTranslate;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.phoenix.toggledrop.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends JavaPlugin {

    public static Main INSTANCE;
    private int ms;

    private final String description = getDescription().getDescription();
    private final String version = getDescription().getVersion();

    private File messagesConfigFile;
    private FileConfiguration messagesConfig;

    @Override
    public void onEnable() {
        // Time in millisecond (ms) for plugin enable
        ms = 0;
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ms++;
            }
        };
        timer.scheduleAtFixedRate(task, 1, 1);

        File configFile = new File(getDataFolder() + File.separator + "config.yml");
        if (!configFile.exists()) {
            getConfig().addDefault("prefix", "&7[&#00E6FF&lToggledrop&7]");
            getConfig().addDefault("version", version);
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        } else {
            saveDefaultConfig();
            reloadConfig();
        }

        INSTANCE = this;
        this.getCommand("toggledrop").setExecutor(new TDCommand(this));
        this.getCommand("toggledrop").setTabCompleter(new TabComplete());
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        createMessagesConfig();

        Logger.log(Logger.LogLevel.INFO, ColorCodeTranslate.chat("&aEnabling Toggledrop plugin in " + ms + "ms!"));
    }

    @Override
    public void onDisable() {
        // Time in millisecond (ms) for plugin enable
        ms = 0;
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ms++;
            }
        };
        timer.scheduleAtFixedRate(task, 1, 1);

        INSTANCE = null;
        Logger.log(Logger.LogLevel.INFO, ColorCodeTranslate.chat("&cDisabling Toggledrop plugin in " + ms + "ms!"));
    }

    public static Main getInstance() {
        return INSTANCE;
    }

    public String getDesc() {
        return description;
    }

    public String getVersion() {
        return version;
    }

    // Getting getMessagesConfig FileConfiguration to return messagesConfig method
    public FileConfiguration getMessagesConfig() {
        return this.messagesConfig;
    }

    // Creating Messages Config File
    private void createMessagesConfig() {
        messagesConfigFile = new File(getDataFolder(), "messages.yml");
        if (!messagesConfigFile.exists()) {
            messagesConfigFile.getParentFile().mkdirs();
            saveResource("messages.yml", false);
        }

        messagesConfig = new YamlConfiguration();
        try {
            messagesConfig.load(messagesConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
