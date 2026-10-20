import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/*
 * TASK 3: ARTIFICIAL INTELLIGENCE CHATBOT
 *
 * Features:
 * 1. Interactive communication
 * 2. Basic NLP techniques
 * 3. Rule-based AI logic
 * 4. Frequently Asked Questions
 * 5. GUI for real-time interaction
 * 6. Chat history
 */

/* ---------- CHATBOT CLASS ---------- */
class ChatBot {

    private Map<String, String> responses;

    public ChatBot() {
        responses = new HashMap<>();
        trainBot();
    }

    /* ---------- TRAIN THE BOT ---------- */
    private void trainBot() {

        responses.put("hello",
                "Hello! How can I help you?");

        responses.put("hi",
                "Hi! Nice to meet you.");

        responses.put("hey",
                "Hey! How can I assist you?");

        responses.put("how are you",
                "I am fine. Thanks for asking!");

        responses.put("what is your name",
                "I am Java AI Chatbot.");

        responses.put("who are you",
                "I am an AI chatbot developed using Java.");

        responses.put("what can you do",
                "I can answer frequently asked questions and "
                + "have simple conversations.");

        responses.put("java",
                "Java is a high-level, object-oriented programming language.");

        responses.put("what is java",
                "Java is a popular object-oriented programming language "
                + "used to develop desktop, web and mobile applications.");

        responses.put("what is nlp",
                "NLP stands for Natural Language Processing. "
                + "It helps computers understand human language.");

        responses.put("what is ai",
                "AI stands for Artificial Intelligence. "
                + "It enables machines to perform intelligent tasks.");

        responses.put("what is machine learning",
                "Machine Learning allows computers to learn patterns "
                + "from data and make predictions or decisions.");

        responses.put("what is chatbot",
                "A chatbot is a software application that communicates "
                + "with users using text or voice.");

        responses.put("thank you",
                "You're welcome!");

        responses.put("thanks",
                "Happy to help!");

        responses.put("bye",
                "Goodbye! Have a nice day.");

        responses.put("exit",
                "Goodbye! See you again.");
    }

    /* ---------- NLP PREPROCESSING ---------- */
    private String preprocess(String input) {

        // Convert to lowercase
        input = input.toLowerCase();

        // Remove punctuation
        input = input.replaceAll("[^a-zA-Z0-9 ]", "");

        // Remove extra spaces
        input = input.trim().replaceAll("\\s+", " ");

        return input;
    }

    /* ---------- TOKENIZATION ---------- */
    private String[] tokenize(String input) {
        return input.split(" ");
    }

    /* ---------- KEYWORD MATCHING ---------- */
    private String keywordMatch(String input) {

        if (input.contains("hello") ||
            input.contains("hi") ||
            input.contains("hey")) {

            return responses.get("hello");
        }

        if (input.contains("java")) {
            return responses.get("java");
        }

        if (input.contains("nlp")) {
            return responses.get("what is nlp");
        }

        if (input.contains("artificial intelligence") ||
            input.equals("ai")) {

            return responses.get("what is ai");
        }

        if (input.contains("machine learning")) {
            return responses.get("what is machine learning");
        }

        if (input.contains("chatbot")) {
            return responses.get("what is chatbot");
        }

        return null;
    }

    /* ---------- SIMPLE SIMILARITY ---------- */
    private double similarity(String input, String question) {

        String[] inputWords = tokenize(input);
        String[] questionWords = tokenize(question);

        int match = 0;

        for (String word1 : inputWords) {

            for (String word2 : questionWords) {

                if (word1.equals(word2)) {
                    match++;
                    break;
                }
            }
        }

        if (inputWords.length == 0)
            return 0;

        return (double) match / inputWords.length;
    }

    /* ---------- AI RESPONSE ---------- */
    public String getResponse(String userInput) {

        String input = preprocess(userInput);

        if (input.isEmpty()) {
            return "Please type something.";
        }

        // Exact match
        if (responses.containsKey(input)) {
            return responses.get(input);
        }

        // NLP keyword matching
        String keywordResponse =
                keywordMatch(input);

        if (keywordResponse != null) {
            return keywordResponse;
        }

        // Similarity-based rule
        double highestScore = 0;
        String bestResponse = null;

        for (Map.Entry<String, String> entry :
                responses.entrySet()) {

            double score =
                    similarity(input, entry.getKey());

            if (score > highestScore) {
                highestScore = score;
                bestResponse = entry.getValue();
            }
        }

        if (highestScore >= 0.5) {
            return bestResponse;
        }

        return "Sorry, I don't understand that question. "
                + "Please ask another question.";
    }
}


/* ---------- GUI CLASS ---------- */
public class AIChatbot extends JFrame {

    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private JButton clearButton;

    private ChatBot bot;

    public AIChatbot() {

        bot = new ChatBot();

        setTitle("AI Chatbot");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createGUI();

        botMessage(
                "Hello! I am your AI Chatbot.\n"
                + "Ask me anything about AI, Java, NLP, "
                + "Machine Learning or Chatbots."
        );
    }

    /* ---------- CREATE GUI ---------- */
    private void createGUI() {

        chatArea = new JTextArea();

        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(
                new Font("Arial", Font.PLAIN, 15)
        );

        JScrollPane scrollPane =
                new JScrollPane(chatArea);

        inputField = new JTextField();

        inputField.setFont(
                new Font("Arial", Font.PLAIN, 15)
        );

        sendButton = new JButton("Send");
        clearButton = new JButton("Clear");

        JPanel bottomPanel =
                new JPanel(new BorderLayout(5, 5));

        bottomPanel.add(
                inputField,
                BorderLayout.CENTER
        );

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(sendButton);
        buttonPanel.add(clearButton);

        bottomPanel.add(
                buttonPanel,
                BorderLayout.EAST
        );

        setLayout(new BorderLayout(10, 10));

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(
                e -> sendMessage()
        );

        inputField.addActionListener(
                e -> sendMessage()
        );

        clearButton.addActionListener(
                e -> chatArea.setText("")
        );
    }

    /* ---------- SEND MESSAGE ---------- */
    private void sendMessage() {

        String message =
                inputField.getText().trim();

        if (message.isEmpty())
            return;

        userMessage(message);

        String response =
                bot.getResponse(message);

        botMessage(response);

        inputField.setText("");

        if (message.equalsIgnoreCase("bye") ||
            message.equalsIgnoreCase("exit")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Chat ended. Thank you!"
            );
        }
    }

    /* ---------- DISPLAY USER MESSAGE ---------- */
    private void userMessage(String message) {

        chatArea.append(
                "You: " + message + "\n"
        );
    }

    /* ---------- DISPLAY BOT MESSAGE ---------- */
    private void botMessage(String message) {

        chatArea.append(
                "Bot: " + message + "\n\n"
        );
    }

    /* ---------- MAIN METHOD ---------- */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            AIChatbot chatbot =
                    new AIChatbot();

            chatbot.setVisible(true);
        });
    }
}