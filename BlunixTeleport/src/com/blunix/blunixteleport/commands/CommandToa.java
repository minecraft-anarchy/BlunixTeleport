package com.blunix.blunixteleport.commands;

import org.bukkit.command.CommandSender;

import com.blunix.blunixteleport.BlunixTeleport;
import com.blunix.blunixteleport.managers.ConfigManager;
import com.blunix.blunixteleport.util.MessageManager;

public class CommandToa extends TPCommand {
	private BlunixTeleport plugin;

	public CommandToa(BlunixTeleport instance) {
		plugin = instance;
		setName("toa");
		setHelpMessage("Prints our service's Terms of Agreement.");
		setPermission("blunixteleport.toa");
		setArgsLength(1);
		setUsage("/tp toa");
		setUniversalCommand(true);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 1) {
			// Executed /toa
			MessageManager.sendMessage(sender, ConfigManager.getToa());
			if (plugin.getPlayersInContract().contains(sender.getName()))
				MessageManager.sendMessage(sender, "&aYou currently accept our Terms of Agreement.");
			else {
				MessageManager.sendMessage(sender, "&cYou haven't accepted our Terms of Agreement yet.");
				MessageManager.sendMessage(sender,
						"&6Type &l/tp toa accept &6or &l/tp toa deny &6to respond to our Terms of Agreement.");
			}

		} else if (args.length == 2 && args[1].toLowerCase().equalsIgnoreCase("accept")) {
			// Executed /toa accept
			if (plugin.getPlayersInContract().contains(sender.getName())) {
				MessageManager.sendMessage(sender, "&cYou already accepted our Terms of Agreement.");
				return;
			}
			plugin.addPlayersInContract(sender.getName());
			MessageManager.sendMessage(sender, "&aThank you for joining our services! Now you can use /tp commands.");

		} else if (args.length == 2 && args[1].toLowerCase().equalsIgnoreCase("deny")) {
			// Executed /toa deny
			if (plugin.getPlayersInContract().contains(sender.getName())) {
				MessageManager.sendMessage(sender, "&cYou successfully rejected our Terms of Agrement.");
				plugin.getPlayersInContract().remove(sender.getName());
				return;
			}

			MessageManager.sendMessage(sender, "&cYou successfully rejected our Terms of Agrement.");
		}
	}
}
