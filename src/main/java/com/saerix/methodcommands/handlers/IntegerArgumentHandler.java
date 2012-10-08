package com.saerix.methodcommands.handlers;

import org.bukkit.command.CommandSender;

import com.saerix.methodcommands.ArgumentHandler;
import com.saerix.methodcommands.ArgumentVerifier;
import com.saerix.methodcommands.CommandArgument;
import com.saerix.methodcommands.InvalidVerifyArgument;
import com.saerix.methodcommands.TransformError;
import com.saerix.methodcommands.VerifyError;

public class IntegerArgumentHandler extends ArgumentHandler<Integer> {
	
	public IntegerArgumentHandler() {
		setMessage("parse_error", "The parameter [%p] is not a number");
		setMessage("min_error", "The parameter [%p] must be equal or greater than %1.");
		setMessage("max_error", "The parameter [%p] must be equal or greater than %1.");
		
		addVerifier("min", new ArgumentVerifier<Integer>() {
			@Override
			public void verify(CommandSender sender, CommandArgument argument, String verifyName, String[] verifyArgs, Integer value, String valueRaw) throws VerifyError {
				if(verifyArgs.length != 1)
					throw new InvalidVerifyArgument(argument.getName());
				
				try {
					int min = Integer.parseInt(verifyArgs[0]);
					if(value < min)
						throw new VerifyError(argument.getMessage("min_error", verifyArgs[0]));
				}
				catch (NumberFormatException e) {
					throw new InvalidVerifyArgument(argument.getName());
				}
			}
		});
			
		addVerifier("max", new ArgumentVerifier<Integer>() {
			@Override
			public void verify(CommandSender sender, CommandArgument argument, String verifyName, String[] verifyArgs, Integer value, String valueRaw) throws VerifyError {
				if(verifyArgs.length != 1)
					throw new InvalidVerifyArgument(argument.getName());
				
				try {
					int max = Integer.parseInt(verifyArgs[0]);
					if(value > max)
						throw new VerifyError(argument.getMessage("max_error", verifyArgs[0]));
				}
				catch (NumberFormatException e) {
					throw new InvalidVerifyArgument(argument.getName());
				}
			}
		});
	}

	@Override
	public Integer transform(CommandSender sender, CommandArgument argument, String value) throws TransformError {
		try {
			return Integer.parseInt(value);
		}
		catch(NumberFormatException e) {
			throw new TransformError(getMessage("parse_error"));
		}
	}
}
