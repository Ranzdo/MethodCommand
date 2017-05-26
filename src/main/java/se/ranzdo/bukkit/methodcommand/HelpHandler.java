package se.ranzdo.bukkit.methodcommand;

public interface HelpHandler {
    String[] getHelpMessage(RegisteredCommand command);

    String getUsage(RegisteredCommand command);
}
