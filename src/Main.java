import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        List<String> result = new ArrayList<>();
        
        // GUI
        JFrame frame = new JFrame("Phonology Checker");
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        // Input
        JLabel inputLabel = new JLabel("Input");
        JTextArea inputField = new JTextArea(5, 30);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(inputLabel, BorderLayout.NORTH);
        topPanel.add(inputField, BorderLayout.CENTER);

        frame.add(topPanel, BorderLayout.NORTH);
        // Button
        JButton button = new JButton("Check");
        frame.add(button, BorderLayout.SOUTH);
        // Output
        JTextArea output = new JTextArea(5, 30);
        output.setEditable(false);
        output.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(output, BorderLayout.CENTER);
        // Button-Logic
        button.addActionListener((ActionEvent e) -> {
            result.clear();
            String input = inputField.getText();
            String[] lines = input.split("\\R");
            for (String line : lines) {
                try {
                    Inventory.test(line);
                } catch (PhonologicalError er) {
                    result.add(er.getMessage());
                }
            }
            if (result.isEmpty()) {
                output.setText("Everything is fine!");
            } else {
                String outputText = "Errors:";
                for (String s : result) {
                    outputText += System.lineSeparator();
                    outputText += s;
                }
                    output.setText(outputText);
            }
        });
        frame.setVisible(true);
    }
}
