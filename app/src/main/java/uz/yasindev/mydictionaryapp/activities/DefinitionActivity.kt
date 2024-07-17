package uz.yasindev.mydictionaryapp.activities

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import uz.yasindev.mydictionaryapp.R
import uz.yasindev.mydictionaryapp.databinding.ActivityDefinationBinding
import java.util.Locale

class DefinitionActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private val binding by lazy { ActivityDefinationBinding.inflate(layoutInflater) }
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var gestureDetector:GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        textToSpeech = TextToSpeech(this, this)
        gestureDetector = GestureDetectorCompat(this, SwipeGestureListener())

        setDataToView()

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


    private fun setDataToView() {
        var id = intent.getIntExtra("ID", -1)
        val eng = intent.getStringExtra("ENG")
        val uzb = intent.getStringExtra("UZB")


        binding.textDefinitionActivity.text = capitalizeFirstLetter(eng!!)
        binding.definition.text = capitalizeFirstLetter(uzb!!)

        binding.soundDictionaryActivity.setOnClickListener {
            Log.d("TAG22", "setDataToView: $eng")
            speakOut(eng)
        }

        binding.backView.setOnClickListener {
            finish()
        }
    }

    private fun capitalizeFirstLetter(str: String): String {
        if (str.isEmpty()) return str
        return str.substring(0, 1).uppercase() + str.substring(1)
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
}