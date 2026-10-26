package net.vortrex.trivia.question;

import net.vortrex.trivia.config.TriviaSettings;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class QuestionService {

    private final JavaPlugin plugin;
    private final TriviaSettings settings;
    private QuestionLoadResult current = new QuestionLoadResult(List.of(), List.of());

    public QuestionService(JavaPlugin plugin, TriviaSettings settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    public QuestionLoadResult reload() {
        File file = new File(plugin.getDataFolder(), "questions.yml");
        if (!file.exists()) {
            plugin.saveResource("questions.yml", false);
        }
        YamlConfiguration questionsYaml = YamlConfiguration.loadConfiguration(file);
        QuestionLoader loader = new QuestionLoader(settings);
        QuestionLoadResult fresh = loader.load(questionsYaml);

        for (String error : fresh.errors()) {
            plugin.getLogger().warning(error);
        }

        if (fresh.questions().isEmpty()) {
            plugin.getLogger().warning("No hay preguntas válidas; se conservan las preguntas cargadas anteriormente.");
        } else {
            current = fresh;
        }

        plugin.getLogger().info("Preguntas válidas cargadas: " + current.questions().size()
                + ", activas: " + getEnabledQuestions().size()
                + ", problemas: " + fresh.errors().size());
        return fresh;
    }

    public List<Question> getQuestions() {
        return current.questions();
    }

    public List<Question> getEnabledQuestions() {
        List<Question> result = new ArrayList<>();
        for (Question question : current.questions()) {
            if (Boolean.TRUE.equals(question.enabled())) {
                result.add(question);
            }
        }
        return result;
    }
}
