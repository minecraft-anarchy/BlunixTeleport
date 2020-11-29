package me.wmorales01.lightweightteleport.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.wmorales01.lightweightteleport.Main;

public class CommandCompleter implements TabCompleter {
	private Main plugin;
	private ArrayList<String> arguments = new ArrayList<String>();

	public CommandCompleter(Main instance) {
		plugin = instance;
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (arguments.isEmpty()) {
			arguments.add("accept");
			arguments.add("bed");
			arguments.add("cooldown");
			arguments.add("delpoi");
			arguments.add("deny");
			arguments.add("gps");
			arguments.add("help");
			arguments.add("home");
			arguments.add("to");
			arguments.add("poi");
			arguments.add("sethome");
			arguments.add("setpoi");
			arguments.add("setpoi");
			arguments.add("wild");
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

			case "help":
				results.add("1");
				results.add("2");
				return results;

			default:
				return results;
			}
		}
		return results;
	}

	private ArrayList<String> getPoiList(ArrayList<String> results, String[] args) {
		ArrayList<String> pois = new ArrayList<String>();

		for (Map.Entry<String, Location> entry : plugin.pois.entrySet()) {
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
