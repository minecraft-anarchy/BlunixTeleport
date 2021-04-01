package com.blunix.blunixteleport.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.blunix.blunixteleport.BlunixTeleport;
import com.blunix.blunixteleport.managers.ConfigManager;

public class CommandCompleter implements TabCompleter {
	private BlunixTeleport plugin;

	public CommandCompleter(BlunixTeleport instance) {
		plugin = instance;
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		ArrayList<String> arguments = new ArrayList<String>();
		if (arguments.isEmpty()) {
			for (TPCommand subCommand : plugin.getSubcommands().values()) {
				if (!sender.hasPermission(subCommand.getPermission()))
					continue;

				arguments.add(subCommand.getName());
			}
		}
		TPCommand subcommand = plugin.getSubcommands().get(args[0]);
		if (args.length > 1 && (subcommand == null || !sender.hasPermission(subcommand.getPermission()))) {
			arguments.clear();
			return arguments;
		}
		if (args.length >= 0 && args.length < 2)
			return getCompletion(arguments, args, 0);

		else if (args.length >= 1 && args.length < 3) {
			arguments.clear();
			switch (subcommand.getName()) {
			case "to":
			case "accept":
			case "deny":
				return null;

			case "poi":
			case "delpoi":
				return getCompletion(getPoiList(), args, 1);

			case "toa":
				arguments.add("accept");
				arguments.add("deny");
				return arguments;

			default:
				return arguments;
			}

		} else if (args.length >= 5 && subcommand.getName().equalsIgnoreCase("gps"))
			return getCompletion((ArrayList<String>) ConfigManager.getGpsWorldLabels(), args, 4);
		
		arguments.clear();
		return arguments;
	}

	private ArrayList<String> getCompletion(ArrayList<String> arguments, String[] args, int index) {
		ArrayList<String> results = new ArrayList<String>();
		for (String argument : arguments) {
			if (!argument.toLowerCase().startsWith(args[index].toLowerCase()))
				continue;

			results.add(argument);
		}
		return results;
	}

	private ArrayList<String> getPoiList() {
		ArrayList<String> poiNames = new ArrayList<String>();
		for (String poi : plugin.getPois().keySet())
			poiNames.add(poi);

		return poiNames;
	}
}
