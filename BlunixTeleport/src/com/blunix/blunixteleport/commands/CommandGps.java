package com.blunix.blunixteleport.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blunix.blunixteleport.managers.ConfigManager;
import com.blunix.blunixteleport.managers.TeleportManager;
import com.blunix.blunixteleport.util.MessageManager;
import com.blunix.blunixteleport.util.Parser;

public class CommandGps extends TPCommand {
	public CommandGps() {
		setName("gps");
		setHelpMessage("Teleports you to the specified coordinates.");
		setPermission("blunixteleport.gps");
		setArgsLength(4);
		setPlayerCommand(true);
		setUsage("/tp gps <X> <Y> <Z> <World>");
		setAffectedByCooldown(true);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;

		Double x = Parser.getDoubleFromString(args[1]) + 0.500;
		Double y = Parser.getDoubleFromString(args[2]) + 0.500;
		Double z = Parser.getDoubleFromString(args[3]) + 0.500;
		World world = null;
		if (args.length > 4) {
			// Executed /tp gps <X> <Y> <Z> <World>
			world = ConfigManager.getGpsWorld(args[4]);
			if (world == null) {
				if (!ConfigManager.getGpsWorldLabels().contains(args[4]))
					MessageManager.sendMessage(player, "&cThis dimension is not defined.");
				else
					MessageManager.sendMessage(player, "&cThe defined dimension does not exist on this server.");

				return;
			}
		
		} else if (args.length == 4) // Executed /tp <X> <Y> <Z>
			world = player.getWorld();

		if (x == null || y == null || z == null) {
			MessageManager.sendMessage(player, "&cBe sure you input a numeric amount for the coordinates!");
			return;
		}

		Location tpLocation = new Location(world, x, y, z);
		TeleportManager.teleportPlayer(player, tpLocation);
	}
}
