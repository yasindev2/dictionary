package uz.yasindev.mydictionaryapp.activities

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import uz.yasindev.mydictionaryapp.R
import uz.yasindev.mydictionaryapp.adapter.SavedWordsAdapter
import uz.yasindev.mydictionaryapp.databinding.ActivitySavedWordsBinding
import java.util.Locale


class SavedWordsActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private val binding by lazy { ActivitySavedWordsBinding.inflate(layoutInflater) }
    private val savedWordsAdapter = SavedWordsAdapter()
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var gestureDetector:GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        textToSpeech = TextToSpeech(this, this)
        gestureDetector = GestureDetectorCompat(this, SwipeGestureListener())

        setDataToListView()
        backButton()
        soundButton()
        itemClickListener()

    }

    private fun itemClickListener() {
        savedWordsAdapter.itemClickListener = { id, engText, uzbText ->
            val intent = Intent(this, DefinitionActivity::class.java)
            intent.putExtra("ID", id)
            intent.putExtra("ENG", engText)
            intent.putExtra("UZB", uzbText)
            startActivity(intent)
            finish()
        }
    }

    private fun soundButton() {
        savedWordsAdapter.soundClickListener = { engText ->
            speakOut(engText)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {

            val result = textToSpeech.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                println("Language not supported or missing data")
            }
        } else {
            println("Initialization failed")
        }
    }

    private fun speakOut(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    private fun backButton() {
        binding.savedWordsBackView.setOnClickListener {
            finish()
        }
    }


    private fun setDataToListView() {

        val savedWords: ArrayList<WordModel>? =
            intent.getSerializableExtra("SAVED_WORDS") as? ArrayList<WordModel>

        binding.listViewSavedActivity.adapter = savedWordsAdapter
        savedWordsAdapter.setData(savedWords!!)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(event!!)
        return super.onTouchEvent(event)
    }

    inner class SwipeGestureListener : GestureDetector.SimpleOnGestureListener() {

        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (e1 != null) {
                val diffX = e2.x - e1.x
                val diffY = e2.y - e1.y
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            // Right swipe detected
                            finish()
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                        }
                        return true
                    }
                }
            }
            return false
        }
    }
}