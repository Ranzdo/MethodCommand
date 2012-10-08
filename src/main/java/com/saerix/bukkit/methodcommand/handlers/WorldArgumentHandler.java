package com.saerix.bukkit.methodcommand.handlers;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.saerix.bukkit.methodcommand.ArgumentHandler;
import com.saerix.bukkit.methodcommand.ArgumentVariable;
import com.saerix.bukkit.methodcommand.CommandArgument;
import com.saerix.bukkit.methodcommand.CommandError;
import com.saerix.bukkit.methodcommand.TransformError;

public class WorldArgumentHandler extends ArgumentHandler<World> {
	public WorldArgumentHandler() {
		setMessage("world_not_found", "The world \"%1\" was not found");
		
		addVariable("sender", "The command executor", new ArgumentVariable<World>() {
			@Override
			public World var(CommandSender sender, CommandArgument argument, String varName) throws CommandError {
				if(!(sender instanceof Player))
					throw new CommandError(argument.getMessage("cant_as_console"));
				
				return ((Player)sender).getWorld();
			}
		});
	}

	@Override
	public World transform(CommandSender sender, CommandArgument argument, String value) throws TransformError {
		World world = Bukkit.getWorld(value);
		if(world == null)
			throw new TransformError(argument.getMessage("world_not_found"));
		return world;
	}
}
