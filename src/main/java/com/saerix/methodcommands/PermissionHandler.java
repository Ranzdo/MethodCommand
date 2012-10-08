package com.saerix.methodcommands;

import org.bukkit.command.CommandSender;

public interface PermissionHandler {
	public boolean hasPermission(CommandSender sender, String[] permissions);
}
