package database;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import java.util.Random;

public class DatabasePopulator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int STRING_LENGTH = 6;

    public static String generateRandomString() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(STRING_LENGTH);
        for (int i = 0; i < STRING_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    public static void populateDatabase() {
        int count = 0;
        ConnectionString connectionString = new ConnectionString("mongodb+srv://sl3mey:windScript%2F!%26@windscript.cn5b6oa.mongodb.net/");
        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase("windscript");
        MongoCollection<Document> collection = database.getCollection("accounts");

        while (count < 500) {
            String randomString = generateRandomString();
            // Überprüfen, ob der Benutzername bereits in der Datenbank existiert
            if (collection.countDocuments(new Document("username", randomString)) == 0) {
                Document document = new Document()
                        .append("username", randomString)
                        .append("password", "password")
                        .append("email", "email")
                        .append("accountType", "normal")
                        .append("hasBoughtGame", "false")
                        .append("wave", 5);
                collection.insertOne(document);
                count++;
            }
        }
        mongoClient.close();
    }

    public static void main(String[] args) {
        populateDatabase();
    }
}
