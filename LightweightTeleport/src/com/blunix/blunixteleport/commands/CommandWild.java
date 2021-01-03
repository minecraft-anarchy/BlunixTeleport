package com.blunix.blunixteleport.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blunix.blunixteleport.BlunixTeleport;
import com.blunix.blunixteleport.managers.TeleportManager;
import com.blunix.blunixteleport.util.MessageManager;

public class CommandWild extends TPCommand {

	public CommandWild() {
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
		Location wildLocation = TeleportManager.getWildLocation(player.getWorld());
		MessageManager.sendMessage(player, "&6Looking for a safe location...");
		Bukkit.getScheduler().runTaskAsynchronously(BlunixTeleport.getPlugin(BlunixTeleport.class), () -> {
			TeleportManager.teleportPlayerSafely(player, wildLocation, true);
		});
	}
}
