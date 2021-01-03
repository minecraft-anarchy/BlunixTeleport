package com.blunix.blunixteleport.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blunix.blunixteleport.BlunixTeleport;
import com.blunix.blunixteleport.util.MessageManager;

public class CommandSetHome extends TPCommand {
	private BlunixTeleport plugin;

	public CommandSetHome(BlunixTeleport instance) {
		plugin = instance;
		setName("sethome");
		setPermission("blunixteleport.sethome");
		setArgsLength(1);
		setPlayerCommand(true);
		setUsage("/tp sethome");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		plugin.addHomes(player.getName(), player.getLocation());
		MessageManager.sendMessage(player, "&aHome set!");
	}

}
