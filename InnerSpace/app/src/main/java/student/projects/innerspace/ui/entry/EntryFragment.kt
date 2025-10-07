package student.projects.innerspace.ui.entry

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
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

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString().trim()
            val content = binding.contentEditText.text.toString().trim()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(requireContext(), "Title and content cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val note = Note(title = title, content = content)
            lifecycleScope.launch {
                AppDatabase.getDatabase(requireContext()).noteDao().insert(note)
                Toast.makeText(requireContext(), "Entry saved", Toast.LENGTH_SHORT).show()
                binding.titleEditText.text.clear()
                binding.contentEditText.text.clear()
                binding.imagePreview.setImageDrawable(null)
            }
        }

        binding.attachImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            val inputStream: InputStream? = selectedImageUri?.let {
                requireContext().contentResolver.openInputStream(it)
            }
            val bitmap = BitmapFactory.decodeStream(inputStream)
            binding.imagePreview.setImageBitmap(bitmap)
        }
    }
}
