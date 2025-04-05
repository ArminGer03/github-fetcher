import kotlinx.coroutines.runBlocking
import repository.UserRepository
import ui.displayUser
import ui.printMenu

fun main() {
    val userRepository = UserRepository()

    while (true) {
        printMenu()

        when (readLine()?.trim()) {
            "1" -> {
                print("Enter GitHub username: ")
                val username = readLine()?.trim()
                if (username.isNullOrEmpty()) {
                    println("Username cannot be empty.")
                    continue
                }
                runBlocking {
                    val existingUser = userRepository.getAllUsers().find { it.user.login.equals(username, ignoreCase = true) }
                    if (existingUser != null) {
                        println("User '$username' is already fetched and cached.")
                        print("Do you want to update the cache with fresh data? (y/n): ")
                        val updateChoice = readLine()?.trim()?.lowercase()
                        if (updateChoice != "y") {
                            println("Using cached data:")
                            displayUser(existingUser)
                            return@runBlocking
                        }
                    }
                    val cachedUser = userRepository.getUser(username, forceUpdate = true)
                    if (cachedUser != null) {
                        println("User information (updated):")
                        displayUser(cachedUser)
                    } else {
                        println("Error fetching user information for '$username'.")
                    }
                }
            }
            "2" -> {
                val allUsers = userRepository.getAllUsers()
                if (allUsers.isEmpty()) {
                    println("No users cached.")
                } else {
                    println("Cached Users:")
                    allUsers.forEach { displayUser(it) }
                }
            }
            "3" -> {
                print("Enter username to search: ")
                val query = readLine()?.trim()
                if (query.isNullOrEmpty()) {
                    println("Search query cannot be empty.")
                    continue
                }
                val results = userRepository.searchByUsername(query)
                if (results.isEmpty()) {
                    println("No user found matching '$query'.")
                } else {
                    println("Search Results:")
                    results.forEach { displayUser(it) }
                }
            }
            "4" -> {
                print("Enter repository name to search: ")
                val query = readLine()?.trim()
                if (query.isNullOrEmpty()) {
                    println("Search query cannot be empty.")
                    continue
                }
                val results = userRepository.searchByRepoName(query)
                if (results.isEmpty()) {
                    println("No user found with a repository matching '$query'.")
                } else {
                    println("Search Results:")
                    results.forEach { displayUser(it) }
                }
            }
            "5" -> {
                println("Exiting program. Goodbye!")
                return
            }
            else -> println("Invalid option. Please try again.")
        }
    }
}
