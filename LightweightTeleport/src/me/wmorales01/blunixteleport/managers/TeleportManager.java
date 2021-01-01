package me.wmorales01.blunixteleport.managers;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import me.wmorales01.blunixteleport.BlunixTeleport;
import me.wmorales01.blunixteleport.events.PlayerMove;
import me.wmorales01.blunixteleport.util.MessageManager;

public class TeleportManager {
	private static BlunixTeleport plugin = BlunixTeleport.getPlugin(BlunixTeleport.class);

	public static void teleportPlayer(Player player, Location location) {
		if (!player.hasPermission("blunixteleport.admin")) {
			if (!hasVisitedDimension(player, location))
				return;

			Location safeLocation = getSafeTpLocation(location);
			if (safeLocation == null) {
				MessageManager.sendMessage(player, "&cThis isn't a safe TP location!");
				return;
			}

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

			// Adding taskID to HashMap
			plugin.addOnGoingTps(player.getName(),
					Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {

						@Override
						public void run() {
							HandlerList.unregisterAll(event);
							player.teleport(safeLocation);
							player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
							PlayerCooldownManager playerCooldown = new PlayerCooldownManager(plugin,
									player.getName());
							playerCooldown.setCooldown();
							plugin.getOnGoingTps().remove(player.getName());
							
							Random random = new Random();
							if (random.nextInt(100) > ConfigManager.getDeathChance())
								return;
							
							player.setHealth(0);
						}

					}, delay).getTaskId());
		} else {
			player.teleport(location);
			player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
		}
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

		int radius = ConfigManager.getTpWildRadius();
		int randomX = random.nextInt(radius - -radius) + -radius;
		int randomZ = random.nextInt(radius - -radius) + -radius;

		spawnLocation.add(randomX, 0, randomZ);

		return spawnLocation;
	}

	public static Location getSafeTpLocation(Location location) {
		World world = location.getWorld();
		Location safeLocation = null;
		if (world.getEnvironment() == Environment.NETHER) {
			Block block = location.getBlock();
			while (block.getType() != Material.AIR) {
				if (block.getType() == Material.BEDROCK)
					break;
				location.add(0,1,0);
				block = location.getBlock();
			}
			
			safeLocation = location;
		} else
			safeLocation = world.getHighestBlockAt(location).getLocation().add(0, 1, 0);
		
		while (!isSafeLocation(safeLocation)) {
			safeLocation.add(0, 1, 0);

			System.out.println("Searching...");
			if (location.getWorld().getEnvironment() == Environment.NETHER && safeLocation.getY() >= 128) {
				return null;

			} else if (location.getY() >= 256)
				return null;
		}

		return safeLocation;
	}

	private static boolean isSafeLocation(Location location) {
		Block block = location.getBlock();
		Material blockType = block.getType();

		if (blockType != Material.AIR && blockType != Material.CAVE_AIR)
			return false;
		if (block.getRelative(BlockFace.DOWN).getType() == Material.LAVA
				|| block.getRelative(BlockFace.DOWN).getType() == Material.AIR
				|| block.getRelative(BlockFace.DOWN).getType() == Material.CAVE_AIR)
			return false;

		return true;
	}
}
