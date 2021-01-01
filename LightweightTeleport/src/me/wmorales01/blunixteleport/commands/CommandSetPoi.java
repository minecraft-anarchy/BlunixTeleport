package me.wmorales01.blunixteleport.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.wmorales01.blunixteleport.BlunixTeleport;
import me.wmorales01.blunixteleport.util.MessageManager;

public class CommandSetPoi extends TPCommand {
	private BlunixTeleport plugin;

	public CommandSetPoi(BlunixTeleport instance) {
		plugin = instance;
		setName("setpoi");
		setPermission("blunixteleport.setpoi");
		setArgsLength(2);
		setPlayerCommand(true);
		setUsage("/tp setpoi <POI Name>");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		String inputPoi = args[1].toLowerCase();
		if (plugin.getPois().containsKey(inputPoi)) {
			MessageManager.sendMessage(player, "&cPOI name already exists.");
			return;
		}
		plugin.addPois(inputPoi, player.getLocation());
		MessageManager.sendMessage(player, "&a&l" + inputPoi + " &ahas been succesfully set as a POI!");
		return;
	}

}
