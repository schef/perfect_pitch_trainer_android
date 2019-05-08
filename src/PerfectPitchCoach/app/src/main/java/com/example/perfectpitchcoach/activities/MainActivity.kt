package com.example.perfectpitchcoach.activities

import android.content.Intent
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.example.perfectpitchcoach.R
import com.example.perfectpitchcoach.database.RoomDatabaseExampleActivity
import com.example.perfectpitchcoach.practices.PracticeMeditationActivity
import com.example.perfectpitchcoach.practices.PracticeTestActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnTestPractice = findViewById<TextView>(R.id.btnTestPractice)
        val btnMeditation = findViewById<TextView>(R.id.btnMeditation)
        val btnRoomDatabase = findViewById<TextView>(R.id.btnRoomDatabase)

        btnTestPractice.setOnClickListener {
            val mainIntent = Intent(this@MainActivity, PracticeTestActivity::class.java)
            startActivity(mainIntent)
            finish()
        }

        btnMeditation.setOnClickListener {
            val mainIntent = Intent(this@MainActivity, PracticeMeditationActivity::class.java)
            startActivity(mainIntent)
            finish()
        }

        btnRoomDatabase.setOnClickListener {
            val mainIntent = Intent(this@MainActivity, RoomDatabaseExampleActivity::class.java)
            startActivity(mainIntent)
            finish()
        }
    }
}
