import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class updateLogic {
    public static void main(String[] args) {
        updateServer();
    }

    static void updateServer() {
        String version = getVersionFromConfig(); // Annahme: Die Version wird aus einer Konfigurationsdatei gelesen
        String latestVersion = getLatestReleaseVersion();

        if (latestVersion.equals(version)) {
            System.out.println("Server's on the newest Version");
        } else {
            System.out.println("A new version (" + latestVersion + ") is available. Updating.....");
            updateFiles();
        }
    }

    public static String getLatestReleaseVersion() {
        String apiUrl = "https://api.github.com/repos/sl3meyy/windScriptBackEnd/releases/latest";
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/vnd.github.v3+json");

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
        String downloadDir = "C:\\Users\\lukas\\downloads"; // Passe den Pfad zum Downloadverzeichnis an

        // Dateien, die heruntergeladen werden sollen
        String[] filesToDownload = {
                "windscriptbackend-"+getLatestReleaseVersion()+".jar",
                "config-"+getLatestReleaseVersion()+".json",
                "versions-"+getLatestReleaseVersion()+".json"
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
        return "0.2.7"; // Annahme: Die aktuelle Version wird statisch zurückgegeben
    }
}
