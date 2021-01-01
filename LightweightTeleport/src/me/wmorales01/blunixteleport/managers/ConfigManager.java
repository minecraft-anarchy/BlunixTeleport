package me.wmorales01.blunixteleport.managers;

import me.wmorales01.blunixteleport.BlunixTeleport;

public class ConfigManager {
	private static BlunixTeleport plugin = BlunixTeleport.getPlugin(BlunixTeleport.class);
	
	public static long getTpCooldown() {
		return plugin.getConfig().getLong("tp-cooldown");
	}
	
	public static long getTpDelay() {
		return plugin.getConfig().getLong("tp-delay") * 20;
	}
	
	public static int getTpWildRadius() {
		return plugin.getConfig().getInt("tp-wild-radius");
	}
	
	public static long getTpRequestExpiration() {
		return plugin.getConfig().getLong("tp-request-expiraton") * 20;
	}
	
	public static String getToa() {
		return plugin.getConfig().getString("toa");
	}
	
	public static long getDeathChance() {
		return plugin.getConfig().getLong("death-chance");
	}
}
