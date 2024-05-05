import com.sun.management.OperatingSystemMXBean
import oshi.SystemInfo
import java.io.*
import java.lang.management.ManagementFactory
import java.util.*




object functionss {
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
                println("Enter your email adress")
                val email = sc.next()
                val randomNumber = emailWindScript.generateRandomNumber(100000, 999999)
                emailWindScript.sendEmail(
                    email, "Auth Code",
                    "Your authentication code is \n\n$randomNumber\n\nDon't give anyone this code"
                )
                println("Enter authcode")
                val authcode = sc.next()
                println(authcode)
                if (randomNumber.toString() == authcode) {
                    register(email, username, pw)
                } else {
                    println("Error, wrong auth-code!")
                }
            }

            "l" -> {
                println("Enter your username")
                val usern = sc.next()
                println("Enter your password")
                val passw = sc.next()
                println("Enter your email")
                val emaill = sc.next()
                login(emaill, usern, passw)
            }

            "d" -> {
                println("Enter your username")
                val user = sc.next()
                println("Enter your password")
                val pass = sc.next()
                println("Enter your email")
                val emailll = sc.next()
                if (deleteAccount(emailll, user, pass)) {
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

            "cpu" -> {
                val avgUsage = measureResourceUsage(10)

                // Print average usage
                println("Average CPU Usage: " + avgUsage[0] + "%")
                println("Average RAM Usage: " + avgUsage[1] + "%")
                println("Average GPU Usage: " + avgUsage[2] + "%")
            }
        }
    }


    //Google Gemini Start
    @Throws(IOException::class)
    fun register(email: String, name: String, password: String): Boolean {
        return registerr(email, name, password, "normal")
    }

    //Google Gemini Ende
    @Throws(IOException::class)
    fun registerr(email: String, name: String, password: String, accountType: String): Boolean {
        //ToDo: Implement checking for emails already in use (for loop ??)
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
                fwa.write("email=$email\n")
                fwa.write("accountType=" + accountType.lowercase(Locale.getDefault()) + "\n")
                fwa.write("hasBoughtGame=false")
                println("Registration Complete!")
            }

            fwa.close()
            return true
        }
    }

    @Throws(IOException::class)
    fun buyGame(username: String): Boolean {
        val fr = FileReader(accountsPath + username + ".txt")
        val br = BufferedReader(fr)
        val fw = FileWriter(accountsPath + username + ".txt", true)

        val user = br.readLine()
        val pw = br.readLine()
        val email = br.readLine()
        val accType = br.readLine()
        val hasBoughtGame = br.readLine()

        if (hasBoughtGame == "true" || hasBoughtGame == "team") {
            return false
        } else if (hasBoughtGame == "false") {
            //Todo: Make paying function and return true if it's successful
            fw.write(user)
            fw.write(pw)
            fw.write(email)
            fw.write(accType)
            fw.write("hasBoughtGame=true")

            return true
        } else {
            fr.close()
            return false
        }
    }

    @Throws(IOException::class)
    fun login(email: String, name: String, password: String): Boolean {
        val fr = FileReader(accountsPath + name + ".txt")
        val br = BufferedReader(fr)

        val user = br.readLine().replace("username=", "")
        val pass = br.readLine().replace("password=", "")
        val mail = br.readLine().replace("email=", "")
        val type = br.readLine().replace("accountType=", "")
        val hasGame = br.readLine().replace("hasBoughtGame=", "")

        if (user == name && pass == password && mail == email && hasGame !== "false" && hasGame !== "ERROR") {
            println("Login Successful!")
            return true
        } else {
            println("Login didn't work!")
            return false
        }
    }


    @Throws(IOException::class)
    fun editAccountType(name: String, newType: String, givenAdminPass: String): Boolean {
        var newType = newType
        val f1 = File(accountsPath + name + ".txt")
        val fr = FileReader(accountsPath + name + ".txt")
        val br = BufferedReader(fr)
        if (f1.exists()) {
            val user = br.readLine()
            val pass = br.readLine()
            val email = br.readLine()
            val accT = br.readLine()
            var hasBought = br.readLine()
            hasBought = hasBought.replace("hasBoughtGame=", "")
            //        FileWriter fw = new FileWriter(accountsPath+name+".txt");
//        BufferedWriter bw = new BufferedWriter(fw);
            val types = "developer,admin,tester,normal"
            if (givenAdminPass == "windScript/!&" && types.contains(newType)) {
                newType = newType.lowercase(Locale.getDefault())


                val fw = FileWriter(accountsPath + name + ".txt", true)
                val fwa = FileWriter(accountsPath + name + ".txt")
                fwa.write("")
                fwa.close()
                fw.write(user + "\n")
                fw.write(pass + "\n")
                fw.write(email + "\n")
                fw.write("accountType=$newType\n")
                if (newType == "developer" || newType == "admin" || newType == "tester") {
                    //Make hasbought == true, but not forever, make another variable just for this, because if the person looses the account type developer for example, and hasn't bought the game before, the person has to buy it!

                    if (hasBought == "true") {
                        fw.write("hasBoughtGame=team-true")
                    } else if (hasBought == "false") {
                        fw.write("hasBoughtGame=team-false")
                    } else {
                        fw.write("hasBoughtGame=ERROR$hasBought")
                    }
                }
                if (newType == "normal") {
                    fw.write("accountType=normal\n")
                    if (hasBought == "team-true") {
                        fw.write("hasBoughtGame=true")
                    } else if (hasBought == "team-false") {
                        fw.write("hasBoughtGame=false")
                    } else {
                        fw.write("hasBoughtGame=$hasBought")
                    }
                }


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
    fun readAccount(username: String): Int {
        //ToDo: Make team member checking with email instead of username. Also send user an email on accoun Type change
        val fr = FileReader(accountsPath + username + ".txt")
        val br = BufferedReader(fr)

        val user = br.readLine()
        val passw = br.readLine()
        val email = br.readLine()

        var accType = br.readLine()
        var hasBoughtGame = br.readLine() ?: return 999
        hasBoughtGame = hasBoughtGame.replace("hasBoughtGame=", "")
        accType = accType.replace("accountType=", "")
        if (hasBoughtGame == "true") {
            when (accType) {
                "developer" -> return 1
                "admin" -> return 2
                "tester" -> return 3
                "normal" -> return 4
            }
        } else if (hasBoughtGame.contains("team")) {
            when (accType) {
                "developer" -> return 1
                "admin" -> return 2
                "tester" -> return 3
                "normal" -> return 4
            }
        } else if (hasBoughtGame == "false") {
            return 999
        }

        fr.close()
        return 0
    }


    @Throws(IOException::class)
    fun deleteAccount(email: String, name: String, password: String): Boolean {
        val f1 = File(accountsPath + name + ".txt")
        val fr = FileReader(accountsPath + name + ".txt")
        val br = BufferedReader(fr)

        var user = br.readLine()
        var pass = br.readLine()
        val mail = br.readLine()
        user = user.replace("username=", "")
        pass = pass.replace("password=", "")

        return if (user == name && pass == password && mail == email) {
            f1.delete()
        } else {
            false
        }

        //        return false;
    }

    fun measureResourceUsage(durationInSeconds: Int): DoubleArray {
        val avgUsage = DoubleArray(3) // Index 0: CPU, Index 1: RAM, Index 2: GPU

        // Start time
        val startTime = System.currentTimeMillis()

        // Variables to store cumulative usage
        var totalCpuUsage = 0.0
        var totalRamUsage = 0.0
        var totalGpuUsage = 0.0

        // SystemInfo object to access hardware information
        val systemInfo = SystemInfo()
        val hardware = systemInfo.hardware

        // OperatingSystem object to access operating system information
        val operatingSystem = systemInfo.operatingSystem

        // Measure resource usage for the specified duration
        while (System.currentTimeMillis() - startTime < durationInSeconds * 1000) {
            // CPU Usage
            val osBean = ManagementFactory.getPlatformMXBean(
                OperatingSystemMXBean::class.java
            )
            val cpuUsage = osBean.systemCpuLoad * 100
            totalCpuUsage += cpuUsage

            // RAM Usage
            val ramUsage = (hardware.memory.total - hardware.memory.available) / hardware.memory.total.toDouble() * 100
            totalRamUsage += ramUsage

            // GPU Usage (Sample implementation, might vary based on GPU model and drivers)
            // Replace with appropriate GPU monitoring library or method
            val gpuUsage = 0.0 // Sample GPU usage measurement
            totalGpuUsage += gpuUsage
        }

        // Calculate average usage
        avgUsage[0] = totalCpuUsage / durationInSeconds
        avgUsage[1] = totalRamUsage / durationInSeconds
        avgUsage[2] = totalGpuUsage / durationInSeconds

        return avgUsage
    }
}