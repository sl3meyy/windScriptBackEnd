package main;

import accountSystem.authServer;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class windscriptBackendServer {
    //ToDo: Implement Multithreading here, and start auth-server.jar on startup (other servers in future too)
    //ToDo: Implement checking for a variable in config.json if test versions should be downloaded. Test version release names: t-0.2.8 for example. Production/Stable = s-0.2.7
    public static void main(String[] args) throws IOException, InterruptedException {

        updateServer();
        updateVersionsOnDatabase();

    }
    static{
        File configFile = new File("config.json");
        File versionsFile = new File("versions.json");
        if(!configFile.exists() || !versionsFile.exists()){
            try {
                forceUpdateServer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
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

    public static void updateServer() throws IOException, InterruptedException {
        boolean production = jsonConfig.getBoolean("production");
        if (production) {
            String version = getVersionFromConfig(); // Annahme: Die Version wird aus einer Konfigurationsdatei gelesen
            String latestVersion = getLatestReleaseVersion();

            if (!latestVersion.equals(version)) {
                System.out.println("A new version (" + latestVersion + ") is available. Updating.....");
                updateFiles();

                String currentDirectory = System.getProperty("user.dir");
                String newFilePath = currentDirectory + File.separator + "windscriptbackend-"+getLatestReleaseVersion()+".jar";

                String os = System.getProperty("os.name").toLowerCase();
                String command;

                if (os.contains("win")) {
                    command = "cmd /c start java -jar " + newFilePath + " && exit";
                } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                    command = "xterm -e java -jar " + newFilePath + " && exit";
                } else if (os.contains("mac")) {
                    command = "open -a Terminal java -jar " + newFilePath + " && exit";
                } else {
                    // Default: just execute the JAR file
                    command = "java -jar " + newFilePath;
                }

                Process p = Runtime.getRuntime().exec(command);


                // Aktuelle Datei beenden
                System.exit(0);
            } else {
                System.out.println("Server's on the newest Version");
                runAuthServer();
            }
        } else {
            System.out.println("Non production version, auto update is disabled.");
            runAuthServer();
        }
    }

    public static void forceUpdateServer() throws IOException, InterruptedException {
                System.out.println("Installing Version:"+getLatestReleaseVersion()+ ").....");
                updateFiles();

                String currentDirectory = System.getProperty("user.dir");
                String newFilePath = currentDirectory + File.separator + "windscriptbackend-"+getLatestReleaseVersion()+".jar";

                String os = System.getProperty("os.name").toLowerCase();
                String command;

                if (os.contains("win")) {
                    command = "cmd /c start java -jar " + newFilePath + " && exit";
                } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                    command = "xterm -e java -jar " + newFilePath + " && exit";
                } else if (os.contains("mac")) {
                    command = "open -a Terminal java -jar " + newFilePath + " && exit";
                } else {
                    // Default: just execute the JAR file
                    command = "java -jar " + newFilePath;
                }

                Process p = Runtime.getRuntime().exec(command);


                // Aktuelle Datei beenden
                System.exit(0);
    }

    public static String getLatestReleaseVersion() {
        String apiUrl = "https://api.github.com/repos/sl3meyy/windScriptBackEnd/releases/latest";
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
            //conn.setRequestProperty("Authorization", "token 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");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Extrahiere die Version aus der JSON-Antwort
        String jsonResponse = response.toString();
        String latestVersion = jsonResponse.split("\"tag_name\":")[1].split(",")[0].replaceAll("\"", "").trim();

        return latestVersion;
    }

    static void updateFiles() {
        String releaseUrl = "https://github.com/sl3meyy/windScriptBackEnd/releases/latest/download/";
        String downloadDir = System.getProperty("user.dir"); // Passe den Pfad zum Downloadverzeichnis an

        // Dateien, die heruntergeladen werden sollen
        String[] filesToDownload = {
                "windscriptbackend-"+getLatestReleaseVersion()+".jar",
                "config.json",
                "versions.json"
        };

        for (String file : filesToDownload) {
            try (BufferedInputStream in = new BufferedInputStream(new URL(releaseUrl + file).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(downloadDir + "/" + file)) {

                byte[] dataBuffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
                System.out.println(file + " successfully downloaded.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Dummy-Methode, um die aktuelle Version aus einer Konfigurationsdatei zu lesen
    static String getVersionFromConfig() {
        return versions.getString("windscript-backend-auth");
    }
    static void cleanFolder(){
        File directory = new File("C:\\Users\\lukas\\downloads");
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (!file.getName().equals("versions.json") && !file.getName().equals("config-"+getLatestReleaseVersion()+".json") && !file.getName().equals("windscriptbackend-"+getLatestReleaseVersion()+".jar") ) {
                    file.delete();
                    System.out.println("Datei gelöscht: " + file.getName());
                }
            }
        } else {
            System.out.println("Das Verzeichnis ist leer oder existiert nicht.");
        }

    }

    static void run() throws IOException, InterruptedException {
        Timer timer = new Timer();
        int interval = 5000; // Intervall in Millisekunden (5 Sekunden)

        // Aufgabe, die alle 5 Sekunden ausgeführt wird
        TimerTask task = new TimerTask() {
            public void run() {
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // Planung der Aufgabe mit dem angegebenen Intervall
        timer.scheduleAtFixedRate(task, 0, interval);


    }
}
