package com.saerix.methodcommand.handlers;

import org.bukkit.command.CommandSender;

import com.saerix.methodcommand.ArgumentHandler;
import com.saerix.methodcommand.ArgumentVerifier;
import com.saerix.methodcommand.CommandArgument;
import com.saerix.methodcommand.InvalidVerifyArgument;
import com.saerix.methodcommand.TransformError;
import com.saerix.methodcommand.VerifyError;

public class StringArgumentHandler extends ArgumentHandler<String> {
	public StringArgumentHandler() {
		setMessage("min_error", "The parameter [%p] must be more than %1 characters.");
		setMessage("max_error", "The parameter [%p] can't be more than %1 characters.");
		
		addVerifier("min", new ArgumentVerifier<String>() {
			@Override
			public void verify(CommandSender sender, CommandArgument argument, String verifyName, String[] verifyArgs, String value, String valueRaw) throws VerifyError {
				if(verifyArgs.length != 1)
					throw new InvalidVerifyArgument(argument.getName());
				
				try {
					int min = Integer.parseInt(verifyArgs[0]);
					if(value.length() < min)
						throw new VerifyError(argument.getMessage("min_error", verifyArgs[0]));
				}
				catch (NumberFormatException e) {
					throw new InvalidVerifyArgument(argument.getName());
				}
			}
		});
		
		
		addVerifier("max", new ArgumentVerifier<String>() {
			@Override
			public void verify(CommandSender sender, CommandArgument argument, String verifyName, String[] verifyArgs, String value, String valueRaw) throws VerifyError {
				if(verifyArgs.length != 1)
					throw new InvalidVerifyArgument(argument.getName());
				
				try {
					int max = Integer.parseInt(verifyArgs[0]);
					if(value.length() > max)
						throw new VerifyError(argument.getMessage("max_error", verifyArgs[0]));
				}
				catch (NumberFormatException e) {
					throw new InvalidVerifyArgument(argument.getName());
				}
			}
		});
	}

	@Override
	public String transform(CommandSender sender, CommandArgument argument, String value) throws TransformError {
		return value;
	}
}
