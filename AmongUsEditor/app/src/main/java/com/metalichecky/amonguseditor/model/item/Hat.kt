package com.metalichecky.amonguseditor.model.item

class Hat(): Item() {
    override val typeName: String = "Hat"

    constructor(id: Int, name: String, assetFileName: String): this() {
        this.id = id
        this.name = name
        this.assetFileName = assetFileName
    }

}