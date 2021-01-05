package com.blunix.blunixteleport.commands;

import org.bukkit.command.CommandSender;

import com.blunix.blunixteleport.util.MessageManager;
import com.blunix.blunixteleport.util.Parser;

public class CommandHelp extends TPCommand {

	public CommandHelp() {
		setName("help");
		setHelpMessage("Displays this list.");
		setPermission("blunixteleport.help");
		setArgsLength(1);
		setUsage("/tp help");
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
