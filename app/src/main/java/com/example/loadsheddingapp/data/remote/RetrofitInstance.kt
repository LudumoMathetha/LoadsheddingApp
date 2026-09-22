package com.example.loadsheddingapp.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Singleton factory providing the Retrofit client instance.
object RetrofitInstance {

    // BASE URL CONFIGURATION:
    // - For Android Emulator accessing localhost on host machine: "http://10.0.2.2:8080/api/"
    // - For physical Android device on local Wi-Fi: "http://<YOUR_COMPUTER_IP>:8080/api/" (e.g., "http://192.168.1.50:8080/api/")
    // - For production hosted cloud API: "https://your-api-domain.com/api/"
    private const val BASE_URL = "http://10.0.2.2:8080/api/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    val api: LoadSheddingApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LoadSheddingApiService::class.java)
    }
}
