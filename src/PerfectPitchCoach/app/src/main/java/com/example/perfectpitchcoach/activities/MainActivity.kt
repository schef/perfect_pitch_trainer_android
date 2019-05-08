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

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()
    private var pause: Boolean = false

    private lateinit var pauseBtn: Button
    private lateinit var playBtn: Button
    private lateinit var stopBtn: Button
    private lateinit var btnRoomDatabase: Button
    private lateinit var tv_pass: TextView
    private lateinit var tv_due: TextView
    private lateinit var seek_bar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        pauseBtn = findViewById(R.id.pauseBtn)
        playBtn = findViewById(R.id.playBtn)
        stopBtn = findViewById(R.id.stopBtn)
        btnRoomDatabase = findViewById(R.id.btnRoomDatabase)
        tv_pass = findViewById(R.id.tv_pass)
        tv_due = findViewById(R.id.tv_due)
        seek_bar = findViewById(R.id.seek_bar)
        mediaPlayer = MediaPlayer()

        // Start the media player
        playBtn.setOnClickListener {
            if (pause) {
                mediaPlayer.seekTo(mediaPlayer.currentPosition)
                mediaPlayer.start()
                pause = false
                Toast.makeText(this, "media playing", Toast.LENGTH_SHORT).show()
            } else {

                mediaPlayer = MediaPlayer.create(applicationContext, R.raw.ne_spavaj_mala_moja)
                mediaPlayer.start()
                Toast.makeText(this, "media playing", Toast.LENGTH_SHORT).show()

            }
            initializeSeekBar()
            playBtn.isEnabled = false
            pauseBtn.isEnabled = true
            stopBtn.isEnabled = true

            mediaPlayer.setOnCompletionListener {
                playBtn.isEnabled = true
                pauseBtn.isEnabled = false
                stopBtn.isEnabled = false
                Toast.makeText(this, "end", Toast.LENGTH_SHORT).show()
            }
        }
        // Pause the media player
        pauseBtn.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                pause = true
                playBtn.isEnabled = true
                pauseBtn.isEnabled = false
                stopBtn.isEnabled = true
                Toast.makeText(this, "media pause", Toast.LENGTH_SHORT).show()
            }
        }
        // Stop the media player
        stopBtn.setOnClickListener {
            if (mediaPlayer.isPlaying || pause.equals(true)) {
                pause = false
                seek_bar.setProgress(0)
                mediaPlayer.stop()
                mediaPlayer.reset()
                mediaPlayer.release()
                handler.removeCallbacks(runnable)

                playBtn.isEnabled = true
                pauseBtn.isEnabled = false
                stopBtn.isEnabled = false
                tv_pass.text = ""
                tv_due.text = ""
                Toast.makeText(this, "media stop", Toast.LENGTH_SHORT).show()
            }
        }

        btnRoomDatabase.setOnClickListener {
            val mainIntent = Intent(this@MainActivity, RoomDatabaseExampleActivity::class.java)
            startActivity(mainIntent)
            finish()
        }

        // Seek bar change listener
        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (b) {
                    mediaPlayer.seekTo(i * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayer.isPlaying || pause.equals(true)) {
            pause = false
            seek_bar.setProgress(0)
            mediaPlayer.stop()
            mediaPlayer.reset()
            mediaPlayer.release()
            handler.removeCallbacks(runnable)

            playBtn.isEnabled = true
            pauseBtn.isEnabled = false
            stopBtn.isEnabled = false
            tv_pass.text = ""
            tv_due.text = ""
            Toast.makeText(this, "media stop", Toast.LENGTH_SHORT).show()
        }
    }

    // Method to initialize seek bar and audio stats
    private fun initializeSeekBar() {
        seek_bar.max = mediaPlayer.seconds

        runnable = Runnable {
            seek_bar.progress = mediaPlayer.currentSeconds

            tv_pass.text = "${mediaPlayer.currentSeconds} sec"
            val diff = mediaPlayer.seconds - mediaPlayer.currentSeconds
            tv_due.text = "$diff sec"

            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }

    // Creating an extension property to get the media player time duration in seconds
    val MediaPlayer.seconds: Int
        get() {
            return this.duration / 1000
        }
    // Creating an extension property to get media player current position in seconds
    val MediaPlayer.currentSeconds: Int
        get() {
            return this.currentPosition / 1000
        }


}
