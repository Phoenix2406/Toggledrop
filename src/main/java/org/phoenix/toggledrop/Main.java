package org.phoenix.toggledrop;

import org.phoenix.toggledrop.command.TDCommand;
import org.phoenix.toggledrop.tabcommand.TabComplete;
import org.phoenix.toggledrop.utils.ColorCodeTranslate;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {

    public static Main INSTANCE;

    private String description = getDescription().getDescription();
    private String version = getDescription().getVersion();

    private File messagesConfigFile;
    private FileConfiguration messagesConfig;

    @Override
    public void onEnable() {

        File configFile = new File(getDataFolder() + File.separator + "config.yml");

        if (!configFile.exists()) {
            getConfig().addDefault("prefix", "&7[&#00E6FF&lToggledrop&7] ");
            getConfig().addDefault("version", version);
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        } else {
            saveDefaultConfig();
            reloadConfig();
        }

        INSTANCE = this;
        getLogger().info(ColorCodeTranslate.chat("&aPlugin has been enable!"));
        this.getCommand("toggledrop").setExecutor(new TDCommand(this));
        this.getCommand("toggledrop").setTabCompleter(new TabComplete(this));
        new TDCommand(this);
        createMessagesConfig();
    }

    @Override
    public void onDisable() {
        INSTANCE = null;
        getLogger().info(ColorCodeTranslate.chat("&cPlugin has been disable!"));
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
