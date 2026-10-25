package net.vortrex.trivia.command;

import net.vortrex.trivia.VortrexTrivia;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TriviaCommand implements CommandExecutor{

    private final VortrexTrivia plugin;
    public TriviaCommand(VortrexTrivia plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("vortrextrivia.sexo")){
            plugin.getMessages().send(sender, "general.no-permission");
            return true;
        }
        plugin.getMessages().send(sender, "general.working");
        return true;
    }
}
