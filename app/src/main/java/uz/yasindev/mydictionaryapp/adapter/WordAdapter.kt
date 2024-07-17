package uz.yasindev.mydictionaryapp.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.zerobranch.layout.SwipeLayout
import com.zerobranch.layout.SwipeLayout.SwipeActionsListener
import uz.yasindev.mydictionaryapp.R
import uz.yasindev.mydictionaryapp.activities.WordModel
import uz.yasindev.mydictionaryapp.databinding.ListItemBinding

class WordAdapter : BaseAdapter() {


    private val data = ArrayList<WordModel>()

    var onClickSoundButton: ((text: String) -> Unit)? = null
    var onClickItem: ((id: Int, eng: String, uzb: String) -> Unit)? = null
    var swipedItemData: ((id: Int) -> Unit)? = null
    var swipedItemUnsavedData: ((id: Int) -> Unit)? = null
    var deleteItem: ((id: Int) -> Unit)? = null
    lateinit var binding:ListItemBinding

    fun setData(data: ArrayList<WordModel>) {
        this.data.clear()
        Log.d("TAGlll", "setData: $data")
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun sortEngWords() {
        this.data.sortBy {
            it.eng
        }
        notifyDataSetChanged()
    }


    private fun capitalizeFirstLetter(str: String): String {
        if (str.isEmpty()) return str
        return str.substring(0, 1).uppercase() + str.substring(1)
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

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {


        if (convertView==null) {
             binding =
                ListItemBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
            Log.d("TAG22", "Create: $position")
        }
        else {
            binding = ListItemBinding.bind(convertView)
            Log.d("TAG22", "Update: $position")

        }


        binding.textView.text = capitalizeFirstLetter(getItem(position).eng)

        binding.soundView.setOnClickListener {
            onClickSoundButton?.invoke(data[position].eng)
        }
        binding.textView.setOnClickListener {
            onClickItem?.invoke(data[position].id, data[position].eng, data[position].uzb)
        }

        binding.swipeLayout.setOnActionsListener(object : SwipeActionsListener {

            override fun onOpen(direction: Int, isContinuous: Boolean) {
                if (direction == SwipeLayout.LEFT) {
                    binding.swipeLayout.close()
                    binding.rightView.setImageResource(R.drawable.save_full)

                    swipedItemData?.invoke(getItem(position).id)
                } else if (direction == SwipeLayout.RIGHT) {
                    binding.swipeLayout.close()
                    binding.rightView.setImageResource(R.drawable.save)
                    swipedItemUnsavedData?.invoke(getItem(position).id)

                }
            }

            override fun onClose() {
//                todo:SnackBar
            }
        })

        binding.textView.setOnLongClickListener {
            deleteItem?.invoke(getItem(position).id)
            true
        }





        return binding.root


    }
}