package com.saba.notebook

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.ComponentActivity

class HomeActivity : ComponentActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var notesListView: ListView
    private lateinit var notesAdapter: ArrayAdapter<String>
    private lateinit var notesList: MutableList<String>
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        dbHelper = DatabaseHelper(this)

        // Assuming userId is passed from Login/Signup activity
        userId = intent.getIntExtra("USER_ID", -1)

        notesListView = findViewById(R.id.notes_list)
        notesList = dbHelper.getNotesByUserId(userId).toMutableList()
        notesAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, notesList)
        notesListView.adapter = notesAdapter

        val addButton: Button = findViewById(R.id.add_note_button)
        addButton.setOnClickListener {
            val noteText = "New Note" // You can change this to get input from a dialog or another UI element.
            val noteId = dbHelper.addNote(userId, noteText)
            if (noteId > -1) {
                notesList.add(noteText)
                notesAdapter.notifyDataSetChanged()
            }
        }

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val btnLogout = findViewById<Button>(R.id.logout_button)

        btnLogout.setOnClickListener {
            // Clear login state
            sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
