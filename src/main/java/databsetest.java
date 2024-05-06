import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;

import java.util.Random;
import java.util.Scanner;

//ToDo: Implement Login

public class databsetest {
    static String serverIp = "mongodb://localhost:27017";
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        test();
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
    static void register(String username, String password, String email){
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("accounts");
        if(!(nameIsUed(username))){
            if(!(emailIsUed(email))){
                // Einzelnes Document für Name und Alter erstellen
                Document document = new Document();
                document.append("username", username);
                document.append("password", password); // Alter als Zahl speichern
                document.append("email", email);
                document.append("accountType", "normal");
                document.append("hasBoughtGame", "false");
                document.append("wave", 5);

                // Dokument in die Datenbank einfügen
                database.getCollection("windscript").insertOne(document);
            }else {
                System.out.println("Email is already in use!");
            }

        }else{
            System.out.println("Username is already in use!");
        }

    }
    static void deleteItems(){
        MongoClient mongoClient = MongoClients.create(serverIp);
        MongoDatabase database = mongoClient.getDatabase("accounts");
        DeleteResult result = database.getCollection("windscript").deleteMany(Filters.eq("age", 14)); // Löscht alle Dokumente mit dem Alter 14
        System.out.println("Anzahl der gelöschten Dokumente: " + result.getDeletedCount());
    }
    static void readItems(){
        MongoClient mongoClient = MongoClients.create(serverIp);
        MongoDatabase database = mongoClient.getDatabase("accounts");
        MongoCollection<Document> collection = database.getCollection("windscript");

        // Mehrere Dokumente abrufen
        FindIterable<Document> documents = collection.find();
        for (Document document : documents) {
            System.out.println("Gefundenes Dokument: " + document);
        }
    }
    static void scanAllDocuments(){
        MongoClient mongoClient = MongoClients.create(serverIp);
        MongoDatabase database = mongoClient.getDatabase("accounts");
        MongoCollection<Document> collection = database.getCollection("windscript");

        // Anzahl der Dokumente in der Kollektion zählen
        long count = collection.countDocuments();
        System.out.println("Anzahl der Dokumente in der Kollektion: " + count);
    }
    static boolean scanForSpecificThing(String category, String value){
        MongoClient mongoClient = MongoClients.create(serverIp);
        MongoDatabase database = mongoClient.getDatabase("accounts");
        MongoCollection<Document> collection = database.getCollection("windscript");

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
        MongoDatabase database = mongoClient.getDatabase("accounts");
        MongoCollection<Document> collection = database.getCollection("windscript");

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
    static boolean emailIsUed(String email){
        MongoClient mongoClient = MongoClients.create(serverIp);
        MongoDatabase database = mongoClient.getDatabase("accounts");
        MongoCollection<Document> collection = database.getCollection("windscript");

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
    static boolean login(String username, String password, String email){
        if(nameIsUed(username) && emailIsUed(email) && scanForSpecificThing("password", password)){
            return true;
        }else {
            return false;
        }
    }
    public static String generateRandomString() {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        final int LENGTH = 6;
        Random random = new Random();
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }
    static void test(){
        int x = 0;
        int y = 200;
        while(x < y){
            x++;

            String randomString = generateRandomString();
            MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongoClient.getDatabase("accounts");
            // Einzelnes Document für Name und Alter erstellen
            Document document = new Document();
            document.append("username", randomString);
            document.append("password", "password"); // Alter als Zahl speichern
            document.append("email", "email");
            document.append("accountType", "normal");
            document.append("hasBoughtGame", "false");
            document.append("wave", 5);

            // Dokument in die Datenbank einfügen
            database.getCollection("windscript").insertOne(document);

        }
    }

}
