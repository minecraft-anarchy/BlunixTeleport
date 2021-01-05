package com.blunix.blunixteleport.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blunix.blunixteleport.BlunixTeleport;
import com.blunix.blunixteleport.managers.ConfigManager;
import com.blunix.blunixteleport.managers.TeleportManager;
import com.blunix.blunixteleport.managers.TeleportRequestManager;
import com.blunix.blunixteleport.util.MessageManager;

public class CommandTo extends TPCommand {
	private BlunixTeleport plugin;

	public CommandTo(BlunixTeleport instance) {
		plugin = instance;
		setName("to");
		setHelpMessage("Sends a TP request to the specified player.");
		setPermission("blunixteleport.request");
		setArgsLength(2);
		setUniversalCommand(true);
		setUsage("/tp to <Player>");
		setAffectedByCooldown(true);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		Player requestedPlayer = Bukkit.getPlayer(args[1]);
		if (requestedPlayer == null) {
			MessageManager.sendMessage(player, "&c&l" + args[1] + " &cis not online.");
			return;
		}
		if (player.equals(requestedPlayer)) {
			MessageManager.sendMessage(sender, "&cYou can't teleport to yourself.");
			return;
		}

		if (!player.hasPermission("blunixteleport.admin")) {
			TeleportRequestManager requestManager = new TeleportRequestManager(plugin, player);
			requestManager.addTeleportRequest(requestedPlayer);
			long expirationTime = ConfigManager.getTpRequestExpiration();
			// Setting request expiration
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

				@Override
				public void run() {
					if (!requestManager.hasTeleportRequest(player))
						return;

					requestManager.removeTeleportRequest(player);
					MessageManager.sendMessage(player, "&cYour TP request has expired.");
				}

			}, expirationTime);

			MessageManager.sendMessage(player, "&6Asking &l" + requestedPlayer.getDisplayName()
					+ " &6to confirm your teleportation request. You will be teleported when the petition gets accepted.");
			MessageManager.sendMessage(requestedPlayer, "&6&l" + player.getDisplayName()
					+ " &6sent you a TP request. Type &l/tp accept <Player> &6to accept it.");
		} else {
			TeleportManager.teleportPlayer(player, requestedPlayer.getLocation());
		}
	}

}
