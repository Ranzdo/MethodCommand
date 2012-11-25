package se.ranzdo.bukkit.methodcommand.handlers;

import org.bukkit.command.CommandSender;

import se.ranzdo.bukkit.methodcommand.ArgumentHandler;
import se.ranzdo.bukkit.methodcommand.ArgumentVerifier;
import se.ranzdo.bukkit.methodcommand.CommandArgument;
import se.ranzdo.bukkit.methodcommand.InvalidVerifyArgument;
import se.ranzdo.bukkit.methodcommand.TransformError;
import se.ranzdo.bukkit.methodcommand.VerifyError;

public class DoubleArgumentHandler extends ArgumentHandler<Double> {

	public DoubleArgumentHandler() {
		setMessage("parse_error", "The parameter [%p] is not a number");
		setMessage("min_error", "The parameter [%p] must be equal or greater than %1.");
		setMessage("max_error", "The parameter [%p] must be equal or greater than %1.");
		
		addVerifier("min", new ArgumentVerifier<Double>() {
			@Override
			public void verify(CommandSender sender, CommandArgument argument, String verifyName, String[] verifyArgs, Double value, String valueRaw) throws VerifyError {
				if(verifyArgs.length != 1)
					throw new InvalidVerifyArgument(argument.getName());
				
				try {
					double min = Double.parseDouble(verifyArgs[0]);
					if(value < min)
						throw new VerifyError(argument.getMessage("min_error", verifyArgs[0]));
				}
				catch (NumberFormatException e) {
					throw new InvalidVerifyArgument(argument.getName());
				}
			}
		});
		addVerifier("max", new ArgumentVerifier<Double>() {
			@Override
			public void verify(CommandSender sender, CommandArgument argument, String verifyName, String[] verifyArgs, Double value, String valueRaw) throws VerifyError {
				if(verifyArgs.length != 1)
					throw new InvalidVerifyArgument(argument.getName());
				
				try {
					double max = Double.parseDouble(verifyArgs[0]);
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
	public Double transform(CommandSender sender, CommandArgument argument, String value) throws TransformError {
		try {
			return Double.parseDouble(value);
		}
		catch(NumberFormatException e) {
			throw new TransformError(getMessage("parse_error"));
		}
	}
}
