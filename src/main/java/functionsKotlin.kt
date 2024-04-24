import java.io.*
import java.util.*

object functionsKotlin {
    var accountsPath: String = "accounts/"


    const val usernameVariable: String = "username="
    const val passwordVariable: String = "password="
    const val accountVariable: String = "accountType="

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val sc = Scanner(System.`in`)
        println("Do you want to [R]egister or [L]ogin or [D]elete an account ? Or test [C]ange account type")
        var choice = sc.next()
        choice = choice.lowercase(Locale.getDefault())

        when (choice) {
            "r" -> {
                println("Enter a username")
                val username = sc.next()
                println("Enter a password!")
                val pw = sc.next()
                register(username, pw)
            }

            "l" -> {
                println("Enter your username")
                val usern = sc.next()
                println("Enter your password")
                val passw = sc.next()
                login(usern, passw)
            }

            "d" -> {
                println("Enter your username")
                val user = sc.next()
                println("Enter your password")
                val pass = sc.next()
                if (deleteAccount(user, pass)) {
                    println("Account has been deleted Successfully!")
                } else {
                    println("Account hasn't been deleted, try again later!")
                }
            }

            "c" -> {
                println("Enter username")
                val userna = sc.next()
                println("Enter the new Account type")
                val newT = sc.next()
                println("Enter the Admin Password!")
                val pwa = sc.next()
                pwa.replace(" ", "")

                editAccountType(userna, newT, pwa)
            }
        }
    }


    //Google Gemini Start
    @Throws(IOException::class)
    fun register(name: String, password: String): Boolean {
        return registerr(name, password, "normal")
    }

    //Google Gemini Ende
    @Throws(IOException::class)
    fun registerr(name: String, password: String, accountType: String): Boolean {
        File("accounts").mkdir()

        val f1 = File(accountsPath + name + ".txt")
        if (f1.isFile) {
            println("Username is already taken!")
            return false
        } else {
            f1.createNewFile()
            val fwa = FileWriter(accountsPath + name + ".txt", true)
            val fw = FileWriter(accountsPath + name + ".txt", false)

            if (name == password) {
                println("Username and password are equal")
                fw.write("")
                fwa.write("Same User And Password")
                return false
            } else {
                fw.write("")
                fwa.write("username=$name\n")
                fwa.write("password=$password\n")
                fwa.write("accountType=" + accountType.lowercase(Locale.getDefault()) + "\n")
                fwa.write("hasBoughtGame=true") //Todo, actually implement this
                println("Registration Complete!")
            }

            fwa.close()
            return true
        }
    }

    @Throws(IOException::class)
    fun login(name: String, password: String): Boolean {
        val fr = FileReader(accountsPath + name + ".txt")
        val br = BufferedReader(fr)

        val user = br.readLine().replace("username=", "")
        val pass = br.readLine().replace("password=", "")
        val type = br.readLine().replace("accountType=", "")
        val hasGame = br.readLine().replace("hasBoughtGame=", "")

        if (user == name && pass == password && hasGame == "true" || hasGame == "team") {
            println("Login Successful!")
            return true
        } else {
            println("Login didn't work!")
            return false
        }
    }

    @Throws(IOException::class)
    fun editAccountType(name: String, newType: String, givenAdminPass: String): Boolean {
        //Todo: Just replace Line with "accountType=" with newType. Currently, it's writing whole File new.
        var newType = newType
        val f1 = File(accountsPath + name + ".txt")
        if (f1.exists()) {
            val fr = FileReader(accountsPath + name + ".txt")
            val br = BufferedReader(fr)

            val user = br.readLine()
            val pass = br.readLine()
            val acc = br.readLine()
            val hasBought = br.readLine()
            //        FileWriter fw = new FileWriter(accountsPath+name+".txt");
//        BufferedWriter bw = new BufferedWriter(fw);
            val types = "developer,admin,tester,normal"
            if (givenAdminPass == "windScript/!&" && types.contains(newType)) {
                newType = newType.lowercase(Locale.getDefault())

                //if hasBought=team und rang wird auf newtype == normal oder so dann hasbought == false uw
                val fw = FileWriter(accountsPath + name + ".txt", true)
                val fwa = FileWriter(accountsPath + name + ".txt")
                fwa.write("")
                fwa.close()
                fw.write(user + "\n")
                fw.write(pass + "\n")
                fw.write("accountType=$newType\n")
                if (newType === "developer" || newType === "admin" || newType === "tester") {
                    //Make hasbought == true, but not forever, make another variable just for this, because if the person looses the account type developer for example, and hasn't bought the game before, the person has to buy it!
                    fw.write("hasBoughtGame=team")
                }
                fw.write(hasBought)


                fw.close()
                br.close()
                fr.close()
                println("Account type changed successfully!")
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
                return true
            } else {
                println("Wrong password or wrong typename !")
                return false
            }
        } else {
            println("User doesn't exist")
            return false
        }
    }

    @Throws(IOException::class)
    fun readAccountType(username: String): Int {
        val fr = FileReader(accountsPath + username + ".txt")
        val br = BufferedReader(fr)

        val user = br.readLine()
        val passw = br.readLine()

        var accType = br.readLine()
        var hasBoughtGame = br.readLine() ?: return 999
        hasBoughtGame = hasBoughtGame.replace("hasBoughtGame=", "")
        accType = accType.replace("accountType=", "")
        when (hasBoughtGame) {
            "true" -> {
                when (accType) {
                    "developer" -> return 1
                    "admin" -> return 2
                    "tester" -> return 3
                    "normal" -> return 4
                }
                when (accType) {
                    "developer" -> return 1
                    "admin" -> return 2
                    "tester" -> return 3
                    "normal" -> return 4
                }
                return 999
            }

            "team" -> {
                when (accType) {
                    "developer" -> return 1
                    "admin" -> return 2
                    "tester" -> return 3
                    "normal" -> return 4
                }
                return 999
            }

            "false" -> return 999
        }

        fr.close()
        return 0
    }


    @Throws(IOException::class)
    fun deleteAccount(name: String, password: String): Boolean {
        val f1 = File(accountsPath + name + ".txt")
        val fr = FileReader(accountsPath + name + ".txt")
        val br = BufferedReader(fr)

        var user = br.readLine()
        var pass = br.readLine()

        user = user.replace("username=", "")
        pass = pass.replace("password=", "")

        return if (user == name && pass == password) {
            //            System.out.println("user and pass are right");
            //            System.out.println("User in file: " + user);
            //            System.out.println("Given user: " + name);
            //            System.out.println("Password in file: " + pass);
            //            System.out.println("Given passw: " + password);

            f1.delete()
        } else {
            //            System.out.println("User and pass are wrong");
            //            System.out.println("User in file: " + user);
            //            System.out.println("Given user: " + name);
            //            System.out.println("Password in file: " + pass);
            //            System.out.println("Given passw: " + password);
            false
        }

        //        return false;
    }
}