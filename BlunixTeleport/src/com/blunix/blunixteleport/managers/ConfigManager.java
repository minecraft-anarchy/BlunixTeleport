package com.blunix.blunixteleport.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import com.blunix.blunixteleport.BlunixTeleport;

public class ConfigManager {
	private static BlunixTeleport plugin = BlunixTeleport.getPlugin(BlunixTeleport.class);
	
	public static long getTpCooldown() {
		return plugin.getConfig().getLong("tp-cooldown");
	}
	
	public static long getTpDelay() {
		return plugin.getConfig().getLong("tp-delay") * 20;
	}
	
	public static int getTpWildMaxRadius() {
		return plugin.getConfig().getInt("tp-wild-max-radius");
	}
	
	public static long getTpRequestExpiration() {
		return plugin.getConfig().getLong("tp-request-expiration") * 20;
	}
	
	public static String getToa() {
		return plugin.getConfig().getString("toa");
	}
	
	public static double getDeathChance() {
		return plugin.getConfig().getLong("death-chance");
	}

	public static int getTpWildMinRadius() {
		return plugin.getConfig().getInt("tp-wild-min-radius");
	}
	
	public static int getRandomLocationsAmount() {
		return plugin.getConfig().getInt("random-locations-amount");
	}
	
	public static List<String> getGpsWorldLabels() {
		ConfigurationSection section = plugin.getConfig().getConfigurationSection("worlds");
		if (section == null)
			return null;
		
		List<String> worldLabels = new ArrayList<String>();
		plugin.getConfig().getConfigurationSection("worlds").getKeys(false).forEach(worldName -> {			
			worldLabels.add(worldName);
		});
		
		return worldLabels;
	}
	
	public static World getGpsWorld(String worldLabel) {
		String worldName = plugin.getConfig().getString("worlds." + worldLabel);
		World world = Bukkit.getWorld(worldName);
		
		return world;
	}
	
	public static String getTeleportDeathMessage() {
		return plugin.getConfig().getString("teleport-death-message");
	}
}
