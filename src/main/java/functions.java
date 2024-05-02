import java.io.*;
import java.lang.management.ManagementFactory;
import java.util.Scanner;
import com.sun.management.OperatingSystemMXBean;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

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
                System.out.println("Enter your email adress");
                String email = sc.next();
                int randomNumber = emailWindScript.generateRandomNumber(100000, 999999);
                emailWindScript.sendEmail(email, "Auth Code", "Your authentication code is \n\n" + randomNumber + "\n\nDon't give anyone this code");
                System.out.println("Enter authcode");
                String authcode = sc.next();
                System.out.println(authcode);
                if(String.valueOf(randomNumber).equals(authcode)){
                    register(email, username, pw);
                }else{
                    System.out.println("Error, wrong auth-code!");
                }

                break;
            case "l":
                System.out.println("Enter your username");
                String usern = sc.next();
                System.out.println("Enter your password");
                String passw = sc.next();
                System.out.println("Enter your email");
                String emaill = sc.next();
                login(emaill, usern, passw);
                break;
            case "d":
                System.out.println("Enter your username");
                String user = sc.next();
                System.out.println("Enter your password");
                String pass = sc.next();
                System.out.println("Enter your email");
                String emailll = sc.next();
                if (deleteAccount(emailll, user, pass)) {
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
            case "cpu":
                double[] avgUsage = measureResourceUsage(10);

                // Print average usage
                System.out.println("Average CPU Usage: " + avgUsage[0] + "%");
                System.out.println("Average RAM Usage: " + avgUsage[1] + "%");
                System.out.println("Average GPU Usage: " + avgUsage[2] + "%");
                break;
        }


    }


    //Google Gemini Start
    static boolean register(String email, String name, String password) throws IOException {
        return registerr(email, name, password, "normal");
    }
    //Google Gemini Ende

    static boolean registerr(String email, String name, String password, String accountType) throws IOException {
        //ToDo: Implement Email verification
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
            else {
                fw.write("");
                fwa.write("username="+name+"\n");
                fwa.write("password="+password+"\n");
                fwa.write("email="+email+"\n");
                fwa.write("accountType="+accountType.toLowerCase()+"\n");
                fwa.write("hasBoughtGame=false");
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
        String email = br.readLine();
        String accType = br.readLine();
        String hasBoughtGame = br.readLine();
        
        if(hasBoughtGame.equals("true") || hasBoughtGame.equals("team")){
            return false;
        } else if (hasBoughtGame.equals("false")) {
            //Todo: Make paying function and return true if it's successful
            fw.write(user);
            fw.write(pw);
            fw.write(email);
            fw.write(accType);
            fw.write("hasBoughtGame=true");

            return true;
        }else{
            fr.close();
            return false;
        }
    }

    static boolean login(String email, String name, String password) throws IOException {
        FileReader fr = new FileReader(accountsPath+name+".txt");
        BufferedReader br = new BufferedReader(fr);

        String user = br.readLine().replace("username=", "");
        String pass = br.readLine().replace("password=", "");
        String mail = br.readLine().replace("email=", "");
        String type = br.readLine().replace("accountType=", "");
        String hasGame = br.readLine().replace("hasBoughtGame=", "");

        if(user.equals(name) && pass.equals(password) && mail.equals(email) && hasGame != "false" && hasGame != "ERROR"){
            System.out.println("Login Successful!");
            return true;
        }else{
            System.out.println("Login didn't work!");
            return false;
        }
    }



    static boolean editAccountType(String name, String newType, String givenAdminPass) throws IOException {

        File f1 = new File(accountsPath + name + ".txt");
        FileReader fr = new FileReader(accountsPath + name + ".txt");
        BufferedReader br = new BufferedReader(fr);
        if (f1.exists()) {
            String user = br.readLine();
            String pass = br.readLine();
            String email = br.readLine();
            String accT = br.readLine();
            String hasBought = br.readLine();
            hasBought = hasBought.replace("hasBoughtGame=", "");
//        FileWriter fw = new FileWriter(accountsPath+name+".txt");
//        BufferedWriter bw = new BufferedWriter(fw);
            String types = "developer,admin,tester,normal";
            if (givenAdminPass.equals("windScript/!&") && types.contains(newType)) {
                newType = newType.toLowerCase();


                FileWriter fw = new FileWriter(accountsPath + name + ".txt", true);
                FileWriter fwa = new FileWriter(accountsPath + name + ".txt");
                fwa.write("");
                fwa.close();
                fw.write(user + "\n");
                fw.write(pass + "\n");
                fw.write(email + "\n");
                fw.write("accountType=" + newType + "\n");
                if (newType.equals("developer") || newType.equals("admin") || newType.equals("tester")) {
                    //Make hasbought == true, but not forever, make another variable just for this, because if the person looses the account type developer for example, and hasn't bought the game before, the person has to buy it!

                    if (hasBought.equals("true")) {
                        fw.write("hasBoughtGame=team-true");
                    } else if (hasBought.equals("false")) {
                        fw.write("hasBoughtGame=team-false");
                    } else {
                        fw.write("hasBoughtGame=ERROR" + hasBought);
                    }
                }
                if (newType.equals("normal")) {

                    fw.write("accountType=normal\n");
                    if (hasBought.equals("team-true")) {
                        fw.write("hasBoughtGame=true");
                    } else if (hasBought.equals("team-false")) {
                        fw.write("hasBoughtGame=false");
                    } else {
                        fw.write("hasBoughtGame=" + hasBought);
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
            } else {
                System.out.println("Wrong password or wrong typename !");
                return false;
            }
        } else {
            System.out.println("User doesn't exist");
            return false;
        }

    }

    static int readAccount(String username) throws IOException {
        //ToDo: Make team member checking with email instead of username. Also send user an email on accoun Type change
        FileReader fr = new FileReader(accountsPath+username+".txt");
        BufferedReader br = new BufferedReader(fr);

        String user = br.readLine();
        String passw = br.readLine();
        String email = br.readLine();

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

        fr.close();
        return 0;

    }


    static boolean deleteAccount(String email, String name, String password) throws IOException {
        File f1 = new File(accountsPath+name+".txt");
        FileReader fr = new FileReader(accountsPath+name+".txt");
        BufferedReader br = new BufferedReader(fr);

        String user = br.readLine();
        String pass = br.readLine();
        String mail = br.readLine();
        user = user.replace("username=", "");
        pass = pass.replace("password=","");

        if (user.equals(name) && pass.equals(password) && mail.equals(email)){
            return f1.delete();
        }else{
            return false;
        }

//        return false;
    }
    public static double[] measureResourceUsage(int durationInSeconds) {
        double[] avgUsage = new double[3]; // Index 0: CPU, Index 1: RAM, Index 2: GPU

        // Start time
        long startTime = System.currentTimeMillis();

        // Variables to store cumulative usage
        double totalCpuUsage = 0.0;
        double totalRamUsage = 0.0;
        double totalGpuUsage = 0.0;

        // SystemInfo object to access hardware information
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();

        // OperatingSystem object to access operating system information
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();

        // Measure resource usage for the specified duration
        while (System.currentTimeMillis() - startTime < durationInSeconds * 1000) {
            // CPU Usage
            OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
            double cpuUsage = osBean.getSystemCpuLoad() * 100;
            totalCpuUsage += cpuUsage;

            // RAM Usage
            double ramUsage = (hardware.getMemory().getTotal() - hardware.getMemory().getAvailable()) /
                    (double) hardware.getMemory().getTotal() * 100;
            totalRamUsage += ramUsage;

            // GPU Usage (Sample implementation, might vary based on GPU model and drivers)
            // Replace with appropriate GPU monitoring library or method
            double gpuUsage = 0.0; // Sample GPU usage measurement
            totalGpuUsage += gpuUsage;
        }

        // Calculate average usage
        avgUsage[0] = totalCpuUsage / durationInSeconds;
        avgUsage[1] = totalRamUsage / durationInSeconds;
        avgUsage[2] = totalGpuUsage / durationInSeconds;

        return avgUsage;
    }

}