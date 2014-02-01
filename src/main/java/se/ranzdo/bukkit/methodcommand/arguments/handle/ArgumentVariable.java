package se.ranzdo.bukkit.methodcommand.arguments.handle;

import org.bukkit.command.CommandSender;
import se.ranzdo.bukkit.methodcommand.CommandError;
import se.ranzdo.bukkit.methodcommand.CommandArgument;

public interface ArgumentVariable<T> {
    public T var(CommandSender sender, CommandArgument argument, String varName) throws CommandError;
}
