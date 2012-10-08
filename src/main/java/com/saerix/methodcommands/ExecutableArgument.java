package com.saerix.methodcommands;

import org.bukkit.command.CommandSender;

public interface ExecutableArgument {
	public Object execute(CommandSender sender, Arguments args) throws CommandError;
}
