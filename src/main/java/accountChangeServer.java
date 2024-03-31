import java.io.*;
import java.net.*;

public class accountChangeServer {
    public static void main(String[] args) throws IOException {
        // Define port number
        int port = 8052;

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
                String user = in.readLine();//KI generiert
                String newAccountType = in.readLine();//KI generiert
                String adminPass = in.readLine(); //KI generiert


                if (!(user.isEmpty() && adminPass.isEmpty() && newAccountType.isEmpty())) {
                    functions.editAccountType(user, newAccountType, adminPass);
                } else {
                    System.out.println("Received User name, or/and password are empty!");
                    break;
                }

                // Send response (optional)

                // Close resources
                in.close();//KI generiert
                out.close();//KI generiert
                clientSocket.close();//KI generier
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }

        serverSocket.close();//KI generiert
        System.out.println("Server stopped");
    }
}
