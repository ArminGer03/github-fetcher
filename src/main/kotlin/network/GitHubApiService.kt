package network

import model.GitHubUser
import model.GitHubRepo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApiService {
    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): Response<GitHubUser>

    @GET("users/{username}/repos")
    suspend fun getRepos(@Path("username") username: String): Response<List<GitHubRepo>>
}
