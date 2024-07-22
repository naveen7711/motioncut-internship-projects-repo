import java.util.ArrayList;
import java.util.Scanner;

public class Quiz {
    private ArrayList<Question> questions;
    private int score;

    public Quiz() {
        questions = new ArrayList<>();
        score = 0;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public void conductQuiz() {
        Scanner scanner = new Scanner(System.in);  // Move scanner here
        for (Question question : questions) {
            presentQuestion(question);
            int userAnswer = getUserAnswer(scanner);  // Pass scanner as an argument
            if (question.isCorrect(userAnswer)) {
                System.out.println("Correct!");
                score++;
            } else {
                System.out.println("Wrong! The correct answer was: " + question.getOptions()[question.getCorrectAnswer()]);
            }
        }
        System.out.println("Quiz Over! Your score is: " + score + "/" + questions.size());
    }

    private void presentQuestion(Question question) {
        System.out.println(question.getQuestionText());
        String[] options = question.getOptions();
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
    }

    private int getUserAnswer(Scanner scanner) {
        int answer = -1;
        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.print("Your answer (1-4): ");
                answer = Integer.parseInt(scanner.nextLine());
                if (answer < 1 || answer > 4) {
                    throw new NumberFormatException();
                }
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
            }
        }
        return answer - 1; // Convert to 0-based index
    }
}