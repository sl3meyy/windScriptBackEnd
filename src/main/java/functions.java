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
                System.out.println("Registration Complete!");
            }

            fwa.close();
            return true;
        }


    }

    static boolean login(String name, String password) throws IOException {
        FileReader fr = new FileReader(accountsPath+name+".txt");
        BufferedReader br = new BufferedReader(fr);

        String user = br.readLine().replace("username=", "");
        String pass = br.readLine().replace("password=", "");
        String type = br.readLine().replace("accountType=", "");


        if(user.equals(name) && pass.equals(password)){
            System.out.println("Login Successful!");
            return true;
        }else{
            System.out.println("Login didn't work!");
            return false;
        }
    }

    static void editAccountType(String name, String newType, String givenAdminPass) throws IOException {
        //Todo: Just replace Line with "accountType=" with newType. Currently, it's writing whole File new.
        FileReader fr = new FileReader(accountsPath+name+".txt");
        BufferedReader br = new BufferedReader(fr);

        String user = br.readLine();
        String pass = br.readLine();
//        FileWriter fw = new FileWriter(accountsPath+name+".txt");
//        BufferedWriter bw = new BufferedWriter(fw);
        String types = "developer,admin,tester,normal";
        if(givenAdminPass.equals("a") && types.contains(newType)){
            newType = newType.toLowerCase();



            FileWriter fw = new FileWriter(accountsPath+name+".txt", true);
            FileWriter fwa = new FileWriter(accountsPath+name+".txt");
            fwa.write("");
            fwa.close();
            fw.write(user+"\n");
            fw.write(pass+"\n");
            fw.write("accountType="+newType);


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
        }else{
            System.out.println("Wrong password or wrong typename !");


        }

    }

    static int readAccountType(String username) throws IOException {

        FileReader fr = new FileReader(accountsPath+username+".txt");
        BufferedReader br = new BufferedReader(fr);

        String user = br.readLine();
        String passw = br.readLine();

        String accType = br.readLine();
        accType = accType.replace("accountType=", "");
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
//            System.out.println("user and pass are right");
//            System.out.println("User in file: " + user);
//            System.out.println("Given user: " + name);
//            System.out.println("Password in file: " + pass);
//            System.out.println("Given passw: " + password);

            return f1.delete();
        }else{
//            System.out.println("User and pass are wrong");
//            System.out.println("User in file: " + user);
//            System.out.println("Given user: " + name);
//            System.out.println("Password in file: " + pass);
//            System.out.println("Given passw: " + password);
            return false;
        }

//        return false;
    }
}