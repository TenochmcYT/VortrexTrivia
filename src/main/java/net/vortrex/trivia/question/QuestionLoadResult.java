package net.vortrex.trivia.question;

import java.util.List;

public record QuestionLoadResult(List<Question> questions, List<String> errors){
    public QuestionLoadResult {
        questions = List.copyOf(questions);
        errors = List.copyOf(errors);
    }
}