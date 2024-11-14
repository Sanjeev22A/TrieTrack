package GUI;

import GitHubService.GitHub;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class GitHubGUI {
    public GitHubGUI() {
        JFrame frame = new JFrame("GitHub File/Directory Uploader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout(10, 10)); // Use BorderLayout for better control

        // Create a panel to hold the form components
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout()); // Use GridBagLayout for more control
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding around components

        // Labels and fields
        JLabel fileLabel = new JLabel("Choose a file or directory:");
        JButton fileButton = new JButton("Browse...");
        JTextField filePathField = new JTextField();
        filePathField.setEditable(false);

        JLabel repoLabel = new JLabel("GitHub Repository URL:");
        JTextField repoField = new JTextField();

        JLabel messageLabel = new JLabel("Commit Message:");
        JTextField messageField = new JTextField();

        JLabel branchLabel = new JLabel("Branch Name:");
        JTextField branchField = new JTextField();

        JButton pushButton = new JButton("Push to GitHub");

        // Add components to the panel with GridBagLayout
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(fileLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(fileButton, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(filePathField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(repoLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(repoField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(messageLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(messageField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(branchLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(branchField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2; // Make the button span two columns
        panel.add(pushButton, gbc);

        frame.add(panel, BorderLayout.CENTER);

        // File chooser for selecting file or directory
        fileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                filePathField.setText(selectedFile.getAbsolutePath());
            }
        });

        // Push button action listener
        pushButton.addActionListener(e -> {
            String filePath = filePathField.getText();
            String repoURL = repoField.getText();
            String commitMessage = messageField.getText();
            String branchName = branchField.getText();

            try {
                GitHub.push(filePath, repoURL, commitMessage, branchName);

                int response = JOptionPane.showConfirmDialog(frame, "Push to GitHub successful. Do you want to exit?", "Success", JOptionPane.INFORMATION_MESSAGE);

                if (response == JOptionPane.YES_OPTION) {
                    frame.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "An error occurred during the push: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new GitHubGUI();
    }
}
