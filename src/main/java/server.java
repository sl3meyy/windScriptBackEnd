import java.io.*;
import java.net.*;

//ToDo: Make Performance profiling, performance tests for 1 hour, 2 hour and so on, without request from client, always write it to a file an
//ToDO: Also require email, username is just ingame, login and registration working with email, and send Emails to users on registration or sth like that, with Verification code


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
                String email = in.readLine();
                String user = in.readLine();//KI generiert
                String pass = in.readLine(); //KI generiert
                email = email.replace(" ", "");
                user = user.replace(" ", "");
                pass = pass.replace(" ", "");



                int newChoice = Integer.parseInt(String.valueOf(choice));

                if (!(user.isEmpty() && pass.isEmpty())) {
                    if (newChoice == 1) { //ToDo: Implement verify logic here (login)
                        if (functions.login(email, user, pass)) {
                            out.println("Login Successful!");
                            System.out.println("Login Successful!");
                        } else {
                            out.println("User not found!");
                            System.out.println("User not found!");
                        }
                    } else if (newChoice == 2) { //ToDo: Implement verify logic here (register)
                        if (functions.register(email, user, pass)) {
                            out.println("Registration Successful!");
                        } else {
                            out.println("Username is taken!");
                        }
                    } else if (newChoice == 3) { //ToDo: Implement verify logic here (account delete)
                        if (functions.deleteAccount(email, user, pass)) {
                            out.println("Account deleted!");
                        } else {
                            out.println("Account hasn't been deleted!");
                        }
                    }  else if (newChoice == 4) {
                        int type = functions.readAccount(user);
                        if(type == 0 ){
                            System.out.println("Something went wrong!");
                            out.println("Your account is broken, please contact the support");
                        }else{
                            if(type == 1){
                                out.println("developer");
                            } else if (type < 3) {
                                out.println("team");
                            } else if (type < 4){
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
                    } else if (newChoice == 6) {
                        String testDuration = in.readLine();

                    }


                } else {

                    System.out.println("Received User name, or/and password are empty!");
                    break;
                }

                // Send response (optional)
                //System.out.println("\nReceived Choice: " + choice + "\nReceived User: " + user + "\nReceived Password: " + pass + "\n");
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