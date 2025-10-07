package student.projects.innerspace.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import student.projects.innerspace.databinding.FragmentDashboardBinding
import student.projects.innerspace.model.Quote

class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        binding.textDashboard.text = "Your notes and entries will appear here"

        // ✅ Call the API before returning the view
        RetrofitClient.quoteApi.getDailyQuote().enqueue(object : Callback<Quote> {
            override fun onResponse(call: Call<Quote>, response: Response<Quote>) {
                val quote = response.body()
                binding.quoteText.text = "\"${quote?.quote}\""
                binding.authorText.text = "- ${quote?.author}"
            }

            override fun onFailure(call: Call<Quote>, t: Throwable) {
                Log.e("QuoteAPI", "Error: ${t.message}")
            }
        })

        return binding.root
    }
}
