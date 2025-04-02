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
