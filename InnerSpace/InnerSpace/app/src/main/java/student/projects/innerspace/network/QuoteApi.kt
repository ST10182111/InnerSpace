import android.telecom.Call
import retrofit2.http.GET

interface QuoteApi {
    @GET("api/quotes/daily")
    fun getDailyQuote(): Call<Quote>
}
