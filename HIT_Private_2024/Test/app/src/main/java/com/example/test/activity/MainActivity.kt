package com.example.test

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.test.databinding.ActivityMainBinding
import java.util.Date

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    var favoriteList = mutableListOf<Note>()
    private val allNoteFragment = AllNoteFragment()
    private val favoriteFragment = FavoriteFragment()

    private val intentResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK){
            val title = it.data?.extras?.getString("title")
            val content = it.data?.extras?.getString("content")

            allNoteFragment.noteAdd = Note(
                title = title,
                content = content,
                createAt = Date(System.currentTimeMillis()),
                isFavorite = false
            )

            allNoteFragment.addNote()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        handleEvent()
    }

    private fun handleEvent() {
        addNote()
        createNote()
        showTaskBar()
    }

    private fun addNote() {
         supportFragmentManager
             .beginTransaction()
             .add(R.id.fragment_container_view_tag, allNoteFragment)
             .commit()
    }

    private fun showTaskBar() {
        binding.favorite.setOnClickListener {
            binding.lineFavorite.visibility = View.VISIBLE
            binding.lineAllNote.visibility = View.INVISIBLE

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container_view_tag, favoriteFragment)
                .setReorderingAllowed(true)
                .commit()

            favoriteList = allNoteFragment.note.filter { it.isFavorite }.toMutableList()
        }

        binding.allNote.setOnClickListener {
            binding.lineFavorite.visibility = View.INVISIBLE
            binding.lineAllNote.visibility = View.VISIBLE

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container_view_tag, allNoteFragment)
                .setReorderingAllowed(true)
                .commit()
        }
    }

    private fun createNote() {
        val intent = Intent(this, SecondActivity::class.java)
        binding.btnAddANote.setOnClickListener {
            intentResultLauncher.launch(intent)
        }
    }
}