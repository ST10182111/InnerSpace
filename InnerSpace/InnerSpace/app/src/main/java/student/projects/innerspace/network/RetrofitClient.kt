import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://your-api-url.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val quoteApi: QuoteApi = retrofit.create(QuoteApi::class.java)
}
