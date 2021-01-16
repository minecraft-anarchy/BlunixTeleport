package com.blunix.blunixteleport.files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.blunix.blunixteleport.BlunixTeleport;

public class PoiData {
	private BlunixTeleport plugin;
	private FileConfiguration dataConfig = null;
	private File configFile = null;

	public PoiData(BlunixTeleport instance) {
		this.plugin = instance;
		saveDefaultConfig();
	}

	public void reloadConfig() {
		if (configFile == null) {
			configFile = new File(plugin.getDataFolder(), "poi.yml");
		}
		dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
		InputStream defaultStream = plugin.getResource("poi.yml");
		if (defaultStream != null) {
			YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
			dataConfig.setDefaults(defaultConfig);
		}
	}

	public FileConfiguration getConfig() {
		if (dataConfig == null) {
			reloadConfig();
		}
		return dataConfig;
	}

	public void saveConfig() {
		if (dataConfig == null || configFile == null) {
			return;
		}
		try {
			getConfig().save(configFile);
		} catch (IOException e) {
			plugin.getLogger().log(Level.SEVERE, "Couldn't save information into " + configFile, e);
		}
	}

	public void saveDefaultConfig() {
		if (configFile == null) {
			configFile = new File(plugin.getDataFolder(), "poi.yml");
		}
		if (!configFile.exists()) {
			plugin.saveResource("poi.yml", false);
		}
	}
}
