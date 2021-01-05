package com.blunix.blunixteleport.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blunix.blunixteleport.BlunixTeleport;
import com.blunix.blunixteleport.managers.TeleportRequestManager;
import com.blunix.blunixteleport.util.MessageManager;

public class CommandDeny extends TPCommand {
	private BlunixTeleport plugin;

	public CommandDeny(BlunixTeleport instance) {
		plugin = instance;
		setName("deny");
		setHelpMessage("Denies the specified TP request.");
		setPermission("blunixteleport.respond");
		setArgsLength(2);
		setPlayerCommand(true);
		setUsage("/tp deny <Player>");
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
		MessageManager.sendMessage(requestedPlayer, "&aTP petition denied.");
		MessageManager.sendMessage(requestPlayer,
				"&c&l" + requestedPlayer.getDisplayName() + " &adenied your TP petition.");		
	}

}
