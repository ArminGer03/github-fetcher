package ui

import model.CachedUser

fun displayUser(cachedUser: CachedUser) {
    println("Username: ${cachedUser.user.login}")
    println("Followers: ${cachedUser.user.followers}")
    println("Following: ${cachedUser.user.following}")
    println("Account Created At: ${cachedUser.user.createdAt}")
    println("Public Repositories:")
    if (cachedUser.repos.isEmpty()) {
        println("  No repositories found.")
    } else {
        cachedUser.repos.forEach { println("  - ${it.name}") }
    }
    println("=====================================")
}


fun printMenu() {
    println(
        " __  __ ______ _   _ _    _ \n" +
                "|  \\/  |  ____| \\ | | |  | |\n" +
                "| \\  / | |__  |  \\| | |  | |\n" +
                "| |\\/| |  __| | . ` | |  | |\n" +
                "| |  | | |____| |\\  | |__| |\n" +
                "|_|  |_|______|_| \\_|\\____/ \n"
    )
    println("1. Fetch GitHub user information by username")
    println("2. Display cached users")
    println("3. Search cached users by username")
    println("4. Search cached users by repository name")
    println("5. Exit")
    print("Please choose an option: ")
}