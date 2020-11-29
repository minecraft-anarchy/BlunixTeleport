package me.wmorales01.lightweightteleport.models;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import me.wmorales01.lightweightteleport.Main;

public class LocationManager {
	private Main plugin;

	public LocationManager(Main instance) {
		this.plugin = instance;
	}

	public void saveHomes() {
		FileConfiguration data = plugin.getLocationData();
		for (Map.Entry<String, Location> entry : plugin.homes.entrySet()) {
			String playerName = entry.getKey();
			Location location = entry.getValue();
			data.set("homes." + playerName + ".x", location.getX());
			data.set("homes." + playerName + ".y", location.getY());
			data.set("homes." + playerName + ".z", location.getZ());
			data.set("homes." + playerName + ".world", location.getWorld().getName());
		}
		plugin.saveLocationData();
	}

	public void restoreHomes() {
		FileConfiguration data = plugin.getLocationData();
		data.getConfigurationSection("homes").getKeys(false).forEach(key -> {
			Location location = getLocationFromData(data, "homes.", key);
			plugin.homes.put(key, location);
		});
	}

	public void savePois() {
		FileConfiguration data = this.plugin.getLocationData();
		for (Map.Entry<String, Location> entry : this.plugin.pois.entrySet()) {
			String poiName = entry.getKey();
			Location location = entry.getValue();
			data.set("pois." + poiName + ".x", location.getX());
			data.set("pois." + poiName + ".y", location.getY());
			data.set("pois." + poiName + ".z", location.getZ());
			data.set("pois." + poiName + ".world", location.getWorld().getName());
		}
		plugin.saveLocationData();
	}

	public void restorePois() {
		FileConfiguration data = this.plugin.getLocationData();
		data.getConfigurationSection("pois").getKeys(false).forEach(key -> {
			Location location = this.getLocationFromData(data, "pois.", key);
			plugin.pois.put(key, location);
		});
	}

	private Location getLocationFromData(FileConfiguration data, String dir, String key) {
		double x = data.getDouble(dir + key + ".x");
		double y = data.getDouble(dir + key + ".y");
		double z = data.getDouble(dir + key + ".z");
		World world = Bukkit.getWorld(data.getString(dir + key + ".world"));
		Location location = new Location(world, x, y, z);
		return location;
	}
}
