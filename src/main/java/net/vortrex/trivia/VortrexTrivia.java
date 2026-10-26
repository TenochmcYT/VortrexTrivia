package net.vortrex.trivia;

import net.vortrex.trivia.command.TriviaCommand;
import net.vortrex.trivia.config.TriviaSettings;
import net.vortrex.trivia.message.Messages;

import net.vortrex.trivia.question.QuestionLoadResult;
import net.vortrex.trivia.question.QuestionLoader;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class VortrexTrivia extends JavaPlugin {

    private TriviaSettings settings;
    private Messages messages;

    // Cuando abre
    @Override
    public void onEnable() {
        // Config
        saveDefaultConfig();
        settings = TriviaSettings.fromConfig(getConfig(), getLogger());
        // Mensajes
        getLogger().info("Iniciado correctamente. version: " + getPluginMeta().getVersion());
        getLogger().info("Valores: puntos: " + settings.defaultPoints() + " tiempo: " + settings.defaultTimeLimitSeconds() + "s");
        messages = new Messages(this);
        messages.load();
        // Cargador de preguntas
        File questionsFile = new File(getDataFolder(), "questions.yml");
        if (!questionsFile.exists()) {
            saveResource("questions.yml", false);
        }
        YamlConfiguration questionsYaml = YamlConfiguration.loadConfiguration(questionsFile);
        QuestionLoader loader = new QuestionLoader(settings);
        QuestionLoadResult result = loader.load(questionsYaml);

        for (String error : result.errors()) {
            getLogger().warning(error);
        }
        getLogger().info("Problemas encontrados en las preguntas: " + result.errors().size());
        getLogger().info("Preguntas válidas cargadas: " + result.questions().size());

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

}
