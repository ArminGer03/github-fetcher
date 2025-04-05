package repository

import model.CachedUser
import network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserRepository {
    private val cache = mutableMapOf<String, CachedUser>()
    private val cacheFilePath = "cache.json"
    private val gson = Gson()

    data class CacheWrapper(val cachedUsers: List<CachedUser>)

    init {
        loadCache()
    }

    private fun loadCache() {
        val file = File(cacheFilePath)
        if (file.exists()) {
            try {
                val json = file.readText()
                val type = object : TypeToken<CacheWrapper>() {}.type
                val wrapper: CacheWrapper = gson.fromJson(json, type)
                wrapper.cachedUsers.forEach { user ->
                    cache[user.user.login.lowercase()] = user
                }
                println("Cache loaded from file with ${cache.size} user(s).")
            } catch (e: Exception) {
                println("Error loading cache: ${e.message}")
            }
        }
    }

    private fun saveCache() {
        try {
            val wrapper = CacheWrapper(cache.values.toList())
            val json = gson.toJson(wrapper)
            File(cacheFilePath).writeText(json)
        } catch (e: Exception) {
            println("Error saving cache: ${e.message}")
        }
    }

    private fun addUser(cachedUser: CachedUser) {
        cache[cachedUser.user.login.lowercase()] = cachedUser
        saveCache()
    }

    suspend fun getUser(username: String, forceUpdate: Boolean = false): CachedUser? {
        val key = username.lowercase()
        if (!forceUpdate) {
            cache[key]?.let {
                return it
            }
        }

        return withContext(Dispatchers.IO) {
            try {
                val userResponse = RetrofitClient.apiService.getUser(username)
                if (userResponse.isSuccessful) {
                    val user = userResponse.body()!!
                    val reposResponse = RetrofitClient.apiService.getRepos(username)
                    val repos = if (reposResponse.isSuccessful) reposResponse.body()!! else emptyList()
                    val cachedUser = CachedUser(user, repos)
                    addUser(cachedUser)
                    cachedUser
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    fun getAllUsers(): List<CachedUser> = cache.values.toList()

    fun searchByUsername(query: String): List<CachedUser> {
        return cache.values.filter { it.user.login.equals(query, ignoreCase = true) }
    }

    fun searchByRepoName(query: String): List<CachedUser> {
        return cache.values.filter { user -> user.repos.any { it.name.equals(query, ignoreCase = true) } }
    }
}
