import kotlinx.coroutines.runBlocking
import repository.UserRepository
import ui.displayUser

fun main() {
    val userRepository = UserRepository()

    while (true) {
        println(" __  __ ______ _   _ _    _ \n" +
                "|  \\/  |  ____| \\ | | |  | |\n" +
                "| \\  / | |__  |  \\| | |  | |\n" +
                "| |\\/| |  __| | . ` | |  | |\n" +
                "| |  | | |____| |\\  | |__| |\n" +
                "|_|  |_|______|_| \\_|\\____/ \n")
        println("1. Fetch GitHub user information by username")
        println("2. Display cached users")
        println("3. Search cached users by username")
        println("4. Search cached users by repository name")
        println("5. Exit")
        print("Please choose an option: ")

        when (readLine()?.trim()) {
            "1" -> {
                print("Enter GitHub username: ")
                val username = readLine()?.trim()
                if (username.isNullOrEmpty()) {
                    println("Username cannot be empty.")
                    continue
                }
                runBlocking {
                    val cachedUser = userRepository.getUser(username)
                    if (cachedUser != null) {
                        println("User information:")
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
