import java.io.*;
import java.net.*;

public class server {
    public static void main(String[] args) throws IOException {

        // Define port number
        int port = 8051;

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
                String user = in.readLine();//KI generiert
                user = user.replace(" ", "");
                String pass = in.readLine(); //KI generiert
                pass = pass.replace(" ", "");

                int newChoice = Integer.parseInt(String.valueOf(choice));

                if (!(user.isEmpty() && pass.isEmpty())) {
                    if (newChoice == 1) {
                        if (functions.login(user, pass)) {
                            out.println("Login Successful!");
                        } else {
                            out.println("User not found!");
                        }
                    } else if (newChoice == 2) {
                        if (functions.register(user, pass)) {
                            out.println("Registration Successful!");
                        } else {
                            out.println("Username is taken!");
                        }
                    } else if (newChoice == 3) {
                        if (functions.deleteAccount(user, pass)) {
                            out.println("Account deleted!");
                        } else {
                            out.println("Account hasn't been deleted!");
                        }
                    }  else if (newChoice == 4) {
                        int type = functions.readAccountType(user);
                            if(type == 0 ){
                                System.out.println("Something went wrong!");
                                out.println("Your account is broken, please contact the support");
                                System.out.println(type);
                            }else{
                                if (type < 4){
                                    System.out.println(type);
                                    System.out.println("test version access granted!");
                                    out.println("test version access granted!");
                                } else if (type == 999) {
                                    System.out.println("You should buy the game before you can play it!");
                                    out.println("You should buy the game before you can play it!");
                                }else if(type >= 4){
                                    System.out.println("You don't have Test Version Access");
                                }

                            }

//                        }

                    } else if (newChoice == 5) {
                        String adminPW = in.readLine();
                        String newType = in.readLine();
                        if(user.equals("sl3mey") && newType != "developer"){
                            System.out.println("You can't change the owners account Type!");
                            out.println("You can't change the owners account Type!");
                        }else{
                            boolean editAccountType = functions.editAccountType(user,newType, adminPW);
                            System.out.println(editAccountType);
                            out.println(String.valueOf(editAccountType));
                        }

                    } else if (newChoice == 6) {

                        String gameType = in.readLine(); //Test or public version
                        if(gameType.equals("public")){
                            File publicVersion = new File("publicVersion.txt");
                            if(publicVersion.exists()){
                                FileReader fr = new FileReader(publicVersion);
                                out.println(fr.read());
                                fr.close();
                            }else{
                                out.println("Error");
                            }

                        }else if(gameType.equals("test")){
                            File testVersion = new File("testVersion.txt");
                            if(testVersion.exists()){
                                FileReader fr = new FileReader(testVersion);
                                out.println(fr.read());
                                fr.close();
                            }else{
                                out.println("Error");
                            }

                        }
                    }


                } else {

                    System.out.println("Received User name, or/and password are empty!");
                    break;
                }

                // Send response (optional)
                System.out.println("\nReceived Choice: " + choice + "\nReceived User: " + user + "\nReceived Password: " + pass + "\n");
                out.println("Informations Received: \nUsername: " + user + "\nPassword: " + pass + "\nChoice: " + choice);

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