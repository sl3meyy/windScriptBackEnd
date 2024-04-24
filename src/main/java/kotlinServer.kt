import java.io.*
import java.net.ServerSocket

object kotlinServer {
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
                var user = `in`.readLine() //KI generiert
                user = user.replace(" ", "")
                var pass = `in`.readLine() //KI generiert
                pass = pass.replace(" ", "")

                val newChoice = choice.toString().toInt()

                if (!(user.isEmpty() && pass.isEmpty())) {
                    if (newChoice == 1) {
                        if (functions.login(user, pass)) {
                            out.println("Login Successful!")
                        } else {
                            out.println("User not found!")
                        }
                    } else if (newChoice == 2) {
                        if (functions.register(user, pass)) {
                            out.println("Registration Successful!")
                        } else {
                            out.println("Username is taken!")
                        }
                    } else if (newChoice == 3) {
                        if (functions.deleteAccount(user, pass)) {
                            out.println("Account deleted!")
                        } else {
                            out.println("Account hasn't been deleted!")
                        }
                    } else if (newChoice == 4) {
                        val type = functions.readAccountType(user)
                        if (type == 0) {
                            println("Something went wrong!")
                            out.println("Your account is broken, please contact the support")
                            println(type)
                        } else {
                            if (type < 4) {
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
                    }
                } else {
                    println("Received User name, or/and password are empty!")
                    break
                }

                // Send response (optional)
                println("\nReceived Choice: $choice\nReceived User: $user\nReceived Password: $pass\n")
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