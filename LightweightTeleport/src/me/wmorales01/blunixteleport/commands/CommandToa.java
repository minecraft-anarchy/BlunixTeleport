package me.wmorales01.blunixteleport.commands;

import org.bukkit.command.CommandSender;

import me.wmorales01.blunixteleport.BlunixTeleport;
import me.wmorales01.blunixteleport.managers.ConfigManager;
import me.wmorales01.blunixteleport.util.MessageManager;

public class CommandToa extends TPCommand {
	private BlunixTeleport plugin;

	public CommandToa(BlunixTeleport instance) {
		plugin = instance;
		setName("setpoi");
		setPermission("blunixteleport.toa");
		setArgsLength(1);
		setUniversalCommand(true);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 1) {
			// Executed /toa
			MessageManager.sendMessage(sender, ConfigManager.getToa());

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
				MessageManager.sendMessage(sender, "&cYou already accepted our Terms of Agreement.");
				return;
			}

			MessageManager.sendMessage(sender, "&cYou have denied /tp Terms of Agreement.");
		}
	}
}
