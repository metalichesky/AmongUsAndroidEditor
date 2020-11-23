package com.metalichecky.amonguseditor.model.item

class Skin(): Item() {
    override val typeName: String = "Skin"

    constructor(id: Int, name: String, assetFileName: String): this() {
        this.id = id
        this.name = name
        this.assetFileName = assetFileName
    }
}