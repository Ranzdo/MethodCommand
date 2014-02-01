package se.ranzdo.bukkit.methodcommand.arguments.handle.defaults;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import se.ranzdo.bukkit.methodcommand.arguments.handle.ArgumentHandler;
import se.ranzdo.bukkit.methodcommand.arguments.handle.TransformError;
import se.ranzdo.bukkit.methodcommand.CommandArgument;


public class MaterialArgumentHandler extends ArgumentHandler<Material> {

    public MaterialArgumentHandler() {
        setMessage("parse_error", "The parameter [%p] is not a valid material.");
        setMessage("include_error", "There is no material named %1");
        setMessage("exclude_error", "There is no material named %1");
    }

    @Override
    public Material transform(CommandSender sender, CommandArgument argument, String value) throws TransformError {
        Material m = null;
        try {
            m = Material.getMaterial(Integer.parseInt(value));
        } catch (NumberFormatException e) {
        }

        if (m != null)
            return m;

        m = Material.getMaterial(value);

        if (m != null)
            return m;


        throw new TransformError(argument.getMessage("parse_error"));
    }
}
