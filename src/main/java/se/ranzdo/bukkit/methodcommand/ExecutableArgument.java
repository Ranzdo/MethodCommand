package se.ranzdo.bukkit.methodcommand;

import org.bukkit.command.CommandSender;
import se.ranzdo.bukkit.methodcommand.arguments.Arguments;

public interface ExecutableArgument {
    public Object execute(CommandSender sender, Arguments args) throws CommandError;
}
