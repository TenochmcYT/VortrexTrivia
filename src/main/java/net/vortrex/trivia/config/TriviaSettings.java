package net.vortrex.trivia.config;

import org.bukkit.configuration.file.FileConfiguration;
import java.util.logging.Logger;

public record TriviaSettings(int defaultTimeLimitSeconds, int defaultPoints, int maxQuestionLength,
                             int maxAnswerLength) {

    public static TriviaSettings fromConfig(FileConfiguration config, Logger logger) {
        int defaultTimeLimit = 20;
        int defaultPoints = 10;
        int defaultQuestioinLength = 120;
        int defaultAnswerLength = 50;
        int limitTimeQuestions = config.getInt("questions.default-time-limit-seconds", defaultTimeLimit);
        int pointsGiven = config.getInt("questions.default-points", defaultPoints);
        int maxLengthQuestion = config.getInt("questions.max-question-length", defaultQuestioinLength);
        int maxLengthAnswer = config.getInt("questions.max-answer-length", defaultAnswerLength);
        if (limitTimeQuestions < 15) {
            logger.warning("questions.default-time-limit-seconds debe ser 15 o más. Recibí: " + limitTimeQuestions + ". Uso 20.");
            limitTimeQuestions = defaultTimeLimit;
        }
        if (pointsGiven < 1) {
            logger.warning("questions.default-points debe ser 1 o más. Recibí: " + pointsGiven + ". Uso 10.");
            pointsGiven = defaultPoints;
        }
        if (maxLengthQuestion < 5) {
            logger.warning("questions.max-question-length debe ser 5 o más. Recibí: " + maxLengthQuestion + ". Uso 120.");
            maxLengthQuestion = defaultQuestioinLength;
        }
        if (maxLengthAnswer < 1) {
            logger.warning("questions.max-answer-length debe ser 1 o más. Recibí: " + maxLengthAnswer + ". Uso 50.");
            maxLengthAnswer = defaultAnswerLength;
        }

        return new TriviaSettings(limitTimeQuestions, pointsGiven, maxLengthQuestion, maxLengthAnswer);
    }


}
