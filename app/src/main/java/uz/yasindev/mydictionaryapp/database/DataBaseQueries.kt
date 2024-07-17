package uz.yasindev.mydictionaryapp.database

import android.content.Context
import android.database.Cursor
import android.util.Log
import uz.yasindev.mydictionaryapp.activities.WordModel

class DataBaseQueries(context: Context) : DbHelper(context, "dictionary.db") {


    private val allData = ArrayList<WordModel>()
    val data = ArrayList<WordModel>()


    fun getAllData(): ArrayList<WordModel> {

        val cursor: Cursor = mDataBase.rawQuery("SELECT * FROM dictionary", null)
        cursor.moveToFirst()

        while (!cursor.isAfterLast) {
            val id = cursor.getInt(0)
            val uzb = cursor.getString(1)
            val eng = cursor.getString(2)
            val isFav = cursor.getInt(3)

            allData.add(WordModel(id, eng, uzb, isFav))
            cursor.moveToNext()
        }

        cursor.close()
        return allData

    }


    fun getSavedWords(): ArrayList<WordModel> {
        val savedWordsData = ArrayList<WordModel>()
        val cursor: Cursor = mDataBase.rawQuery("SELECT * FROM dictionary WHERE is_fav=1", null)

        cursor.moveToFirst()

        while (!cursor.isAfterLast) {
            val id = cursor.getInt(0)
            val eng = cursor.getString(1)
            val uzb = cursor.getString(2)
            val isFav = cursor.getInt(3)

            savedWordsData.add(WordModel(id, eng, uzb, isFav))
            cursor.moveToNext()
            Log.d("TAG444", "getSavedWords: $savedWordsData")
        }

        cursor.close()
        return savedWordsData
    }

    fun addSavedWords(id: Int) {

        val cursor: Cursor =
            mDataBase.rawQuery("UPDATE dictionary SET is_fav=1 WHERE id=?", arrayOf(id.toString()))

        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            cursor.moveToNext()
        }

        cursor.close()

        return

    }

    fun unSaveWords(id: Int) {

        val cursor: Cursor =
            mDataBase.rawQuery("UPDATE dictionary SET is_fav=0 WHERE id=?", arrayOf(id.toString()))

        cursor.moveToFirst()

        while (!cursor.isAfterLast) {
            cursor.moveToNext()
        }

        cursor.close()

    }

    fun deleteWords(id: Int) {

        val cursor: Cursor =
            mDataBase.rawQuery("DELETE FROM dictionary  WHERE id=?", arrayOf(id.toString()))

        cursor.moveToFirst()

        while (!cursor.isAfterLast) {
            cursor.moveToNext()
        }

        cursor.close()

    }

    fun addNewWord(engText: String, uzbText: String) {


        val cursor: Cursor = mDataBase.rawQuery(
            "INSERT INTO dictionary(uzb,eng,is_fav) VALUES(?,?,0) ", arrayOf(engText, uzbText)
        )

        cursor.moveToFirst()

        while (!cursor.isAfterLast) {
            cursor.moveToNext()
        }

        cursor.close()

    }

    fun searchWord(text: String) {
        data.clear()
        val cursor: Cursor =
            mDataBase.rawQuery("SELECT * FROM dictionary WHERE uzb LIKE ?", arrayOf("%$text%"))

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val eng = cursor.getString(1)
                val uzb = cursor.getString(2)
                val isFav = cursor.getInt(3)
                data.add(WordModel(id, uzb, eng, isFav))
            } while (cursor.moveToNext())
        }

        cursor.close()
    }


}