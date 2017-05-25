package se.ranzdo.bukkit.methodcommand;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Arguments {
    private List<String> arguments;
    private int argCounter = 0;

    private Map<Flag, List<String>> flags       = Maps.newHashMap();
    private Map<Flag, Integer>      flagCounter = Maps.newHashMap();

    public Arguments(String[] args, Map<String, Flag> flags) throws CommandError {
        List<String> largs = Lists.newArrayList(args);

        //Find the flags
        for (Entry<String, Flag> entry : flags.entrySet()) {
            Flag flag = entry.getValue();

            int flagIndex = largs.indexOf("-" + flag.getIdentifier());

            if (flagIndex == -1) {
                continue;
            }

            largs.remove(flagIndex);

            int endIndex = flag.getArguments().size() + flagIndex;

            if (endIndex > largs.size()) throw new CommandError(
                    "The flag -" + flag.getIdentifier() + " does not have the required parameters."
            );

            flagCounter.put(flag, 0);

            List<String> flagArgs = Lists.newArrayList();
            this.flags.put(flag, flagArgs);

            for (int i = flagIndex; i < endIndex; i++) {
                flagArgs.add(largs.remove(flagIndex));
            }
        }

        //The rest is normal arguments
        arguments = largs;
    }

    public boolean flagExists(Flag flag) {
        return flags.get(flag) != null;
    }

    public boolean hasNext() {
        return argCounter < size();
    }

    public boolean hasNext(Flag flag) {
        Integer c = flagCounter.get(flag);

        return c != null && c < size(flag);
    }

    public String nextArgument() {
        String arg = arguments.get(argCounter);
        argCounter++;

        return arg;
    }

    public String nextFlagArgument(Flag flag) {
        List<String> args = flags.get(flag);

        return args == null ? null : args.get(flagCounter.put(flag, flagCounter.get(flag) + 1));
    }

    public int over() {
        return size() - argCounter;
    }

    public int over(Flag flag) {
        return size(flag) - flagCounter.get(flag);
    }

    public int size() {
        return arguments.size();
    }

    public int size(Flag flag) {
        List<String> args = flags.get(flag);

        return args == null ? 0 : args.size();
    }
}
