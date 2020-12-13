package me.wmorales01.lightweightteleport.models;

import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;

import me.wmorales01.lightweightteleport.Main;

public class CooldownManager {
	private Main plugin;

	public CooldownManager(Main instance) {
		this.plugin = instance;
	}
	
	public void saveCooldowns() {
		for (Map.Entry<UUID, Long> entry : plugin.cooldowns.entrySet())
			plugin.getLocationData().set("cooldowns." + entry.getKey().toString(), entry.getValue());
		
		plugin.saveLocationData();
	}
	
	public void loadCooldowns() {
		FileConfiguration data = plugin.getLocationData();
		
		data.getConfigurationSection("cooldowns").getKeys(false).forEach(uuid -> {
			long cooldown = data.getLong("cooldowns." + uuid);
			plugin.cooldowns.put(UUID.fromString(uuid), cooldown);
		});
		
		data.set("cooldowns", null);
		plugin.saveLocationData();
	}
}
