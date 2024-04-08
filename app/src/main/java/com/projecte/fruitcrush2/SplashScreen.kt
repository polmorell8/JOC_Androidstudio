package com.projecte.fruitcrush2

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.Timer
import kotlin.concurrent.schedule

class SplashScreen : AppCompatActivity() {
    private val duracio: Long = 5000 // 5000 milliseconds = 5 seconds
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        canviarActivity()
        playSplashMusic()
    }

    private fun canviarActivity() {
        Timer().schedule(duracio) {
            saltainici()
        }
    }

    private fun playSplashMusic() {
        mediaPlayer = MediaPlayer.create(this, R.raw.splash_music)
        mediaPlayer.start()
    }

    private fun saltainici() {
        // Stop the music before starting the new activity
        mediaPlayer.stop()
        mediaPlayer.release()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Finish the splash activity
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release() // Release the media player when the activity is destroyed
    }
}
