package com.metalichecky.amonguseditor.model.item

abstract class Item {
    var id: Int = 0
    var name: String = ""
    var assetFileName: String = ""
    var selected: Boolean = false
}