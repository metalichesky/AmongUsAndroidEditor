package com.metalichecky.amonguseditor.model.item

class Pet(): Item() {
    override val typeName: String = "Pet"


    constructor(id: Int, name: String, assetFileName: String): this() {
        this.id = id
        this.name = name
        this.assetFileName = assetFileName
    }

}