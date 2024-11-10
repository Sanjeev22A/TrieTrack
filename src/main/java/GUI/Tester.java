package GUI;

import java.io.File;

public class Tester {
    public static void main(String[] args) {
        // Get the current working directory
        String currentWorkingDir = System.getProperty("user.dir");
        System.out.println("Current Working Directory: " + currentWorkingDir);

        // Move up one directory
        File oneDirectoryUp = new File(currentWorkingDir).getParentFile();
        if (oneDirectoryUp != null) {
            System.out.println("One Directory Up: " + oneDirectoryUp.getAbsolutePath());
            printFilesInDirectory(oneDirectoryUp);
        }

        // Move up two directories
        File twoDirectoriesUp = oneDirectoryUp != null ? oneDirectoryUp.getParentFile() : null;
        if (twoDirectoriesUp != null) {
            System.out.println("Two Directories Up: " + twoDirectoriesUp.getAbsolutePath());
            printFilesInDirectory(twoDirectoriesUp);
        }
    }

    // Helper method to print files in a given directory
    private static void printFilesInDirectory(File directory) {
        if (directory.exists() && directory.isDirectory()) {
            System.out.println("Files in " + directory.getAbsolutePath() + ":");
            File[] files = directory.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    System.out.println(file.getName());
                }
            } else {
                System.out.println("No files found in this directory.");
            }
        } else {
            System.out.println("The provided path is not a directory or doesn't exist.");
        }
    }
}
