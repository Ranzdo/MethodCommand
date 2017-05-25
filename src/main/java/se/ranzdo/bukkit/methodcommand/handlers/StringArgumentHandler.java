package se.ranzdo.bukkit.methodcommand.handlers;

import org.bukkit.command.CommandSender;
import se.ranzdo.bukkit.methodcommand.*;
import se.ranzdo.bukkit.methodcommand.VerifyError;


public class StringArgumentHandler extends ArgumentHandler<String> {
    public StringArgumentHandler() {
        setMessage("min_error", "The parameter [%p] must be more than %1 characters.");
        setMessage("max_error", "The parameter [%p] can't be more than %1 characters.");

        addVerifier("min", (sender, argument, verifyName, verifyArgs, value, valueRaw) -> {
            if (verifyArgs.length != 1) {
                throw new InvalidVerifyArgument(argument.getName());
            }

            try {
                int min = Integer.parseInt(verifyArgs[0]);

                if (value.length() < min) {
                    throw new VerifyError(argument.getMessage("min_error", verifyArgs[0]));
                }
            } catch (NumberFormatException e) {
                throw new InvalidVerifyArgument(argument.getName());
            }
        });

        addVerifier("max", (sender, argument, verifyName, verifyArgs, value, valueRaw) -> {
            if (verifyArgs.length != 1) {
                throw new InvalidVerifyArgument(argument.getName());
            }

            try {
                int max = Integer.parseInt(verifyArgs[0]);

                if (value.length() > max) {
                    throw new VerifyError(argument.getMessage("max_error", verifyArgs[0]));
                }
            } catch (NumberFormatException e) {
                throw new InvalidVerifyArgument(argument.getName());
            }
        });
    }

    @Override
    public String transform(CommandSender sender, CommandArgument argument, String value) throws TransformError {
        return value;
    }
}
