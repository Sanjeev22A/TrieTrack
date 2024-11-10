package Database;

import Service.FileDS;
import Service.Log;
import Service.Storer;

import java.util.List;

public interface DB {

    // Method to add a Storer object to the database
    void addStorer(Storer storer);

    // Method to retrieve a Storer object by filename, path, and dependency
    Storer getStorer(String filename, String path, String dependency);

    // Method to update a Storer object in the database
    void updateStorer(String filename, String path, String dependency, FileDS newFileDS, List<Log> logs);

    // Method to delete a Storer object from the database by filename, path, and dependency
    void deleteStorer(String filename, String path, String dependency);
}
