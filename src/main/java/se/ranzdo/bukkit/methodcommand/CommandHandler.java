package se.ranzdo.bukkit.methodcommand;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import se.ranzdo.bukkit.methodcommand.annotations.Command;
import se.ranzdo.bukkit.methodcommand.arguments.handle.ArgumentHandler;
import se.ranzdo.bukkit.methodcommand.arguments.handle.defaults.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class CommandHandler implements CommandExecutor {

    private JavaPlugin plugin;
    private Map<Class<?>, ArgumentHandler<?>> argumentHandlers = new HashMap<Class<?>, ArgumentHandler<?>>();
    private Map<org.bukkit.command.Command, RootCommand> rootCommands = new HashMap<org.bukkit.command.Command, RootCommand>();

    private HelpHandler helpHandler;
    private PermissionHandler permissionHandler;

    private String helpSuffix = "help";

    public CommandHandler(JavaPlugin plugin) {
        this.plugin = plugin;

        setHelpHandler(new DefaultHelpHandler());
        setPermissionHandler(new DefaultPermissionHandler());

        registerArgumentHandler(String.class, new StringArgumentHandler());
        registerArgumentHandler(int.class, new IntegerArgumentHandler());
        registerArgumentHandler(double.class, new DoubleArgumentHandler());
        registerArgumentHandler(Player.class, new PlayerArgumentHandler());
        registerArgumentHandler(World.class, new WorldArgumentHandler());
        registerArgumentHandler(EntityType.class, new EntityTypeArgumentHandler());
        registerArgumentHandler(Material.class, new MaterialArgumentHandler());
    }

    @SuppressWarnings("unchecked")
    public <T> ArgumentHandler<? extends T> getArgumentHandler(Class<T> clazz) {
        return (ArgumentHandler<? extends T>) argumentHandlers.get(clazz);
    }

    public HelpHandler getHelpHandler() {
        return helpHandler;
    }

    public PermissionHandler getPermissionHandler() {
        return permissionHandler;
    }

    public <T> void registerArgumentHandler(Class<? extends T> clazz, ArgumentHandler<T> argHandler) {
        if (argumentHandlers.get(clazz) != null)
            throw new IllegalArgumentException("The is already a ArgumentHandler bound to the class " + clazz.getName() + ".");

        argumentHandlers.put(clazz, argHandler);
    }

    public void registerCommands(Object commands) {
        for (Method method : commands.getClass().getDeclaredMethods()) {
            Command commandAnno = method.getAnnotation(Command.class);
            if (commandAnno == null)
                continue;

            String[] identifiers = commandAnno.identifier().split(" ");
            if (identifiers.length == 0)
                throw new RegisterCommandMethodException(method, "Invalid identifiers");

            PluginCommand rootPcommand = plugin.getCommand(identifiers[0]);

            if (rootPcommand == null)
                throw new RegisterCommandMethodException(method, "The rootcommand (the first identifier) is not registered in the plugin.yml");

            if (rootPcommand.getExecutor() != this)
                rootPcommand.setExecutor(this);

            RootCommand rootCommand = rootCommands.get(rootPcommand);

            if (rootCommand == null) {
                rootCommand = new RootCommand(rootPcommand, this);
                rootCommands.put(rootPcommand, rootCommand);
            }

            RegisteredCommand mainCommand = rootCommand;

            for (int i = 1; i < identifiers.length; i++) {
                String suffix = identifiers[i];
                if (mainCommand.doesSuffixCommandExist(suffix)) {
                    mainCommand = mainCommand.getSuffixCommand(suffix);
                } else {
                    RegisteredCommand newCommand = new RegisteredCommand(suffix, this, mainCommand);
                    mainCommand.addSuffixCommand(suffix, newCommand);
                    mainCommand = newCommand;
                }
            }

            mainCommand.set(commands, method);
        }
    }

    public void setHelpHandler(HelpHandler helpHandler) {
        this.helpHandler = helpHandler;
    }

    public void setPermissionHandler(PermissionHandler permissionHandler) {
        this.permissionHandler = permissionHandler;
    }

    public String getHelpSuffix() {
        return helpSuffix;
    }

    public void setHelpSuffix(String suffix) {
        this.helpSuffix = suffix;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        RootCommand rootCommand = rootCommands.get(command);
        if (rootCommand == null) {
            return false;
        }

        rootCommand.execute(sender, args);

        return true;
    }
}
