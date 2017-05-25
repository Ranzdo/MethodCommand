package se.ranzdo.bukkit.methodcommand;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

public class CommandUtil {	
	private static Pattern verifyArgumentsPattern = Pattern.compile("^(.*?)\\[(.*?)\\]$");
	public static String escapeArgumentVariable(String var) {
		if(var == null)
			return null;
		
		if(var.matches("^\\\\*\\?.*$"))
			return "\\"+var;
		
		return var;
	}
	
	public static Map<String, String[]> parseVerifiers(String verifiers) {
		Map<String, String[]> map = new LinkedHashMap<String, String[]>();
		
		if(verifiers.equals(""))
			return map;
		
		String[] arguments = verifiers.split("\\|");
		
		for(String arg : arguments) {
			Matcher matcher = verifyArgumentsPattern.matcher(arg);
			if(!matcher.matches())
				throw new IllegalArgumentException("The argrument \""+arg+"\" is in invalid form.");
			
			List<String> parameters =  new ArrayList<String>();
			
			String sparameters = matcher.group(2);
			if(sparameters != null) {		
				for(String parameter : sparameters.split(","))
					parameters.add(parameter.trim());
			}
			
			String argName = matcher.group(1).trim();
			
			map.put(argName, parameters.toArray(new String[0]));
		}
		
		return map;
	}
	
	private static CommandMap commandMap;
	
	public static CommandMap getCommandMap() {
		if(commandMap == null) {
			commandMap = ReflectionUtil.getField(Bukkit.getServer().getPluginManager(), "commandMap");
		}
		
		return commandMap;
	}
	
	private static Constructor<PluginCommand> commandCon;
	
	public static PluginCommand createCommand(Plugin plugin, String name) {
		CommandMap map = getCommandMap();
		
		try {
			if(commandCon == null) {
				commandCon = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
				commandCon.setAccessible(true);
			}
			
			PluginCommand command = commandCon.newInstance(name, plugin);
			map.register(plugin.getName(), command);
			
			return command;
		}
		catch(Throwable ignore) {}
		
		return null;
	}
}
