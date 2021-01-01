package me.wmorales01.blunixteleport.managers;

import me.wmorales01.blunixteleport.BlunixTeleport;

public class PlayerCooldownManager {
	private BlunixTeleport plugin;
	private String player;

	public PlayerCooldownManager(BlunixTeleport instance, String player) {
		plugin = instance;
		this.player = player;
	}

	public void setCooldown() {
		long cooldown = ConfigManager.getTpCooldown() * 1000;
		plugin.addCooldowns(player, System.currentTimeMillis() + cooldown);
	}

	public long getCooldown() {
		long cooldown = 0;

		if (!plugin.getCooldowns().containsKey(player))
			return cooldown;

		cooldown = plugin.getCooldowns().get(player);
		return (cooldown - System.currentTimeMillis()) / 1000;
	}

	public boolean hasCooldown() {
		if (plugin.getCooldowns().get(player) == null)
			return false;

		long currentCooldown = plugin.getCooldowns().get(player);

		if (currentCooldown > System.currentTimeMillis())
			return true;

		return false;
	}
}
