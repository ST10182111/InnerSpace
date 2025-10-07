package student.projects.innerspace.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import student.projects.innerspace.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        val prefs = requireContext().getSharedPreferences("userPrefs", Context.MODE_PRIVATE)

        // 🌙 Theme toggle
        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            val theme = if (isChecked) "Dark" else "Light"
            prefs.edit().putString("theme", theme).apply()
            Toast.makeText(requireContext(), "Theme set to $theme", Toast.LENGTH_SHORT).show()
        }

        // 🔠 Font size slider
        binding.fontSizeSlider.addOnChangeListener { _, value, _ ->
            prefs.edit().putInt("fontSize", value.toInt()).apply()
            Toast.makeText(requireContext(), "Font size: ${value.toInt()}", Toast.LENGTH_SHORT).show()
        }

        // 🌍 Language dropdown
        val languages = listOf("English", "Zulu")
        val languageAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, languages)
        binding.languageSpinner.adapter = languageAdapter

        binding.languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedLanguage = languages[position]
                prefs.edit().putString("language", selectedLanguage).apply()
                Toast.makeText(requireContext(), "Language set to $selectedLanguage", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // 🔔 Notification toggle
        binding.notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("notificationsEnabled", isChecked).apply()
            val status = if (isChecked) "enabled" else "disabled"
            Toast.makeText(requireContext(), "Notifications $status", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }
}
