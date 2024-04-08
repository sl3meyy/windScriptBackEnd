import java.io.*;
import java.net.*;

public class versionsServer {
    public static void main(String[] args) throws IOException {

        // Define port number
        int port = 8053;


        ServerSocket serverSocket = new ServerSocket(port);//KI generiert
        System.out.println("Server started on port " + port);

        while (true) {
            try {
                // Accept client connection
                Socket clientSocket = serverSocket.accept();//KI generiert
                System.out.println("---------------------\nClient connected\n");

                // Create input and output streams
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Receive strings from client
                String choice = in.readLine();//KI generiert


                int newChoice = Integer.parseInt(String.valueOf(choice));

                if (!(choice.isEmpty())) {
                    if(newChoice == 0){
                        FileReader fr = new FileReader("publicVersion.txt");

                        out.println("version=public-0.2");

                        fr.close();
                    }else if (newChoice == 1) {
                        FileReader fr = new FileReader("testVersion.txt");

                        out.println(fr.read());

                        fr.close();
                    }

                } else {

                    System.out.println("Received User name, or/and password are empty!");
                    break;
                }

                // Send response (optional)
                System.out.println("\nReceived Choice: " + choice);


                // Close resources
                in.close();//KI generiert
                out.close();//KI generiert
                clientSocket.close();//KI generiert
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }

        serverSocket.close();//KI generiert
        System.out.println("Server stopped");
    }

}
