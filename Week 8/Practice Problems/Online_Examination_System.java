import java.util.*;

interface Question {
    String getPrompt();
    boolean isCorrect(String answer);
}

class MultipleChoiceQuestion implements Question {
    private final String prompt;
    private final String correctAnswer;

    public MultipleChoiceQuestion(String prompt, String correctAnswer) {
        this.prompt = prompt;
        this.correctAnswer = correctAnswer;
    }

    public String getPrompt() {
        return prompt;
    }

    public boolean isCorrect(String answer) {
        return correctAnswer.equalsIgnoreCase(answer);
    }
}

class TrueFalseQuestion implements Question {
    private final String prompt;
    private final boolean correctAnswer;

    public TrueFalseQuestion(String prompt, boolean correctAnswer) {
        this.prompt = prompt;
        this.correctAnswer = correctAnswer;
    }

    public String getPrompt() {
        return prompt;
    }

    public boolean isCorrect(String answer) {
        return Boolean.toString(correctAnswer).equalsIgnoreCase(answer);
    }
}

class Student {
    private final String name;
    private final Set<String> submittedExaminations = new HashSet<>();

    public Student(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean hasSubmitted(String examinationName) {
        return submittedExaminations.contains(examinationName);
    }

    public void markSubmitted(String examinationName) {
        submittedExaminations.add(examinationName);
    }
}

class Examination {
    private final String name;
    private final List<Question> questions;

    public Examination(String name, List<Question> questions) {
        this.name = name;
        this.questions = new ArrayList<>(questions);
    }

    public String getName() {
        return name;
    }

    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions);
    }
}

class Attempt {
    private final Student student;
    private final Examination examination;
    private final Map<Integer, String> answers = new HashMap<>();
    private boolean submitted;

    public Attempt(Student student, Examination examination) {
        this.student = student;
        this.examination = examination;
    }

    public void answerQuestion(int questionNumber, String answer) {
        if (submitted) {
            throw new IllegalStateException("Submitted answers cannot be changed.");
        }
        if (questionNumber < 1 || questionNumber > examination.getQuestions().size()) {
            throw new IllegalArgumentException("Invalid question number.");
        }
        answers.put(questionNumber - 1, answer);
        System.out.println("Question " + questionNumber + " answered with '" + answer + "'.");
    }

    public void submit() {
        if (submitted) {
            throw new IllegalStateException("Attempt already submitted.");
        }
        submitted = true;
        student.markSubmitted(examination.getName());
        System.out.println("Examination '" + examination.getName() + "' submitted successfully.");
        evaluate();
    }

    private void evaluate() {
        int correct = 0;
        for (int i = 0; i < examination.getQuestions().size(); i++) {
            String answer = answers.get(i);
            if (answer != null && examination.getQuestions().get(i).isCorrect(answer)) {
                correct++;
            }
        }
        System.out.println("Result for '" + examination.getName() + "' attempt: "
                + correct + "/" + examination.getQuestions().size() + " correct");
    }
}

public class Online_Examination_System {
    public static void main(String[] args) {
        Student student = new Student("Student");
        Examination exam = new Examination("Math Quiz", Arrays.asList(
                new MultipleChoiceQuestion("Question 1", "A"),
                new MultipleChoiceQuestion("Question 2", "B")
        ));

        if (student.hasSubmitted(exam.getName())) {
            System.out.println("A submitted attempt already exists.");
            return;
        }

        System.out.println("Examination '" + exam.getName() + "' started by " + student.getName() + ".");
        Attempt attempt = new Attempt(student, exam);
        attempt.answerQuestion(1, "A");
        attempt.answerQuestion(2, "C");
        attempt.submit();
    }
}
