package se.ranzdo.bukkit.methodcommand.handlers;

import org.bukkit.command.CommandSender;

import se.ranzdo.bukkit.methodcommand.ArgumentHandler;
import se.ranzdo.bukkit.methodcommand.ArgumentVerifier;
import se.ranzdo.bukkit.methodcommand.CommandArgument;
import se.ranzdo.bukkit.methodcommand.InvalidVerifyArgument;
import se.ranzdo.bukkit.methodcommand.VerifyError;


public abstract class NumberArgumentHandler<T extends Number> extends ArgumentHandler<T> {
	
	public NumberArgumentHandler() {
		setMessage("parse_error", "The parameter [%p] is not a number");
		setMessage("min_error", "The parameter [%p] must be equal or greater than %1.");
		setMessage("max_error", "The parameter [%p] must be equal or greater than %1.");
		
		addVerifier("min", new ArgumentVerifier<T>() {
			@Override
			public void verify(CommandSender sender, CommandArgument argument, String verifyName, String[] verifyArgs, T value, String valueRaw) throws VerifyError {
				if(verifyArgs.length != 1)
					throw new InvalidVerifyArgument(argument.getName());
				
				try {
					double min = Double.parseDouble(verifyArgs[0]);
					if(value.doubleValue() < min)
						throw new VerifyError(argument.getMessage("min_error", verifyArgs[0]));
				}
				catch (NumberFormatException e) {
					throw new InvalidVerifyArgument(argument.getName());
				}
			}
		});
		
		addVerifier("max", new ArgumentVerifier<T>() {
			@Override
			public void verify(CommandSender sender, CommandArgument argument, String verifyName, String[] verifyArgs, T value, String valueRaw) throws VerifyError {
				if(verifyArgs.length != 1)
					throw new InvalidVerifyArgument(argument.getName());
				
				try {
					double max = Double.parseDouble(verifyArgs[0]);
					if(value.doubleValue() > max)
						throw new VerifyError(argument.getMessage("max_error", verifyArgs[0]));
				}
				catch (NumberFormatException e) {
					throw new InvalidVerifyArgument(argument.getName());
				}
			}	
		});
	}
}
