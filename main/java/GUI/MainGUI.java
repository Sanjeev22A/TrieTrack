package GUI;

import Service.FFile;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Objects;

public class MainGUI {

    public MainGUI() {

        JFrame frame = new JFrame("File Picker");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());


        ImageIcon wallpaperIcon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Wallpaper.png")));

        JLabel backgroundLabel = new JLabel(wallpaperIcon);
        backgroundLabel.setLayout(new BorderLayout());


        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);


        JLabel titleLabel = new JLabel("Select a File or Directory");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        JButton openButton = new JButton("Browse Files");
        openButton.setFont(new Font("Arial", Font.PLAIN, 18));
        openButton.setBackground(new Color(100, 149, 237));
        openButton.setForeground(Color.WHITE);
        openButton.setFocusPainted(false);
        openButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        openButton.setAlignmentX(Component.CENTER_ALIGNMENT);


        openButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

            int returnValue = fileChooser.showOpenDialog(frame);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String filename=selectedFile.getName();
                String path=selectedFile.getParent();
                System.out.println("From main GUI:"+path);
                FFile fileObj=new FFile(filename,path);
                new TextEditorGUI(fileObj, frame);
            } else {
                JOptionPane.showMessageDialog(frame, "No file selected",
                        "Selection Cancelled", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton pushButton = new JButton("Push to GitHub");
        pushButton.setFont(new Font("Arial", Font.PLAIN, 18));
        pushButton.setBackground(new Color(50, 205, 50)); // Green color for GitHub
        pushButton.setForeground(Color.WHITE);
        pushButton.setFocusPainted(false);
        pushButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        pushButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        pushButton.addActionListener(e -> {
            new GitHubGUI(); // Open the GitHub GUI for file push
        });


        JButton creatorsButton = new JButton("Creators Info");
        creatorsButton.setFont(new Font("Arial", Font.PLAIN, 18));
        creatorsButton.setBackground(new Color(255, 99, 71)); // Tomato red color
        creatorsButton.setForeground(Color.WHITE);
        creatorsButton.setFocusPainted(false);
        creatorsButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        creatorsButton.setAlignmentX(Component.CENTER_ALIGNMENT);


        creatorsButton.addActionListener(e -> {
            String info = "<html><b>Creator 1:</b><br>Name: Sanjeev A<br>Register No: 2022503057<br>Department: Computer Technology<br>Year: 3rd Year<br><br>" +
                    "<b>Creator 2:</b><br>Name: BharaniDharan S<br>Register No: 2022503055<br>Department: Computer Technology<br>Year: 3rd Year</html>";
            JOptionPane.showMessageDialog(frame, info, "Creators Information", JOptionPane.INFORMATION_MESSAGE);
        });


        panel.add(Box.createRigidArea(new Dimension(0, 40))); // Space at the top
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // Space between label and button
        panel.add(openButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // Space between buttons
        panel.add(pushButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // Space between buttons
        panel.add(creatorsButton);


        backgroundLabel.add(panel, BorderLayout.CENTER);


        frame.add(backgroundLabel);


        frame.setVisible(true);
    }


}
