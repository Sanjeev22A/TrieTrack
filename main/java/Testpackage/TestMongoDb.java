package Testpackage;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.util.List;

public class TestMongoDb {
    public static void main(String[] args) {
        // Connection string for your MongoDB instance
        String uri = "mongodb://localhost:27017/"; // Replace with your MongoDB URI if needed

        // Create a MongoClient instance
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            // Connect to the database
            MongoDatabase database = mongoClient.getDatabase("Chatapp"); // Replace "test" with your database name

            // Print a success message
            System.out.println("Connected to the database: " + database.getName());
            List<String> collections = database.listCollectionNames().into(new java.util.ArrayList<>());
            System.out.println("Collections in the database:");
            for (String collectionName : collections) {
                System.out.println(collectionName);
            }
        } catch (Exception e) {
            // Print an error message if connection fails
            System.err.println("Failed to connect to MongoDB: " + e.getMessage());
        }
    }
}
