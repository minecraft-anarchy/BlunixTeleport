package me.wmorales01.blunixteleport.managers;

import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

import me.wmorales01.blunixteleport.BlunixTeleport;

public class DataManager {
	private BlunixTeleport plugin;

	public DataManager(BlunixTeleport instance) {
		this.plugin = instance;
	}
	
	public void saveCooldowns() {
		for (Map.Entry<String, Long> entry : plugin.getCooldowns().entrySet())
			plugin.getLocationData().set("cooldowns." + entry.getKey(), entry.getValue());
		
		plugin.saveLocationData();
	}
	
	public void loadCooldowns() {
		FileConfiguration data = plugin.getLocationData();
		
		data.getConfigurationSection("cooldowns").getKeys(false).forEach(player -> {
			long cooldown = data.getLong("cooldowns." + player);
			plugin.addCooldowns(player, cooldown);
		});
		
		data.set("cooldowns", null);
		plugin.saveLocationData();
	}
	
	public void savePlayerContracts() {
		for (String player : plugin.getPlayersInContract())
			plugin.getLocationData().set("contracts." + player, true);
		
		plugin.saveLocationData();
	}
	
	public void loadPlayerContracts() {
		FileConfiguration data = plugin.getLocationData();
		
		data.getConfigurationSection("contracts").getKeys(false).forEach(player -> {
			plugin.addPlayersInContract(player);
		});
		plugin.getLocationData().set("contracts", null);
		plugin.saveLocationData();
	}
}
