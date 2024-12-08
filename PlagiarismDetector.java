package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

public class PlagiarismDetector extends JFrame {
    private JTextArea inputArea1;
    private JTextArea inputArea2;
    private JButton checkButton;
    private JLabel resultLabel;
    private JButton uploadButton1;
    private JButton uploadButton2;
    private JLabel uploadMessage1;
    private JLabel uploadMessage2;
    File file1;
    private File file2;
    private JRadioButton textOption;
    private JRadioButton fileOption;

    public PlagiarismDetector() {
        setTitle("Plagiarism Detector");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        // Set light background color for the window
        getContentPane().setBackground(new Color(245, 245, 245)); // Light gray background

        // Gradient Background Panel
        JPanel coverPanel = new JPanel(new BorderLayout());
        coverPanel.setBackground(new Color(255, 240, 245)); // Soft light pink for the title background
        JLabel coverTitle = new JLabel("Welcome to Plagiarism Detector");
        coverTitle.setFont(new Font("Serif", Font.BOLD, 40));
        coverTitle.setForeground(new Color(0, 51, 102)); // Dark blue text
        coverTitle.setHorizontalAlignment(SwingConstants.CENTER);

        // Create a cleaner button panel with smaller square buttons
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10)); // Increased rows for Guide button
        buttonPanel.setBackground(new Color(255, 240, 245));

        JButton startButton = createSquareButton("Start", new Color(173, 216, 230), Color.BLACK); // Light Sky Blue
        startButton.addActionListener(e -> {
            remove(coverPanel);
            initMainUI();
            revalidate();
            repaint();
        });

        JButton aboutUsButton = createSquareButton("About Us", new Color(173, 216, 230), Color.BLACK); // Light Sky Blue
        aboutUsButton.addActionListener(e -> showAboutUs());

        JButton guideButton = createSquareButton("Guide", new Color(173, 216, 230), Color.BLACK); // Light Sky Blue (matching Start & About Us)
        guideButton.setFont(new Font("Arial", Font.BOLD, 16));
        guideButton.addActionListener(e -> showGuide());

        JButton exitButton = createSquareButton("Exit", new Color(255, 182, 193), Color.BLACK); // Light pink exit button
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(startButton);
        buttonPanel.add(aboutUsButton);
        buttonPanel.add(guideButton);  // Added Guide button
        buttonPanel.add(exitButton);

        coverPanel.add(coverTitle, BorderLayout.CENTER);
        coverPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(coverPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initMainUI() {
        // Create radio buttons for mode selection with a larger panel
        JPanel modePanel = new JPanel();
        modePanel.setLayout(new FlowLayout());
        modePanel.setBorder(BorderFactory.createTitledBorder("Choose Input Method"));

        textOption = new JRadioButton("Text Input");
        fileOption = new JRadioButton("File Upload");

        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(textOption);
        modeGroup.add(fileOption);
        textOption.setSelected(true);

        // Increase the size of the radio buttons for better visibility
        textOption.setFont(new Font("Arial", Font.PLAIN, 18));
        fileOption.setFont(new Font("Arial", Font.PLAIN, 18));

        modePanel.add(textOption);
        modePanel.add(fileOption);
        modePanel.setPreferredSize(new Dimension(900, 100)); // Increased size for "Choose Input Method"

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel textInputPanel = new JPanel();
        textInputPanel.setLayout(new BoxLayout(textInputPanel, BoxLayout.Y_AXIS));
        textInputPanel.setBorder(BorderFactory.createTitledBorder("Enter Text"));

        inputArea1 = createTextArea("Enter first text...");
        inputArea2 = createTextArea("Enter second text...");

        textInputPanel.add(new JLabel("Text 1:"));
        textInputPanel.add(new JScrollPane(inputArea1));
        textInputPanel.add(new JLabel("Text 2:"));
        textInputPanel.add(new JScrollPane(inputArea2));

        JPanel uploadPanel = new JPanel();
        uploadPanel.setLayout(new BoxLayout(uploadPanel, BoxLayout.Y_AXIS));
        uploadPanel.setBorder(BorderFactory.createTitledBorder("Upload Documents"));

        uploadButton1 = createSquareButton("Upload File 1", new Color(173, 216, 230), Color.BLACK);
        uploadButton2 = createSquareButton("Upload File 2", new Color(173, 216, 230), Color.BLACK);

        uploadMessage1 = new JLabel("");
        uploadMessage2 = new JLabel("");

        uploadButton1.addActionListener(e -> {
            file1 = chooseFile();
            if (file1 != null) {
                uploadMessage1.setText("File 1 uploaded: " + file1.getName());
            }
        });

        uploadButton2.addActionListener(e -> {
            file2 = chooseFile();
            if (file2 != null) {
                uploadMessage2.setText("File 2 uploaded: " + file2.getName());
            }
        });

        uploadPanel.add(new JLabel("File 1:"));
        uploadPanel.add(uploadButton1);
        uploadPanel.add(uploadMessage1);
        uploadPanel.add(new JLabel("File 2:"));
        uploadPanel.add(uploadButton2);
        uploadPanel.add(uploadMessage2);

        mainPanel.add(textInputPanel);
        mainPanel.add(uploadPanel);

        checkButton = createSquareButton("Check Similarity", new Color(173, 216, 230), Color.BLACK); // Light Sky Blue
        checkButton.addActionListener(e -> performCheck());

        resultLabel = new JLabel("Similarity: ");
        resultLabel.setFont(new Font("Serif", Font.BOLD, 28)); // Increased font size
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.add(checkButton, BorderLayout.CENTER);
        resultPanel.add(resultLabel, BorderLayout.SOUTH);

        // Final layout
        add(modePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(resultPanel, BorderLayout.SOUTH);

        textOption.addActionListener(e -> {
            setTextInputEnabled(true);
            setFileInputEnabled(false);
        });

        fileOption.addActionListener(e -> {
            setTextInputEnabled(false);
            setFileInputEnabled(true);
        });
    }

    private JButton createSquareButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setPreferredSize(new Dimension(180, 40)); // Adjusted size for smaller buttons
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void showAboutUs() {
        JOptionPane.showMessageDialog(this,
                "This project is an Object-Oriented Programming (Java) project.\n" +
                        "Developed by:\nIfra Fatima\nAreesha Zubair\nHafsa Bibi\nKhizra Anwar",
                "About Us", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showGuide() {
        JOptionPane.showMessageDialog(this,
                "Guide to use Plagiarism Detector:\n\n" +
                        "- To check plagiarism using text, select 'Text Input' mode, and enter text in the provided fields.\n" +
                        "- To check plagiarism using files, select 'File Upload' mode, and upload two text files.\n" +
                        "- Click 'Check Similarity' to see the plagiarism percentage.",
                "Guide", JOptionPane.INFORMATION_MESSAGE);
    }

    private JTextArea createTextArea(String placeholder) {
        JTextArea textArea = new JTextArea(5, 20);
        textArea.setText(placeholder);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        textArea.setForeground(Color.GRAY); // Placeholder color

        textArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textArea.getText().equals(placeholder)) {
                    textArea.setText(""); // Clear placeholder when focusing
                    textArea.setForeground(Color.BLACK); // Reset text color to black
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textArea.getText().isEmpty()) {
                    textArea.setText(placeholder); // Reset placeholder if text area is empty
                    textArea.setForeground(Color.GRAY); // Set placeholder color back
                }
            }
        });

        return textArea;
    }


    private File chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    private void setTextInputEnabled(boolean enabled) {
        inputArea1.setEnabled(enabled);
        inputArea2.setEnabled(enabled);
    }

    private void setFileInputEnabled(boolean enabled) {
        uploadButton1.setEnabled(enabled);
        uploadButton2.setEnabled(enabled);
    }

    <T> String calculateTextSimilarity(T input1, T input2) {
        Set<String> set1 = new HashSet<>();
        Set<String> set2 = new HashSet<>();

        if (input1 instanceof String) {
            set1 = getWordsFromText((String) input1);
        } else if (input1 instanceof File) {
            set1 = getWordsFromFile((File) input1);
        }

        if (input2 instanceof String) {
            set2 = getWordsFromText((String) input2);
        } else if (input2 instanceof File) {
            set2 = getWordsFromFile((File) input2);
        }

        double intersectionSize = getIntersectionSize(set1, set2);
        double unionSize = getUnionSize(set1, set2);
        return String.format("%.2f", (intersectionSize / unionSize) * 100);
    }

    private Set<String> getWordsFromText(String text) {
        Set<String> words = new HashSet<>();
        String[] tokens = text.split("\\s+");
        for (String token : tokens) {
            words.add(token.toLowerCase());
        }
        return words;
    }

    private Set<String> getWordsFromFile(File file) {
        Set<String> words = new HashSet<>();
        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            String[] tokens = content.split("\\s+");
            for (String token : tokens) {
                words.add(token.toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    private double getIntersectionSize(Set<String> set1, Set<String> set2) {
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        return intersection.size();
    }

    private double getUnionSize(Set<String> set1, Set<String> set2) {
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        return union.size();
    }

    private void performCheck() {
        String result = "";
        if (textOption.isSelected()) {
            String text1 = inputArea1.getText();
            String text2 = inputArea2.getText();
            result = calculateTextSimilarity(text1, text2);
        } else if (fileOption.isSelected()) {
            if (file1 != null && file2 != null) {
                result = calculateTextSimilarity(file1, file2);
            } else {
                JOptionPane.showMessageDialog(this, "Please upload both files first.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        resultLabel.setText("Similarity: " + result + "%");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PlagiarismDetector detector = new PlagiarismDetector();
            detector.setVisible(true);
        });
    }
}
