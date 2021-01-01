package me.wmorales01.blunixteleport.commands;

import org.bukkit.command.CommandSender;

public abstract class TPCommand {
	private String name;
	private String helpMessage;
	private String permission;
	private int argsLength;
	private String usage;
	private boolean isConsoleCommand;
	private boolean isPlayerCommand;
	private boolean isUniversalCommand;
	private boolean isAffectedByCooldown;
	
	public abstract void execute(CommandSender sender, String[] args);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHelpMessage() {
		return helpMessage;
	}

	public void setHelpMessage(String helpMessage) {
		this.helpMessage = helpMessage;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public int getArgsLength() {
		return argsLength;
	}

	public void setArgsLength(int argsLength) {
		this.argsLength = argsLength;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public boolean isConsoleCommand() {
		return isConsoleCommand;
	}

	public void setConsoleCommand(boolean isConsoleCommand) {
		this.isConsoleCommand = isConsoleCommand;
	}

	public boolean isPlayerCommand() {
		return isPlayerCommand;
	}

	public void setPlayerCommand(boolean isPlayerCommand) {
		this.isPlayerCommand = isPlayerCommand;
	}

	public boolean isUniversalCommand() {
		return isUniversalCommand;
	}

	public void setUniversalCommand(boolean isUniversalCommand) {
		this.isUniversalCommand = isUniversalCommand;
	}

	public boolean isAffectedByCooldown() {
		return isAffectedByCooldown;
	}

	public void setAffectedByCooldown(boolean isAffectedByCooldown) {
		this.isAffectedByCooldown = isAffectedByCooldown;
	}
}
