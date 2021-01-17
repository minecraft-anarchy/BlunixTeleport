package com.blunix.blunixteleport.util;

import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blunix.blunixteleport.BlunixTeleport;
import com.blunix.blunixteleport.commands.TPCommand;

public class MessageManager {
	public static String PREFIX = "";
	private static BlunixTeleport plugin = BlunixTeleport.getPlugin(BlunixTeleport.class);

	public static void sendMessage(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + message));
	}

	public static void sendHelpMessage(CommandSender sender, int page) {
		String helpMessage = "";
		for (TPCommand subCommand : plugin.getSubcommands().values()) {
			if (!sender.hasPermission(subCommand.getPermission()))
				continue;
			if (subCommand.getName().equalsIgnoreCase("poi")) {
				helpMessage += "&5/poi <POI> &6- &9Teleports you to the specified POI.\n";
				helpMessage += "&5/poi &6- &9Displays the POI list.\n";
				continue;
			}
			String command = subCommand.getUsage();
			String help = subCommand.getHelpMessage();

			helpMessage += "&5" + command + " &6- &9" + help + "\n";
		}

		if (helpMessage.equalsIgnoreCase(""))
			return;
		MessageManager.sendMessage(sender, helpMessage);
	}

	public static void sendCooldownMessage(Player player, long cooldown) {
		long hours = TimeUnit.SECONDS.toHours(cooldown);
		cooldown -= hours * 3600;
		long minutes = TimeUnit.SECONDS.toMinutes(cooldown);
		cooldown -= minutes * 60;
		String message = "&cYour TP is in cooldown! You need to wait &l";

		if (hours > 0)
			sendMessage(player, message + hours + " hours " + minutes + " minutes and " + cooldown + " seconds&c.");
		else if (minutes > 0)
			sendMessage(player, message + minutes + " minutes and " + cooldown + " seconds&c.");
		else
			sendMessage(player, message + cooldown + " seconds&c.");
	}

	public static void sendNoPermissionMessage(CommandSender sender) {
		sendMessage(sender, "&cYou do not have permissions to use this command!");
	}
}
