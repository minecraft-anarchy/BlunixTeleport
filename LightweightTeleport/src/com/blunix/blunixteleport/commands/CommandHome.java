package com.blunix.blunixteleport.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blunix.blunixteleport.BlunixTeleport;
import com.blunix.blunixteleport.managers.TeleportManager;
import com.blunix.blunixteleport.util.MessageManager;

public class CommandHome extends TPCommand{
	private BlunixTeleport plugin;

	public CommandHome(BlunixTeleport instance) {
		plugin = instance;
		setName("home");
		setPermission("blunixteleport.home");
		setArgsLength(1);
		setPlayerCommand(true);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		
		if (!plugin.getHomes().containsKey(player.getName())) {
			MessageManager.sendMessage(player, "&cYou don't have a home yet! Use &l/tp sethome &cto set one.");
			return;
		}
		Location home = plugin.getHomes().get(player.getName());
		TeleportManager.teleportPlayer(player, home);
	}

}
