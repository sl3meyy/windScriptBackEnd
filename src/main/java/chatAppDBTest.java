import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import java.util.Arrays;
import java.util.Date;

public class chatAppDBTest {
    public static void main(String[] args) {
        // Verbindung zur MongoDB-Datenbank herstellen
        ConnectionString connString = new ConnectionString("mongodb+srv://sl3mey:windScript%2F!%26@windscript.cn5b6oa.mongodb.net/");
        MongoClient mongoClient = MongoClients.create(connString);
        MongoDatabase database = mongoClient.getDatabase("chatapp");
        MongoCollection<Document> messagesCollection = database.getCollection("messages");

        // Nachricht speichern
        Document document = new Document();
        document.append("sender", "Benutzer1");
        document.append("recipient", "Benutzer2");
        document.append("content", "Hallo, wie geht es dir?");
        document.append("timestamp", new Date());
        messagesCollection.insertOne(document);

        // Freunde eines Benutzers speichern
        MongoCollection<Document> usersCollection = database.getCollection("users");
        Document user = new Document();
        user.append("username", "Benutzer1");
        user.append("friends", Arrays.asList("Benutze2", "Benutzer3")); // Liste von Dokumenten
        usersCollection.insertOne(user);

        // Verbindung schließen
        mongoClient.close();
    }
}
