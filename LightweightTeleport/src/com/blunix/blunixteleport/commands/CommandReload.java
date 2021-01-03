package com.blunix.blunixteleport.commands;

import org.bukkit.command.CommandSender;

import com.blunix.blunixteleport.BlunixTeleport;
import com.blunix.blunixteleport.managers.LocationDataManager;
import com.blunix.blunixteleport.util.MessageManager;

public class CommandReload extends TPCommand{
	private BlunixTeleport plugin;
	
	public CommandReload(BlunixTeleport instance) {
		plugin = instance;
		setName("reload");
		setHelpMessage("Reloads the plugin's config.");
		setPermission("blunixteleport.reload");
		setArgsLength(1);
		setUsage("/tp reload");
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
