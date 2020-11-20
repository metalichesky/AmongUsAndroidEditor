package com.metalichecky.amonguseditor.repo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
            imageView?.setImageDrawable(getAssetDrawable(item))
        }
    }

    fun getAssetDrawable(item: Item): Drawable? {
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

    fun getAssetBitmap(item: Item): Bitmap? {
        val imageAssetName = item.assetFileName
        var imageAssetPath = getAssetDirName(item) ?: return null
        imageAssetPath += File.separator + imageAssetName
        return getAssetBitmap(imageAssetPath)
    }

    fun getAssetBitmap(assetPath: String): Bitmap? {
        var fileStream: InputStream? = null
        try {
            fileStream = App.instance.assets.open(assetPath)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        fileStream ?: return null
        return BitmapFactory.decodeStream(fileStream)
    }
}