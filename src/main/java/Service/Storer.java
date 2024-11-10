package Service;

import java.io.Serializable;
import java.util.List;

public class Storer implements Serializable {
    String filename;
    String path;
    FileDS fileDS;
    String dependency;
    List<Log> logs;  // New field added

    public Storer(String filename, String path, FileDS fileDS, String dependency, List<Log> logs) {
        this.filename = filename;
        this.path = path;
        this.fileDS = fileDS;
        this.dependency = dependency;
        this.logs = logs;  // Initialize the logs field
    }

    // Getters and Setters
    public String getFilename() {
        return filename;
    }

    public String getPath() {
        return path;
    }

    public FileDS getFileDS() {
        return fileDS;
    }

    public String getDependency() {
        return dependency;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

    @Override
    public String toString() {
        return "Storer{" +
                "filename='" + filename + '\'' +
                ", path='" + path + '\'' +
                ", dependency='" + dependency + '\'' +
                ", logs=" + logs +
                '}';
    }
}
