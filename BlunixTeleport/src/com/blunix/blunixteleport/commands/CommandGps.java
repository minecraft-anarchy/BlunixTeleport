package com.blunix.blunixteleport.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blunix.blunixteleport.BlunixTeleport;
import com.blunix.blunixteleport.managers.TeleportManager;
import com.blunix.blunixteleport.util.MessageManager;
import com.blunix.blunixteleport.util.Parser;

public class CommandGps extends TPCommand {
	public CommandGps() {
		setName("gps");
		setHelpMessage("Teleports you to the specified coordinates.");
		setPermission("blunixteleport.gps");
		setArgsLength(5);
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
		if (args[4].equalsIgnoreCase("overworld"))
			world = Bukkit.getWorld("world");
		else if (args[4].equalsIgnoreCase("nether"))
			world = Bukkit.getWorld("world_nether");
		else if (args[4].equalsIgnoreCase("end"))
			world = Bukkit.getWorld("world_the_end");
		
		if (world == null) {
			MessageManager.sendMessage(player, "&cUnknown dimension.");
			return;
		}

		if (x == null || y == null || z == null) {
			MessageManager.sendMessage(player, "&cBe sure you input a numeric amount for the coordinates!");
			return;
		}

		Location tpLocation = new Location(world, x, y, z);
		MessageManager.sendMessage(player, "&6Looking for a safe location...");
		Bukkit.getScheduler().runTaskAsynchronously(BlunixTeleport.getPlugin(BlunixTeleport.class), () -> {
			TeleportManager.teleportPlayerSafely(player, tpLocation, false);
		});
	}
}
