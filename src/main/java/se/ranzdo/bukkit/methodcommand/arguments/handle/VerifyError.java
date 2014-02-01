package se.ranzdo.bukkit.methodcommand.arguments.handle;

import se.ranzdo.bukkit.methodcommand.CommandError;

public class VerifyError extends CommandError {
	private static final long serialVersionUID = 1L;

	public VerifyError(String msg) {
		super(msg);
	}

}
