import java.io.*;
import java.util.Scanner;

public class functions {


    static String accountsPath = "accounts/";


    static final String usernameVariable = "username=";
    static final String passwordVariable = "password=";
    static final String accountVariable = "accountType=";

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Do you want to [R]egister or [L]ogin or [D]elete an account ? Or test [C]ange account type");
        String choice = sc.next();
        choice = choice.toLowerCase();

        switch(choice){
            case "r":
                System.out.println("Enter a username");
                String username = sc.next();
                System.out.println("Enter a password!");
                String pw = sc.next();
                register(username, pw);
                break;
            case "l":
                System.out.println("Enter your username");
                String usern = sc.next();
                System.out.println("Enter your password");
                String passw = sc.next();
                login(usern, passw);
                break;
            case "d":
                System.out.println("Enter your username");
                String user = sc.next();
                System.out.println("Enter your password");
                String pass = sc.next();
                if (deleteAccount(user, pass)) {
                    System.out.println("Account has been deleted Successfully!");
                }else {
                    System.out.println("Account hasn't been deleted, try again later!");
                }
                break;
            case "c":
                System.out.println("Enter username");
                String userna = sc.next();
                System.out.println("Enter the new Account type");
                String newT = sc.next();
                System.out.println("Enter the Admin Password!");
                String pwa = sc.next();
                pwa.replace(" ", "");

                editAccountType(userna, newT, pwa);
                break;
        }


    }


    //Google Gemini Start
    static boolean register(String name, String password) throws IOException {
        return registerr(name, password, "normal");
    }
    //Google Gemini Ende

    static boolean registerr(String name, String password, String accountType) throws IOException {
        new File("accounts").mkdir();

        File f1 = new File(accountsPath+name+".txt");
        if(f1.isFile()){
            System.out.println("Username is already taken!");
            return false;
        }else{
            f1.createNewFile();
            FileWriter fwa = new FileWriter(accountsPath+name+".txt", true);
            FileWriter fw = new FileWriter(accountsPath+name+".txt", false);

            if(name.equals(password)) {
                System.out.println("Username and password are equal");
                fw.write("");
                fwa.write("Same User And Password");
                return false;
            }
            else{
                fw.write("");
                fwa.write("username="+name+"\n");
                fwa.write("password="+password+"\n");
                fwa.write("accountType="+accountType.toLowerCase()+"\n");
                fwa.write("hasBoughtGame=false"); //Todo, actually implement this     Server should have a special input for buying the game, user has to be logged in tho
                System.out.println("Registration Complete!");
            }

            fwa.close();
            return true;
        }

    }
    static boolean buyGame(String username) throws IOException {
        FileReader fr = new FileReader(accountsPath+username+".txt");
        BufferedReader br = new BufferedReader(fr);
        FileWriter fw = new FileWriter(accountsPath+username+".txt", true);

        String user = br.readLine();
        String pw = br.readLine();
        String accType = br.readLine();
        String hasBoughtGame = br.readLine();
        
        if(hasBoughtGame.equals("true") || hasBoughtGame.equals("team")){
            return false;
        } else if (hasBoughtGame.equals("false")) {
            //Todo: Make paying function and return true if it's successful
            fw.write(user);
            fw.write(pw);
            fw.write(accType);
            fw.write("hasBoughtGame=true");

            return true;
        }else{
            fr.close();
            return false;
        }
    }

    static boolean login(String name, String password) throws IOException {
        FileReader fr = new FileReader(accountsPath+name+".txt");
        BufferedReader br = new BufferedReader(fr);

        String user = br.readLine().replace("username=", "");
        String pass = br.readLine().replace("password=", "");
        String type = br.readLine().replace("accountType=", "");
        String hasGame = br.readLine().replace("hasBoughtGame=", "");

        if(user.equals(name) && pass.equals(password) && hasGame != "false" && hasGame != "ERROR"){
            System.out.println("Login Successful!");
            return true;
        }else{
            System.out.println("Login didn't work!");
            return false;
        }
    }

    static boolean editAccountType(String name, String newType, String givenAdminPass) throws IOException {
        //Todo: Just replace Line with "accountType=" with newType. Currently, it's writing whole File new.
        File f1 = new File(accountsPath+name+".txt");
        FileReader fr = new FileReader(accountsPath+name+".txt");
        BufferedReader br = new BufferedReader(fr);
        if(f1.exists()){

            String user = br.readLine();
            String pass = br.readLine();
            String acc = br.readLine();
            String hasBought = br.readLine();
            hasBought=hasBought.replace("hasBoughtGame=", "");
//        FileWriter fw = new FileWriter(accountsPath+name+".txt");
//        BufferedWriter bw = new BufferedWriter(fw);
            String types = "developer,admin,tester,normal";
            if(givenAdminPass.equals("windScript/!&") && types.contains(newType)){
                newType = newType.toLowerCase();

                //if hasBought=team und rang wird auf newtype == normal oder so dann hasbought == false uw

                FileWriter fw = new FileWriter(accountsPath+name+".txt", true);
                FileWriter fwa = new FileWriter(accountsPath+name+".txt");
                fwa.write("");
                fwa.close();
                fw.write(user+"\n");
                fw.write(pass+"\n");
                fw.write("accountType="+newType+"\n");
                if(newType.equals("developer") || newType.equals("admin") || newType.equals("tester")){
                    //Make hasbought == true, but not forever, make another variable just for this, because if the person looses the account type developer for example, and hasn't bought the game before, the person has to buy it!

                    if(hasBought.equals("true")){
                        fw.write("hasBoughtGame=team-true");
                    } else if (hasBought.equals("false")) {
                        fw.write("hasBoughtGame=team-false");
                    }else{
                        fw.write("hasBoughtGame=ERROR"+hasBought);
                    }
                }
                if (newType.equals("normal")) {
                    fwa.write("");
                    fwa.close();
                    fw.write(user);
                    fw.write(pass);
                    fw.write("accountType=normal");
                    if(hasBought.equals("team-true")){
                        fw.write("hasBoughtGame=true");
                    } else if (hasBought.equals("team-false")) {
                        fw.write("hasBoughtGame=false");
                    }
                }


                fw.close();
                br.close();
                fr.close();
                System.out.println("Account type changed successfully!");
//            String user = br.readLine();
//            String pass = br.readLine();
//
//            FileWriter fw2 = new FileWriter(accountsPath+name+".txt");
//            fw2.write("");
//            fw2.close();
//            FileWriter fw = new FileWriter(accountsPath+name+".txt", true);
//
//
//            fw.write(user+"\n");
//            fw.write(pass+"\n");
//            fw.write("accountType="+newType+"\n");
//
//
//            fw.close();
//            fr.close();
                return true;
            }else{
                System.out.println("Wrong password or wrong typename !");
                return false;
            }
        }else {
            System.out.println("User doesn't exist");
            return false;
        }

    }

    static int readAccount(String username) throws IOException {

        FileReader fr = new FileReader(accountsPath+username+".txt");
        BufferedReader br = new BufferedReader(fr);

        String user = br.readLine();
        String passw = br.readLine();

        String accType = br.readLine();
        String hasBoughtGame = br.readLine();
        if(hasBoughtGame == null){
            return 999;
        }
        hasBoughtGame = hasBoughtGame.replace("hasBoughtGame=", "");
        accType = accType.replace("accountType=", "");
        if(hasBoughtGame.equals("true")){
            switch(accType){
                case "developer":
                    return 1;
                case "admin":
                    return 2;
                case "tester":
                    return 3;
                case "normal":
                    return 4;
            }
        } else if (hasBoughtGame.contains("team")) {
            switch(accType){
                case "developer":
                    return 1;
                case "admin":
                    return 2;
                case "tester":
                    return 3;
                case "normal":
                    return 4;
            }
        } else if (hasBoughtGame.equals("false")) {
            return 999;
        }
//        switch (hasBoughtGame){
//            case "true":
//                switch(accType){
//                    case "developer":
//                        return 1;
//                    case "admin":
//                        return 2;
//                    case "tester":
//                        return 3;
//                    case "normal":
//                        return 4;
//                }
//            case "team-false":
//                switch(accType) {
//                    case "developer":
//                        return 1;
//                    case "admin":
//                        return 2;
//                    case "tester":
//                        return 3;
//                    case "normal":
//                        return 4;
//                }
//            case "team-true":
//                switch(accType) {
//                    case "developer":
//                        return 1;
//                    case "admin":
//                        return 2;
//                    case "tester":
//                        return 3;
//                    case "normal":
//                        return 4;
//                }
//            case "false":
//                return 999;
//        }

//        if(accType == "developer"){
//            return  1;
//        }else if(accType == "admin"){
//            return 2;
//        }else if(accType == "tester"){
//            return 3;
//        } else if (accType == "normal") {
//            return 4;
//        }else{
//            fr.close();
//            return 0;
//        }

        fr.close();
        return 0;

    }


    static boolean deleteAccount(String name, String password) throws IOException {
        File f1 = new File(accountsPath+name+".txt");
        FileReader fr = new FileReader(accountsPath+name+".txt");
        BufferedReader br = new BufferedReader(fr);

        String user = br.readLine();
        String pass = br.readLine();

        user = user.replace("username=", "");
        pass = pass.replace("password=","");

        if (user.equals(name) && pass.equals(password)){
            return f1.delete();
        }else{
            return false;
        }

//        return false;
    }
}