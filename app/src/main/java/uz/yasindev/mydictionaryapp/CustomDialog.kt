package uz.yasindev.mydictionaryapp

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.EditText

class CustomDialog(context: Context) : Dialog(context) {

    var addWordData: ((engText: String, uzbText: String) -> Unit)? = null

    init {
        setContentView(R.layout.custom_add_dialog)
        loadView()
    }

    private fun loadView() {
        val engText = findViewById<EditText>(R.id.eng_text)
        val uzbText = findViewById<EditText>(R.id.uzb_text)
        val saveButton = findViewById<Button>(R.id.save_btn)

        saveButton.setOnClickListener {
            val eng = engText.text.toString().trim()
            val uzb = uzbText.text.toString().trim()


            addWordData?.invoke(eng,uzb)
        }


    }

}