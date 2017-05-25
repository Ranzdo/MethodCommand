package se.ranzdo.bukkit.methodcommand;

import org.bukkit.command.CommandSender;

public interface PermissionHandler {
    boolean hasPermission(CommandSender sender, String[] permissions);
}
