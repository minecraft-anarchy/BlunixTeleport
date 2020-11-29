package me.wmorales01.lightweightteleport;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.wmorales01.lightweightteleport.commands.CommandCompleter;
import me.wmorales01.lightweightteleport.commands.Commands;
import me.wmorales01.lightweightteleport.files.LocationDataManager;
import me.wmorales01.lightweightteleport.models.LocationManager;

public class Main extends JavaPlugin {
	public LocationDataManager locationManager;
	public Map<String, Location> homes = new HashMap<String, Location>();
	public Map<String, Location> pois = new HashMap<String, Location>();

	public void onEnable() {
		locationManager = new LocationDataManager(this);
		getCommand("teleport").setExecutor(new Commands(this));
		getCommand("teleport").setTabCompleter(new CommandCompleter(this));

		if (getLocationData().contains("homes")) {
			LocationManager locManager = new LocationManager(this);
			locManager.restoreHomes();
		}

		if (getLocationData().contains("pois")) {
			LocationManager locManager = new LocationManager(this);
			locManager.restorePois();
		}
	}

	public void onDisable() {
		if (!homes.isEmpty()) {
			LocationManager locManager = new LocationManager(this);
			locManager.saveHomes();
		}

		if (!pois.isEmpty()) {
			LocationManager locManager = new LocationManager(this);
			locManager.savePois();
		}
	}

	public FileConfiguration getLocationData() {
		return locationManager.getConfig();
	}

	public void saveLocationData() {
		locationManager.saveConfig();
	}
}
