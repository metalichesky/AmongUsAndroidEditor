package com.metalichecky.amonguseditor.repo

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.metalichecky.amonguseditor.App
import com.metalichecky.amonguseditor.model.item.Hat
import com.metalichecky.amonguseditor.model.item.Item
import com.metalichecky.amonguseditor.model.item.Pet
import com.metalichecky.amonguseditor.model.item.Skin
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import java.lang.Exception

object AssetRepo {

    fun getAssetDirName(item: Item): String? {
        val itemDirName = when(item) {
            is Hat -> {
                "hats"
            }
            is Pet -> {
                "pets"
            }
            is Skin -> {
                "skins"
            }
            else -> null
        }
        return itemDirName
    }

    fun loadAssetImageInView(item: Item, imageView: ImageView?) {
        GlobalScope.launch {
            imageView?.setImageDrawable(getAssetImage(item))
        }
    }

    fun getAssetImage(item: Item): Drawable? {
        val imageAssetName = item.assetFileName
        var imageAssetPath = getAssetDirName(item) ?: return null
        imageAssetPath += File.separator + imageAssetName
        var fileStream: InputStream? = null
            try {
                 fileStream = App.instance.assets.open(imageAssetPath)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        fileStream ?: return null
        return Drawable.createFromStream(fileStream, null)
    }

}