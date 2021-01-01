package me.wmorales01.blunixteleport.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.wmorales01.blunixteleport.managers.TeleportManager;

public class CommandWild extends TPCommand {

	public CommandWild() {
		setName("wild");
		setPermission("blunixteleport.wild");
		setArgsLength(1);
		setPlayerCommand(true);
		setAffectedByCooldown(true);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		Location wildLocation = TeleportManager.getWildLocation(player.getWorld());
		TeleportManager.teleportPlayer(player, wildLocation);
	}
}
