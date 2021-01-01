package me.wmorales01.blunixteleport.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.wmorales01.blunixteleport.BlunixTeleport;
import me.wmorales01.blunixteleport.managers.PlayerCooldownManager;
import me.wmorales01.blunixteleport.util.MessageManager;

public class CommandCooldown extends TPCommand {
	private BlunixTeleport plugin;

	public CommandCooldown(BlunixTeleport instance) {
		plugin = instance;
		setName("cooldown");
		setPermission("blunixteleport.cooldown");
		setArgsLength(1);
		setPlayerCommand(true);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		
		PlayerCooldownManager playerCooldown = new PlayerCooldownManager(plugin, player.getName());
		if (!playerCooldown.hasCooldown()) {
			MessageManager.sendMessage(player, "&9You have no cooldown.");
			return;
		}
		
		long cooldown = playerCooldown.getCooldown();
		MessageManager.sendMessage(player, "&9Current cooldown: &6" + cooldown + " &9seconds.");
	}
}
