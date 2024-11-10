package GUI;

import Service.FFile;
import Service.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;

public class TextEditorGUI {
    private JFrame frame;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private FFile currentFile;
    private JFrame mainFrame;  // Reference to the main GUI frame
    private JPanel logSidePanel;
    private String currentScope = "Test"; // Default scope, can switch between Test/Production
    private int currentVersion = 1; // Default starting version

    private JTextField versionScopeField;

    public TextEditorGUI(FFile file, JFrame mainFrame) {
        this.currentFile = file;
        this.mainFrame = mainFrame;

        // Create the frame for the text editor
        frame = new JFrame("Text Editor - " + file.filename);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the frame

        // Set the layout
        frame.setLayout(new BorderLayout());

        // Create a JTextArea for text editing
        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        // Add a scroll pane to the text area
        scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);


        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);


        JButton saveButton = new JButton("Save");
        JButton exitButton = new JButton("Exit");
        JButton toggleScopeButton = new JButton("Switch Scope");
        JButton updateVersionButton = new JButton("Update Version");
        JButton deleteVersionButton = new JButton("Delete Version");
        JButton changeVersionButton = new JButton("Change Version");


        toolBar.add(saveButton);
        toolBar.add(exitButton);
        toolBar.add(toggleScopeButton);
        toolBar.add(updateVersionButton);
        toolBar.add(deleteVersionButton);
        toolBar.add(changeVersionButton);


        frame.add(toolBar, BorderLayout.NORTH);


        JPanel versionScopePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        versionScopeField = new JTextField(20);
        versionScopeField.setEditable(false);
        versionScopeField.setHorizontalAlignment(JTextField.RIGHT);
        versionScopeField.setFont(new Font("Arial", Font.PLAIN, 12));
        updateVersionScopeDisplay();

        versionScopePanel.add(versionScopeField);
        frame.add(versionScopePanel, BorderLayout.SOUTH);
        logSidePanel=createLogSidePanel(currentVersion,currentScope);
        frame.add(logSidePanel,BorderLayout.EAST);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String[] options = {"Save as Same Version", "Save as New Version"};
                int choice = JOptionPane.showOptionDialog(frame,
                        "Would you like to save as the same version or a new version?",
                        "Save Options",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                        null, options, options[0]);

                if (choice == 0) {

                    saveFileAsSameVersion();
                } else if (choice == 1) {

                    saveFileAsNewVersion();
                }
            }
        });


        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeEditor();
            }
        });


        toggleScopeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleScope();
            }
        });


        updateVersionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateVersion();
            }
        });


        deleteVersionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteVersion();
            }
        });


        changeVersionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeVersion();
            }
        });


        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeEditor();
            }
        });


        loadFile();
        updateLogSidePanelContent(logSidePanel,currentVersion,currentScope);

        frame.setVisible(true);
    }

    private JPanel createLogSidePanel(int versionNumber, String scope) {
        logSidePanel = new JPanel();
        logSidePanel.setLayout(new BoxLayout(logSidePanel, BoxLayout.Y_AXIS));
        logSidePanel.setBorder(BorderFactory.createTitledBorder("Logs for Version " + versionNumber));
        logSidePanel.setPreferredSize(new Dimension(300, frame.getHeight()));


        logSidePanel.setAlignmentX(Component.RIGHT_ALIGNMENT);


        updateLogSidePanelContent(logSidePanel, versionNumber, scope);

        return logSidePanel;
    }
    private void updateLogSidePanelContent(JPanel logPanel, int versionNumber, String scope) {

        logPanel.removeAll();
        logPanel.setBorder(BorderFactory.createTitledBorder("Logs for Version " + versionNumber));


        List<Log> logs = currentFile.getLogs(versionNumber, scope);
        if (logs.isEmpty()) {
            JLabel noLogsLabel = new JLabel("No logs available.");
            logPanel.add(noLogsLabel);
        } else {
            for (Log log : logs) {
                String logLabelText = "<html><strong>Version:</strong> " + log.versionNumber + "<br>"
                        + "<strong>Scope:</strong> " + log.scope + "<br>"
                        + "<strong>Message:</strong> " + log.content + "<br>"
                        + "<strong>Timestamp:</strong> "
                        + "<br><strong>Date:</strong> " + DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault()).format(log.timestamp)
                        + "<br><strong>Time:</strong> " + DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault()).format(log.timestamp)
                        + "<br><br></html>";
                JLabel logLabel = new JLabel(logLabelText);
                logPanel.add(logLabel);
            }
        }


        logPanel.revalidate();
        logPanel.repaint();
    }


    private void promptForLogMessage(int versionNumber,String scope){
        int response=JOptionPane.showConfirmDialog(frame,"Do you want to add a message?","Log Entry",JOptionPane.YES_NO_OPTION);
        if(response==JOptionPane.YES_OPTION){
            String logMessage=JOptionPane.showInputDialog(frame,"Enter the log message:");
            if(logMessage!=null && !logMessage.trim().isEmpty()){
                currentFile.addLog(versionNumber,scope,logMessage);
            }
            else{
                currentFile.addLog(versionNumber,scope,"Changes made to file");
            }
        }
    }

    private void loadFile() {
        List<String> content = currentFile.getContentByVersionAndScope(currentVersion, currentScope); // Get the latest content for the selected scope
        textArea.setText(String.join("\n", content));

    }


    private void saveFileAsSameVersion() {
        promptForLogMessage(currentVersion,currentScope);
        List<String> content = List.of(textArea.getText().split("\n"));
        currentFile.saveToFile(content, currentScope,currentVersion); // Save the file as the same version
        JOptionPane.showMessageDialog(frame, "File saved as the same version.", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
        updateLogSidePanelContent(logSidePanel,currentVersion,currentScope);
    }


    private void saveFileAsNewVersion() {

        List<String> content = List.of(textArea.getText().split("\n"));
        int version = currentFile.saveToFile(content, currentScope); // Save the file and get the new version
        promptForLogMessage(version,currentScope);
        JOptionPane.showMessageDialog(frame, "File saved as new version: " + version, "Save Successful", JOptionPane.INFORMATION_MESSAGE);
        currentVersion = version;  // Update current version
        updateVersionScopeDisplay();  // Update the display for the version and scope
        updateLogSidePanelContent(logSidePanel,version,currentScope);
    }



    private void toggleScope() {
        // Available scopes
        String[] scopes = {"Test", "Production"};

        // Show a dialog to let the user select a scope
        String selectedScope = (String) JOptionPane.showInputDialog(frame,
                "Select a scope",
                "Scope Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                scopes,
                currentScope);


        if (selectedScope != null) {

            currentScope = selectedScope;
            JOptionPane.showMessageDialog(frame, "Switched to " + currentScope + " scope", "Scope Changed", JOptionPane.INFORMATION_MESSAGE);
            loadFile();
            updateVersionScopeDisplay();
            updateLogSidePanelContent(logSidePanel,currentVersion,currentScope);
        }
    }

    private void updateVersion() {

        HashSet<Integer> availableVersions = currentFile.getAllVersions(currentScope);


        if (availableVersions.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No available versions found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        String[] versionArray = availableVersions.stream().map(String::valueOf).toArray(String[]::new);


        String selectedVersion = (String) JOptionPane.showInputDialog(frame,
                "Select a version to update",
                "Update Version",
                JOptionPane.QUESTION_MESSAGE,
                null,
                versionArray,
                versionArray[0]);


        if (selectedVersion != null) {
            int versionNumber = Integer.parseInt(selectedVersion);


            List<String> currentVersionContent = currentFile.getContentByVersionAndScope(versionNumber, currentScope);
            if (currentVersionContent.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No content found for version " + versionNumber, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            JTextArea editTextArea = new JTextArea(String.join("\n", currentVersionContent));
            editTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
            editTextArea.setLineWrap(true);
            editTextArea.setWrapStyleWord(true);

            JScrollPane editScrollPane = new JScrollPane(editTextArea);
            JDialog editDialog = new JDialog(frame, "Edit Version", true);
            editDialog.setSize(600, 400);
            editDialog.setLayout(new BorderLayout());
            editDialog.add(editScrollPane, BorderLayout.CENTER);


            JPanel buttonPanel = new JPanel();
            JButton saveButton = new JButton("Save");
            JButton cancelButton = new JButton("Cancel");

            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);
            editDialog.add(buttonPanel, BorderLayout.SOUTH);

            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    String editedContent = editTextArea.getText();


                    List<String> updatedContent = List.of(editedContent.split("\n"));
                    currentFile.saveToFile(updatedContent, currentScope, versionNumber);
                    promptForLogMessage(versionNumber, currentScope);
                    updateLogSidePanelContent(logSidePanel,currentVersion,currentScope);

                    currentVersion = versionNumber;
                    updateVersionScopeDisplay();


                    editDialog.dispose();
                    JOptionPane.showMessageDialog(frame, "Version updated successfully.", "Version Updated", JOptionPane.INFORMATION_MESSAGE);
                }
            });


            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    editDialog.dispose();
                }
            });


            editDialog.setVisible(true);
        }
    }




    private void deleteVersion() {

        HashSet<Integer> availableVersions = currentFile.getAllVersions(currentScope);


        if (availableVersions.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No available versions found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        String[] versionArray = availableVersions.stream().map(String::valueOf).toArray(String[]::new);


        String selectedVersion = (String) JOptionPane.showInputDialog(frame,
                "Select a version to delete",
                "Delete Version",
                JOptionPane.QUESTION_MESSAGE,
                null,
                versionArray,
                versionArray[0]);
        if (selectedVersion != null) {
            int versionNumber = Integer.parseInt(selectedVersion);


            boolean success = currentFile.deleteVersion(versionNumber, currentScope);
            currentFile.deleteLogs(versionNumber,currentScope);

            if (success) {
                JOptionPane.showMessageDialog(frame, "Version " + versionNumber + " deleted successfully.", "Version Deleted", JOptionPane.INFORMATION_MESSAGE);
                loadFile();
                updateVersionScopeDisplay();
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to delete version " + versionNumber + ".", "Error", JOptionPane.ERROR_MESSAGE);
            }
            updateLogSidePanelContent(logSidePanel,currentVersion,currentScope);
        }
    }


    private void changeVersion() {

        HashSet<Integer> availableVersions = currentFile.getAllVersions(currentScope);

        if (availableVersions.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No available versions found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        String[] versionArray = availableVersions.stream().map(String::valueOf).toArray(String[]::new);


        String selectedVersion = (String) JOptionPane.showInputDialog(frame,
                "Select a version to view",
                "Change Version",
                JOptionPane.QUESTION_MESSAGE,
                null,
                versionArray,
                versionArray[0]);
        if (selectedVersion != null) {
            int versionNumber = Integer.parseInt(selectedVersion);


            List<String> currentVersionContent = currentFile.getContentByVersionAndScope(versionNumber, currentScope);
            if (currentVersionContent.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No content found for version " + versionNumber, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            textArea.setText(String.join("\n", currentVersionContent));
            currentVersion = versionNumber;
            updateLogSidePanelContent(logSidePanel,currentVersion,currentScope);
            updateVersionScopeDisplay();
        }
    }


    private void updateVersionScopeDisplay() {
        versionScopeField.setText("Version: " + currentVersion + " | Scope: " + currentScope);
    }


    private void closeEditor() {

        int option = JOptionPane.showConfirmDialog(frame, "Do you want to save changes before exiting?", "Exit", JOptionPane.YES_NO_CANCEL_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            saveFileAsSameVersion();// Save the file before exiting
            frame.dispose();
        } else if (option == JOptionPane.NO_OPTION) {
            frame.dispose();
        }
    }
}
