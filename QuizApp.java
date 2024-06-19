import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class QuizApp {
    private JFrame frame;
    private JLabel questionLabel;
    private JButton[] optionButtons;
    private JLabel timerLabel;
    private JLabel scoreLabel;
    private int currentQuestion;
    private int score;
    private int timeLimit;
    private int timeLeft;
    private Timer timer;
    private List<Question> questions;

    public QuizApp() {
        frame = new JFrame("Quiz Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        questions = new ArrayList<>();
        questions.add(new Question("What is the capital of France?", new String[]{"London", "Berlin", "Paris", "Rome"}, "Paris"));
        questions.add(new Question("What is the highest mountain in the world?", new String[]{"K2", "Kangchenjunga", "Lhotse", "Mount Everest"}, "Mount Everest"));
        // Add more questions here

        currentQuestion = 0;
        score = 0;
        timeLimit = 30; // Time limit in seconds
        timeLeft = timeLimit;

        questionLabel = new JLabel(questions.get(currentQuestion).getQuestion());
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        frame.add(questionLabel, BorderLayout.NORTH);

        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new GridLayout(2, 2));
        optionButtons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JButton(questions.get(currentQuestion).getOptions()[i]);
            optionButtons[i].addActionListener(new OptionButtonListener());
            optionPanel.add(optionButtons[i]);
        }
        frame.add(optionPanel, BorderLayout.CENTER);

        timerLabel = new JLabel("Time: 00:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        frame.add(timerLabel, BorderLayout.EAST);

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        frame.add(scoreLabel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

        startTimer();
    }

    private void startTimer() {
        timeLeft = timeLimit; // Reset the time left for the new question
        if (timer != null) {
            timer.stop(); // Stop the previous timer if it's still running
        }
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                timerLabel.setText("Time: " + String.format("%02d:%02d", timeLeft / 60, timeLeft % 60));
                if (timeLeft <= 0) {
                    nextQuestion();
                }
            }
        });
        timer.start();
    }

    private void nextQuestion() {
        if (timer != null) {
            timer.stop(); // Stop the timer for the current question
        }
        currentQuestion++;
        if (currentQuestion < questions.size()) {
            questionLabel.setText(questions.get(currentQuestion).getQuestion());
            for (int i = 0; i < 4; i++) {
                optionButtons[i].setText(questions.get(currentQuestion).getOptions()[i]);
            }
            startTimer();
        } else {
            showResults();
        }
    }

    private void showResults() {
        for (JButton button : optionButtons) {
            button.setEnabled(false);
        }
        questionLabel.setText("Quiz finished!\nFinal Score: " + score);
        timerLabel.setText("");
    }

    private class OptionButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            String selectedOption = button.getText();
            if (selectedOption.equals(questions.get(currentQuestion).getAnswer())) {
                score++;
                scoreLabel.setText("Score: " + score);
            }
            nextQuestion();
        }
    }

    private class Question {
        private String question;
        private String[] options;
        private String answer;

        public Question(String question, String[] options, String answer) {
            this.question = question;
            this.options = options;
            this.answer = answer;
        }

        public String getQuestion() {
            return question;
        }

        public String[] getOptions() {
            return options;
        }

        public String getAnswer() {
            return answer;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new QuizApp();
            }
        });
    }
}
