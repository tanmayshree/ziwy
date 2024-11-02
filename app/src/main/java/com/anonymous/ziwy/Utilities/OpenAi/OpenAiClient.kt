package com.anonymous.ziwy.Utilities.OpenAi

import com.anonymous.ziwy.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OpenAiClient {

    private const val BASE_URL = "https://api.openai.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            println("Adding Authorization header to request. API Key: ${BuildConfig.OPENAI_API_KEY}")
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${BuildConfig.OPENAI_API_KEY}")
                .build()
            chain.proceed(request)
        }
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }

    val apiService: OpenAiService by lazy {
        retrofit.create(OpenAiService::class.java)
    }

}

const val PROMPT = """
    Read this coupon to get the following information in JSON format:
    - **Coupon code**: Coupon code.
    - **Expiry date**: Expiry date of the coupon in YYYY-MM-DD format.
    - **Brand**: Brand for which the coupon is available.
    - **Offer**: Offer displayed on the coupon.
    - **Product**: Products for which the coupon is applicable in array format.
    - **Spend**: Minimum spend required to redeem the coupon.
    - **Currency**: Currency in which the spend amount is.

    The output should be in this format:

    {
      "coupon_code": "COUPON_CODE",
      "expiry_date": "EXPIRY_DATE",
      "brand": "BRAND",
      "offer": "OFFER",
      "product": ["PRODUCT1", "PRODUCT2"],
      "spend": "SPEND",
      "currency": "CURRENCY"
    }

    If any information is missing, return null for that field.
"""
