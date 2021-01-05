package com.blunix.blunixteleport.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blunix.blunixteleport.BlunixTeleport;
import com.blunix.blunixteleport.managers.TeleportManager;
import com.blunix.blunixteleport.managers.TeleportRequestManager;
import com.blunix.blunixteleport.util.MessageManager;

public class CommandAccept extends TPCommand {
	private BlunixTeleport plugin;

	public CommandAccept(BlunixTeleport instance) {
		plugin = instance;
		setName("accept");
		setHelpMessage("Accepts the specified TP request.");
		setPermission("blunixteleport.respond");
		setArgsLength(2);
		setPlayerCommand(true);
		setUsage("/tp accept <Player>");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player requestedPlayer = (Player) sender;

		Player requestPlayer = Bukkit.getPlayer(args[1]);
		if (requestPlayer == null) {
			MessageManager.sendMessage(requestedPlayer, "&cThis player is not online.");
			return;
		}
		TeleportRequestManager requestManager = new TeleportRequestManager(plugin, requestedPlayer);
		if (!requestManager.hasTeleportRequest(requestPlayer))
			return;

		plugin.getTeleportPetitions().remove(requestPlayer);
		TeleportManager.teleportPlayer(requestPlayer, requestedPlayer.getLocation());

		MessageManager.sendMessage(requestedPlayer, "&a&l" + requestPlayer.getDisplayName() + " &awill be teleported to you.");
		MessageManager.sendMessage(requestPlayer,
				"&a&l" + requestedPlayer.getDisplayName() + " &ahas accepted you TP petition.");
	}

}
