package se.ranzdo.bukkit.methodcommand;

import org.bukkit.command.CommandSender;
import se.ranzdo.bukkit.methodcommand.annotations.Arg;
import se.ranzdo.bukkit.methodcommand.arguments.handle.ArgumentHandler;
import se.ranzdo.bukkit.methodcommand.arguments.Arguments;

import java.lang.reflect.Array;


public class WildcardArgument extends CommandArgument {
    private boolean join;

    public WildcardArgument(Arg commandArgAnnotation, Class<?> argumentClass, ArgumentHandler<?> argumentHandler, boolean join) {
        super(commandArgAnnotation, argumentClass, argumentHandler);
        this.join = join;
    }

    public WildcardArgument(String name, String description, String def, String verifiers, Class<?> argumentClass, ArgumentHandler<?> handler, boolean join) {
        super(name, description, def, verifiers, argumentClass, handler);
        this.join = join;
    }

    @Override
    public Object execute(CommandSender sender, Arguments args) throws CommandError {
        if (!args.hasNext()) {
            Object o = getHandler().handle(sender, this, getDefault().equals(" ") ? "" : getDefault());
            if (join)
                return o;
            else {
                Object array = Array.newInstance(getArgumentClass(), 1);
                Array.set(array, 0, o);
                return array;
            }
        }

        if (join) {
            StringBuilder sb = new StringBuilder();

            while (args.hasNext()) {
                sb.append(args.nextArgument()).append(" ");
            }

            return getHandler().handle(sender, this, ArgumentHandler.escapeArgumentVariable(sb.toString().trim()));
        } else {
            Object array = Array.newInstance(getArgumentClass(), args.count());

            for (int i = 0; i < args.count(); i++)
                Array.set(array, i, getHandler().handle(sender, this, ArgumentHandler.escapeArgumentVariable(args.nextArgument())));

            return array;
        }
    }

    public boolean willJoin() {
        return join;
    }
}
