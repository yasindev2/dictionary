package uz.yasindev.mydictionaryapp

import android.widget.SearchView

open class Search:SearchView.OnCloseListener,SearchView.OnQueryTextListener {

    override fun onClose(): Boolean {
        TODO("Not yet implemented")
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        TODO("Not yet implemented")
    }
}