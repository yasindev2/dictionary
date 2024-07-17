package uz.yasindev.mydictionaryapp.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.SearchView.OnQueryTextListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import uz.yasindev.mydictionaryapp.CustomDialog
import uz.yasindev.mydictionaryapp.adapter.WordAdapter
import uz.yasindev.mydictionaryapp.database.DataBaseQueries
import uz.yasindev.mydictionaryapp.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var dataBaseQueries: DataBaseQueries
    private lateinit var textToSpeech: TextToSpeech
    private val adapter = WordAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        dataBaseQueries = DataBaseQueries(this)
        textToSpeech = TextToSpeech(this, this)


        setDataToListView()
        sortButton()
        loadAction()
        soundButton()
        searchWord()
        savedWordsButton()

    }

    private fun savedWordsButton() {
        binding.saveView.setOnClickListener {
            val intent = Intent(this, SavedWordsActivity::class.java)
            val savedWords = dataBaseQueries.getSavedWords()
            intent.putExtra("SAVED_WORDS", savedWords)
            startActivity(intent)
        }


    }


    private fun searchWord() {

        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    Log.d("TAG000", "onQueryTextSubmit: $query")
                    dataBaseQueries.searchWord(query)
                    adapter.setData(dataBaseQueries.data)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               if(newText?.isEmpty() == true){
                   adapter.setData(dataBaseQueries.getAllData())
               }
                return false
            }
        })


    }


    private fun loadAction() {
        Log.d("TAG1111", "loadAction: PRESSED")
        adapter.onClickItem = { id, eng, uzb ->
            val intent = Intent(this, DefinitionActivity::class.java)
            intent.putExtra("ID", id)
            intent.putExtra("ENG", eng)
            intent.putExtra("UZB", uzb)
            startActivity(intent)

        }

        adapter.swipedItemData = { id ->
            dataBaseQueries.addSavedWords(id)
            Snackbar.make(binding.root, "Saved", Snackbar.LENGTH_LONG).setAction("Undo") {

                dataBaseQueries.unSaveWords(id)
            }.setBackgroundTint(Color.WHITE).setTextColor(Color.BLACK).show()
        }

        adapter.swipedItemUnsavedData = { id: Int ->
            dataBaseQueries.unSaveWords(id)

            Snackbar.make(binding.root, "Unsaved", Snackbar.LENGTH_LONG).setAction("Undo") {
                dataBaseQueries.addSavedWords(id)
            }.setBackgroundTint(Color.WHITE).setTextColor(Color.BLACK).show()
        }

        adapter.deleteItem = { id ->
            AlertDialog.Builder(this).setTitle("Do you want to delete this word?")
                .setNegativeButton("No") { dialog, value ->
                    dialog.cancel()
                }.setPositiveButton("Yes") { dialog, value ->
                    dataBaseQueries.deleteWords(id)
                    adapter.setData(dataBaseQueries.getAllData())
                }.show()

        }

        binding.listView.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                Log.d("TAG66", "onScrollStateChanged: working")
                binding.addView.visibility = View.VISIBLE
            }

            override fun onScroll(
                view: AbsListView?,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {
                binding.addView.visibility = View.INVISIBLE

                Log.d("TAG66", "onScroll: working")

                if (view != null && totalItemCount > 0) {
                    val lastVisibleItem = firstVisibleItem + visibleItemCount
                    val isLastItemVisible = lastVisibleItem == totalItemCount

                    if (isLastItemVisible) {
                        setBottomMargin(binding.listView, 80)
                    } else {
                        setBottomMargin(binding.listView, 0)
                    }
                }

            }


        })

        binding.addView.setOnClickListener {
            val customDialog = CustomDialog(this)
            customDialog.addWordData = { eng, uzb ->
                dataBaseQueries.addNewWord(eng, uzb)
                adapter.setData(dataBaseQueries.getAllData())
                customDialog.cancel()
            }

            customDialog.show()

        }


    }

    private fun setBottomMargin(view: View, bottomMargin: Int) {
        val params = view.layoutParams as ViewGroup.MarginLayoutParams
        params.bottomMargin = bottomMargin
        view.layoutParams = params
    }

    private fun soundButton() {
        adapter.onClickSoundButton = { text ->
            speakOut(text)
        }
    }

    private fun sortButton() {
        binding.sortView.setOnClickListener {
            adapter.sortEngWords()
        }
    }

    private fun setDataToListView() {
        val allData = dataBaseQueries.getAllData()
        binding.listView.adapter = adapter
        adapter.setData(allData)
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

    override fun onDestroy() {
        textToSpeech.stop()
        textToSpeech.shutdown()
        super.onDestroy()
    }
}
