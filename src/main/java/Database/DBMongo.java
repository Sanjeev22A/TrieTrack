package Database;

import Service.FileDS;
import Service.Log;
import Service.Storer;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.BsonBinary;
import org.bson.Document;
import org.bson.types.Binary;

import java.io.*;
import java.util.List;

public class DBMongo implements DB {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017/";
    private static final String DATABASE_NAME = "VersionControlSoftware";
    private static final String COLLECTION_NAME = "file_objects";

    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public DBMongo() {
        client = MongoClients.create(CONNECTION_STRING);
        database = client.getDatabase(DATABASE_NAME);
        collection = database.getCollection(COLLECTION_NAME);

        createCompoundIndex();
    }
    //Note for creating the index as filename+path rather than otherway around- This is because filename would be searched first rather than searching for all the files in a particular directory-My heuristics for optimization
    private void createCompoundIndex() {
        collection.createIndex(new Document("filename", 1).append("path", 1).append("dependency", 1),
                new com.mongodb.client.model.IndexOptions().unique(true));
    }

    private byte[] serializeObject(Object obj) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            oos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error in serializing the object", e);
        }
    }

    private Object deserializeObject(byte[] data) {
        try (ByteArrayInputStream bios = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bios)) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error in deserializing the object", e);
        }
    }

    public void addStorer(Storer storer) {
        Document existingDoc = collection.find(Filters.and(Filters.eq("filename", storer.getFilename()),
                Filters.eq("path", storer.getPath()),
                Filters.eq("dependency", storer.getDependency()))).first();
        if (existingDoc != null) {
            updateStorer(storer.getFilename(), storer.getPath(), storer.getDependency(), storer.getFileDS(),storer.getLogs());
        } else {
            byte[] serializedData = this.serializeObject(storer);
            Document doc = new Document("filename", storer.getFilename())
                    .append("path", storer.getPath())
                    .append("dependency", storer.getDependency())
                    .append("data", new BsonBinary(serializedData));
            collection.insertOne(doc);

        }
    }

    public Storer getStorer(String filename, String path, String dependency) {
        Document doc = collection.find(Filters.and(Filters.eq("filename", filename),
                Filters.eq("path", path),
                Filters.eq("dependency", dependency))).first();
        if (doc == null) {
            return null;
        }
        Binary binaryData = doc.get("data", Binary.class);
        byte[] serializedData = binaryData.getData();

        Storer deserializedStorer = (Storer) deserializeObject(serializedData);
        System.out.println("Deserialized logs: " + deserializedStorer.getLogs());  // Debugging deserialized logs
        return deserializedStorer;
    }

    public void updateStorer(String filename, String path, String dependency, FileDS newFileDS, List<Log> logs) {
        Document doc = collection.find(Filters.and(Filters.eq("filename", filename),
                Filters.eq("path", path),
                Filters.eq("dependency", dependency))).first();
        if (doc == null) {
            throw new RuntimeException("File with the specified filename, path, and dependency does not exist.");
        }
        Storer updatedStorer = new Storer(filename, path, newFileDS, dependency,logs);
        byte[] serializedData = serializeObject(updatedStorer);

        Document updatedDoc = new Document("data", new BsonBinary(serializedData));
        collection.updateOne(Filters.and(Filters.eq("filename", filename), Filters.eq("path", path), Filters.eq("dependency", dependency)),
                new Document("$set", updatedDoc));
    }

    public void deleteStorer(String filename, String path, String dependency) {
        Document doc = collection.find(Filters.and(Filters.eq("filename", filename),
                Filters.eq("path", path),
                Filters.eq("dependency", dependency))).first();
        if (doc == null) {
            throw new RuntimeException("File with the specified filename, path, and dependency does not exist.");
        }
        collection.deleteOne(Filters.and(Filters.eq("filename", filename), Filters.eq("path", path), Filters.eq("dependency", dependency)));
    }


}
