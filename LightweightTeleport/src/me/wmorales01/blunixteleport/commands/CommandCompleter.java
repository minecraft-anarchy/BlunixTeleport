package me.wmorales01.blunixteleport.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.wmorales01.blunixteleport.BlunixTeleport;

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
		ArrayList<String> results = new ArrayList<String>();
		
		if (args.length >= 0 && args.length < 2) {
			for (String argument : arguments) {
				if (argument.startsWith(args[0].toLowerCase()))
					results.add(argument);
			}

			return results;
		}
		if (args.length >= 1 && args.length < 3) {
			switch (args[0].toLowerCase()) {
			case "accept":
				return null;

			case "delpoi":
				results = getPoiList(results, args);
				return results;

			case "to":
				return null;

			case "poi":
				results = getPoiList(results, args);
				return results;

			case "deny":
				return null;

			default:
				return results;
			}
		}
		return results;
	}

	private ArrayList<String> getPoiList(ArrayList<String> results, String[] args) {
		ArrayList<String> pois = new ArrayList<String>();

		for (Map.Entry<String, Location> entry : plugin.getPois().entrySet()) {
			String poiName = entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1);
			pois.add(poiName);
		}

		for (String poi : pois) {
			if (poi.toLowerCase().startsWith(args[1].toLowerCase())) {
				results.add(poi);
			}
		}
		return results;
	}
}
