package se.ranzdo.bukkit.methodcommand;

import org.bukkit.command.CommandSender;

public interface ExecutableArgument {
    Object execute(CommandSender sender, Arguments args) throws CommandError;
}
