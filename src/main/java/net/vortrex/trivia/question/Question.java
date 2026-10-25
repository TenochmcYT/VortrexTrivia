package net.vortrex.trivia.question;

import java.util.List;

public record Question(String id, Boolean enabled, String category, Difficulty difficulty, String text, String correctAnswer, List<String> wrongAnswers, String explanation, int timeLimitSeconds, int points, int weight, List<String> tags) {

    public static final int MIN_OPTIONS = 2;
    public static final int MAX_OPTIONS = 6;

    public Question {
        wrongAnswers = java.util.List.copyOf(wrongAnswers);
        tags = java.util.List.copyOf(tags);
    }

    public int optionCount() {
        return 1 + wrongAnswers.size();
    }

    public boolean isCorrect(String option){
        return correctAnswer.equals(option);
    }
}
