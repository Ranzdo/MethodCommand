package se.ranzdo.bukkit.methodcommand;

import com.google.common.collect.Maps;
import org.bukkit.command.CommandSender;

import java.util.Map;
import java.util.Map.Entry;

public abstract class ArgumentHandler<T> {
    CommandHandler handler;
    private Map<String, ArgumentVerifier<T>> verifiers    = Maps.newHashMap();
    private Map<String, Variable>            vars         = Maps.newHashMap();
    private Map<String, String>              messageNodes = Maps.newHashMap();

    public ArgumentHandler() {
        //Default messages
        setMessage("include_error", "[%p] has an invalid value.");
        setMessage("exclude_error", "[%p] has an invalid value.");
        setMessage("cant_as_console", "You can't do this as console.");

        //Default verifiers
        addVerifier("include", (sender, argument, verifyName, verifyArgs, value, valueRaw) -> {
            for (String include : verifyArgs) {
                try {
                    if (transform(sender, argument, include) != value) throw new VerifyError(
                            argument.getMessage("include_error", valueRaw)
                    );
                } catch (TransformError e) {
                    throw new IllegalArgumentException(
                            "Could not transform the verify" + " argument " + include,
                            e
                    );
                }
            }
        });

        addVerifier("exclude", (sender, argument, verifyName, verifyArgs, value, valueRaw) -> {
            for (String exclude : verifyArgs) {
                try {
                    if (transform(sender, argument, exclude) == value) throw new VerifyError(
                            argument.getMessage("exclude_error", valueRaw)
                    );
                } catch (TransformError e) {
                    throw new IllegalArgumentException(
                            "Could not transform the verify" + " argument " + exclude,
                            e
                    );
                }
            }
        });
    }

    public final void addVariable(String varName, String userFriendlyName, ArgumentVariable<T> var) {
        if (verifierExists(varName)) throw new IllegalArgumentException(
                "A variable with the name " + varName + " does already exist."
        );

        vars.put(varName, new Variable(userFriendlyName, var));
    }

    public final void addVerifier(String name, ArgumentVerifier<T> verify) {
        if (verifierExists(name)) throw new IllegalArgumentException(
                "A verifier with the name " + name + " does already exist."
        );

        verifiers.put(name, verify);
    }

    public final CommandHandler getCommandHandler() {
        return handler;
    }

    public final String getMessage(String node) {
        return messageNodes.get(node);
    }

    public final ArgumentVariable<T> getVariable(String varName) {
        return vars.get(varName) == null ? null : vars.get(varName).variable;
    }

    public final String getVariableUserFriendlyName(String varName) {
        return vars.get(varName) == null ? null : vars.get(varName).userFriendlyName;
    }

    public final ArgumentVerifier<T> getVerifier(String argName) {
        return verifiers.get(argName);
    }

    final T handle(CommandSender sender, CommandArgument argument, String arg) throws CommandError {
        if (arg == null) {
            return null;
        }

        T transformed;

        if (arg.startsWith("?")) {
            String              varName = arg.substring(1, arg.length());
            ArgumentVariable<T> var     = getVariable(varName);

            if (var == null) throw new IllegalArgumentException(
                    "The ArgumentVariable '" + varName + "' is not registered."
            );

            transformed = var.var(sender, argument, varName);
        } else if (arg.matches("^\\\\+\\?.*$")) {
            arg = arg.substring(1, arg.length());
            transformed = transform(sender, argument, arg);
        } else {
            transformed = transform(sender, argument, arg);
        }

        for (Entry<String, String[]> verifier : argument.getVerifyArguments().entrySet()) {
            ArgumentVerifier<T> v = this.verifiers.get(verifier.getKey());
            if (v == null) {
                throw new VerifierNotRegistered(verifier.getKey());
            }

            v.verify(sender, argument, verifier.getKey(), verifier.getValue(), transformed, arg);
        }

        return transformed;
    }

    public final void setMessage(String node, String def) {
        messageNodes.put(node, def);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ArgumentHandler -> ").append(getClass().getName()).append("\n");
        sb.append("Set messages: \n");

        for (Entry<String, String> entry : messageNodes.entrySet()) {
            sb.append(entry.getKey()).append(" = \"").append(entry.getValue()).append("\";\n");
        }

        sb.append("\nAvailable verifiers: \n");

        for (Entry<String, ArgumentVerifier<T>> entry : verifiers.entrySet()) {
            sb.append(entry.getKey()).append(" = \"").append(entry.getValue().getClass().getName()).append("\";\n");
        }

        sb.append("\nAvailable variables: \n");

        for (Entry<String, Variable> entry : vars.entrySet()) {
            sb.append(entry.getKey()).append(" = \"").append(entry.getValue().userFriendlyName).append("\";\n");
        }

        return sb.toString();
    }

    public abstract T transform(CommandSender sender, CommandArgument argument, String value) throws TransformError;

    public final boolean variableExists(String varName) {
        return vars.get(varName) != null;
    }

    public final boolean verifierExists(String argName) {
        return verifiers.get(argName) != null;
    }

    private final class Variable {
        String              userFriendlyName;
        ArgumentVariable<T> variable;

        Variable(String userFriendlyName, ArgumentVariable<T> variable) {
            this.userFriendlyName = userFriendlyName;
            this.variable = variable;
        }
    }
}
