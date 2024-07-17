package uz.yasindev.mydictionaryapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import uz.yasindev.mydictionaryapp.activities.WordModel
import uz.yasindev.mydictionaryapp.databinding.ListItemBinding
import uz.yasindev.mydictionaryapp.databinding.SavedWordsListItemBinding

class SavedWordsAdapter : BaseAdapter() {
    private val data = ArrayList<WordModel>()

    var soundClickListener:((eng:String)->Unit)?= null
    var itemClickListener:((id:Int,eng:String,uzb:String)->Unit?)? = null

    fun setData(data: ArrayList<WordModel>) {
        this.data.clear()
        this.data.addAll(data)
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): WordModel {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return data[position].id.toLong()
    }

    private fun capitalizeFirstLetter(str: String): String {
        if (str.isEmpty()) return str
        return str.substring(0, 1).uppercase() + str.substring(1)
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = SavedWordsListItemBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        val engText = getItem(position).uzb
        val uzbText = getItem(position).eng
        val id = getItem(position).id

        binding.textView.text = capitalizeFirstLetter(engText)

        binding.soundView.setOnClickListener {
            soundClickListener?.invoke(engText)
        }

        binding.root.setOnClickListener {
            itemClickListener?.invoke(id,engText,uzbText)
        }

        return binding.root
    }
}