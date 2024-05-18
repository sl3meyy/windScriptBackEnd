package database;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import accountSystem.emailWindScript;

//ToDo: Implement Email sending on successful registration, not on trying to register
//ToDo: Implement Email verification
//ToDo: Make team member checking with email instead of username. Also send user an email on account Type change

public class databaseFunctions {
    static String content;

    static {
        try {
            content = new String(Files.readAllBytes(Paths.get("config.json")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static JSONObject jsonConfig = new JSONObject(content);
    static String serverIp = jsonConfig.getString("db-ip");
    static String dbName = jsonConfig.getString("db-name");
    static String accountsCollection = jsonConfig.getString("accounts-collectionName");
    static Scanner sc = new Scanner(System.in);

    public databaseFunctions() throws IOException {
    }

    public static void main(String[] args) {
        System.out.println(checkIfUserIsTeamMember("sl3mey"));
        registerWithConsole();
    }
    static void registerWithConsole(){
        System.out.println("Enter your username: ");
        String name = sc.next();
        System.out.println("Enter your password: ");
        String password = sc.next();
        System.out.println("Enter your email:");
        String email = sc.next();
        register(name, password, email);
    }
    public static void register(String username, String password, String email){
        MongoClient mongoClient = MongoClients.create(serverIp);
        MongoDatabase database = mongoClient.getDatabase(dbName);
        if(!(nameIsUed(username))){
            if(!(emailIsUed(email))){
                Document document = new Document();
                document.append("username", username);
                document.append("password", password);
                document.append("email", email);
                document.append("accountType", "normal");
                document.append("teamMember", false);

                // Dokument in die Datenbank einfügen
                database.getCollection(accountsCollection).insertOne(document);
                emailWindScript.sendEmail(email, "Registration Complete!", "Someone (hopefully you) registered with your email at WindScript. If you didn't register, please contact the Support");
            }else {
                System.out.println("Email is already in use!");
            }

        }else{
            System.out.println("Username is already in use!");
        }

    }
    public static void deleteItems(){
        MongoClient mongoClient = MongoClients.create(serverIp);
        MongoDatabase database = mongoClient.getDatabase(dbName);
        DeleteResult result = database.getCollection(accountsCollection).deleteMany(Filters.eq("age", 14)); // Löscht alle Dokumente mit dem Alter 14
        System.out.println("Anzahl der gelöschten Dokumente: " + result.getDeletedCount());
    }
    public static void readItems(){
        MongoClient mongoClient = MongoClients.create(serverIp);
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection(accountsCollection);

        // Mehrere Dokumente abrufen
        FindIterable<Document> documents = collection.find();
        for (Document document : documents) {
            System.out.println("Gefundenes Dokument: " + document);
        }
    }
    public static void scanAllDocuments(){
        MongoClient mongoClient = MongoClients.create(serverIp);
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection(accountsCollection);

        // Anzahl der Dokumente in der Kollektion zählen
        long count = collection.countDocuments();
        System.out.println("Anzahl der Dokumente in der Kollektion: " + count);
    }
    static boolean scanForSpecificThing(String category, String value){
        MongoClient mongoClient = MongoClients.create(serverIp);
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection(accountsCollection);

        // Suchkriterium definieren
        Document query = new Document(category, value);

        // Dokumente basierend auf dem Suchkriterium abrufen
        FindIterable<Document> documents = collection.find(query);

        // Überprüfen, ob Dokumente gefunden wurden
        boolean found = false;
        for (Document document : documents) {
            //System.out.println("Gefundenes Dokument: " + document);
            found = true;
        }

        return found;
    }

    static boolean nameIsUed(String name){
        MongoClient mongoClient = MongoClients.create(serverIp);
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection(accountsCollection);

        // Name, den du überprüfen möchtest
        // Filter für die Überprüfung
        Document query = new Document("username", name);

        // Anzahl der Dokumente mit diesem Namen zählen
        long count = collection.countDocuments(query);

        // Überprüfen, ob der Name bereits existiert
        if (count > 0) {
            //System.out.println("Der Name '" + nameToCheck + "' existiert bereits in der Datenbank.");
            return true;
        } else {
            //System.out.println("Der Name '" + nameToCheck + "' existiert noch nicht in der Datenbank.");
            return false;
        }
    }
    static boolean checkIfUserIsTeamMember(String username){
        MongoClient mongoClient = MongoClients.create(serverIp);
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection(accountsCollection);

        Bson filter = Filters.and(
                Filters.eq("username", username),
                Filters.eq("teamMember", true)
        );
        Document result = collection.find(filter).first();
        if (result != null) {
            return true;
        } else {
            return false;
        }
    }
    static boolean emailIsUed(String email){
        MongoClient mongoClient = MongoClients.create(serverIp);
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection(accountsCollection);

        // Name, den du überprüfen möchtest
        // Filter für die Überprüfung
        Document query = new Document("email", email);

        // Anzahl der Dokumente mit diesem Namen zählen
        long count = collection.countDocuments(query);

        // Überprüfen, ob der Name bereits existiert
        if (count > 0) {
            //System.out.println("Der Name '" + nameToCheck + "' existiert bereits in der Datenbank.");
            return true;
        } else {
            //System.out.println("Der Name '" + nameToCheck + "' existiert noch nicht in der Datenbank.");
            return false;
        }
    }
    public static boolean login(String username, String password, String email){
        if(nameIsUed(username) && emailIsUed(email) && scanForSpecificThing("password", password)){
            accountSystem.emailWindScript.sendEmail(email, "Someone logged in to your account", "Someone has successfully logged in to your account (hopefully you) If you don't know who's been this, please contact the Support and reset your password");
            return true;
        }else {
            return false;
        }
    }

}
