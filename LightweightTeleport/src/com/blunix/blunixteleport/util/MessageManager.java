package com.blunix.blunixteleport.util;

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
//		if (page == 1) {
//			sendMessage(sender,
//					"\n&5&l\"&5&l/tp help &6- &9Displays this list.\n"
//							+ "&5&l/tp bed &6- &9\n"
//							+ "&5&l/tp to <Player> &6- &9\n"
//							+ "&5&l/tp deny <Player> &6- &9\n"
//							+ "/tp accept <Player> &6- &9Accepts an specific player TP request.\n"
//							+ "&5&l/tp poi <POI Name> &6- &9\n"
//							+ "&5&l/tp setpoi <POI Name> &6- &9Sets a POI location.\n"
//							+ "&5&l/tp delpoi <POI Name> &6- &9Deletes one existing POI.\n"
//							+ "&5&l/tp poi &6- &9Displays the full list of Point of Interest.\n"
//							+ "&5&l/tp gps <x> <y> <y> &6- &9Teleports you to the specified coordinates.\n"
//							+ "&5&l/tp wild &6- &9Teleports you to a random location in the current world.\n"
//							+ "&5&l/tp cooldown &6- &9Shows your current TP cooldown.\n");
////							+ "&9Type &l/tp help 2 &9to display the second page of commands.");
//
//		} else if (page == 2) {
////			sendMessage(player,
////					"\n&5&l/tp home &6- &9Teleports you back to your home.\n"
////							+ "&5&l/tp sethome &6- &9Sets the location you will be TPd with /tp home.);
//		}
	}

	public static void sendCooldownMessage(Player player, long cooldown) {
		sendMessage(player, "&cYour TP is in cooldown! You need to wait &l" + cooldown + " seconds.");
	}

	public static void sendNoPermissionMessage(CommandSender sender) {
		sendMessage(sender, "&cYou do not have permissions to use this command!");
	}
}
