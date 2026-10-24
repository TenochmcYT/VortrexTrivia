package net.vortrex.trivia.command;

import net.kyori.adventure.text.minimessage.MiniMessage;
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

        if (!sender.hasPermission("vortrextrivia.play")){
            return true;
        }
        sender.sendMessage(MiniMessage.miniMessage().deserialize("<#a855f7>VortrexTrivia</#a855f7> <dark_gray>» <gray>funcionando correctamente."));
        return true;
    }
}
