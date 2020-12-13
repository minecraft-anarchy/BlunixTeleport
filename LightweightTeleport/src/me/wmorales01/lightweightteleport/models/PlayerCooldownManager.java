package me.wmorales01.lightweightteleport.models;

import java.util.UUID;

import me.wmorales01.lightweightteleport.Main;

public class PlayerCooldownManager {
	private Main plugin;
	private UUID uuid;

	public PlayerCooldownManager(Main instance, UUID uuid) {
		plugin = instance;
		this.uuid = uuid;
	}

	public void setCooldown() {
		plugin.cooldowns.put(uuid, System.currentTimeMillis() + 30 * 1000);
	}

	public long getCooldown() {
		long cooldown = 0;

		if (!plugin.cooldowns.containsKey(uuid))
			return cooldown;

		cooldown = plugin.cooldowns.get(uuid);
		return (cooldown - System.currentTimeMillis()) / 1000;
	}

	public boolean hasCooldown() {
		if (plugin.cooldowns.get(uuid) == null)
			return false;

		long currentCooldown = plugin.cooldowns.get(uuid);

		if (currentCooldown > System.currentTimeMillis())
			return true;

		return false;
	}
}
