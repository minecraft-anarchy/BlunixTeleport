package com.blunix.blunixteleport.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blunix.blunixteleport.BlunixTeleport;
import com.blunix.blunixteleport.managers.PlayerCooldownManager;
import com.blunix.blunixteleport.util.MessageManager;

public class CommandRunner implements CommandExecutor {
	private BlunixTeleport plugin;

	public CommandRunner(BlunixTeleport instance) {
		plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!cmd.getName().equalsIgnoreCase("teleport"))
			return true;
		if (args.length < 1) {
			MessageManager.sendHelpMessage(sender, 1);
			return true;
		}
		if (!plugin.getSubcommands().containsKey(args[0].toLowerCase())) {
			MessageManager.sendMessage(sender, "&cUnknown command! Type &l/tp help &cto see the full command list.");
			return true;
		}
		
		if (!plugin.getPlayersInContract().contains(sender.getName()) && !args[0].equalsIgnoreCase("toa")) {
			MessageManager.sendMessage(sender,
					"&6To use the TP service you must read and accept our terms of agreement, type &l/tp toa to read them.");
			return true;
		}

		TPCommand subCommand = plugin.getSubcommands().get(args[0].toLowerCase());
		if (subCommand.isPlayerCommand() && !(sender instanceof Player)) {
			MessageManager.sendMessage(sender, "&cNot available for consoles.");
			return true;
		} else if (subCommand.isConsoleCommand() && sender instanceof Player) {
			MessageManager.sendMessage(sender, "&cNot available for players.");
			return true;
		}

		if (args.length < subCommand.getArgsLength()) {
			MessageManager.sendMessage(sender, "&cUsage: &l" + subCommand.getUsage());
			return true;
		}

//		if (!sender.hasPermission(subCommand.getPermission())) {
//			MessageManager.sendNoPermissionMessage(sender);
//			return true;
//		}

		if (subCommand.isAffectedByCooldown() && subCommand.isPlayerCommand()) {
			Player player = (Player) sender;
			PlayerCooldownManager playerCooldown = new PlayerCooldownManager(plugin, player.getName());
			if (playerCooldown.hasCooldown()) {
				MessageManager.sendCooldownMessage(player, playerCooldown.getCooldown());
				return true;
			}
		}

		subCommand.execute(sender, args);
		return true;
	}
}
