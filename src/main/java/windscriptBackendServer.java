import accountSystem.authServer;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class windscriptBackendServer {
    //ToDo: Implement Multithreading here, and start auth-server.jar on startup (other servers in future too
    static String content;
    static String versionsContent;

    static {
        try {
            content = new String(Files.readAllBytes(Paths.get("config.json")));
            versionsContent = new String(Files.readAllBytes(Paths.get("versions.json")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static JSONObject versions = new JSONObject(versionsContent);
    static JSONObject jsonConfig = new JSONObject(content);
    static String serverIp = jsonConfig.getString("db-ip");
    static String dbName = jsonConfig.getString("db-name");
    static String accountsCollection = jsonConfig.getString("accounts-collectionName");
    static String versionsCollection = jsonConfig.getString("versions-collectionName");
    static Scanner sc = new Scanner(System.in);
    static MongoClient mongoClient = MongoClients.create(serverIp);
    static MongoDatabase database = mongoClient.getDatabase(dbName);

    static void updateVersionsOnDatabase(){
        DeleteResult result = database.getCollection(versionsCollection).deleteMany(new Document());
        Document document = new Document()
            .append("astrum-live", versions.getString("astrum-live"))
            .append("astrum-test", versions.getString("astrum-test"))
            .append("astrum-launcher", versions.getString("astrum-launcher"))
            .append("windscript-backend", versions.getString("windscript-backend"))
            .append("windscript-backend-auth", versions.getString("windscript-backend-auth"));

        database.getCollection(versionsCollection).insertOne(document);
    }

    static void runAuthServer() throws IOException, InterruptedException {
        authServer.run();
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        updateVersionsOnDatabase();
        runAuthServer();
    }
}
