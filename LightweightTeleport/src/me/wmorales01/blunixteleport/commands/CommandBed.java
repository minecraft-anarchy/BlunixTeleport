package me.wmorales01.blunixteleport.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.wmorales01.blunixteleport.managers.TeleportManager;

public class CommandBed extends TPCommand {
	
	public CommandBed() {
		setName("bed");
		setHelpMessage("Teleports you to your beds location (In case you have one).");
		setPermission("blunixteleport.bed");
		setArgsLength(1);
		setPlayerCommand(true);
		setAffectedByCooldown(true);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		
		if (player.getBedSpawnLocation() == null) {
			player.sendMessage("Your bed is missing or obstructed");
			return;
		}
		TeleportManager.teleportPlayer(player, player.getBedSpawnLocation());
	}
	
	
}
