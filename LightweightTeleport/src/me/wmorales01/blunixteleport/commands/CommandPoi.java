package me.wmorales01.blunixteleport.commands;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.wmorales01.blunixteleport.BlunixTeleport;
import me.wmorales01.blunixteleport.managers.TeleportManager;
import me.wmorales01.blunixteleport.util.MessageManager;

public class CommandPoi extends TPCommand {
	private BlunixTeleport plugin;

	public CommandPoi(BlunixTeleport instance) {
		plugin = instance;
		setName("poi");
		setPermission("blunixteleport.poi");
		setArgsLength(1);
		setPlayerCommand(true);
		setAffectedByCooldown(true);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;

		if (args.length == 1) {
			// Executed /tp poi
			if (plugin.getPois().isEmpty()) {
				MessageManager.sendMessage(player, "&cCurrently there are not any POIs");
				return;
			}
			// Concatenate every POI into a String
			String poiList = "";
			for (Map.Entry<String, Location> entry : plugin.getPois().entrySet()) {
				// Capitalize first letter
				String poiName = entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1);
				poiList += poiName + "\n";
			}
			MessageManager.sendMessage(player, "&9" + poiList);
			return;

		} else if (args.length > 1) {
			// Executed /tp poi <POI>
			String selectedPoi = args[1].toLowerCase();
			if (!plugin.getPois().containsKey(selectedPoi)) {
				MessageManager.sendMessage(player, "&cUnknown POI");
				return;
			}
			Location poi = plugin.getPois().get(selectedPoi);
			TeleportManager.teleportPlayer(player, poi);
		}
	}

}
