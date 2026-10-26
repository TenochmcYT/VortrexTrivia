package net.vortrex.trivia.question;

import net.vortrex.trivia.config.TriviaSettings;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Locale;
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

            String text = section.getString("text", "").trim();
            String correct = section.getString("correct", "").trim();
            List<String> wrong = new ArrayList<>();
            for (String answer : section.getStringList("wrong")) {
                wrong.add(answer.trim());
            }
            String category = section.getString("category", "general").trim();
            String difficultyText = section.getString("difficulty", "MEDIUM").trim();
            String explanation = section.getString("explanation", "").trim();
            boolean enabled = section.getBoolean("enabled", true);
            int timeLimit = section.getInt("time-limit-seconds", settings.defaultTimeLimitSeconds());
            int points = section.getInt("points", settings.defaultPoints());
            int weight = section.getInt("weight", 1);
            List<String> tags = new ArrayList<>();
            for (String tag : section.getStringList("tags")) {
                tags.add(tag.trim());
            }

            List<String> problems = new ArrayList<>();
            if (text.isBlank()) problems.add("el enunciado está vacío");
            if (text.length() > settings.maxQuestionLength()) problems.add("el enunciado supera el máximo de " + settings.maxQuestionLength() + " caracteres");
            if (correct.isBlank()) problems.add("falta la respuesta correcta");
            if (correct.length() > settings.maxAnswerLength()) problems.add("la respuesta correcta supera el máximo de " + settings.maxAnswerLength() + " caracteres");
            for (String answer : wrong) {
                if (answer.isBlank()) problems.add("una respuesta incorrecta está vacía");
                if (answer.length() > settings.maxAnswerLength()) problems.add("la respuesta incorrecta '" + answer + "' supera el máximo de " + settings.maxAnswerLength() + " caracteres");
            }
            List<String> options = new ArrayList<>();
            options.add(correct);
            options.addAll(wrong);
            if (options.size() < Question.MIN_OPTIONS || options.size() > Question.MAX_OPTIONS) {
                problems.add("debe tener entre " + Question.MIN_OPTIONS + " y " + Question.MAX_OPTIONS + " opciones; tiene " + options.size());
            }
            Set<String> seenOptions = new HashSet<>();
            for (String option : options) {
                if (!option.isBlank() && !seenOptions.add(option.toLowerCase(Locale.ROOT))) {
                    problems.add("la opción '" + option + "' está repetida");
                }
            }
            Difficulty difficulty = Difficulty.MEDIUM;
            try {
                difficulty = Difficulty.valueOf(difficultyText.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                problems.add("la dificultad '" + difficultyText + "' no existe. Usa EASY, MEDIUM o HARD");
            }
            if (timeLimit < 5) problems.add("el tiempo debe ser 5 segundos o más (recibí " + timeLimit + ")");
            if (points < 1) problems.add("los puntos deben ser 1 o más (recibí " + points + ")");
            if (weight < 1) problems.add("el peso debe ser 1 o más (recibí " + weight + ")");

            if (!problems.isEmpty()) {
                for (String problem : problems) {
                    errors.add("Pregunta '" + id + "' (#" + position + "): " + problem);
                }
                continue;
            }

            questions.add(new Question(id, enabled, category, difficulty, text, correct,
                    wrong, explanation, timeLimit, points, weight, tags));
        }
        return new QuestionLoadResult(questions, errors);
    }
}