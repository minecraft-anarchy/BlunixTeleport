package me.wmorales01.lightweightteleport;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.wmorales01.lightweightteleport.commands.CommandCompleter;
import me.wmorales01.lightweightteleport.commands.Commands;
import me.wmorales01.lightweightteleport.files.LocationDataManager;
import me.wmorales01.lightweightteleport.models.CooldownManager;
import me.wmorales01.lightweightteleport.models.LocationManager;

public class Main extends JavaPlugin {
	private LocationDataManager locationManager;
	public Map<Player, Player> teleportPetitions = new HashMap<Player, Player>();
	public Map<String, Location> homes = new HashMap<String, Location>();
	public Map<String, Location> pois = new HashMap<String, Location>();
	public Map<UUID, Long> cooldowns = new HashMap<UUID, Long>();

	@Override
	public void onEnable() {
		locationManager = new LocationDataManager(this);
		getCommand("teleport").setExecutor(new Commands(this));
		getCommand("teleport").setTabCompleter(new CommandCompleter(this));

		if (getLocationData().getConfigurationSection("homes") != null) {
			LocationManager locManager = new LocationManager(this);
			locManager.restoreHomes();
		}

		if (getLocationData().getConfigurationSection("pois") != null) {
			LocationManager locManager = new LocationManager(this);
			locManager.restorePois();
		}
		
		if (getLocationData().getConfigurationSection("cooldowns") != null) {
			CooldownManager cooldownManager = new CooldownManager(this);
			cooldownManager.loadCooldowns();
		}
	}

	@Override
	public void onDisable() {
		if (!homes.isEmpty()) {
			LocationManager locManager = new LocationManager(this);
			locManager.saveHomes();
		}

		if (!pois.isEmpty()) {
			LocationManager locManager = new LocationManager(this);
			locManager.savePois();
		}
		
		if (!cooldowns.isEmpty()) {
			CooldownManager cooldownManager = new CooldownManager(this);
			cooldownManager.saveCooldowns();
		}
	}

	public FileConfiguration getLocationData() {
		return locationManager.getConfig();
	}

	public void saveLocationData() {
		locationManager.saveConfig();
	}
}
