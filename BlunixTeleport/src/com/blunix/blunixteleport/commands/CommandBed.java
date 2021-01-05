package com.blunix.blunixteleport.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blunix.blunixteleport.managers.TeleportManager;

public class CommandBed extends TPCommand {
	
	public CommandBed() {
		setName("bed");
		setHelpMessage("Teleports you to your beds location (In case you have one).");
		setPermission("blunixteleport.bed");
		setArgsLength(1);
		setUsage("/tp bed");
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
