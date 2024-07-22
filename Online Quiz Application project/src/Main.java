import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Quiz quiz = new Quiz();

        // Sample questions
        quiz.addQuestion(new Question("What is the capital of France?", new String[]{"Berlin", "Madrid", "Paris", "Rome"}, 2));
        quiz.addQuestion(new Question("What is 5 + 7?", new String[]{"10", "11", "12", "13"}, 2));
        quiz.addQuestion(new Question("Which planet is known as the Red Planet?", new String[]{"Earth", "Mars", "Jupiter", "Saturn"}, 1));
        quiz.addQuestion(new Question("What is the largest ocean on Earth?", new String[]{"Atlantic", "Indian", "Arctic", "Pacific"}, 3));

        quiz.conductQuiz();
    }
}