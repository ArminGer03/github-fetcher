package repository

import model.CachedUser
import network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository {
    // In-memory cache to store fetched users
    private val cache = mutableMapOf<String, CachedUser>()

    // Retrieves a user from the cache or fetches it via API if not present
    suspend fun getUser(username: String): CachedUser? {
        // Check in memory cache
        cache[username.lowercase()]?.let {
            return it
        }
        // If not in cache, fetch from API
        return withContext(Dispatchers.IO) {
            try {
                val userResponse = RetrofitClient.apiService.getUser(username)
                if (userResponse.isSuccessful) {
                    val user = userResponse.body()!!
                    val reposResponse = RetrofitClient.apiService.getRepos(username)
                    val repos = if (reposResponse.isSuccessful) reposResponse.body()!! else emptyList()
                    val cachedUser = CachedUser(user, repos)
                    cache[username.lowercase()] = cachedUser
                    cachedUser
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    // Returns all cached users
    fun getAllUsers(): List<CachedUser> = cache.values.toList()

    // Searches cached users by username
    fun searchByUsername(query: String): List<CachedUser> {
        return cache.values.filter { it.user.login.equals(query, ignoreCase = true) }
    }

    // Searches cached users by repository name
    fun searchByRepoName(query: String): List<CachedUser> {
        return cache.values.filter { user -> user.repos.any { it.name.equals(query, ignoreCase = true) } }
    }
}
