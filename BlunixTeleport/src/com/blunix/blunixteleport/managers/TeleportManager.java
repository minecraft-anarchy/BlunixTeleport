package com.blunix.blunixteleport.managers;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import com.blunix.blunixteleport.BlunixTeleport;
import com.blunix.blunixteleport.events.PlayerMove;
import com.blunix.blunixteleport.util.MessageManager;

public class TeleportManager {
	private static BlunixTeleport plugin = BlunixTeleport.getPlugin(BlunixTeleport.class);

	public static void teleportPlayer(Player player, Location location) {
		if (!player.hasPermission("blunixteleport.admin")) {
			if (!hasVisitedDimension(player, location))
				return;

			if (plugin.getOnGoingTps().containsKey(player.getName())) {
				MessageManager.sendMessage(player, "&cYou are already being teleported!");
				return;
			}

			long delay = ConfigManager.getTpDelay();
			long delaySeconds = delay / 20;
			MessageManager.sendMessage(player, "&6You will be teleported in &l" + delaySeconds
					+ " seconds&6, if you move your TP will be cancelled.");

			// Registering MoveEvent to check for player's movement.
			PlayerMove event = new PlayerMove(plugin);
			Bukkit.getServer().getPluginManager().registerEvents(event, plugin);

			startTeleportAnimation(player, (long) delay * 0.10);
			final Location teleportLocation = location;
			int taskID = Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {

				@Override
				public void run() {
					// Unregistering event to avoid lag
					HandlerList.unregisterAll(event);
					player.teleport(teleportLocation);
					player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
					PlayerCooldownManager playerCooldown = new PlayerCooldownManager(plugin, player.getName());
					playerCooldown.setCooldown();
					plugin.getOnGoingTps().remove(player.getName());

					// Stopping animation
					int animationTaskID = plugin.getTpAnimations().get(player.getName());
					plugin.getTpAnimations().remove(player.getName());
					Bukkit.getScheduler().cancelTask(animationTaskID);

					Random random = new Random();
					if (random.nextInt(100) > ConfigManager.getDeathChance())
						return;
					killPlayer(player);
				}

			}, delay).getTaskId();
			plugin.addOnGoingTps(player.getName(), taskID);
		} else {
			player.teleport(location);
			player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
		}
	}

	public static void teleportPlayerSafely(Player player, final Location location, boolean shouldKeepSearching) {
		Location safeLocation = getSafeTpLocation(location);

		Bukkit.getScheduler().runTask(plugin, () -> {
			if (safeLocation == null && !shouldKeepSearching) {
				MessageManager.sendMessage(player, "&cThat isn't a safe location!");
				return;
			} else if (safeLocation == null && shouldKeepSearching) {
				Bukkit.getScheduler().runTaskAsynchronously(plugin,
						() -> teleportPlayerSafely(player, getWildLocation(location.getWorld()), shouldKeepSearching));
				return;
			}
			TeleportManager.teleportPlayer(player, safeLocation);
		});
	}

	private static void startTeleportAnimation(Player player, double delay) {
		Random random = new Random();
		Location playerLocation = player.getLocation();
		playerLocation.getWorld().playSound(playerLocation, Sound.ENTITY_ENDERMAN_STARE, 1, 0);
		int animationTaskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

			@Override
			public void run() {
				int randomX = random.nextInt(1 - -1) + -1;
				int randomZ = random.nextInt(1 - -1) + -1;
				playerLocation.clone().add(randomX, 0, randomZ);
				playerLocation.getWorld().spawnParticle(Particle.PORTAL, playerLocation, 50);
				player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, (int) (delay / 5), 0, true));
			}

		}, 0, (long) delay);

		plugin.getTpAnimations().put(player.getName(), animationTaskID);
	}

	private static boolean hasVisitedDimension(Player player, Location location) {
		Environment locationEnvironment = location.getWorld().getEnvironment();
		if (locationEnvironment == Environment.NETHER && !hasAdvancement(player, "story/enter_the_nether")) {
			MessageManager.sendMessage(player, "&cYou need to visit The Nether by yourself before teleporting there.");
			return false;

		} else if (locationEnvironment == Environment.THE_END && !hasAdvancement(player, "end/root")) {
			MessageManager.sendMessage(player, "&cYou need to visit The End by yourself before teleporting there.");
			return false;
		}

		return true;
	}

	private static boolean hasAdvancement(Player player, String advancementName) {
		Advancement advancement = Bukkit.getAdvancement(NamespacedKey.minecraft(advancementName));
		if (!player.getAdvancementProgress(advancement).isDone())
			return false;

		return true;
	}

	public static Location getWildLocation(World world) {
		Location spawnLocation = world.getSpawnLocation();
		Random random = new Random();

		int maxRadius = ConfigManager.getTpWildMaxRadius();
		int minRadius = ConfigManager.getTpWildMinRadius();
		int randomFactor = maxRadius - minRadius;
		int randomX = random.nextInt(randomFactor - -randomFactor) + -randomFactor;
		int randomY = random.nextInt(90 - 40) + 40;
		int randomZ = random.nextInt(randomFactor - -randomFactor) + -randomFactor;

		spawnLocation.setY(randomY);
		if (randomX < 0 && randomZ < 0) {
			minRadius *= -1;
			randomX += minRadius;
			randomZ += minRadius;
		} else if (randomX < 0 && randomZ > 0) {
			randomX += minRadius * -1;
			randomZ += minRadius;
		} else {
			randomX += minRadius;
			randomZ += minRadius;
		}
		spawnLocation.add(randomX, 0, randomZ);

		return spawnLocation;
	}

	public static Location getSafeTpLocation(Location location) {
		while (!isSafeLocation(location)) {

			if (location.getWorld().getEnvironment() == Environment.NETHER && location.getY() > 125) {
				return null;

			} else if (location.getY() >= 256)
				return null;

			location.add(0, 1, 0);
		}

		return location;
	}

	private static boolean isSafeLocation(Location location) {
		Block legBlock = location.getBlock();
		Block feetBlock = legBlock.getRelative(BlockFace.DOWN);
		Block headBlock = legBlock.getRelative(BlockFace.UP);
		Block underneathBlock = feetBlock.getRelative(BlockFace.DOWN);

		// Checking for head blocks
		if (headBlock.getType().isSolid() || headBlock.getType() == Material.LAVA
				|| headBlock.getType() == Material.WATER)
			return false;

		// Checking for leg blocks
		if (legBlock.getType().isSolid() || legBlock.getType() == Material.LAVA || legBlock.getType() == Material.WATER)
			return false;

		// Check for feet blocks
		if (feetBlock.getType() == Material.LAVA || feetBlock.getType() == Material.AIR
				|| feetBlock.getType() == Material.CAVE_AIR || feetBlock.getType() == Material.WATER)
			return false;
		else if (feetBlock.getType().isSolid())
			return true;

		// Check for underneath blocks
		if (underneathBlock.getType() == Material.LAVA || underneathBlock.getType() == Material.AIR
				|| underneathBlock.getType() == Material.CAVE_AIR || underneathBlock.getType() == Material.WATER)
			return false;

		return true;
	}

	private static void killPlayer(Player player) {
		BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
			Location location = player.getLocation();
			location.getWorld().spawnParticle(Particle.CRIMSON_SPORE, location, 10);
		}, 0, 20);
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 1, false, true, true));
		player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 80, 4, false, true, true));
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			player.setHealth(0);
			MessageManager.sendMessage(player, ConfigManager.getTeleportDeathMessage());
			task.cancel();
		}, 100);
		
		plugin.getCooldowns().remove(player.getName());
	}
}
