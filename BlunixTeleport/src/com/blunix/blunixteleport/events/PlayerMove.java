package com.blunix.blunixteleport.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.blunix.blunixteleport.BlunixTeleport;
import com.blunix.blunixteleport.util.MessageManager;

public class PlayerMove implements Listener {
	private BlunixTeleport plugin;
	
	public PlayerMove(BlunixTeleport instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (!plugin.getOnGoingTps().containsKey(player.getName()))
			return;
		
		Location initLocation = event.getFrom();
		double initX = initLocation.getX();
		double initY = initLocation.getY();
		double initZ = initLocation.getZ();
		Location finalLocation = event.getTo();
		double finalX = finalLocation.getX();
		double finalY = finalLocation.getY();
		double finalZ = finalLocation.getZ();
		
		if (initX == finalX && initY == finalY && initZ == finalZ)
			return;
		
		// Cancelling TP and event
		Bukkit.getScheduler().cancelTask(plugin.getOnGoingTps().get(player.getName()));
		Bukkit.getScheduler().cancelTask(plugin.getTpAnimations().get(player.getName()));
		plugin.getOnGoingTps().remove(player.getName());
		MessageManager.sendMessage(player, "&cTP cancelled due to player's movement.");
		HandlerList.unregisterAll(this);
	}

}
