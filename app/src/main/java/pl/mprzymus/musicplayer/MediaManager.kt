package pl.mprzymus.musicplayer

import android.content.Context
import android.media.MediaPlayer
import android.widget.SeekBar

class MediaManager(val tracks: List<Int>, context: Context) {
    private var _currentTrack = 0
    val currentTrack: Int
        get() = _currentTrack
    var mediaPlayer: MediaPlayer

    init {
        mediaPlayer = MediaPlayer.create(context, tracks[_currentTrack])
    }

    fun pauseButton() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
        else {
            mediaPlayer.start()
        }
    }

    fun onChangeTime(seconds: Int) {
        mediaPlayer.seekTo(mediaPlayer.currentPosition + seconds * 1000)
    }

    fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    fun setSeekBarMaxValue(seekBar: SeekBar) {
        seekBar.max = mediaPlayer.duration
    }

    fun nextSong(context: Context, seekBar: SeekBar) {
        _currentTrack = (_currentTrack + 1) % tracks.size
        changeSong(context, seekBar)
    }

    fun previousSong(context: Context, seekBar: SeekBar) {
        _currentTrack = (_currentTrack - 1) % tracks.size
        changeSong(context, seekBar)
    }

    private fun changeSong(context: Context, seekBar: SeekBar) {
        val wasPlaying = mediaPlayer.isPlaying
        mediaPlayer.release()
        mediaPlayer = MediaPlayer.create(context, tracks[_currentTrack])
        if (wasPlaying) {
            mediaPlayer.start()
        }
        setSeekBarMaxValue(seekBar)
    }

    fun onStop() {
        mediaPlayer.seekTo(0)
        mediaPlayer.stop()
        mediaPlayer.prepare()
    }

    fun onSeekBarPositionChanged(position: Int) {
        mediaPlayer.seekTo(position)
    }
}