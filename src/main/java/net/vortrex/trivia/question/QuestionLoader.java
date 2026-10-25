package net.vortrex.trivia.question;

import net.vortrex.trivia.config.TriviaSettings;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionLoader {

    private final TriviaSettings settings;

    public QuestionLoader(TriviaSettings settings) {
        this.settings = settings;
    }

    public QuestionLoadResult load(FileConfiguration yaml) {
        List<Question> questions = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        Map<String, Integer> seenIds = new HashMap<>();

        List<Map<?, ?>> rawList = yaml.getMapList("questions");
        if (rawList.isEmpty()) {
            errors.add("No hay preguntas configuradas.");
            return new QuestionLoadResult(questions, errors);
        }

        for (int i = 0; i < rawList.size(); i++) {
            int position = i + 1;

            Map<?, ?> raw = rawList.get(i);
            ConfigurationSection section = new MemoryConfiguration().createSection("question", raw);
            String id = section.getString("id", "").trim();

            if (id.isEmpty()) {
                errors.add("A la pregunta #" + position + " le falta el id.");
                continue;
            }

            Integer firstPosition = seenIds.putIfAbsent(id, position);
            if (firstPosition != null) {
                errors.add("El id '" + id + "' en la posición #" + position + " ya se usó en la pregunta #" + firstPosition + ".");
                continue;
            }
        }
        return new QuestionLoadResult(questions, errors);
    }
}