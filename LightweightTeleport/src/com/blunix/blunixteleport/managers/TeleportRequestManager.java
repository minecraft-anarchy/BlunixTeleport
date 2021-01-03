package com.blunix.blunixteleport.managers;

import org.bukkit.entity.Player;

import com.blunix.blunixteleport.BlunixTeleport;
import com.blunix.blunixteleport.util.MessageManager;

public class TeleportRequestManager {
	private BlunixTeleport plugin;
	private Player player;

	public TeleportRequestManager(BlunixTeleport instance, Player player) {
		plugin = instance;
		this.player = player;
	}

	public boolean hasTeleportRequest(Player petitionPlayer) {
		if (!plugin.getTeleportPetitions().containsValue(player)) {
			MessageManager.sendMessage(player, "&cYou don't have any TP petitions right now.");
			return false;
		}

		if (plugin.getTeleportPetitions().get(petitionPlayer) != player) {
			MessageManager.sendMessage(player,
					"&cYou don't have any tp petitions from &l" + petitionPlayer.getDisplayName());
			return false;
		}

		return true;
	}

	public void addTeleportRequest(Player petitionPlayer) {
		if (plugin.getTeleportPetitions().containsKey(petitionPlayer)) {
			MessageManager.sendMessage(player,
					"&cYou have already sent a petition to &l" + petitionPlayer.getDisplayName() + "&c.");
			return;
		}
		
		plugin.addTeleportPetitions(petitionPlayer, player);
	}
	
	public void removeTeleportRequest(Player petitionPlayer) {
		plugin.getTeleportPetitions().remove(petitionPlayer);
	}
}
