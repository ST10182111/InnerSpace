package student.projects.innerspace.ui.entry

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import student.projects.innerspace.data.AppDatabase
import student.projects.innerspace.data.Note
import student.projects.innerspace.databinding.FragmentEntryBinding
import kotlinx.coroutines.launch
import java.io.InputStream

class EntryFragment : Fragment() {

    private lateinit var binding: FragmentEntryBinding
    private var selectedImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEntryBinding.inflate(inflater, container, false)

        // 🔧 Load customization preferences
        val prefs = requireContext().getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val fontSize = prefs.getInt("fontSize", 16)
        val fontStyle = prefs.getString("fontStyle", "sans-serif") ?: "sans-serif"
        val theme = prefs.getString("theme", "Light")
        val bgColor = if (theme == "Dark") "#121212" else "#FFFFFF"

        // 🎨 Apply font size and style
        binding.titleEditText.textSize = fontSize.toFloat()
        binding.contentEditText.textSize = fontSize.toFloat()
        binding.titleEditText.typeface = Typeface.create(fontStyle, Typeface.NORMAL)
        binding.contentEditText.typeface = Typeface.create(fontStyle, Typeface.NORMAL)

        // 🎨 Apply background color based on theme
        binding.root.setBackgroundColor(Color.parseColor(bgColor))

        // 💾 Save journal entry to RoomDB
        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString().trim()
            val content = binding.contentEditText.text.toString().trim()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(requireContext(), "Title and content cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val note = Note(
                title = title,
                content = content,
                imageUri = selectedImageUri?.toString()
            )

            lifecycleScope.launch {
                AppDatabase.getDatabase(requireContext()).noteDao().insert(note)
                Toast.makeText(requireContext(), "Entry saved", Toast.LENGTH_SHORT).show()
                binding.titleEditText.text.clear()
                binding.contentEditText.text.clear()
                binding.imagePreview.setImageDrawable(null)
                selectedImageUri = null
            }
        }

        // 📸 Image picker
        binding.attachImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // 🗑️ Remove image
        binding.removeImageButton.setOnClickListener {
            selectedImageUri = null
            binding.imagePreview.setImageDrawable(null)
            Toast.makeText(requireContext(), "Image removed", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    // 📸 Handle image selection and preview
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data