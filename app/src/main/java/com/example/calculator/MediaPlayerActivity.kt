package com.example.calculator

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_AUDIO
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File

class MediaPlayerActivity : AppCompatActivity() {
    private var log_tag : String = "MY_LOG_TAG"
    private var mediaPlayer : MediaPlayer? = null
    private lateinit var musicSeekBar : SeekBar
    private lateinit var volumeSeekBar : SeekBar
    private lateinit var playPauseButton : Button
    private lateinit var currentTrackName : TextView
    private lateinit var trackList : ListView
    private lateinit var handler : Handler
    private var musicFiles : ArrayList<File> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_media_player)

        musicSeekBar = findViewById(R.id.musicSeekBar)
        volumeSeekBar = findViewById(R.id.volumeSeekBar)
        playPauseButton = findViewById(R.id.playPauseButton)
        currentTrackName = findViewById(R.id.currentTrackName)
        trackList = findViewById(R.id.trackList)
        handler = Handler(Looper.getMainLooper())

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_media_player_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val requestPermessionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show()
                playMusic()
            } else {
                Toast.makeText(this, "Please grant permission", Toast.LENGTH_LONG).show()
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermessionLauncher.launch(READ_MEDIA_AUDIO)
        } else {
            requestPermessionLauncher.launch(READ_EXTERNAL_STORAGE)
        }
        playPauseButton.setOnClickListener {
            if (mediaPlayer != null) {
                if (mediaPlayer!!.isPlaying) {
                    mediaPlayer!!.pause()
                } else {
                    mediaPlayer!!.start()
                    updateSeekBar()
                }
            }
        }
        volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (mediaPlayer != null) {
                    val volume = progress / 100f
                    mediaPlayer!!.setVolume(volume, volume)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        musicSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer!!.seekTo(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun playMusic(){
        var musicPath: String = Environment.getExternalStorageDirectory().path + "/Download"
        var directory: File = File(musicPath)

        if (directory.exists() && directory.isDirectory) {
            val files = directory.listFiles()
            if (files != null) {
                musicFiles.clear()
                val fileNames = ArrayList<String>()

                files.forEach {
                    if (it.isFile && it.name.endsWith(".mp3")) {
                        musicFiles.add(it)
                        fileNames.add(it.name)
                    }
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, fileNames)
                trackList.adapter = adapter
                trackList.setOnItemClickListener { parent, view, position, id ->
                    startPlaying(musicFiles[position])
                }
            }
        } else {
            Toast.makeText(this, "Папка Music не найдена", Toast.LENGTH_SHORT).show()
        }
    }
    private fun startPlaying(file: File) {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
        }
        mediaPlayer = MediaPlayer()
        try {
            mediaPlayer!!.setDataSource(file.path)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
            currentTrackName.text = file.name
            musicSeekBar.max = mediaPlayer!!.duration
            updateSeekBar()

        } catch (e: Exception) {
            Log.d(log_tag, "Error playing file: " + e.message)
        }
    }
    private fun updateSeekBar() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                    musicSeekBar.progress = mediaPlayer!!.currentPosition
                    handler.postDelayed(this, 1000)
                }
            }
        }, 1000)
    }
    override fun onResume() {
        super.onResume()
    }
    override fun onPause() {
        super.onPause()
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            mediaPlayer!!.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }
}