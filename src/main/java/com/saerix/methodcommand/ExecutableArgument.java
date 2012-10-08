package com.saerix.methodcommand;

import org.bukkit.command.CommandSender;

public interface ExecutableArgument {
	public Object execute(CommandSender sender, Arguments args) throws CommandError;
}
