package com.metalichecky.amonguseditor.model.item

abstract class Item {
    abstract val typeName: String

    var id: Int = 0
    var name: String = ""
    var assetFileName: String = ""
    var selected: Boolean = false

}