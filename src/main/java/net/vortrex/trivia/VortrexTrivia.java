package net.vortrex.trivia;

import net.vortrex.trivia.command.TriviaCommand;
import net.vortrex.trivia.config.TriviaSettings;
import net.vortrex.trivia.message.Messages;
import net.vortrex.trivia.question.QuestionService;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class VortrexTrivia extends JavaPlugin {

    private TriviaSettings settings;
    private Messages messages;
    private QuestionService questionService;

    // Cuando abre
    @Override
    public void onEnable() {
        // Config
        saveDefaultConfig();
        settings = TriviaSettings.fromConfig(getConfig(), getLogger());
        // Mensajes
        messages = new Messages(this);
        messages.load();
        //preguntas
        questionService = new QuestionService(this, settings);
        questionService.reload();
        // Comandos
        Objects.requireNonNull(getCommand("trivia"), "El comando ‘trivia’ no está declarado en plugin.yml").setExecutor(new TriviaCommand(this));
    }
    // Cuando cierra
    @Override
    public void onDisable() {
        // Mensaje de cerrado
        getLogger().info("Desactivado correctamente. version: " + getPluginMeta().getVersion());
    }

    // Getters
    public TriviaSettings getSettings() {return settings;}
    public Messages getMessages() {return messages;}
    public QuestionService getQuestionService() {return questionService;}
}
