package se.ranzdo.bukkit.methodcommand;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class DefaultHelpHandler implements HelpHandler {
    private String formatArgument(CommandArgument argument) {
        String def = argument.getDefault();
        if (def.equals(" ")) {
            def = "";
        } else if (def.startsWith("?")) {
            String varName = def.substring(1);
            def = argument.getHandler().getVariableUserFriendlyName(varName);
            if (def == null)
                throw new IllegalArgumentException("The ArgumentVariable '" + varName + "' is not registered.");
            def = ChatColor.GOLD + " | " + ChatColor.WHITE + def;
        } else {
            def = ChatColor.GOLD + " | " + ChatColor.WHITE + def;
        }

        return ChatColor.AQUA + "[" + argument.getName() + def + ChatColor.AQUA + "] " + ChatColor.DARK_AQUA + argument.getDescription();
    }

    @Override
    public String[] getHelpMessage(RegisteredCommand command) {
        ArrayList<String> message = new ArrayList<String>();

        if (command.isSet()) {
            message.add(ChatColor.AQUA + command.getDescription());
        }

        message.add(getUsage(command));

        if (command.isSet()) {
            for (CommandArgument argument : command.getArguments()) {
                message.add(formatArgument(argument));
            }
            if (command.getWildcard() != null) {
                message.add(formatArgument(command.getWildcard()));
            }
            List<Flag> flags = command.getFlags();
            if (flags.size() > 0) {
                message.add(ChatColor.GOLD + "Flags:");
                for (Flag flag : flags) {
                    StringBuilder args = new StringBuilder();
                    for (FlagArgument argument : flag.getArguments()) {
                        args.append(" [" + argument.getName() + "]");
                    }
                    message.add("-" + flag.getIdentifier() + ChatColor.AQUA + args.toString());
                    for (FlagArgument argument : flag.getArguments()) {
                        message.add(formatArgument(argument));
                    }
                }
            }
        }


        List<RegisteredCommand> subcommands = command.getSuffixes();
        if (subcommands.size() > 0) {
            message.add(ChatColor.GOLD + "Subcommands:");
            for (RegisteredCommand scommand : subcommands) {
                message.add(scommand.getUsage());
            }
        }

        return message.toArray(new String[0]);
    }

    @Override
    public String getUsage(RegisteredCommand command) {
        StringBuilder usage = new StringBuilder();
        usage.append(command.getLabel());

        RegisteredCommand parent = command.getParent();
        while (parent != null) {
            usage.insert(0, parent.getLabel() + " ");
            parent = parent.getParent();
        }

        usage.insert(0, "/");

        if (!command.isSet())
            return usage.toString();

        usage.append(ChatColor.AQUA);

        for (CommandArgument argument : command.getArguments()) {
            usage.append(" [" + argument.getName() + "]");
        }

        usage.append(ChatColor.WHITE);

        for (Flag flag : command.getFlags()) {
            usage.append(" (-" + flag.getIdentifier() + ChatColor.AQUA);
            for (FlagArgument arg : flag.getArguments()) {
                usage.append(" [" + arg.getName() + "]");
            }
            usage.append(ChatColor.WHITE + ")");
        }

        if (command.getWildcard() != null) {
            usage.append(ChatColor.AQUA + " [" + command.getWildcard().getName() + "]");
        }

        return usage.toString();
    }
}
