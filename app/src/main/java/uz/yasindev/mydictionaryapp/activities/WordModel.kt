package uz.yasindev.mydictionaryapp.activities

import java.io.Serializable

data class WordModel(
    val id:Int,
    val uzb:String,
    val eng:String,
    val isFav:Int,
):Serializable
