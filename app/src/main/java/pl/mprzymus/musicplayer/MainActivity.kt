package pl.mprzymus.musicplayer

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import pl.mprzymus.musicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mediaManager: MediaManager
    private var handler: Handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mediaManager = MediaManager(getTrackList(), applicationContext)

        binding.playButton.setOnClickListener {
            mediaManager.pauseButton()
            switchPlayButtonImage()
        }

        binding.timeBack.setOnClickListener { mediaManager.onChangeTime(-10) }
        binding.timeAhead.setOnClickListener { mediaManager.onChangeTime(10) }
        binding.nextSong.setOnClickListener {
            changeSong { mediaManager.nextSong(applicationContext, binding.seekBar) }

        }
        binding.previousSong.setOnClickListener {
            changeSong { mediaManager.previousSong(applicationContext, binding.seekBar) }
        }
        mediaManager.setSeekBarMaxValue(binding.seekBar)
        startSeekBarRefresh(mediaManager.mediaPlayer)
    }

    private fun changeSong(changeSong: () -> Unit) {
        handler.removeCallbacks(runnable)
        changeSong()
        startSeekBarRefresh(mediaManager.mediaPlayer)
    }

    private fun startSeekBarRefresh(mediaPlayer: MediaPlayer) {
        val seekBar = binding.seekBar
        runnable = Runnable {
            seekBar.progress = mediaPlayer.currentPosition
            handler.postDelayed(runnable, 100)
        }
        handler.postDelayed(runnable, 100)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun switchPlayButtonImage() {
        if (mediaManager.isPlaying()) {
            binding.playButton.setImageDrawable(getDrawable(R.drawable.ic_media_pause))
        } else {
            binding.playButton.setImageDrawable(getDrawable(R.drawable.ic_media_play))
        }
    }
}
