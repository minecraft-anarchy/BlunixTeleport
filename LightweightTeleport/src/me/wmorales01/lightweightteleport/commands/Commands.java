package me.wmorales01.lightweightteleport.commands;

import org.bukkit.Sound;
import org.bukkit.Location;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import me.wmorales01.lightweightteleport.Main;
import org.bukkit.command.CommandExecutor;

public class Commands implements CommandExecutor {
	private Main plugin;

	public Commands(Main instance) {
		plugin = instance;
	}

	String prefix = "";

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!cmd.getName().equalsIgnoreCase("teleport")) {
			return false;
		}

		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cNot available for consoles."));
			return true;
		}

		Player player = (Player) sender;
		if (args.length == 0) {
			sendMessage(player, "&cType &c&l/tp help &cto see the full command list.");
			return true;
		}

		switch (args[0].toLowerCase()) {
		case "accept":
			if (!player.hasPermission("tp.respond")) {
				noPermissionMessage(player);
				return true;
			}
			if (args.length == 1) {
				sendMessage(player, "&cUsage: &l/tp accept <Player>.");
				return true;

			} else if (args.length > 2) {
				tooManyArgumentsMessage(player);
				return true;
			}

		case "delpoi":
			if (!player.hasPermission("tp.delpoi"))
				this.noPermissionMessage(player);

			if (args.length < 2) {
				this.sendMessage(player, "&cUsage: &l/delpoi <POI Name>");
				return true;
			} else if (args.length > 2) {
				this.tooManyArgumentsMessage(player);
				return true;
			}
			String deletePoi = args[1].toLowerCase();
			if (!plugin.pois.containsKey(deletePoi)) {
				sendMessage(player, "&cThere is not any existing POI with this name.");
				return true;
			}
			// Delete POI both from internal HashMap and locations.yml
			plugin.pois.remove(deletePoi);
			plugin.getLocationData().set("pois." + deletePoi, null);
			plugin.saveLocationData();
			sendMessage(player, "&a&l" + deletePoi + " &ahas been removed successfully!");
			return true;

		case "setpoi":
			if (!player.hasPermission("tp.setpoi")) {
				noPermissionMessage(player);
				return true;
			}
			if (args.length < 2) {
				sendMessage(player, "&cUsage: &l/tp setpoi <POI Name>");
				return true;
			} else if (args.length > 2) {
				tooManyArgumentsMessage(player);
				return true;
			}
			String inputPoi = args[1].toLowerCase();
			if (plugin.pois.containsKey(inputPoi)) {
				sendMessage(player, "&cPOI name already exists.");
				return true;
			}
			plugin.pois.put(inputPoi, player.getLocation());
			sendMessage(player, "&a&l" + inputPoi + " &ahas been succesfully set as a POI!");
			return true;

		case "cooldown": {
			if (!player.hasPermission("tp.cooldown")) {
				noPermissionMessage(player);
				return true;
			}
			// Display cooldown
			return true;
		}
		case "to":
			if (!player.hasPermission("tp.request")) {
				this.noPermissionMessage(player);
				return true;
			}
			if (args.length < 2) {
				sendMessage(player, "&cUsage: &l/tp to <Player>");
				return true;
			} else if (args.length > 2) {
				tooManyArgumentsMessage(player);
				return true;
			}
			Player requestedPlayer = Bukkit.getPlayer(args[1]);
			if (requestedPlayer == null) {
				sendMessage(player, "&cPlayer not found.");
				return true;
			}
			// Send Request
			return true;

		case "bed":
			if (!player.hasPermission("tp.bed")) {
				noPermissionMessage(player);
				return true;
			}
			if (player.getBedSpawnLocation() == null) {
				sendMessage(player, "&cYou don't have any bed to TP right now!");
				return true;
			}
			teleportPlayer(player, player.getBedSpawnLocation());
			return true;

		case "gps":
			if (!player.hasPermission("tp.gps")) {
				noPermissionMessage(player);
				return true;
			}
			if (args.length < 4) {
				sendMessage(player, "&cUsage: &l/tp gps <x> <y> <z>");
				return true;
			} else if (args.length > 4) {
				tooManyArgumentsMessage(player);
				return true;
			}
			// Tp player to location
			return true;

		case "poi":
			if (!player.hasPermission("tp.poilist")) {
				noPermissionMessage(player);
				return true;
			}
			if (args.length == 1) {
				if (plugin.pois.isEmpty()) {
					sendMessage(player, "&cCurrently there are not any POIs");
					return true;
				}
				// Concatenate every POI into a String
				String poiList = "";
				for (Map.Entry<String, Location> entry : plugin.pois.entrySet()) {
					// Capitalize first letter
					String poiName = entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1);
					poiList = poiList + poiName + ".";
				}
				sendMessage(player, "&9" + poiList);
				return true;

			} else if (args.length > 2) {
				tooManyArgumentsMessage(player);
				return true;
			}
			String tpPoi = args[1].toLowerCase();
			if (!plugin.pois.containsKey(tpPoi)) {
				sendMessage(player, "&cUnknown POI");
				return true;
			}
			Location poi = plugin.pois.get(tpPoi);
			teleportPlayer(player, poi);
			return true;

		case "deny":
			if (!player.hasPermission("tp.respond")) {
				noPermissionMessage(player);
				return true;
			}
			if (args.length == 1) {
				sendMessage(player, "&cUsage: &l/tp deny <Player>.");
				return true;
			} else if (args.length > 2) {
				tooManyArgumentsMessage(player);
				return true;
			}
			return true;

		case "help":
			if (args.length == 1 || args[1].equals("1")) {
				sendMessage(player,
						"\n&5&l/tp accept <Player> &6- &9Accepts an specific player TP request.\n&5&l/tp bed &6- &9Teleports you to your bed location (In case you have one).\n&5&l/tp cooldown &6- &9Shows your current TP cooldown.\n&5&l/tp delpoi <POI Name> &6- &9Deletes one existing POI.\n&5&l/tp deny <Player> &6- &9Denies an specific player TP request.\n&5&l/tp gps <x> <y> <y> &6- &9Teleports you to the specified coordinates.\n&5&l/tp help &6- &9Displays this list.");
				return true;

			} else if (args.length == 2 && args[1].equals("2")) {
				sendMessage(player,
						"\n&5&l/tp home &6- &9Teleports you back to your home.\n&5&l/tp to <Player> &6- &9Sends a request to another player to teleport to him.\n&5&l/tp poi &6- &9Displays the full list of Point of Interest.\n&5&l/tp poi <POI Name> &6- &9Teleports you to the specified POI.\n&5&l/tp sethome &6- &9Sets the location you will be TPd with /tp home.\n&5&l/tp setpoi <POI Name> &6- &9Sets a POI location.\n&5&l/tp wild &6- &9Teleports you to a random location in the current world.");
				return true;

			} else if (args.length > 2) {
				tooManyArgumentsMessage(player);
				return true;
			}
			return true;

		case "home":
			if (!player.hasPermission("tp.home")) {
				noPermissionMessage(player);
				return true;
			}
			if (!plugin.homes.containsKey(player.getName())) {
				sendMessage(player, "&cYou don't have a home yet! Use &l/tp sethome &cto set one.");
				return true;
			}
			Location home = plugin.homes.get(player.getName());
			teleportPlayer(player, home);
			return true;

		case "wild":
			if (!player.hasPermission("tp.wild")) {
				noPermissionMessage(player);
				return true;
			}
			return true;

		case "sethome":
			if (!player.hasPermission("tp.sethome")) {
				noPermissionMessage(player);
				return true;
			}
			if (args.length > 1) {
				tooManyArgumentsMessage(player);
				return true;
			}
			plugin.homes.put(player.getName(), player.getLocation());
			sendMessage(player, "&aHome set!");
			return true;

		default:
			sendMessage(player, "&cUnknown command! Type &c&l/tp help &cto see all available commands.");
			return true;
		}
	}

	private void teleportPlayer(Player player, Location location) {
		player.teleport(location);
		player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
	}

	private void sendMessage(Player player, String message) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
	}

	private void tooManyArgumentsMessage(Player player) {
		sendMessage(player, "&cToo many arguments!");
	}

	private void noPermissionMessage(Player player) {
		sendMessage(player, "&cYou do not have permissions to use this command!");
	}
}
