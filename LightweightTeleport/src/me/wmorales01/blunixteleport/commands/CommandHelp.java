package me.wmorales01.blunixteleport.commands;

import org.bukkit.command.CommandSender;

import me.wmorales01.blunixteleport.util.MessageManager;
import me.wmorales01.blunixteleport.util.Parser;

public class CommandHelp extends TPCommand {

	public CommandHelp() {
		setName("help");
		setHelpMessage("Displays this list.");
		setPermission("blunixteleport.help");
		setArgsLength(1);
		setUniversalCommand(true);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 1) {
			MessageManager.sendHelpMessage(sender, 1);
			return;

		} else if (args.length == 2) {
			int page = Parser.getIntegerFromString(args[1]);
			
			MessageManager.sendHelpMessage(sender, page);
			return;
		}
	}

}
