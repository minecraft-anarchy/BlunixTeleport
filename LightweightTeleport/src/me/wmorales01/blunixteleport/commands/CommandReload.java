package me.wmorales01.blunixteleport.commands;

import org.bukkit.command.CommandSender;

import me.wmorales01.blunixteleport.BlunixTeleport;
import me.wmorales01.blunixteleport.managers.LocationDataManager;
import me.wmorales01.blunixteleport.util.MessageManager;

public class CommandReload extends TPCommand{
	private BlunixTeleport plugin;
	
	public CommandReload(BlunixTeleport instance) {
		plugin = instance;
		
		setName("reload");
		setPermission("blunixteleport.reload");
		setArgsLength(1);
		setUniversalCommand(true);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		plugin.reloadConfig();
		LocationDataManager manager = new LocationDataManager(plugin);
		manager.saveHomes();
		manager.savePois();
		manager.restoreHomes();
		manager.restorePois();
		
		MessageManager.sendMessage(sender, "&aConfig reloaded!");
	}
}
