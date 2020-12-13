package me.wmorales01.lightweightteleport.commands;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.advancement.Advancement;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.wmorales01.lightweightteleport.Main;
import me.wmorales01.lightweightteleport.models.PlayerCooldownManager;

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
		PlayerCooldownManager playerCooldown = new PlayerCooldownManager(plugin, player.getUniqueId());
		if (args.length == 0) {
			sendHelpMessage(player, "");
			return true;
		}

		switch (args[0].toLowerCase()) {
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

		case "cooldown":
			if (!player.hasPermission("tp.cooldown")) {
				noPermissionMessage(player);
				return true;
			}
			if (!playerCooldown.hasCooldown()) {
				sendMessage(player, "&9You have no cooldown.");
				return true;
			}
			long cooldown = playerCooldown.getCooldown();
			sendMessage(player, "&9Current cooldown: &6" + cooldown + " &9seconds.");
			return true;

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
			if (playerCooldown.hasCooldown()) {
				sendCooldownMessage(player, playerCooldown.getCooldown());
				return true;
			}
			Player requestedPlayer = Bukkit.getPlayer(args[1]);
			if (requestedPlayer == null) {
				sendMessage(player, "&c&l" + args[1] + " &cis not online.");
				return true;
			}
			plugin.teleportPetitions.put(player, requestedPlayer);
			sendMessage(requestedPlayer, "&6&l" + player.getDisplayName()
					+ " &6sent you a TP request. Type &l/tp accept <Player> &6to accept it.");
			return true;

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
			Player petitionPlayer = Bukkit.getPlayer(args[1]);
			if (petitionPlayer == null) {
				sendMessage(player, "&cThis player is not online.");
				return true;
			}
			if (!hasTeleportPetitions(player, petitionPlayer))
				return true;

			plugin.teleportPetitions.remove(petitionPlayer);
			teleportPlayer(petitionPlayer, player.getLocation());
			sendMessage(player, "&a&l" + petitionPlayer.getDisplayName() + " &awill be teleported to you.");
			sendMessage(petitionPlayer, "&a&l" + player.getDisplayName() + " &ahas accepted you TP petition.");
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
			petitionPlayer = Bukkit.getPlayer(args[1]);
			if (petitionPlayer == null) {
				sendMessage(player, "&cThis player is not online.");
				return true;
			}

			if (!hasTeleportPetitions(player, petitionPlayer))
				return true;

			plugin.teleportPetitions.remove(petitionPlayer);
			sendMessage(player, "&aTP petition denied.");
			sendMessage(petitionPlayer, "&c&l" + player.getDisplayName() + " &adenied your TP petition.");
			return true;

		case "bed":
			if (!player.hasPermission("tp.bed")) {
				noPermissionMessage(player);
				return true;
			}
			if (playerCooldown.hasCooldown()) {
				sendCooldownMessage(player, playerCooldown.getCooldown());
				return true;
			}
			if (player.getBedSpawnLocation() == null) {
				player.sendMessage("Your bed is missing or obstructed");
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
				sendMessage(player, "&cUsage: &l/tp gps <X> <Y> <Z>");
				return true;
			} else if (args.length > 4) {
				tooManyArgumentsMessage(player);
				return true;
			}
			if (playerCooldown.hasCooldown()) {
				sendCooldownMessage(player, playerCooldown.getCooldown());
				return true;
			}
			Double x = getCoordinateFromString(args[1]);
			Double y = getCoordinateFromString(args[2]);
			Double z = getCoordinateFromString(args[3]);
			World world = player.getWorld();

			if (x == null || y == null || z == null) {
				sendMessage(player, "&cBe sure you input a numeric amount for the coordinates!");
				return true;
			}

			Location tpLocation = new Location(world, x, y, z);
			teleportPlayer(player, tpLocation);
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
			if (playerCooldown.hasCooldown()) {
				sendCooldownMessage(player, playerCooldown.getCooldown());
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

		case "help":
			if (args.length == 1) {
				sendHelpMessage(player, "");
				return true;

			} else if (args.length == 2 && args[1].equals("2")) {
				sendHelpMessage(player, args[1]);
				return true;

			} else if (args.length > 2) {
				tooManyArgumentsMessage(player);
				return true;
			}
			return true;

		case "wild":
			if (!player.hasPermission("tp.wild")) {
				noPermissionMessage(player);
				return true;
			}
			if (playerCooldown.hasCooldown()) {
				sendCooldownMessage(player, playerCooldown.getCooldown());
				return true;
			}
			return true;

//		case "home":
//			if (!player.hasPermission("tp.home")) {
//				noPermissionMessage(player);
//				return true;
//			}
//			if (!plugin.homes.containsKey(player.getName())) {
//				sendMessage(player, "&cYou don't have a home yet! Use &l/tp sethome &cto set one.");
//				return true;
//			}
//			Location home = plugin.homes.get(player.getName());
//			teleportPlayer(player, home);
//			return true;

//		case "sethome":
//			if (!player.hasPermission("tp.sethome")) {
//				noPermissionMessage(player);
//				return true;
//			}
//			if (args.length > 1) {
//				tooManyArgumentsMessage(player);
//				return true;
//			}
//			plugin.homes.put(player.getName(), player.getLocation());
//			sendMessage(player, "&aHome set!");
//			return true;

		default:
			sendMessage(player, "&cUnknown command! Type &c&l/tp help &cto see all available commands.");
			return true;
		}
	}

	private void teleportPlayer(Player player, Location location) {
		Environment playerEnvironment = player.getWorld().getEnvironment();
		if (playerEnvironment.equals(Environment.NETHER) && !hasAdvancement(player, "story/enter_the_nether")) {
			sendMessage(player, "&cYou need to visit The Nether by yourself before teleporting there.");
			return;

		} else if (playerEnvironment.equals(Environment.THE_END) && !hasAdvancement(player, "end/root")) {
			sendMessage(player, "&cYou need to visit The End by yourself before teleporting there.");
			return;
		}

		// Start timer
		player.teleport(location);
		player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
		PlayerCooldownManager playerCooldown = new PlayerCooldownManager(plugin, player.getUniqueId());
		playerCooldown.setCooldown();

	}

	private boolean hasAdvancement(Player player, String advancementName) {
		Advancement advancement = Bukkit.getAdvancement(NamespacedKey.minecraft(advancementName));
		if (!player.getAdvancementProgress(advancement).isDone())
			return false;

		return true;
	}

	private Double getCoordinateFromString(String stringCoordinate) {
		double coordinate;
		try {
			coordinate = Double.parseDouble(stringCoordinate);
		} catch (Exception e) {
			return null;
		}
		return coordinate;
	}

	private boolean hasTeleportPetitions(Player player, Player petitionPlayer) {
		if (!plugin.teleportPetitions.containsKey(petitionPlayer)) {
			sendMessage(player, "&cYou don't have any TP petitions right now.");
			return false;
		}

		if (plugin.teleportPetitions.get(petitionPlayer) != player) {
			sendMessage(player, "&cYou don't have any tp petitions from &l" + petitionPlayer.getDisplayName());
			return false;
		}

		return true;
	}

	private void sendMessage(Player player, String message) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
	}

	private void sendHelpMessage(Player player, String page) {
		if (page.equals("1") || page.equals("")) {
			sendMessage(player,
					"\n&5&l/tp accept <Player> &6- &9Accepts an specific player TP request.\n"
							+ "&5&l/tp bed &6- &9Teleports you to your bed location (In case you have one).\n"
							+ "&5&l/tp cooldown &6- &9Shows your current TP cooldown.\n"
							+ "&5&l/tp delpoi <POI Name> &6- &9Deletes one existing POI.\n"
							+ "&5&l/tp deny <Player> &6- &9Denies an specific player TP request.\n"
							+ "&5&l/tp gps <x> <y> <y> &6- &9Teleports you to the specified coordinates.\n"
							+ "&5&l/tp help &6- &9Displays this list.\n"
							+ "&9Type &l/tp help 2 &9to display the second page of commands.");

		} else if (page.equals("2")) {
			sendMessage(player,
					"\n&5&l/tp home &6- &9Teleports you back to your home.\n"
							+ "&5&l/tp to <Player> &6- &9Sends a request to another player to teleport to him.\n"
							+ "&5&l/tp poi &6- &9Displays the full list of Point of Interest.\n"
							+ "&5&l/tp poi <POI Name> &6- &9Teleports you to the specified POI.\n"
							+ "&5&l/tp sethome &6- &9Sets the location you will be TPd with /tp home.\n"
							+ "&5&l/tp setpoi <POI Name> &6- &9Sets a POI location.\n"
							+ "&5&l/tp wild &6- &9Teleports you to a random location in the current world.");
		}
	}
	
	private void sendCooldownMessage(Player player, long cooldown) {
		sendMessage(player, "&cYour TP is in cooldown! You need to wait &l" + cooldown + " seconds.");
	}

	private void tooManyArgumentsMessage(Player player) {
		sendMessage(player, "&cToo many arguments!");
	}

	private void noPermissionMessage(Player player) {
		sendMessage(player, "&cYou do not have permissions to use this command!");
	}
}
