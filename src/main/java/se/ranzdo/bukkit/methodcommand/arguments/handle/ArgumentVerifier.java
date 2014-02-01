package se.ranzdo.bukkit.methodcommand.arguments.handle;

import org.bukkit.command.CommandSender;
import se.ranzdo.bukkit.methodcommand.CommandArgument;

public interface ArgumentVerifier<T> {
	public void verify(CommandSender sender, CommandArgument argument, String verifyName, String[] verifyArgs, T value, String valueRaw) throws VerifyError;
}
