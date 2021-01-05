package com.blunix.blunixteleport.managers;

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
	
	public static long getDeathChance() {
		return plugin.getConfig().getLong("death-chance");
	}

	public static int getTpWildMinRadius() {
		return plugin.getConfig().getInt("tp-wild-min-radius");
	}
	
	public static int getRandomLocationsAmount() {
		return plugin.getConfig().getInt("random-locations-amount");
	}
}
