package net.vortrex.trivia.command;

import net.vortrex.trivia.VortrexTrivia;
import net.vortrex.trivia.question.QuestionLoadResult;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class TriviaCommand implements CommandExecutor{

    private final VortrexTrivia plugin;
    public TriviaCommand(VortrexTrivia plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!sender.hasPermission("vortrextrivia.play")) {
                plugin.getMessages().send(sender, "general.no-permission");
                return true;
            }
            plugin.getMessages().send(sender, "general.working",
                    Placeholder.unparsed("version", plugin.getPluginMeta().getVersion()));
            return true;
        }

        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "validar" -> handleValidate(sender);
            default -> plugin.getMessages().send(sender, "general.unknown-subcommand");
        }
        return true;
    }

    private void handleValidate(CommandSender sender) {
        if (!sender.hasPermission("vortrextrivia.admin.validate")) {
            plugin.getMessages().send(sender, "general.no-permission");
            return;
        }

        QuestionLoadResult result = plugin.getQuestionService().reload();
        plugin.getMessages().send(sender, "validate.summary",
                Placeholder.unparsed("valid", String.valueOf(result.questions().size())),
                Placeholder.unparsed("errors", String.valueOf(result.errors().size())));

        if (result.questions().isEmpty()) {
            plugin.getMessages().send(sender, "validate.kept-previous");
        }

        if (!result.errors().isEmpty()) {
            int shown = Math.min(result.errors().size(), 10);
            for (int i = 0; i < shown; i++) {
                plugin.getMessages().send(sender, "validate.error-line",
                        Placeholder.unparsed("error", result.errors().get(i)));
            }
            if (result.errors().size() > shown) {
                plugin.getMessages().send(sender, "validate.more",
                        Placeholder.unparsed("count", String.valueOf(result.errors().size() - shown)));
            }
        } else {
            plugin.getMessages().send(sender, "validate.all-ok");
        }
    }
}
