package model

import com.google.gson.annotations.SerializedName

data class GitHubUser(
    val login: String,
    val followers: Int,
    val following: Int,
    @SerializedName("created_at") val createdAt: String
)

data class GitHubRepo(
    val name: String
)

data class CachedUser(
    val user: GitHubUser,
    val repos: List<GitHubRepo>
)
