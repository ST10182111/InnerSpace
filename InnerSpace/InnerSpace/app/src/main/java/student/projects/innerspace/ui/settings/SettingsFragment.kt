package student.projects.innerspace.ui.settings

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import student.projects.innerspace.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        // Theme toggle
        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            val theme = if (isChecked) "Dark" else "Light"
            Toast.makeText(requireContext(), "Theme set to $theme", Toast.LENGTH_SHORT).show()
            // Save to SharedPreferences or apply theme logic here
        }

        // Font size slider
        binding.fontSizeSlider.addOnChangeListener { _, value, _ ->
            Toast.makeText(requireContext(), "Font size: ${value.toInt()}", Toast.LENGTH_SHORT).show()
            // Save font size preference
        }

        // Language dropdown
        val languages = listOf("English", "Zulu")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, languages)
        binding.languageSpinner.adapter = adapter

        // Notification toggle
        binding.notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) "enabled" else "disabled"
            Toast.makeText(requireContext(), "Notifications $status", Toast.LENGTH_SHORT).show()
            // Save notification preference
        }

        return binding.root
    }
}
