package com.blunix.blunixteleport.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blunix.blunixteleport.BlunixTeleport;
import com.blunix.blunixteleport.util.MessageManager;

public class CommandDelPoi extends TPCommand {
	private BlunixTeleport plugin;

	public CommandDelPoi(BlunixTeleport instance) {
		plugin = instance;
		setName("delpoi");
		setHelpMessage("Delets the specified POI.");
		setPermission("blunixteleport.delpoi");
		setArgsLength(2);
		setUniversalCommand(true);
		setUsage("/tp delpoi <POI>");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		String deletePoi = args[1];
		if (!plugin.getPois().containsKey(deletePoi)) {
			MessageManager.sendMessage(player, "&cThere is not any existing POI with this name.");
			return;
		}
		
		// Delete POI both from internal HashMap and locations.yml
		plugin.getPois().remove(deletePoi);
		plugin.getLocationData().set("pois." + deletePoi, null);
		plugin.saveLocationData();
		MessageManager.sendMessage(player, "&a&l" + deletePoi + " &ahas been removed successfully!");
		return;
	}
}
