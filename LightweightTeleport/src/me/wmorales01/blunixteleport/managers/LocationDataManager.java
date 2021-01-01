package me.wmorales01.blunixteleport.managers;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import me.wmorales01.blunixteleport.BlunixTeleport;

public class LocationDataManager {
	private BlunixTeleport plugin;

	public LocationDataManager(BlunixTeleport instance) {
		this.plugin = instance;
	}

	public void saveHomes() {
		if (plugin.getHomes().isEmpty())
			return;
		
		FileConfiguration data = plugin.getLocationData();
		for (Map.Entry<String, Location> entry : plugin.getHomes().entrySet()) {
			String playerName = entry.getKey();
			Location location = entry.getValue();
			data.set("homes." + playerName + ".x", location.getX());
			data.set("homes." + playerName + ".y", location.getY());
			data.set("homes." + playerName + ".z", location.getZ());
			data.set("homes." + playerName + ".pitch", location.getPitch());
			data.set("homes." + playerName + ".yaw", location.getYaw());
			data.set("homes." + playerName + ".world", location.getWorld().getName());
		}
		plugin.saveLocationData();
	}

	public void restoreHomes() {
		FileConfiguration data = plugin.getLocationData();
		ConfigurationSection dataSection = data.getConfigurationSection("homes");
		if (dataSection == null)
			return;
		
		dataSection.getKeys(false).forEach(key -> {
			Location location = getLocationFromData(data, "homes.", key);
			plugin.addHomes(key, location);
		});
	}

	public void savePois() {
		if (plugin.getPois().isEmpty())
			return;
		
		FileConfiguration data = plugin.getLocationData();
		for (Map.Entry<String, Location> entry : plugin.getPois().entrySet()) {
			String poiName = entry.getKey();
			Location location = entry.getValue();
			data.set("pois." + poiName + ".x", location.getX());
			data.set("pois." + poiName + ".y", location.getY());
			data.set("pois." + poiName + ".z", location.getZ());
			data.set("pois." + poiName + ".pitch", location.getPitch());
			data.set("pois." + poiName + ".yaw", location.getYaw());
			data.set("pois." + poiName + ".world", location.getWorld().getName());
		}
		plugin.saveLocationData();
	}

	public void restorePois() {
		FileConfiguration data = plugin.getLocationData();
		ConfigurationSection dataSection = data.getConfigurationSection("pois");
		if (dataSection == null)
			return;
		
		dataSection.getKeys(false).forEach(key -> {
			Location location = this.getLocationFromData(data, "pois.", key);
			plugin.addPois(key, location);
		});
	}

	private Location getLocationFromData(FileConfiguration data, String dir, String key) {
		double x = data.getDouble(dir + key + ".x");
		double y = data.getDouble(dir + key + ".y");
		double z = data.getDouble(dir + key + ".z");
		double pitch = data.getDouble(dir + key + ".pitch");
		double yaw = data.getDouble(dir + key + ".yaw");
		World world = Bukkit.getWorld(data.getString(dir + key + ".world"));
		Location location = new Location(world, x, y, z);
		location.setPitch((float) pitch);
		location.setYaw((float) yaw);
		return location;
	}
}
