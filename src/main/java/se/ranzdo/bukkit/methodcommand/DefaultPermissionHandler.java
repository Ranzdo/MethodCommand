package se.ranzdo.bukkit.methodcommand;

import org.bukkit.command.CommandSender;

public class DefaultPermissionHandler implements PermissionHandler {
    @Override
    public boolean hasPermission(CommandSender sender, String[] permissions) {
        for (String perm : permissions) {
            if (!sender.hasPermission(perm))
                return false;
        }
        return true;
    }
}
