package me.wmorales01.blunixteleport.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.wmorales01.blunixteleport.managers.TeleportManager;
import me.wmorales01.blunixteleport.util.MessageManager;
import me.wmorales01.blunixteleport.util.Parser;

public class CommandGps extends TPCommand {
	public CommandGps() {
		setName("gps");
		setPermission("blunixteleport.gps");
		setArgsLength(4);
		setPlayerCommand(true);
		setUsage("/tp gps <X> <Y> <Z>");
		setAffectedByCooldown(true);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;

		Double x = Parser.getDoubleFromString(args[1]) + 0.500;
		Double y = Parser.getDoubleFromString(args[2]) + 0.500;
		Double z = Parser.getDoubleFromString(args[3]) + 0.500;
		World world = player.getWorld();

		if (x == null || y == null || z == null) {
			MessageManager.sendMessage(player, "&cBe sure you input a numeric amount for the coordinates!");
			return;
		}

		Location tpLocation = new Location(world, x, y, z);
		TeleportManager.teleportPlayer(player, tpLocation);
	}
}
