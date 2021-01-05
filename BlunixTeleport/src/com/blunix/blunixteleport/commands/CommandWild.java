package com.blunix.blunixteleport.commands;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blunix.blunixteleport.BlunixTeleport;
import com.blunix.blunixteleport.managers.ConfigManager;
import com.blunix.blunixteleport.managers.TeleportManager;
import com.blunix.blunixteleport.util.MessageManager;

public class CommandWild extends TPCommand {
	private BlunixTeleport plugin;

	public CommandWild(BlunixTeleport instance) {
		plugin = instance;
		setName("wild");
		setHelpMessage("Teleports you to a random location in the world.");
		setPermission("blunixteleport.wild");
		setArgsLength(1);
		setUsage("/tp wild");
		setPlayerCommand(true);
		setAffectedByCooldown(true);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		Random random = new Random();
		String worldName = player.getWorld().getName();
		Location location = null;
		int randomNumber;
		int attemptCounter = 0; // To avoid looping forever
		do {
			randomNumber = random.nextInt(ConfigManager.getRandomLocationsAmount() + 1);
			int x = plugin.getRandomLocationData().getInt(worldName + "." + randomNumber + ".x");
			int y = plugin.getRandomLocationData().getInt(worldName + "." + randomNumber + ".y");
			int z = plugin.getRandomLocationData().getInt(worldName + "." + randomNumber + ".z");

			location = new Location(player.getWorld(), x, y, z);
			attemptCounter++;

			if (attemptCounter == 10) {
				Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
					MessageManager.sendMessage(player, "&6Looking for a safe location...");
					TeleportManager.teleportPlayerSafely(player, TeleportManager.getWildLocation(player.getWorld()), true);
					return;
				});
				return;
			}
		} while (location.getX() == 0 && location.getY() == 0 && location.getZ() == 0);

		TeleportManager.teleportPlayer(player, location);
		plugin.getRandomLocationData().set(worldName + "." + randomNumber, null);
		plugin.saveRandomLocationData();
	}
}
