import java.io.*
import java.net.ServerSocket

//ToDo: Make Performance profiling, performance tests for 1 hour, 2 hour and so on, without request from client, always write it to a file an
//ToDO: Also require email, username is just ingame, login and registration working with email, and send Emails to users on registration or sth like that, with Verification code
object server {
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        // Define port number

        val port = 8051

        val serverSocket = ServerSocket(port) //KI generiert
        println("Server started on port $port")

        while (true) {
            try {
                // Accept client connection
                val clientSocket = serverSocket.accept() //KI generiert
                println("---------------------\nClient connected\n")

                // Create input and output streams
                val `in` = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
                val out = PrintWriter(clientSocket.getOutputStream(), true)

                // Receive strings from client
                val choice = `in`.readLine() //KI generiert
                //var email = `in`.readLine()
                var user = `in`.readLine() //KI generiert
                var pass = `in`.readLine() //KI generiert
                //email = email.replace(" ", "")
                user = user.replace(" ", "")
                pass = pass.replace(" ", "")
                val email = "placeholder@windscript.net";

                val newChoice = choice.toString().toInt()

                if (!(user.isEmpty() && pass.isEmpty())) {
                    if (newChoice == 1) { //ToDo: Implement verify logic here (login)
                        if (functions.login(email, user, pass)) {
                            out.println("Login Successful!")
                            println("Login Successful!")
                        } else {
                            out.println("User not found!")
                            println("User not found!")
                        }
                    } else if (newChoice == 2) { //ToDo: Implement verify logic here (register)
                        if (functions.register(email, user, pass)) {
                            out.println("Registration Successful!")
                        } else {
                            out.println("Username is taken!")
                        }
                    } else if (newChoice == 3) { //ToDo: Implement verify logic here (account delete)
                        if (functions.deleteAccount(email, user, pass)) {
                            out.println("Account deleted!")
                        } else {
                            out.println("Account hasn't been deleted!")
                        }
                    } else if (newChoice == 4) {
                        val type = functions.readAccount(user)
                        if (type == 0) {
                            println("Something went wrong!")
                            out.println("Your account is broken, please contact the support")
                        } else {
                            if (type == 1) {
                                out.println("developer")
                            } else if (type < 3) {
                                out.println("team")
                            } else if (type < 4) {
                                println(type)
                                println("test version access granted!")
                                out.println("test version access granted!")
                            } else if (type == 999) {
                                println("You should buy the game before you can play it!")
                                out.println("You should buy the game before you can play it!")
                            } else if (type >= 4) {
                                println("You don't have Test Version Access")
                            }
                        }

                        //                        }
                    } else if (newChoice == 5) {
                        val adminPW = `in`.readLine()
                        val newType = `in`.readLine()
                        if (user == "sl3mey" && newType !== "developer") {
                            println("You can't change the owners account Type!")
                            out.println("You can't change the owners account Type!")
                        } else {
                            val editAccountType = functions.editAccountType(user, newType, adminPW)
                            println(editAccountType)
                            out.println(editAccountType.toString())
                        }
                    } else if (newChoice == 6) {
                        val gameType = `in`.readLine() //Test or public version
                        if (gameType == "public") {
                            val publicVersion = File("publicVersion.txt")
                            if (publicVersion.exists()) {
                                val fr = FileReader(publicVersion)
                                out.println(fr.read())
                                fr.close()
                            } else {
                                out.println("Error")
                            }
                        } else if (gameType == "test") {
                            val testVersion = File("testVersion.txt")
                            if (testVersion.exists()) {
                                val fr = FileReader(testVersion)
                                out.println(fr.read())
                                fr.close()
                            } else {
                                out.println("Error")
                            }
                        }
                    } else if (newChoice == 6) {
                        val testDuration = `in`.readLine()
                    }
                } else {
                    println("Received User name, or/and password are empty!")
                    break
                }

                // Send response (optional)
                //System.out.println("\nReceived Choice: " + choice + "\nReceived User: " + user + "\nReceived Password: " + pass + "\n");
                out.println("Informations Received: \nUsername: $user\nPassword: $pass\nChoice: $choice")

                // Close resources
                `in`.close() //KI generiert
                out.close() //KI generiert
                clientSocket.close() //KI generiert
            } catch (e: Exception) {
                println("An error occurred: " + e.message)
            }
        }

        serverSocket.close() //KI generiert
        println("Server stopped")
    }
}