package com.blunix.blunixteleport.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blunix.blunixteleport.BlunixTeleport;
import com.blunix.blunixteleport.managers.ConfigManager;
import com.blunix.blunixteleport.managers.TeleportManager;
import com.blunix.blunixteleport.util.MessageManager;

public class CommandGenerateLocations extends TPCommand {
	private BlunixTeleport plugin;

	public CommandGenerateLocations(BlunixTeleport instance) {
		plugin = instance;
		setName("generatelocations");
		setHelpMessage("Generates locations for /tp wild.");
		setPermission("blunixteleport.generatelocations");
		setArgsLength(1);
		setUsage("/tp generatelocations");
		setUniversalCommand(true);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		ArrayList<Location> safeLocations = new ArrayList<Location>();
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			for (World world : Bukkit.getWorlds())
				for (int i = 1; i < ConfigManager.getRandomLocationsAmount() + 1; i++) {
					Location safeLocation = null;
					do {
						Location randomLocation = TeleportManager.getWildLocation(world);
						safeLocation = TeleportManager.getSafeTpLocation(randomLocation);
					} while (safeLocation == null);

					safeLocations.add(safeLocation);
					MessageManager.sendMessage(Bukkit.getConsoleSender(),
							"&9" + i + " locations for &l" + world.getName() + " &9generated.");
					if (i % 5 == 0 && sender instanceof Player)
						MessageManager.sendMessage(sender,
								"&9" + i + " locations for &l" + world.getName() + " &9generated.");
				}

			int var = 1;
			for (Location location : safeLocations) {
				int x = (int) location.getX();
				int y = (int) location.getY();
				int z = (int) location.getZ();
				String worldName = location.getWorld().getName();

				plugin.getRandomLocationData().set(worldName + "." + var + ".x", x);
				plugin.getRandomLocationData().set(worldName + "." + var + ".y", y);
				plugin.getRandomLocationData().set(worldName + "." + var + ".z", z);
				var++;
				plugin.saveRandomLocationData();
			}
			plugin.saveRandomLocationData();
		});
	}
}
