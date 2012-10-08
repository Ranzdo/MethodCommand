package com.saerix.methodcommand;

import org.bukkit.command.CommandSender;

public interface ArgumentVerifier<T> {
	public void verify(CommandSender sender, CommandArgument argument, String verifyName, String[] verifyArgs, T value, String valueRaw) throws VerifyError;
}
